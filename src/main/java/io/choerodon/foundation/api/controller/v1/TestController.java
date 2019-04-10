package io.choerodon.foundation.api.controller.v1;

import io.choerodon.core.base.BaseController;
import io.choerodon.core.iam.InitRoleCode;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.foundation.api.service.ObjectSchemeService;
import io.choerodon.foundation.api.service.PageFieldService;
import io.choerodon.foundation.api.service.PageService;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shinan.chen
 * @since 2019/3/29
 */
@RestController
@RequestMapping(value = "/v1/organizations/{organization_id}/test")
public class TestController extends BaseController {
    @Autowired
    private ObjectSchemeService objectSchemeService;
    @Autowired
    private PageService pageService;
    @Autowired
    private PageFieldService pageFieldService;

    @Permission(level = ResourceLevel.ORGANIZATION, roles = {InitRoleCode.ORGANIZATION_ADMINISTRATOR, InitRoleCode.ORGANIZATION_MEMBER})
    @ApiOperation(value = "initOrg")
    @GetMapping("/initOrg")
    public ResponseEntity initOrg(@ApiParam(value = "组织id", required = true)
                                  @PathVariable("organization_id") Long organizationId) {
        //初始化对象方案
        objectSchemeService.initObjectSchemeByOrg(organizationId);
        //初始化页面
        pageService.initPageByOrg(organizationId);
        //初始化页面字段
        pageFieldService.initPageFieldByOrg(organizationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
