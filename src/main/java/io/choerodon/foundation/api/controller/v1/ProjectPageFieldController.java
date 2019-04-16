package io.choerodon.foundation.api.controller.v1;

import io.choerodon.core.iam.InitRoleCode;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.foundation.api.dto.AdjustOrderDTO;
import io.choerodon.foundation.api.dto.PageFieldDTO;
import io.choerodon.foundation.api.dto.PageFieldUpdateDTO;
import io.choerodon.foundation.api.service.PageFieldService;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
@RestController
@RequestMapping("/v1/projects/{project_id}/page_field")
public class ProjectPageFieldController {

    @Autowired
    private PageFieldService pageFieldService;

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "根据页面编码获取页面字段列表")
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listQuery(@ApiParam(value = "项目id", required = true)
                                                         @PathVariable("project_id") Long projectId,
                                                         @ApiParam(value = "组织id", required = true)
                                                         @RequestParam Long organizationId,
                                                         @ApiParam(value = "页面编码", required = true)
                                                         @RequestParam String pageCode) {
        return new ResponseEntity<>(pageFieldService.listQuery(organizationId, projectId, pageCode), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "调整顺序")
    @PostMapping(value = "/adjust_order")
    public ResponseEntity<PageFieldDTO> adjustFieldOrder(@ApiParam(value = "项目id", required = true)
                                                         @PathVariable("project_id") Long projectId,
                                                         @ApiParam(value = "组织id", required = true)
                                                         @RequestParam Long organizationId,
                                                         @ApiParam(value = "页面编码", required = true)
                                                         @RequestParam String pageCode,
                                                         @ApiParam(value = "调整顺序对象", required = true)
                                                         @RequestBody AdjustOrderDTO adjustOrder) {
        return new ResponseEntity<>(pageFieldService.adjustFieldOrder(organizationId, projectId, pageCode, adjustOrder), HttpStatus.CREATED);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "修改页面字段")
    @PutMapping(value = "/{field_id}")
    public ResponseEntity<PageFieldDTO> update(@ApiParam(value = "项目id", required = true)
                                               @PathVariable("project_id") Long projectId,
                                               @ApiParam(value = "组织id", required = true)
                                               @RequestParam Long organizationId,
                                               @ApiParam(value = "页面编码", required = true)
                                               @RequestParam String pageCode,
                                               @ApiParam(value = "页面字段id", required = true)
                                               @PathVariable("field_id") Long fieldId,
                                               @RequestBody @Valid PageFieldUpdateDTO updateDTO) {
        return new ResponseEntity<>(pageFieldService.update(organizationId, projectId, pageCode, fieldId, updateDTO), HttpStatus.CREATED);
    }
}
