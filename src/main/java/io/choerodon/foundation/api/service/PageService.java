package io.choerodon.foundation.api.service;

import io.choerodon.core.domain.Page;
import io.choerodon.foundation.api.dto.PageDTO;
import io.choerodon.foundation.api.dto.PageSearchDTO;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
public interface PageService {
    Page<PageDTO> pageQuery(Long organizationId, PageRequest pageRequest, PageSearchDTO searchDTO);

    /**
     * 组织层初始化页面
     *
     * @param organizationId
     */
    void initPageByOrg(Long organizationId);
}
