package io.choerodon.foundation.infra.mapper;

import io.choerodon.foundation.domain.FieldDataLog;
import io.choerodon.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/6/19
 */
public interface FieldDataLogMapper extends Mapper<FieldDataLog> {
    List<FieldDataLog> queryByInstanceId(@Param("projectId") Long projectId, @Param("schemeCode") String schemeCode, @Param("instanceId") Long instanceId);
}
