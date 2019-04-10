package io.choerodon.foundation.api.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.foundation.api.dto.FieldValueDTO;
import io.choerodon.foundation.api.dto.FieldValueUpdateDTO;
import io.choerodon.foundation.api.dto.PageFieldViewCreateDTO;
import io.choerodon.foundation.api.dto.PageFieldViewDTO;
import io.choerodon.foundation.api.service.FieldValueService;
import io.choerodon.foundation.domain.FieldValue;
import io.choerodon.foundation.infra.enums.ObjectSchemeCode;
import io.choerodon.foundation.infra.mapper.FieldValueMapper;
import io.choerodon.foundation.infra.repository.ObjectSchemeFieldRepository;
import io.choerodon.foundation.infra.utils.EnumUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
@Component
public class FieldValueServiceImpl implements FieldValueService {
    @Autowired
    private FieldValueMapper fieldValueMapper;
    @Autowired
    private ObjectSchemeFieldRepository objectSchemeFieldRepository;

    private static final String ERROR_SCHEMECODE_ILLEGAL = "error.schemeCode.illegal";
    private static final String ERROR_OPTION_ILLEGAL = "error.option.illegal";
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public void fillValues(Long organizationId, Long projectId, Long instanceId, String schemeCode, List<PageFieldViewDTO> pageFieldViews) {
        List<FieldValueDTO> values = modelMapper.map(fieldValueMapper.queryList(projectId, instanceId, schemeCode, null), new TypeToken<List<FieldValueDTO>>() {
        }.getType());
        Map<Long, List<FieldValueDTO>> valueGroup = values.stream().collect(Collectors.groupingBy(FieldValueDTO::getFieldId));
        pageFieldViews.forEach(view -> view.setFieldValues(valueGroup.get(view.getFieldId())));
    }

    @Override
    public void createFieldValues(Long projectId, Long instanceId, String schemeCode, List<PageFieldViewCreateDTO> createDTOs) {
        if (!EnumUtil.contain(ObjectSchemeCode.class, schemeCode)) {
            throw new CommonException(ERROR_SCHEMECODE_ILLEGAL);
        }
        List<FieldValue> fieldValues = new ArrayList<>();
        createDTOs.forEach(createDTO -> {
            List<FieldValue> values = modelMapper.map(createDTO.getFieldValues(), new TypeToken<List<FieldValue>>() {
            }.getType());
            values.forEach(value -> {
                value.setFieldId(createDTO.getFieldId());
            });
            fieldValues.addAll(values);
        });
        fieldValueMapper.batchInsert(projectId, instanceId, schemeCode, fieldValues);
    }

    @Override
    public List<FieldValueDTO> updateFieldValue(Long organizationId, Long projectId, Long instanceId, Long fieldId, String schemeCode, List<FieldValueUpdateDTO> updateDTOs) {
        if (!EnumUtil.contain(ObjectSchemeCode.class, schemeCode)) {
            throw new CommonException(ERROR_SCHEMECODE_ILLEGAL);
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
        List<FieldValue> fieldValues = modelMapper.map(updateDTOs, new TypeToken<List<FieldValue>>() {
        }.getType());
        fieldValues.forEach(fieldValue -> fieldValue.setFieldId(fieldId));
        fieldValueMapper.batchInsert(projectId, instanceId, schemeCode, fieldValues);
        return modelMapper.map(fieldValueMapper.queryList(projectId, instanceId, schemeCode, fieldId), new TypeToken<List<FieldValueDTO>>() {
        }.getType());
    }

    @Override
    public void checkDeleteOption(Long fieldId, List<Long> optionIds) {
        if (!optionIds.isEmpty()) {
            List<FieldValue> fieldValues = fieldValueMapper.queryByOptionIds(fieldId, optionIds);
            if (!fieldValues.isEmpty()) {
                throw new CommonException(ERROR_OPTION_ILLEGAL);
            }
        }
    }
}
