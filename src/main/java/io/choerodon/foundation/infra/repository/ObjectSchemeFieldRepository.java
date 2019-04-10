package io.choerodon.foundation.infra.repository;

import io.choerodon.foundation.domain.ObjectSchemeField;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
public interface ObjectSchemeFieldRepository {
    ObjectSchemeField create(ObjectSchemeField field);

    void delete(Long fieldId);

    void update(ObjectSchemeField field);

    ObjectSchemeField queryById(Long organizationId, Long projectId, Long fieldId);
}
