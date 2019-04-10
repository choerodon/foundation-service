package io.choerodon.foundation.api.service;


import io.choerodon.foundation.api.dto.LookupTypeWithValuesDTO;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
public interface LookupValueService {

    LookupTypeWithValuesDTO queryLookupValueByCode(Long organizationId, String typeCode);

}