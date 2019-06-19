package io.choerodon.foundation.api.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.foundation.api.dto.*;
import io.choerodon.foundation.api.service.FieldValueService;
import io.choerodon.foundation.api.service.PageFieldService;
import io.choerodon.foundation.domain.FieldValue;
import io.choerodon.foundation.domain.ObjectSchemeField;
import io.choerodon.foundation.domain.PageField;
import io.choerodon.foundation.infra.enums.FieldType;
import io.choerodon.foundation.infra.enums.ObjectSchemeCode;
import io.choerodon.foundation.infra.enums.ObjectSchemeFieldContext;
import io.choerodon.foundation.infra.enums.PageCode;
import io.choerodon.foundation.infra.mapper.FieldValueMapper;
import io.choerodon.foundation.infra.repository.ObjectSchemeFieldRepository;
import io.choerodon.foundation.infra.utils.EnumUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class FieldValueServiceImpl implements FieldValueService {
    @Autowired
    private FieldValueMapper fieldValueMapper;
    @Autowired
    private PageFieldService pageFieldService;
    @Autowired
    private ObjectSchemeFieldRepository objectSchemeFieldRepository;

    private static final String ERROR_PAGECODE_ILLEGAL = "error.pageCode.illegal";
    private static final String ERROR_CONTEXT_ILLEGAL = "error.context.illegal";
    private static final String ERROR_SCHEMECODE_ILLEGAL = "error.schemeCode.illegal";
    private static final String ERROR_OPTION_ILLEGAL = "error.option.illegal";
    private static final String ERROR_FIELDTYPE_ILLEGAL = "error.fieldType.illegal";
    private static final String ERROR_SYSTEM_ILLEGAL = "error.system.illegal";

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public void fillValues(Long organizationId, Long projectId, Long instanceId, String schemeCode, List<PageFieldViewDTO> pageFieldViews) {
        List<FieldValueDTO> values = modelMapper.map(fieldValueMapper.queryList(projectId, instanceId, schemeCode, null), new TypeToken<List<FieldValueDTO>>() {
        }.getType());
        Map<Long, List<FieldValueDTO>> valueGroup = values.stream().collect(Collectors.groupingBy(FieldValueDTO::getFieldId));
        pageFieldViews.forEach(view -> {
            List<FieldValueDTO> fieldValues = valueGroup.get(view.getFieldId());
            handleDTO2Value(view, view.getFieldType(), fieldValues);
        });
    }

    /**
     * 处理FieldValueDTO为value
     *
     * @param view
     * @param fieldType
     * @param values
     */
    private void handleDTO2Value(PageFieldViewDTO view, String fieldType, List<FieldValueDTO> values) {
        Object valueStr = null;
        Object value = null;
        if (values != null && !values.isEmpty()) {
            Long[] longValues = new Long[values.size()];
            switch (fieldType) {
                case FieldType.CHECKBOX:
                case FieldType.MULTIPLE:
                    values.stream().map(FieldValueDTO::getOptionId).collect(Collectors.toList()).toArray(longValues);
                    value = longValues;
                    valueStr = values.stream().map(FieldValueDTO::getOptionValue).collect(Collectors.joining(", "));
                    break;
                case FieldType.RADIO:
                case FieldType.SINGLE:
                    value = values.get(0).getOptionId();
                    valueStr = values.get(0).getOptionValue();
                    break;
                case FieldType.DATETIME:
                    value = values.get(0).getDateValue();
                    valueStr = value;
                    break;
                case FieldType.TIME:
                    value = values.get(0).getDateValue();
                    DateFormat df = new SimpleDateFormat("HH:mm:ss");
                    if (value != null) {
                        valueStr = df.format(value);
                    }
                    break;
                case FieldType.INPUT:
                    value = values.get(0).getStringValue();
                    valueStr = value.toString();
                    break;
                case FieldType.NUMBER:
                    value = values.get(0).getNumberValue();
                    //是否包括小数
                    if (view.getExtraConfig() != null && view.getExtraConfig()) {
                        valueStr = value.toString();
                    } else {
                        valueStr = value.toString().split("\\.")[0];
                        value = valueStr;
                    }
                    break;
                case FieldType.TEXT:
                    value = values.get(0).getTextValue();
                    valueStr = value.toString();
                    break;
                case FieldType.MEMBER:
                    break;
                default:
                    break;
            }
        }
        view.setValueStr(valueStr);
        view.setValue(value);
    }

    /**
     * 处理value为FieldValue
     *
     * @param fieldValues
     * @param fieldType
     * @param value
     */
    private void handleValue2DTO(List<FieldValue> fieldValues, String fieldType, Object value) {
        FieldValue fieldValue = new FieldValue();
        if (value != null) {
            try {
                switch (fieldType) {
                    case FieldType.CHECKBOX:
                    case FieldType.MULTIPLE:
                        List<Integer> optionIds = (List<Integer>) value;
                        for (Integer optionId : optionIds) {
                            FieldValue oValue = new FieldValue();
                            oValue.setOptionId(Long.parseLong(String.valueOf(optionId)));
                            fieldValues.add(oValue);
                        }
                        break;
                    case FieldType.RADIO:
                    case FieldType.SINGLE:
                        Long optionId = Long.parseLong(value.toString());
                        fieldValue.setOptionId(optionId);
                        fieldValues.add(fieldValue);
                        break;
                    case FieldType.DATETIME:
                    case FieldType.TIME:
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date dateValue = df.parse(value.toString());
                        fieldValue.setDateValue(dateValue);
                        fieldValues.add(fieldValue);
                        break;
                    case FieldType.INPUT:
                        String stringValue = (String) value;
                        fieldValue.setStringValue(stringValue);
                        fieldValues.add(fieldValue);
                        break;
                    case FieldType.NUMBER:
                        String numberValue = value.toString();
                        fieldValue.setNumberValue(numberValue);
                        fieldValues.add(fieldValue);
                        break;
                    case FieldType.TEXT:
                        String textValue = (String) value;
                        fieldValue.setTextValue(textValue);
                        fieldValues.add(fieldValue);
                        break;
                    case FieldType.MEMBER:
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                throw new CommonException(e.getMessage());
            }
        }
    }

    private String handleDTO2StringValue(String fieldType, List<FieldValueDTO> values) {
        String valueStr = "";
        if (values != null && !values.isEmpty()) {
            Long[] longValues = new Long[values.size()];
            switch (fieldType) {
                case FieldType.CHECKBOX:
                case FieldType.MULTIPLE:
                    values.stream().map(FieldValueDTO::getOptionId).collect(Collectors.toList()).toArray(longValues);
                    valueStr = values.stream().map(FieldValueDTO::getOptionValue).collect(Collectors.joining(", "));
                    break;
                case FieldType.RADIO:
                case FieldType.SINGLE:
                    valueStr = values.get(0).getOptionValue();
                    break;
                case FieldType.DATETIME:
                    Object value1 = values.get(0).getDateValue();
                    DateFormat dff = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    if (value1 != null) {
                        valueStr = dff.format(value1);
                    }
                    break;
                case FieldType.TIME:
                    Object value = values.get(0).getDateValue();
                    DateFormat df = new SimpleDateFormat("HH:mm:ss");
                    if (value != null) {
                        valueStr = df.format(value);
                    }
                    break;
                case FieldType.INPUT:
                    valueStr = values.get(0).getStringValue();
                    break;
                case FieldType.NUMBER:
                    valueStr = values.get(0).getNumberValue().split("\\.")[0];
                    break;
                case FieldType.TEXT:
                    valueStr = values.get(0).getTextValue();
                    break;
                default:
                    break;
            }
        }
        return valueStr;
    }

    @Override
    public void createFieldValues(Long organizationId, Long projectId, Long instanceId, String schemeCode, List<PageFieldViewCreateDTO> createDTOs) {
        if (!EnumUtil.contain(ObjectSchemeCode.class, schemeCode)) {
            throw new CommonException(ERROR_SCHEMECODE_ILLEGAL);
        }
        List<FieldValue> fieldValues = new ArrayList<>();
        createDTOs.forEach(createDTO -> {
            List<FieldValue> values = new ArrayList<>();
            handleValue2DTO(values, createDTO.getFieldType(), createDTO.getValue());
            //校验
            ObjectSchemeField field = objectSchemeFieldRepository.queryById(organizationId, projectId, createDTO.getFieldId());
            if (field.getSystem()) {
                throw new CommonException(ERROR_SYSTEM_ILLEGAL);
            }
            values.forEach(value -> value.setFieldId(createDTO.getFieldId()));
            fieldValues.addAll(values);
        });
        if (!fieldValues.isEmpty()) {
            fieldValueMapper.batchInsert(projectId, instanceId, schemeCode, fieldValues);
        }
    }

    @Override
    public List<FieldValueDTO> updateFieldValue(Long organizationId, Long projectId, Long instanceId, Long fieldId, String schemeCode, PageFieldViewUpdateDTO updateDTO) {
        if (!EnumUtil.contain(ObjectSchemeCode.class, schemeCode)) {
            throw new CommonException(ERROR_SCHEMECODE_ILLEGAL);
        }
        if (!EnumUtil.contain(FieldType.class, updateDTO.getFieldType())) {
            throw new CommonException(ERROR_FIELDTYPE_ILLEGAL);
        }
        //校验
        objectSchemeFieldRepository.queryById(organizationId, projectId, fieldId);
        //删除原fieldValue
        FieldValue delete = new FieldValue();
        delete.setInstanceId(instanceId);
        delete.setSchemeCode(schemeCode);
        delete.setFieldId(fieldId);
        delete.setProjectId(projectId);
        fieldValueMapper.delete(delete);
        //创建新fieldValue
        List<FieldValue> fieldValues = new ArrayList<>();
        handleValue2DTO(fieldValues, updateDTO.getFieldType(), updateDTO.getValue());
        fieldValues.forEach(fieldValue -> fieldValue.setFieldId(fieldId));
        if (!fieldValues.isEmpty()) {
            fieldValueMapper.batchInsert(projectId, instanceId, schemeCode, fieldValues);
        }
        return modelMapper.map(fieldValueMapper.queryList(projectId, instanceId, schemeCode, fieldId), new TypeToken<List<FieldValueDTO>>() {
        }.getType());
    }

    @Override
    public void deleteByOptionIds(Long fieldId, List<Long> optionIds) {
        if (!optionIds.isEmpty()) {
            for (Long optionId : optionIds) {
                if (optionId == null) {
                    throw new CommonException(ERROR_OPTION_ILLEGAL);
                }
            }
            fieldValueMapper.deleteByOptionIds(fieldId, optionIds);
        }
    }

    @Override
    public void deleteByFieldId(Long fieldId) {
        FieldValue delete = new FieldValue();
        delete.setFieldId(fieldId);
        fieldValueMapper.delete(delete);
    }

    @Override
    public void createFieldValuesWithQuickCreate(Long organizationId, Long projectId, Long instanceId, PageFieldViewParamDTO paramDTO) {
        if (!EnumUtil.contain(PageCode.class, paramDTO.getPageCode())) {
            throw new CommonException(ERROR_PAGECODE_ILLEGAL);
        }
        if (!EnumUtil.contain(ObjectSchemeCode.class, paramDTO.getSchemeCode())) {
            throw new CommonException(ERROR_SCHEMECODE_ILLEGAL);
        }
        if (!EnumUtil.contain(ObjectSchemeFieldContext.class, paramDTO.getContext())) {
            throw new CommonException(ERROR_CONTEXT_ILLEGAL);
        }
        List<PageField> pageFields = pageFieldService.queryPageField(organizationId, projectId, paramDTO.getPageCode(), paramDTO.getContext());
        //过滤掉不显示字段和系统字段
        pageFields = pageFields.stream().filter(PageField::getDisplay).filter(x -> !x.getSystem()).collect(Collectors.toList());
        List<FieldValue> fieldValues = new ArrayList<>();
        pageFields.forEach(create -> {
            List<FieldValue> values = new ArrayList<>();
            //处理默认值
            handleDefaultValue2DTO(values, create);
            values.forEach(value -> value.setFieldId(create.getFieldId()));
            fieldValues.addAll(values);
        });
        if (!fieldValues.isEmpty()) {
            fieldValueMapper.batchInsert(projectId, instanceId, paramDTO.getSchemeCode(), fieldValues);
        }
    }

    @Override
    public Map<String, String> queryFieldValueMapWithInstanceId(Long organizationId, Long projectId, Long instanceId) {
        Map<String, String> result = new HashMap<>();
        List<FieldValueDTO> values = modelMapper.map(fieldValueMapper
                        .queryList(projectId, instanceId, null, null),
                new TypeToken<List<FieldValueDTO>>() {
                }.getType());
        Map<Long, List<FieldValueDTO>> valueGroup = values.stream().collect(Collectors.groupingBy(FieldValueDTO::getFieldId));

        valueGroup.forEach((fieldId, fieldValueDTOList) -> {
            ObjectSchemeField objectSchemeField = objectSchemeFieldRepository.queryById(organizationId, projectId, fieldId);
            result.put(objectSchemeField.getCode(), handleDTO2StringValue(objectSchemeField.getFieldType(), fieldValueDTOList));
        });

        return result;
    }

    /**
     * 处理valueStr为FieldValue
     *
     * @param fieldValues
     * @param create
     */
    private void handleDefaultValue2DTO(List<FieldValue> fieldValues, PageField create) {
        String defaultValue = create.getDefaultValue();
        String fieldType = create.getFieldType();
        FieldValue fieldValue = new FieldValue();
        //处理默认当前时间
        if (fieldType.equals(FieldType.DATETIME) || fieldType.equals(FieldType.TIME)) {
            if (create.getExtraConfig() != null && create.getExtraConfig()) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                defaultValue = df.format(new Date());
            }
        }
        if (defaultValue != null && !defaultValue.equals("")) {
            try {
                switch (fieldType) {
                    case FieldType.CHECKBOX:
                    case FieldType.MULTIPLE:
                        String[] optionIds = defaultValue.split(",");
                        for (String optionId : optionIds) {
                            FieldValue oValue = new FieldValue();
                            oValue.setOptionId(Long.parseLong(optionId));
                            fieldValues.add(oValue);
                        }
                        break;
                    case FieldType.RADIO:
                    case FieldType.SINGLE:
                        Long optionId = Long.parseLong(defaultValue);
                        fieldValue.setOptionId(optionId);
                        fieldValues.add(fieldValue);
                        break;
                    case FieldType.DATETIME:
                    case FieldType.TIME:
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        Date dateValue = df.parse(defaultValue);
                        fieldValue.setDateValue(dateValue);
                        fieldValues.add(fieldValue);
                        break;
                    case FieldType.INPUT:
                        fieldValue.setStringValue(defaultValue);
                        fieldValues.add(fieldValue);
                        break;
                    case FieldType.NUMBER:
                        fieldValue.setNumberValue(defaultValue);
                        fieldValues.add(fieldValue);
                        break;
                    case FieldType.TEXT:
                        fieldValue.setTextValue(defaultValue);
                        fieldValues.add(fieldValue);
                        break;
                    case FieldType.MEMBER:
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                throw new CommonException(e.getMessage());
            }
        }
    }
}
