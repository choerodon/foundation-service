package io.choerodon.foundation.infra.mapper;

import io.choerodon.foundation.api.dto.PageSearchDTO;
import io.choerodon.foundation.domain.Page;
import io.choerodon.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
public interface PageMapper extends Mapper<Page> {
    /**
     * 分页查询页面
     *
     * @param organizationId
     * @param searchDTO
     * @return
     */
    List<Page> fulltextSearch(@Param("organizationId") Long organizationId, @Param("searchDTO") PageSearchDTO searchDTO);
}
