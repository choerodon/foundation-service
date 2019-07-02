package io.choerodon.foundation.infra.repository;

import io.choerodon.foundation.api.dto.ObjectSchemeFieldSearchDTO;
import io.choerodon.foundation.domain.ObjectSchemeField;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
public interface ObjectSchemeFieldRepository {
    ObjectSchemeField create(ObjectSchemeField field);

    void delete(Long fieldId);

    void update(ObjectSchemeField field);

    ObjectSchemeField queryById(Long organizationId, Long projectId, Long fieldId);

    List<ObjectSchemeField> listQuery(Long organizationId, Long projectId, ObjectSchemeFieldSearchDTO searchDTO);

    ObjectSchemeField queryByFieldCode(Long organizationId, Long projectId, String fieldCode);
}
