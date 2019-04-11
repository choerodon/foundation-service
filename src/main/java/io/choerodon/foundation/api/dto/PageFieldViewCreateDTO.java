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
    @ApiModelProperty(value = "字段值列表")
    private List<FieldValueUpdateDTO> fieldValues;

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }

    public List<FieldValueUpdateDTO> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(List<FieldValueUpdateDTO> fieldValues) {
        this.fieldValues = fieldValues;
    }
}
