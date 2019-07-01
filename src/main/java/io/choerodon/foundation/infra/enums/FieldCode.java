package io.choerodon.foundation.infra.enums;

import io.choerodon.foundation.domain.ObjectSchemeField;
import io.choerodon.foundation.domain.PageField;
import io.choerodon.foundation.infra.feign.IamFeignClient;
import io.choerodon.foundation.infra.feign.dto.ProjectCategoryDTO;
import io.choerodon.foundation.infra.feign.dto.ProjectDTO;
import io.choerodon.foundation.infra.utils.SpringBeanUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shinan.chen
 * @since 2019/4/2
 */
public class FieldCode {
    private FieldCode() {
    }

    public static final String ISSUE_TYPE = "issueType";
    public static final String SUMMARY = "summary";
    public static final String DESCRIPTION = "description";
    public static final String REMAINING_TIME = "remainingTime";
    public static final String STORY_POINTS = "storyPoints";
    public static final String STATUS = "status";
    public static final String PRIORITY = "priority";
    public static final String COMPONENT = "component";
    public static final String LABEL = "label";
    public static final String INFLUENCE_VERSION = "influenceVersion";
    public static final String FIX_VERSION = "fixVersion";
    public static final String EPIC = "epic";
    public static final String SPRINT = "sprint";
    public static final String EPIC_NAME = "epicName";
    public static final String REPORTER = "reporter";
    public static final String ASSIGNEE = "assignee";
    public static final String CREATION_DATE = "creationDate";
    public static final String LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String TIME_TRACE = "timeTrace";
    public static final String BENFIT_HYPOTHESIS = "benfitHypothesis";
    public static final String ACCEPTANCE_CRITERA = "acceptanceCritera";
    public static final String FEATURE_TYPE = "featureType";
    public static final String PI = "pi";

    /**
     * 项目群/项目群子项目/敏捷项目对字段的过滤
     *
     * @param organizationId
     * @param projectId
     * @param fields
     * @return
     */
    public static List<ObjectSchemeField> objectSchemeFieldsFilter(Long organizationId, Long projectId, List<ObjectSchemeField> fields) {
        if (projectId != null) {
            IamFeignClient iamFeignClient = SpringBeanUtil.getBean(IamFeignClient.class);
            ProjectDTO project = iamFeignClient.queryProjectInfo(projectId).getBody();
            if (project != null) {
                List<String> categoryCodes = project.getCategories().stream().map(ProjectCategoryDTO::getCode).collect(Collectors.toList());
                if (categoryCodes.contains(ProjectCategoryCode.PROGRAM)) {
                    //项目群
                    return fields;
                } else if (categoryCodes.contains(ProjectCategoryCode.PROGRAM_PROJECT)) {
                    //项目群子项目
                    return fields;
                } else {
                    //敏捷项目
                    return fields.stream().filter(field -> !field.getCode().equals(PI) && !field.getCode().equals(BENFIT_HYPOTHESIS) && !field.getCode().equals(ACCEPTANCE_CRITERA)).collect(Collectors.toList());
                }
            }
        }
        return fields;
    }

    /**
     * 项目群/项目群子项目/敏捷项目对字段的过滤
     *
     * @param organizationId
     * @param projectId
     * @param fields
     * @return
     */
    public static List<PageField> pageFieldsFilter(Long organizationId, Long projectId, List<PageField> fields) {
        if (projectId != null) {
            IamFeignClient iamFeignClient = SpringBeanUtil.getBean(IamFeignClient.class);
            ProjectDTO project = iamFeignClient.queryProjectInfo(projectId).getBody();
            if (project != null) {
                List<String> categoryCodes = project.getCategories().stream().map(ProjectCategoryDTO::getCode).collect(Collectors.toList());
                if (categoryCodes.contains(ProjectCategoryCode.PROGRAM)) {
                    //项目群
                    return fields;
                } else if (categoryCodes.contains(ProjectCategoryCode.PROGRAM_PROJECT)) {
                    //项目群子项目
                    return fields;
                } else {
                    //敏捷项目
                    return fields.stream().filter(field -> !field.getFieldCode().equals(PI) && !field.getFieldCode().equals(BENFIT_HYPOTHESIS) && !field.getFieldCode().equals(ACCEPTANCE_CRITERA)).collect(Collectors.toList());
                }
            }
        }
        return fields;
    }
}
