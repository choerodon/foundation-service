package io.choerodon.foundation.api.controller.v1;

import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.core.base.BaseController;
import com.github.pagehelper.PageInfo;
import io.choerodon.core.iam.InitRoleCode;
import io.choerodon.foundation.api.dto.ObjectSchemeDTO;
import io.choerodon.foundation.api.dto.ObjectSchemeSearchDTO;
import io.choerodon.foundation.api.service.ObjectSchemeService;
import io.choerodon.mybatis.annotation.SortDefault;
import io.choerodon.base.domain.PageRequest;
import io.choerodon.base.domain.Sort;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author shinan.chen
 * @since 2019/3/29
 */
@RestController
@RequestMapping(value = "/v1/organizations/{organization_id}/object_scheme")
public class ObjectSchemeController extends BaseController {
    @Autowired
    private ObjectSchemeService objectSchemeService;

    @Permission(type = ResourceType.ORGANIZATION, roles = {InitRoleCode.ORGANIZATION_ADMINISTRATOR, InitRoleCode.ORGANIZATION_MEMBER})
    @ApiOperation(value = "分页查询对象方案列表")
    @CustomPageRequest
    @PostMapping
    public ResponseEntity<PageInfo<ObjectSchemeDTO>> pageQuery(@ApiIgnore
                                                           @SortDefault(value = "id", direction = Sort.Direction.DESC) PageRequest pageRequest,
                                                           @ApiParam(value = "组织id", required = true)
                                                           @PathVariable("organization_id") Long organizationId,
                                                           @ApiParam(value = "search dto", required = true)
                                                           @RequestBody(required = false) ObjectSchemeSearchDTO searchDTO) {
        return new ResponseEntity<>(objectSchemeService.pageQuery(organizationId, pageRequest, searchDTO), HttpStatus.OK);
    }
}
