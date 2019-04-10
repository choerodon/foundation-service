package io.choerodon.foundation.infra.enums;

/**
 * @author shinan.chen
 * @since 2019/4/2
 */
public enum InitPageE {

    AGILE_ISSUE_CREATE("敏捷问题创建页面", ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE),
    AGILE_ISSUE_EDIT("敏捷问题编辑页面", ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT);

    private String name;
    private String schemeCode;
    private String pageCode;

    InitPageE(String name, String schemeCode, String pageCode) {
        this.name = name;
        this.schemeCode = schemeCode;
        this.pageCode = pageCode;
    }

    public String getName() {
        return name;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public String getPageCode() {
        return pageCode;
    }
}
