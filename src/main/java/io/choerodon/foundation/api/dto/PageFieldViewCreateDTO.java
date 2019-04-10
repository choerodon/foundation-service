package io.choerodon.foundation.api.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
public class PageFieldViewCreateDTO {
    @ApiModelProperty(value = "字段id")
    private Long fieldId;
    @ApiModelProperty(value = "项目id")
    private Long projectId;
    @ApiModelProperty(value = "组织id")
    private Long organizationId;
    @ApiModelProperty(value = "字段值列表")
    private List<FieldValueUpdateDTO> fieldValues;

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<FieldValueUpdateDTO> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<FieldValueUpdateDTO> fieldValues) {
        this.fieldValues = fieldValues;
    }
}
