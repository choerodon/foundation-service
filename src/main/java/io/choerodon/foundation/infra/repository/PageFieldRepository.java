package io.choerodon.foundation.infra.repository;

import io.choerodon.foundation.domain.PageField;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
public interface PageFieldRepository {
    PageField create(PageField pageField);

    void delete(Long pageFieldId);

    void update(PageField pageField);

    PageField queryById(Long organizationId, Long projectId, Long pageFieldId);
}
