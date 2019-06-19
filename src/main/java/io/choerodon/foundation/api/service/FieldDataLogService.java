package io.choerodon.foundation.api.service;

import io.choerodon.foundation.api.dto.FieldDataLogCreateDTO;
import io.choerodon.foundation.api.dto.FieldDataLogDTO;

/**
 * @author shinan.chen
 * @since 2019/6/19
 */
public interface FieldDataLogService {

    FieldDataLogDTO createDataLog(Long projectId, String schemeCode, FieldDataLogCreateDTO create);

    void deleteByFieldId(Long projectId, Long fieldId);
}
