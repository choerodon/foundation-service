package io.choerodon.foundation.api.service;

import io.choerodon.foundation.api.dto.FieldValueDTO;
import io.choerodon.foundation.api.dto.FieldValueUpdateDTO;
import io.choerodon.foundation.api.dto.PageFieldViewCreateDTO;
import io.choerodon.foundation.api.dto.PageFieldViewDTO;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
public interface FieldValueService {
    /**
     * 填充字段值
     *
     * @param organizationId
     * @param projectId
     * @param instanceId
     * @param schemeCode
     * @param pageFieldViews
     */
    void fillValues(Long organizationId, Long projectId, Long instanceId, String schemeCode, List<PageFieldViewDTO> pageFieldViews);

    /**
     * 创建实例时，批量创建值
     *
     * @param projectId
     * @param instanceId
     * @param schemeCode
     * @param createDTOs
     */
    void createFieldValues(Long projectId, Long instanceId, String schemeCode, List<PageFieldViewCreateDTO> createDTOs);

    /**
     * 保存值/修改值
     *
     * @param organizationId
     * @param projectId
     * @param instanceId
     * @param fieldId
     * @param schemeCode
     * @param updateDTOs
     * @return
     */
    List<FieldValueDTO> updateFieldValue(Long organizationId, Long projectId, Long instanceId, Long fieldId, String schemeCode, List<FieldValueUpdateDTO> updateDTOs);

    /**
     * 校验是否能删除option
     *
     * @param fieldId
     * @param optionIds
     */
    void checkDeleteOption(Long fieldId, List<Long> optionIds);

    /**
     * 删除字段相关值
     *
     * @param fieldId
     */
    void deleteByFieldId(Long fieldId);
}