package io.choerodon.foundation.infra.mapper;

import io.choerodon.foundation.domain.FieldOption;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
public interface FieldOptionMapper extends BaseMapper<FieldOption> {

    /**
     * 根据字段id获取字段选项
     *
     * @param organizationId
     * @param fieldId
     * @return
     */
    List<FieldOption> selectByFieldId(@Param("organizationId") Long organizationId, @Param("fieldId") Long fieldId);

    /**
     * 根据字段id列表获取字段选项
     *
     * @param organizationId
     * @param fieldIds
     * @return
     */
    List<FieldOption> selectByFieldIds(@Param("organizationId") Long organizationId, @Param("fieldIds") List<Long> fieldIds);
}
