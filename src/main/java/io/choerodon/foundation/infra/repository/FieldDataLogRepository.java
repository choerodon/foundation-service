package io.choerodon.foundation.infra.repository;

import io.choerodon.foundation.domain.FieldDataLog;

/**
 * @author shinan.chen
 * @since 2019/6/19
 */
public interface FieldDataLogRepository {
    FieldDataLog create(FieldDataLog create);
}
