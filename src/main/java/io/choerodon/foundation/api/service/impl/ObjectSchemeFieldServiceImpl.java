package io.choerodon.foundation.api.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.foundation.api.dto.*;
import io.choerodon.foundation.api.service.FieldOptionService;
import io.choerodon.foundation.api.service.FieldValueService;
import io.choerodon.foundation.api.service.ObjectSchemeFieldService;
import io.choerodon.foundation.api.service.PageFieldService;
import io.choerodon.foundation.domain.LookupTypeWithValues;
import io.choerodon.foundation.domain.LookupValue;
import io.choerodon.foundation.domain.ObjectScheme;
import io.choerodon.foundation.domain.ObjectSchemeField;
import io.choerodon.foundation.infra.enums.FieldType;
import io.choerodon.foundation.infra.enums.LookupType;
import io.choerodon.foundation.infra.enums.ObjectSchemeCode;
import io.choerodon.foundation.infra.enums.ObjectSchemeFieldContext;
import io.choerodon.foundation.infra.feign.IamFeignClient;
import io.choerodon.foundation.infra.mapper.LookupValueMapper;
import io.choerodon.foundation.infra.mapper.ObjectSchemeFieldMapper;
import io.choerodon.foundation.infra.mapper.ObjectSchemeMapper;
import io.choerodon.foundation.infra.repository.ObjectSchemeFieldRepository;
import io.choerodon.foundation.infra.utils.EnumUtil;
import io.choerodon.foundation.infra.utils.FieldValueUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shinan.chen
 * @since 2019/3/29
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class ObjectSchemeFieldServiceImpl implements ObjectSchemeFieldService {
    @Autowired
    private ObjectSchemeFieldMapper objectSchemeFieldMapper;
    @Autowired
    private ObjectSchemeMapper objectSchemeMapper;
    @Autowired
    private ObjectSchemeFieldRepository objectSchemeFieldRepository;
    @Autowired
    private FieldOptionService fieldOptionService;
    @Autowired
    private PageFieldService pageFieldService;
    @Autowired
    private FieldValueService fieldValueService;
    @Autowired
    private LookupValueMapper lookupValueMapper;
    @Autowired
    private IamFeignClient iamFeignClient;

    private ModelMapper modelMapper = new ModelMapper();
    private static final String ERROR_SCHEMECODE_ILLEGAL = "error.schemeCode.illegal";
    private static final String ERROR_CONTEXT_ILLEGAL = "error.context.illegal";
    private static final String ERROR_FIELDTYPE_ILLEGAL = "error.fieldType.illegal";
    private static final String ERROR_FIELD_ILLEGAL = "error.field.illegal";
    private static final String ERROR_FIELD_NAMEEXIST = "error.field.nameExist";
    private static final String ERROR_FIELD_CODEEXIST = "error.field.codeExist";

    @Override
    public Map<String, Object> listQuery(Long organizationId, Long projectId, String schemeCode) {
        Map<String, Object> result = new HashMap<>(2);
        if (!EnumUtil.contain(ObjectSchemeCode.class, schemeCode)) {
            throw new CommonException(ERROR_SCHEMECODE_ILLEGAL);
        }
        ObjectSchemeFieldSearchDTO searchDTO = new ObjectSchemeFieldSearchDTO();
        searchDTO.setSchemeCode(schemeCode);
        List<ObjectSchemeFieldDTO> fieldDTOS = modelMapper.map(objectSchemeFieldMapper.listQuery(organizationId, projectId, searchDTO), new TypeToken<List<ObjectSchemeFieldDTO>>() {
        }.getType());
        fillContextName(fieldDTOS);
        ObjectScheme select = new ObjectScheme();
        select.setSchemeCode(schemeCode);
        result.put("name", objectSchemeMapper.selectOne(select).getName());
        result.put("content", fieldDTOS);
        return result;
    }

    /**
     * 填充contextName
     *
     * @param fieldDTOS
     */
    private void fillContextName(List<ObjectSchemeFieldDTO> fieldDTOS) {
        LookupTypeWithValues typeWithValues = lookupValueMapper.queryLookupValueByCode(LookupType.CONTEXT);
        Map<String, String> codeMap = typeWithValues.getLookupValues().stream().collect(Collectors.toMap(LookupValue::getValueCode, LookupValue::getName));
        for (ObjectSchemeFieldDTO fieldDTO : fieldDTOS) {
            String[] contextCodes = fieldDTO.getContext().split(",");
            List<String> contextNames = new ArrayList<>(contextCodes.length);
            for (String contextCode : contextCodes) {
                contextNames.add(codeMap.get(contextCode));
            }
            fieldDTO.setContextName(contextNames.stream().collect(Collectors.joining(",")));
        }
    }

    @Override
    public ObjectSchemeFieldDetailDTO create(Long organizationId, Long projectId, ObjectSchemeFieldCreateDTO fieldCreateDTO) {
        if (!EnumUtil.contain(FieldType.class, fieldCreateDTO.getFieldType())) {
            throw new CommonException(ERROR_FIELDTYPE_ILLEGAL);
        }
        if (checkName(organizationId, projectId, fieldCreateDTO.getName(), fieldCreateDTO.getSchemeCode())) {
            throw new CommonException(ERROR_FIELD_NAMEEXIST);
        }
        if (checkCode(organizationId, projectId, fieldCreateDTO.getCode(), fieldCreateDTO.getSchemeCode())) {
            throw new CommonException(ERROR_FIELD_CODEEXIST);
        }
        for (String context : fieldCreateDTO.getContext()) {
            if (!EnumUtil.contain(ObjectSchemeFieldContext.class, context)) {
                throw new CommonException(ERROR_CONTEXT_ILLEGAL);
            }
        }
        ObjectSchemeField field = modelMapper.map(fieldCreateDTO, ObjectSchemeField.class);
        field.setContext(Arrays.asList(fieldCreateDTO.getContext()).stream().collect(Collectors.joining(",")));
        field.setOrganizationId(organizationId);
        field.setProjectId(projectId);
        objectSchemeFieldRepository.create(field);
        //创建pageField
        if (projectId != null) {
            pageFieldService.createByFieldWithPro(organizationId, projectId, field);
        } else {
            pageFieldService.createByFieldWithOrg(organizationId, field);
        }

        return queryById(organizationId, projectId, field.getId());
    }

    @Override
    public ObjectSchemeFieldDetailDTO queryById(Long organizationId, Long projectId, Long fieldId) {
        ObjectSchemeField field = objectSchemeFieldRepository.queryById(organizationId, projectId, fieldId);
        ObjectSchemeFieldDetailDTO fieldDetailDTO = modelMapper.map(field, ObjectSchemeFieldDetailDTO.class);
        fieldDetailDTO.setContext(field.getContext().split(","));
        //获取字段选项，并设置默认值
        List<FieldOptionDTO> fieldOptions = fieldOptionService.queryByFieldId(organizationId, fieldId);
        if (!fieldOptions.isEmpty() && field.getDefaultValue() != null) {
            List<String> defaultIds = Arrays.asList(field.getDefaultValue().split(","));
            fieldOptions.forEach(fieldOption -> {
                if (defaultIds.contains(fieldOption.getId().toString())) {
                    fieldOption.setIsDefault(true);
                } else {
                    fieldOption.setIsDefault(false);
                }
            });
            fieldDetailDTO.setFieldOptions(fieldOptions);
        }
        FieldValueUtil.handleDefaultValue(fieldDetailDTO);
        return fieldDetailDTO;
    }

    @Override
    public void delete(Long organizationId, Long projectId, Long fieldId) {
        ObjectSchemeField field = objectSchemeFieldRepository.queryById(organizationId, projectId, fieldId);
        //组织层无法删除项目层
        if (projectId == null && field.getProjectId() != null) {
            throw new CommonException(ERROR_FIELD_ILLEGAL);
        }
        //项目层无法删除组织层
        if (projectId != null && field.getProjectId() == null) {
            throw new CommonException(ERROR_FIELD_ILLEGAL);
        }
        //无法删除系统字段
        if (field.getSystem()) {
            throw new CommonException(ERROR_FIELD_ILLEGAL);
        }
        objectSchemeFieldRepository.delete(fieldId);
        //删除pageFields
        pageFieldService.deleteByFieldId(fieldId);
        //删除字段值
        fieldValueService.deleteByFieldId(fieldId);
    }

    @Override
    public ObjectSchemeFieldDetailDTO update(Long organizationId, Long projectId, Long fieldId, ObjectSchemeFieldUpdateDTO updateDTO) {
        //处理字段选项
        if (updateDTO.getFieldOptions() != null) {
            String defaultIds = fieldOptionService.handleFieldOption(organizationId, fieldId, updateDTO.getFieldOptions());
            if (defaultIds != null && !"".equals(defaultIds)) {
                updateDTO.setDefaultValue(defaultIds);
            }
        }
        objectSchemeFieldRepository.queryById(organizationId, projectId, fieldId);
        ObjectSchemeField update = modelMapper.map(updateDTO, ObjectSchemeField.class);
        //处理context
        String[] contexts = updateDTO.getContext();
        if (contexts != null && contexts.length != 0) {
            for (String context : contexts) {
                if (!EnumUtil.contain(ObjectSchemeFieldContext.class, context)) {
                    throw new CommonException(ERROR_CONTEXT_ILLEGAL);
                }
            }
            update.setContext(Arrays.asList(contexts).stream().collect(Collectors.joining(",")));
        }
        update.setId(fieldId);
        objectSchemeFieldRepository.update(update);
        return queryById(organizationId, projectId, fieldId);
    }

    @Override
    public Boolean checkName(Long organizationId, Long projectId, String name, String schemeCode) {
        if (!EnumUtil.contain(ObjectSchemeCode.class, schemeCode)) {
            throw new CommonException(ERROR_SCHEMECODE_ILLEGAL);
        }
        ObjectSchemeFieldSearchDTO search = new ObjectSchemeFieldSearchDTO();
        search.setName(name);
        search.setSchemeCode(schemeCode);
        return !objectSchemeFieldMapper.listQuery(organizationId, projectId, search).isEmpty();
    }

    @Override
    public Boolean checkCode(Long organizationId, Long projectId, String code, String schemeCode) {
        if (!EnumUtil.contain(ObjectSchemeCode.class, schemeCode)) {
            throw new CommonException(ERROR_SCHEMECODE_ILLEGAL);
        }
        ObjectSchemeFieldSearchDTO search = new ObjectSchemeFieldSearchDTO();
        search.setCode(code);
        search.setSchemeCode(schemeCode);
        return !objectSchemeFieldMapper.listQuery(organizationId, projectId, search).isEmpty();
    }
}
