package io.choerodon.foundation.domain;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
public class LookupTypeWithValues {

    private String typeCode;

    private String name;

    private String description;

    private Long objectVersionNumber;

    private List<LookupValue> lookupValues;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public List<LookupValue> getLookupValues() {
        return lookupValues;
    }

    public void setLookupValues(List<LookupValue> lookupValues) {
        this.lookupValues = lookupValues;
    }
}