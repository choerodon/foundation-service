package io.choerodon.foundation.infra.utils;

public final class SagaTopic {

    private SagaTopic() {
    }

    public static class Project {
        private Project() {
        }

        public static final String PROJECT_CREATE = "iam-create-project";
        public static final String TASK_PROJECT_UPDATE = "foundation-create-project";
    }

    public static class Organization {
        private Organization() {
        }

        public static final String ORG_CREATE = "org-create-organization";
        public static final String TASK_ORG_CREATE = "foundation-create-organization";

    }


}
