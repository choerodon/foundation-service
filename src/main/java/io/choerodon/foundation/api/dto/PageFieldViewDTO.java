package io.choerodon.foundation.api.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
public class PageFieldViewDTO {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "字段id")
    private Long fieldId;
    @ApiModelProperty(value = "字段名称")
    private String fieldName;
    @ApiModelProperty(value = "字段类型")
    private String fieldType;
    @ApiModelProperty(value = "默认值")
    private String defaultValue;
    @ApiModelProperty(value = "额外配置（是否当前时间/是否包括小数）")
    private Boolean extraConfig;
    @ApiModelProperty(value = "是否显示")
    private Boolean display;
    @ApiModelProperty(value = "是否系统")
    private Boolean system;
    @ApiModelProperty(value = "是否必填")
    private Boolean required;
    @ApiModelProperty(value = "rank排序")
    private String rank;
    @ApiModelProperty(value = "项目id")
    private Long projectId;
    @ApiModelProperty(value = "组织id")
    private Long organizationId;
    @ApiModelProperty(value = "字段选项列表")
    private List<FieldOptionDTO> fieldOptions;
    @ApiModelProperty(value = "字段值列表")
    private List<FieldValueDTO> fieldValues;

    public List<FieldValueDTO> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<FieldValueDTO> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public List<FieldOptionDTO> getFieldOptions() {
        return fieldOptions;
    }

    public void setFieldOptions(List<FieldOptionDTO> fieldOptions) {
        this.fieldOptions = fieldOptions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getExtraConfig() {
        return extraConfig;
    }

    public void setExtraConfig(Boolean extraConfig) {
        this.extraConfig = extraConfig;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
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
}