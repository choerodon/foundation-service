package io.choerodon.foundation.infra.repository.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.foundation.domain.FieldDataLog;
import io.choerodon.foundation.infra.mapper.FieldDataLogMapper;
import io.choerodon.foundation.infra.repository.FieldDataLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shinan.chen
 * @since 2019/6/19
 */
@Component
public class FieldDataLogRepositoryImpl implements FieldDataLogRepository {
    @Autowired
    private FieldDataLogMapper fieldDataLogMapper;

    private static final String ERROR_DATALOG_ILLEGAL = "error.dataLog.illegal";
    private static final String ERROR_DATALOG_CREATE = "error.dataLog.create";

    @Override
    public FieldDataLog create(FieldDataLog create) {
        if (fieldDataLogMapper.insert(create) != 1) {
            throw new CommonException(ERROR_DATALOG_CREATE);
        }
        return fieldDataLogMapper.selectByPrimaryKey(create.getId());
    }
}
