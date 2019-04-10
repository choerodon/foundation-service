package io.choerodon.foundation.api.service;


import io.choerodon.foundation.api.dto.LookupTypeDTO;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
public interface LookupTypeService {

    List<LookupTypeDTO> listLookupType(Long organizationId);
}