package io.choerodon.foundation.api.controller.v1;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.InitRoleCode;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.foundation.api.dto.ObjectSchemeFieldCreateDTO;
import io.choerodon.foundation.api.dto.ObjectSchemeFieldDetailDTO;
import io.choerodon.foundation.api.dto.ObjectSchemeFieldUpdateDTO;
import io.choerodon.foundation.api.service.ObjectSchemeFieldService;
import io.choerodon.foundation.infra.enums.ObjectSchemeCode;
import io.choerodon.foundation.infra.utils.EnumUtil;
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
 * @since 2019/3/29
 */
@RestController
@RequestMapping("/v1/projects/{project_id}/object_scheme_field")
public class ProjectObjectSchemeFieldController {

    @Autowired
    private ObjectSchemeFieldService objectSchemeFieldService;

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "根据方案编码获取字段列表")
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listQuery(@ApiParam(value = "项目id", required = true)
                                                         @PathVariable("project_id") Long projectId,
                                                         @ApiParam(value = "组织id", required = true)
                                                         @RequestParam Long organizationId,
                                                         @ApiParam(value = "方案编码", required = true)
                                                         @RequestParam String schemeCode) {
        return new ResponseEntity<>(objectSchemeFieldService.listQuery(organizationId, projectId, schemeCode), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "创建字段")
    @PostMapping
    public ResponseEntity<ObjectSchemeFieldDetailDTO> create(@ApiParam(value = "项目id", required = true)
                                                             @PathVariable("project_id") Long projectId,
                                                             @ApiParam(value = "组织id", required = true)
                                                             @RequestParam Long organizationId,
                                                             @ApiParam(value = "字段对象", required = true)
                                                             @RequestBody @Valid ObjectSchemeFieldCreateDTO fieldCreateDTO) {
        return new ResponseEntity<>(objectSchemeFieldService.create(organizationId, projectId, fieldCreateDTO), HttpStatus.CREATED);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "查询字段详情")
    @GetMapping(value = "/{field_id}")
    public ResponseEntity<ObjectSchemeFieldDetailDTO> queryById(@ApiParam(value = "项目id", required = true)
                                                                @PathVariable("project_id") Long projectId,
                                                                @ApiParam(value = "组织id", required = true)
                                                                @RequestParam Long organizationId,
                                                                @ApiParam(value = "字段id", required = true)
                                                                @PathVariable("field_id") Long fieldId) {
        return new ResponseEntity<>(objectSchemeFieldService.queryById(organizationId, projectId, fieldId), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "删除字段")
    @DeleteMapping(value = "/{field_id}")
    public ResponseEntity delete(@ApiParam(value = "项目id", required = true)
                                 @PathVariable("project_id") Long projectId,
                                 @ApiParam(value = "组织id", required = true)
                                 @RequestParam Long organizationId,
                                 @PathVariable("field_id") Long fieldId) {
        objectSchemeFieldService.delete(organizationId, projectId, fieldId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "修改字段")
    @PutMapping(value = "/{field_id}")
    public ResponseEntity<ObjectSchemeFieldDetailDTO> update(@ApiParam(value = "项目id", required = true)
                                                             @PathVariable("project_id") Long projectId,
                                                             @ApiParam(value = "组织id", required = true)
                                                             @RequestParam Long organizationId,
                                                             @ApiParam(value = "字段id", required = true)
                                                             @PathVariable("field_id") Long fieldId,
                                                             @RequestBody @Valid ObjectSchemeFieldUpdateDTO updateDTO) {
        return new ResponseEntity<>(objectSchemeFieldService.update(organizationId, projectId, fieldId, updateDTO), HttpStatus.CREATED);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "校验字段名称是否已使用")
    @GetMapping(value = "/check_name")
    public ResponseEntity<Boolean> checkName(@ApiParam(value = "项目id", required = true)
                                             @PathVariable("project_id") Long projectId,
                                             @ApiParam(value = "组织id", required = true)
                                             @RequestParam Long organizationId,
                                             @ApiParam(value = "字段名称", required = true)
                                             @RequestParam("name") String name,
                                             @ApiParam(value = "方案编码", required = true)
                                             @RequestParam String schemeCode) {
        return new ResponseEntity<>(objectSchemeFieldService.checkName(organizationId, projectId, name, schemeCode), HttpStatus.OK);
    }

    @Permission(level = ResourceLevel.PROJECT, roles = {InitRoleCode.PROJECT_OWNER})
    @ApiOperation(value = "校验字段编码是否已使用")
    @GetMapping(value = "/check_code")
    public ResponseEntity<Boolean> checkCode(@ApiParam(value = "项目id", required = true)
                                             @PathVariable("project_id") Long projectId,
                                             @ApiParam(value = "组织id", required = true)
                                             @RequestParam Long organizationId,
                                             @ApiParam(value = "字段编码", required = true)
                                             @RequestParam("code") String code,
                                             @ApiParam(value = "方案编码", required = true)
                                             @RequestParam String schemeCode) {
        return new ResponseEntity<>(objectSchemeFieldService.checkCode(organizationId, projectId, code, schemeCode), HttpStatus.OK);
    }
}
