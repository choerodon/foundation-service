package io.choerodon.foundation.api.service;

import io.choerodon.core.domain.Page;
import io.choerodon.foundation.api.dto.ObjectSchemeDTO;
import io.choerodon.foundation.api.dto.ObjectSchemeSearchDTO;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * @author shinan.chen
 * @since 2019/3/29
 */
public interface ObjectSchemeService {

    Page<ObjectSchemeDTO> pageQuery(Long organizationId, PageRequest pageRequest, ObjectSchemeSearchDTO searchDTO);
}
