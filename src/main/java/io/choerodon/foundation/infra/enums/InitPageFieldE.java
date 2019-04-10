package io.choerodon.foundation.infra.enums;

/**
 * @author shinan.chen
 * @since 2019/4/2
 */
public class InitPageFieldE {

    public enum AgileIssueCreateE {
        IssueType(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.ISSUE_TYPE, true),
        Summary(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.SUMMARY, true),
        Priority(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.PRIORITY, true),
        Description(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.DESCRIPTION, true),
        EstimateTime(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.ESTIMATE_TIME, true),
        StoryPoint(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.STORY_POINT, true),
        Assignee(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.ASSIGNEE, true),
        Epic(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.EPIC, true),
        Sprint(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.SPRINT, true),
        FixVersion(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.FIX_VERSION, true),
        Component(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.COMPONENT, true),
        Label(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_CREATE, FieldCode.LABEL, true);
        private String schemeCode;
        private String pageCode;
        private String fieldCode;
        private Boolean display;

        AgileIssueCreateE(String schemeCode, String pageCode, String fieldCode, Boolean display) {
            this.schemeCode = schemeCode;
            this.pageCode = pageCode;
            this.fieldCode = fieldCode;
            this.display = display;
        }

        public String getSchemeCode() {
            return schemeCode;
        }

        public String getPageCode() {
            return pageCode;
        }

        public String getFieldCode() {
            return fieldCode;
        }

        public Boolean getDisplay() {
            return display;
        }
    }

    public enum AgileIssueEditE {
        Status(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.STATUS, true),
        Priority(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.PRIORITY, true),
        Component(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.COMPONENT, true),
        Label(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.LABEL, true),
        InfluenceVersion(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.INFLUENCE_VERSION, true),
        FixVersion(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.FIX_VERSION, true),
        Epic(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.EPIC, true),
        EpicName(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.EPIC_NAME, true),
        Sprint(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.SPRINT, true),
        TimeTrace(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.TIME_TRACE, true),
        Reporter(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.REPORTER, true),
        Assignee(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.ASSIGNEE, true),
        CreationDate(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.CREATION_DATE, true),
        LastUpdateDate(ObjectSchemeCode.AGILE_ISSUE, PageCode.AGILE_ISSUE_EDIT, FieldCode.LAST_UPDATE_DATE, true);

        private String schemeCode;
        private String pageCode;
        private String fieldCode;
        private Boolean display;

        AgileIssueEditE(String schemeCode, String pageCode, String fieldCode, Boolean display) {
            this.schemeCode = schemeCode;
            this.pageCode = pageCode;
            this.fieldCode = fieldCode;
            this.display = display;
        }

        public String getSchemeCode() {
            return schemeCode;
        }

        public String getPageCode() {
            return pageCode;
        }

        public String getFieldCode() {
            return fieldCode;
        }

        public Boolean getDisplay() {
            return display;
        }
    }
}
