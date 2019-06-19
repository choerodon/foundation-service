package io.choerodon.foundation.api.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author shinan.chen
 * @since 2019/6/19
 */
public class FieldDataLogDTO {
    @ApiModelProperty(value = "字段值id")
    private Long id;
    @ApiModelProperty(value = "实例对象id")
    private Long instanceId;
    @ApiModelProperty(value = "字段id")
    private Long fieldId;

    @ApiModelProperty(value = "旧值")
    private String oldValue;
    @ApiModelProperty(value = "旧值str")
    private String oldString;
    @ApiModelProperty(value = "新值")
    private String newValue;
    @ApiModelProperty(value = "新值str")
    private String newString;

    @ApiModelProperty(value = "项目id")
    private Long projectId;
    @ApiModelProperty(value = "方案编码：用于区分不同方案字段，否则可能instanceId一样")
    private String schemeCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getOldString() {
        return oldString;
    }

    public void setOldString(String oldString) {
        this.oldString = oldString;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getNewString() {
        return newString;
    }

    public void setNewString(String newString) {
        this.newString = newString;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }
}
