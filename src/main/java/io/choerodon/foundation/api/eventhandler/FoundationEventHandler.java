package io.choerodon.foundation.api.eventhandler;

import com.alibaba.fastjson.JSONObject;
import io.choerodon.asgard.saga.annotation.SagaTask;
import io.choerodon.foundation.api.dto.payload.OrganizationCreateEventPayload;
import io.choerodon.foundation.api.service.ObjectSchemeService;
import io.choerodon.foundation.api.service.PageFieldService;
import io.choerodon.foundation.api.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.choerodon.foundation.infra.utils.SagaTopic.Organization.ORG_CREATE;
import static io.choerodon.foundation.infra.utils.SagaTopic.Organization.TASK_ORG_CREATE;

/**
 * @author shinan.chen
 * @since 2019/4/2
 */
@Component
public class FoundationEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(FoundationEventHandler.class);

    @Autowired
    private ObjectSchemeService objectSchemeService;
    @Autowired
    private PageService pageService;
    @Autowired
    private PageFieldService pageFieldService;

    @SagaTask(code = TASK_ORG_CREATE,
            description = "创建组织事件",
            sagaCode = ORG_CREATE,
            seq = 1)
    public String handleOrgaizationCreateByConsumeSagaTask(String data) {
        LOGGER.info("consume create organization saga: {}", data);
        OrganizationCreateEventPayload organizationEventPayload = JSONObject.parseObject(data, OrganizationCreateEventPayload.class);
        Long organizationId = organizationEventPayload.getId();
        //初始化页面
        pageService.initPageByOrg(organizationId);
        //初始化页面字段
        pageFieldService.initPageFieldByOrg(organizationId);
        return data;
    }
}
