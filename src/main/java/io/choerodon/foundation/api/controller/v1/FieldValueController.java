package io.choerodon.foundation.api.controller.v1;

import io.choerodon.core.iam.InitRoleCode;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.foundation.api.dto.*;
import io.choerodon.foundation.api.service.FieldValueService;
import io.choerodon.foundation.api.service.PageFieldService;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
@RestController
@RequestMapping("/v1/projects/{project_id}/field_value")
public class FieldValueController {

    @Autowired
    private PageFieldService pageFieldService;
    @Autowired
    private FieldValueService fieldValueService;

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "界面上获取字段列表，带有字段选项")
    @PostMapping("/list")
    public ResponseEntity<List<PageFieldViewDTO>> queryPageFieldViewList(@ApiParam(value = "项目id", required = true)
                                                                         @PathVariable("project_id") Long projectId,
                                                                         @ApiParam(value = "组织id", required = true)
                                                                         @RequestParam Long organizationId,
                                                                         @ApiParam(value = "参数对象", required = true)
                                                                         @RequestBody @Valid PageFieldViewParamDTO paramDTO) {
        return new ResponseEntity<>(pageFieldService.queryPageFieldViewList(organizationId, projectId, paramDTO), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "根据实例id从界面上获取字段列表，带有字段值、字段选项")
    @PostMapping("/list/{instance_id}")
    public ResponseEntity<List<PageFieldViewDTO>> queryPageFieldViewListWithInstanceId(@ApiParam(value = "项目id", required = true)
                                                                                       @PathVariable("project_id") Long projectId,
                                                                                       @ApiParam(value = "实例id", required = true)
                                                                                       @PathVariable("instance_id") Long instanceId,
                                                                                       @ApiParam(value = "组织id", required = true)
                                                                                       @RequestParam Long organizationId,
                                                                                       @ApiParam(value = "参数对象", required = true)
                                                                                       @RequestBody @Valid PageFieldViewParamDTO paramDTO) {
        return new ResponseEntity<>(pageFieldService.queryPageFieldViewListWithInstanceId(organizationId, projectId, instanceId, paramDTO), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "创建实例时，批量创建字段值")
    @PostMapping("/{instance_id}")
    public ResponseEntity createFieldValues(@ApiParam(value = "项目id", required = true)
                                            @PathVariable("project_id") Long projectId,
                                            @ApiParam(value = "实例id", required = true)
                                            @PathVariable("instance_id") Long instanceId,
                                            @ApiParam(value = "组织id", required = true)
                                            @RequestParam Long organizationId,
                                            @ApiParam(value = "方案编码", required = true)
                                            @RequestParam String schemeCode,
                                            @ApiParam(value = "自定义字段列表（包含值）", required = true)
                                            @RequestBody List<PageFieldViewCreateDTO> createDTOs) {
        fieldValueService.createFieldValues(organizationId, projectId, instanceId, schemeCode, createDTOs);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_MEMBER})
    @ApiOperation(value = "保存值/修改值")
    @PostMapping("/update/{instance_id}")
    public ResponseEntity<List<FieldValueDTO>> updateFieldValue(@ApiParam(value = "项目id", required = true)
                                                                @PathVariable("project_id") Long projectId,
                                                                @ApiParam(value = "组织id", required = true)
                                                                @RequestParam Long organizationId,
                                                                @ApiParam(value = "实例id", required = true)
                                                                @PathVariable("instance_id") Long instanceId,
                                                                @ApiParam(value = "字段id", required = true)
                                                                @RequestParam Long fieldId,
                                                                @ApiParam(value = "方案编码", required = true)
                                                                @RequestParam String schemeCode,
                                                                @ApiParam(value = "值对象列表", required = true)
                                                                @RequestBody PageFieldViewUpdateDTO updateDTO) {
        return new ResponseEntity<>(fieldValueService.updateFieldValue(organizationId, projectId, instanceId, fieldId, schemeCode, updateDTO), HttpStatus.OK);
    }
}
