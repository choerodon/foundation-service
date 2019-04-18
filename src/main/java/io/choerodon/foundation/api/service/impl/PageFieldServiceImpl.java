package io.choerodon.foundation.api.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.foundation.api.dto.*;
import io.choerodon.foundation.api.service.FieldOptionService;
import io.choerodon.foundation.api.service.FieldValueService;
import io.choerodon.foundation.api.service.PageFieldService;
import io.choerodon.foundation.domain.ObjectSchemeField;
import io.choerodon.foundation.domain.Page;
import io.choerodon.foundation.domain.PageField;
import io.choerodon.foundation.domain.ProjectPageField;
import io.choerodon.foundation.infra.annotation.CopyPageField;
import io.choerodon.foundation.infra.enums.*;
import io.choerodon.foundation.infra.mapper.ObjectSchemeFieldMapper;
import io.choerodon.foundation.infra.mapper.PageFieldMapper;
import io.choerodon.foundation.infra.mapper.PageMapper;
import io.choerodon.foundation.infra.mapper.ProjectPageFieldMapper;
import io.choerodon.foundation.infra.repository.PageFieldRepository;
import io.choerodon.foundation.infra.utils.EnumUtil;
import io.choerodon.foundation.infra.utils.RankUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class PageFieldServiceImpl implements PageFieldService {
    @Autowired
    private PageFieldMapper pageFieldMapper;
    @Autowired
    private PageFieldRepository pageFieldRepository;
    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private ObjectSchemeFieldMapper objectSchemeFieldMapper;
    @Autowired
    private ProjectPageFieldMapper projectPageFieldMapper;
    @Autowired
    private FieldOptionService optionService;
    @Autowired
    private FieldValueService fieldValueService;

    private static final String ERROR_PAGECODE_ILLEGAL = "error.pageCode.illegal";
    private static final String ERROR_CONTEXT_ILLEGAL = "error.context.illegal";
    private static final String ERROR_SCHEMECODE_ILLEGAL = "error.schemeCode.illegal";
    private static final String ERROR_FIELDCODE_ILLEGAL = "error.fieldCode.illegal";
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public Map<String, Object> listQuery(Long organizationId, Long projectId, String pageCode, String context) {
        Map<String, Object> result = new HashMap<>(2);
        if (!EnumUtil.contain(PageCode.class, pageCode)) {
            throw new CommonException(ERROR_PAGECODE_ILLEGAL);
        }
        if (context != null && !EnumUtil.contain(ObjectSchemeFieldContext.class, context)) {
            throw new CommonException(ERROR_CONTEXT_ILLEGAL);
        }
        List<PageField> pageFields = queryPageField(organizationId, projectId, pageCode, context);
        Page select = new Page();
        select.setPageCode(pageCode);
        result.put("name", pageMapper.selectOne(select).getName());
        result.put("content", modelMapper.map(pageFields, new TypeToken<List<PageFieldDTO>>() {
        }.getType()));
        return result;
    }

    /**
     * 若没有项目层配置则获取组织层配置
     *
     * @param organizationId
     * @param projectId
     * @param pageCode
     * @return
     */
    @Override
    public List<PageField> queryPageField(Long organizationId, Long projectId, String pageCode, String context) {
        List<PageField> pageFields;
        if (projectId != null && projectPageFieldMapper.queryOne(organizationId, projectId) != null) {
            pageFields = pageFieldMapper.listQuery(organizationId, projectId, pageCode, context);
        } else {
            pageFields = pageFieldMapper.listQuery(organizationId, null, pageCode, context);
        }
        //若没有数据则初始化【修复旧数据】
        if (pageFields.isEmpty()) {
            initPageFieldByOrg(organizationId);
            if (projectId != null && projectPageFieldMapper.queryOne(organizationId, projectId) != null) {
                pageFields = pageFieldMapper.listQuery(organizationId, projectId, pageCode, context);
            } else {
                pageFields = pageFieldMapper.listQuery(organizationId, null, pageCode, context);
            }
        }
        return pageFields;
    }

    @Override
    @CopyPageField
    public PageFieldDTO adjustFieldOrder(Long organizationId, Long projectId, String pageCode, AdjustOrderDTO adjustOrder) {
        if (!EnumUtil.contain(PageCode.class, pageCode)) {
            throw new CommonException(ERROR_PAGECODE_ILLEGAL);
        }
        PageField current = pageFieldMapper.queryByFieldId(organizationId, projectId, pageCode, adjustOrder.getCurrentFieldId());
        PageField outset = pageFieldMapper.queryByFieldId(organizationId, projectId, pageCode, adjustOrder.getOutsetFieldId());
        PageField update = new PageField();
        update.setId(current.getId());
        update.setObjectVersionNumber(current.getObjectVersionNumber());
        if (adjustOrder.getBefore()) {
            update.setRank(RankUtil.genNext(outset.getRank()));
        } else {
            String rightRank = pageFieldMapper.queryRightRank(organizationId, projectId, pageCode, outset.getRank());
            if (rightRank == null) {
                update.setRank(RankUtil.genPre(outset.getRank()));
            } else {
                update.setRank(RankUtil.between(outset.getRank(), rightRank));
            }
        }
        pageFieldRepository.update(update);
        return modelMapper.map(pageFieldMapper.queryByFieldId(organizationId, projectId, pageCode, current.getFieldId()), PageFieldDTO.class);
    }

    @Override
    @CopyPageField
    public PageFieldDTO update(Long organizationId, Long projectId, String pageCode, Long fieldId, PageFieldUpdateDTO updateDTO) {
        if (!EnumUtil.contain(PageCode.class, pageCode)) {
            throw new CommonException(ERROR_PAGECODE_ILLEGAL);
        }
        PageField field = pageFieldMapper.queryByFieldId(organizationId, projectId, pageCode, fieldId);
        PageField update = modelMapper.map(updateDTO, PageField.class);
        update.setId(field.getId());
        pageFieldRepository.update(update);
        return modelMapper.map(pageFieldMapper.queryByFieldId(organizationId, projectId, pageCode, fieldId), PageFieldDTO.class);
    }

    @Override
    public void initPageFieldByOrg(Long organizationId) {
        if (pageFieldMapper.listQuery(organizationId, null, null, null).isEmpty()) {
            //查询page
            List<Page> pages = pageMapper.fulltextSearch(organizationId, new PageSearchDTO());
            Map<String, Long> pageMap = pages.stream().collect(Collectors.toMap(Page::getPageCode, Page::getId));
            //查询field
            List<ObjectSchemeField> fields = objectSchemeFieldMapper.listQuery(organizationId, null, new ObjectSchemeFieldSearchDTO());
            Map<String, Map<String, Long>> schemeCodeFieldMap = fields.stream().collect(Collectors.groupingBy(ObjectSchemeField::getSchemeCode, Collectors.toMap(ObjectSchemeField::getCode, ObjectSchemeField::getId)));
            handleInitPageFieldE(organizationId, schemeCodeFieldMap, pageMap);
        }
    }

    private void handleInitPageFieldE(Long organizationId, Map<String, Map<String, Long>> schemeCodeFieldMap, Map<String, Long> pageMap) {
        Class[] clzes = InitPageFieldE.class.getClasses();
        Arrays.asList(clzes).forEach(cls -> {
            List<InitPageFieldDTO> initPageFields = modelMapper.map(Arrays.asList(cls.getEnumConstants()), new TypeToken<List<InitPageFieldDTO>>() {
            }.getType());
            String rank = RankUtil.mid();
            for (InitPageFieldDTO pageField : initPageFields) {
                Map<String, Long> fieldMap = schemeCodeFieldMap.get(pageField.getSchemeCode());
                if (fieldMap == null) {
                    throw new CommonException(ERROR_SCHEMECODE_ILLEGAL);
                }
                Long fieldId = fieldMap.get(pageField.getFieldCode());
                if (fieldId == null) {
                    throw new CommonException(ERROR_FIELDCODE_ILLEGAL);
                }
                pageField.setFieldId(fieldId);
                Long pageId = pageMap.get(pageField.getPageCode());
                if (pageId == null) {
                    throw new CommonException(ERROR_PAGECODE_ILLEGAL);
                }
                pageField.setPageId(pageId);
                //设置rank
                pageField.setRank(rank);
                rank = RankUtil.genPre(rank);
            }
            List<PageField> pageFields = modelMapper.map(initPageFields, new TypeToken<List<PageField>>() {
            }.getType());
            pageFieldMapper.batchInsert(organizationId, null, pageFields);
        });
    }

    @Override
    @CopyPageField
    public void createByFieldWithPro(Long organizationId, Long projectId, ObjectSchemeField field) {
        //查询page
        PageSearchDTO searchDTO = new PageSearchDTO();
        searchDTO.setSchemeCode(field.getSchemeCode());
        List<Page> pages = pageMapper.fulltextSearch(organizationId, searchDTO);
        pages.forEach(page -> {
            //创建pageField
            PageField pageField = new PageField();
            pageField.setProjectId(projectId);
            pageField.setOrganizationId(organizationId);
            pageField.setDisplay(true);
            pageField.setFieldId(field.getId());
            pageField.setPageId(page.getId());
            String minRank = pageFieldMapper.queryMinRank(organizationId, projectId, page.getPageCode());
            //若没有数据则初始化【修复旧数据】
            if (minRank == null) {
                initPageFieldByOrg(organizationId);
                minRank = pageFieldMapper.queryMinRank(organizationId, projectId, page.getPageCode());
            }
            pageField.setRank(RankUtil.genPre(minRank));
            pageFieldRepository.create(pageField);
        });
    }

    @Override
    public void createByFieldWithOrg(Long organizationId, ObjectSchemeField field) {
        //项目层自定义同样需要创建字段
        List<ProjectPageField> projectPageFields = projectPageFieldMapper.queryByOrgId(organizationId);
        //查询page
        PageSearchDTO searchDTO = new PageSearchDTO();
        searchDTO.setSchemeCode(field.getSchemeCode());
        List<Page> pages = pageMapper.fulltextSearch(organizationId, searchDTO);
        pages.forEach(page -> {
            //组织层创建pageField
            PageField pageField = new PageField();
            pageField.setOrganizationId(organizationId);
            pageField.setDisplay(true);
            pageField.setFieldId(field.getId());
            pageField.setPageId(page.getId());
            String minRank = pageFieldMapper.queryMinRank(organizationId, null, page.getPageCode());
            //若没有数据则初始化【修复旧数据】
            if (minRank == null) {
                initPageFieldByOrg(organizationId);
                minRank = pageFieldMapper.queryMinRank(organizationId, null, page.getPageCode());
            }
            pageField.setRank(RankUtil.genPre(minRank));
            pageFieldRepository.create(pageField);
            //项目层创建pageField
            projectPageFields.forEach(projectPageField -> {
                pageField.setId(null);
                pageField.setProjectId(projectPageField.getProjectId());
                pageFieldRepository.create(pageField);
            });
        });
    }

    @Override
    public void deleteByFieldId(Long fieldId) {
        pageFieldMapper.deleteByFieldId(fieldId);
    }

    @Override
    public List<PageFieldViewDTO> queryPageFieldViewList(Long organizationId, Long projectId, PageFieldViewParamDTO paramDTO) {
        if (!EnumUtil.contain(PageCode.class, paramDTO.getPageCode())) {
            throw new CommonException(ERROR_PAGECODE_ILLEGAL);
        }
        if (!EnumUtil.contain(ObjectSchemeCode.class, paramDTO.getSchemeCode())) {
            throw new CommonException(ERROR_SCHEMECODE_ILLEGAL);
        }
        if (!EnumUtil.contain(ObjectSchemeFieldContext.class, paramDTO.getContext())) {
            throw new CommonException(ERROR_CONTEXT_ILLEGAL);
        }
        List<PageField> pageFields = queryPageField(organizationId, projectId, paramDTO.getPageCode(), paramDTO.getContext());
        //modelMapper设置严格匹配策略
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        pageFields = pageFields.stream().filter(PageField::getDisplay).collect(Collectors.toList());
        List<PageFieldViewDTO> pageFieldViews = modelMapper.map(pageFields, new TypeToken<List<PageFieldViewDTO>>() {
        }.getType());
        //填充option
        optionService.fillOptions(organizationId, projectId, pageFieldViews);
        handleDefaultValue(pageFieldViews);
        return pageFieldViews;
    }

    @Override
    public List<PageFieldViewDTO> queryPageFieldViewListWithInstanceId(Long organizationId, Long projectId, Long instanceId, PageFieldViewParamDTO paramDTO) {
        List<PageFieldViewDTO> pageFieldViews = queryPageFieldViewList(organizationId, projectId, paramDTO);
        //填充value
        fieldValueService.fillValues(organizationId, projectId, instanceId, paramDTO.getSchemeCode(), pageFieldViews);
        return pageFieldViews;
    }

    /**
     * 处理默认值
     *
     * @param pageFieldViews
     */
    private void handleDefaultValue(List<PageFieldViewDTO> pageFieldViews) {
        for (PageFieldViewDTO view : pageFieldViews) {
            switch (view.getFieldType()) {
                case FieldType.CHECKBOX:
                case FieldType.MULTIPLE:
                    handleDefaultValueIds(view);
                    break;
                case FieldType.RADIO:
                case FieldType.SINGLE:
                    if (view.getDefaultValue() != null && !"".equals(view.getDefaultValue())) {
                        view.setDefaultValue(Long.valueOf(String.valueOf(view.getDefaultValue())));
                    }
                    break;
                case FieldType.DATETIME:
                case FieldType.TIME:
                    //如果勾选了默认当前
                    if (view.getExtraConfig() != null && view.getExtraConfig()) {
                        view.setDefaultValue(new Date());
                    }
                    break;
                case FieldType.NUMBER:
                    //如果勾选了是否小数
                    if (view.getExtraConfig() != null && !view.getExtraConfig()) {
                        view.setDefaultValue(view.getDefaultValue().toString().split("\\.")[0]);
                    }
                    break;
                case FieldType.INPUT:
                case FieldType.TEXT:
                case FieldType.MEMBER:
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 处理默认值多选id
     *
     * @param view
     */
    private void handleDefaultValueIds(PageFieldViewDTO view) {
        Object defaultValue = view.getDefaultValue();
        if (defaultValue != null && !"".equals(defaultValue)) {
            String[] defaultIdStrs = String.valueOf(defaultValue).split(",");
            if (defaultIdStrs != null) {
                Long[] defaultIds = new Long[defaultIdStrs.length];
                for (int i = 0; i < defaultIdStrs.length; i++) {
                    defaultIds[i] = Long.valueOf(defaultIdStrs[i]);
                }
                view.setDefaultValue(defaultIds);
            }
        }
    }
}
