package io.choerodon.foundation.infra.repository;

import io.choerodon.foundation.domain.FieldOption;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
public interface FieldOptionRepository {
    FieldOption create(FieldOption option);

    void delete(Long optionId);

    void update(FieldOption option);

    FieldOption queryById(Long organizationId, Long optionId);
}
