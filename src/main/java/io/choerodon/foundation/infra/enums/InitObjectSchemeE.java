package io.choerodon.foundation.infra.enums;

/**
 * @author shinan.chen
 * @since 2019/4/4
 */
public enum InitObjectSchemeE {

    AGILE_ISSUE("默认敏捷字段方案", ObjectSchemeCode.AGILE_ISSUE),
    TEST_ISSUE("默认测试字段方案", ObjectSchemeCode.TEST_ISSUE);

    private String name;
    private String schemeCode;

    InitObjectSchemeE(String name, String schemeCode) {
        this.name = name;
        this.schemeCode = schemeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }
}
