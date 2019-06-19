package io.choerodon.foundation.api.service.impl;

import io.choerodon.foundation.api.dto.FieldDataLogCreateDTO;
import io.choerodon.foundation.api.dto.FieldDataLogDTO;
import io.choerodon.foundation.api.service.FieldDataLogService;
import io.choerodon.foundation.domain.FieldDataLog;
import io.choerodon.foundation.infra.mapper.FieldDataLogMapper;
import io.choerodon.foundation.infra.repository.FieldDataLogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shinan.chen
 * @since 2019/6/19
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class FieldDataLogServiceImpl implements FieldDataLogService {
    @Autowired
    private FieldDataLogMapper fieldDataLogMapper;
    @Autowired
    private FieldDataLogRepository fieldDataLogRepository;

    private static final String ERROR_PAGECODE_ILLEGAL = "error.pageCode.illegal";
    private static final String ERROR_CONTEXT_ILLEGAL = "error.context.illegal";
    private static final String ERROR_SCHEMECODE_ILLEGAL = "error.schemeCode.illegal";
    private static final String ERROR_OPTION_ILLEGAL = "error.option.illegal";
    private static final String ERROR_FIELDTYPE_ILLEGAL = "error.fieldType.illegal";
    private static final String ERROR_SYSTEM_ILLEGAL = "error.system.illegal";

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public FieldDataLogDTO createDataLog(Long projectId, String schemeCode, FieldDataLogCreateDTO create) {
        FieldDataLog dataLog = modelMapper.map(create, FieldDataLog.class);
        dataLog.setProjectId(projectId);
        dataLog.setSchemeCode(schemeCode);
        return modelMapper.map(fieldDataLogRepository.create(dataLog), FieldDataLogDTO.class);
    }

    @Override
    public void deleteByFieldId(Long projectId, Long fieldId) {
        FieldDataLog delete = new FieldDataLog();
        delete.setFieldId(fieldId);
        delete.setProjectId(projectId);
        fieldDataLogMapper.delete(delete);
    }
}
