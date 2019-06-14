package io.choerodon.foundation.api.service;

import com.github.pagehelper.PageInfo;
import io.choerodon.foundation.api.dto.PageDTO;
import io.choerodon.foundation.api.dto.PageSearchDTO;
import io.choerodon.base.domain.PageRequest;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
public interface PageService {
    PageInfo<PageDTO> pageQuery(Long organizationId, PageRequest pageRequest, PageSearchDTO searchDTO);
}
