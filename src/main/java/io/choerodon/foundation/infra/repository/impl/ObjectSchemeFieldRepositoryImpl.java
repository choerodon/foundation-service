package io.choerodon.foundation.infra.repository.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.foundation.api.dto.ObjectSchemeFieldSearchDTO;
import io.choerodon.foundation.domain.ObjectSchemeField;
import io.choerodon.foundation.infra.enums.FieldCode;
import io.choerodon.foundation.infra.mapper.ObjectSchemeFieldMapper;
import io.choerodon.foundation.infra.repository.ObjectSchemeFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
@Component
public class ObjectSchemeFieldRepositoryImpl implements ObjectSchemeFieldRepository {
    @Autowired
    private ObjectSchemeFieldMapper objectSchemeFieldMapper;

    private static final String ERROR_FIELD_ILLEGAL = "error.field.illegal";
    private static final String ERROR_FIELD_CREATE = "error.field.create";
    private static final String ERROR_FIELD_DELETE = "error.field.delete";
    private static final String ERROR_FIELD_NOTFOUND = "error.field.notFound";
    private static final String ERROR_FIELD_UPDATE = "error.field.update";

    @Override
    public ObjectSchemeField create(ObjectSchemeField field) {
        field.setSystem(false);
        field.setRequired(false);
        if (objectSchemeFieldMapper.insert(field) != 1) {
            throw new CommonException(ERROR_FIELD_CREATE);
        }
        return objectSchemeFieldMapper.selectByPrimaryKey(field.getId());
    }

    @Override
    public void delete(Long fieldId) {
        if (objectSchemeFieldMapper.deleteByPrimaryKey(fieldId) != 1) {
            throw new CommonException(ERROR_FIELD_DELETE);
        }
    }

    @Override
    public void update(ObjectSchemeField field) {
        if (objectSchemeFieldMapper.updateByPrimaryKeySelective(field) != 1) {
            throw new CommonException(ERROR_FIELD_UPDATE);
        }
    }

    @Override
    public ObjectSchemeField queryById(Long organizationId, Long projectId, Long fieldId) {
        ObjectSchemeField field = objectSchemeFieldMapper.queryById(fieldId);
        if (field == null) {
            throw new CommonException(ERROR_FIELD_NOTFOUND);
        }
        if (!field.getOrganizationId().equals(organizationId) && !field.getOrganizationId().equals(0L)) {
            throw new CommonException(ERROR_FIELD_ILLEGAL);
        }
        if (field.getProjectId() != null && !field.getProjectId().equals(projectId) && !field.getProjectId().equals(0L)) {
            throw new CommonException(ERROR_FIELD_ILLEGAL);
        }
        return field;
    }

    @Override
    public List<ObjectSchemeField> listQuery(Long organizationId, Long projectId, ObjectSchemeFieldSearchDTO searchDTO) {
        List<ObjectSchemeField> fields = objectSchemeFieldMapper.listQuery(organizationId, projectId, searchDTO);
        return FieldCode.objectSchemeFieldsFilter(organizationId, projectId, fields);
    }
}
