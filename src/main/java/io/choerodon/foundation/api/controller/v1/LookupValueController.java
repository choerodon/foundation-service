package io.choerodon.foundation.api.controller.v1;

import io.choerodon.base.annotation.Permission;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.core.exception.CommonException;
import io.choerodon.foundation.api.dto.LookupTypeWithValuesDTO;
import io.choerodon.foundation.api.service.LookupValueService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
@RestController
@RequestMapping(value = "/v1/organizations/{organization_id}/lookup_values")
public class LookupValueController {

    @Autowired
    private LookupValueService lookupValueService;

    @Permission(type = ResourceType.ORGANIZATION, permissionPublic = true)
    @ApiOperation("根据type code查询其下的value值")
    @GetMapping(value = "/{typeCode}")
    public ResponseEntity<LookupTypeWithValuesDTO> queryLookupValueByCode(@ApiParam(value = "项目id", required = true)
                                                                          @PathVariable(name = "organization_id") Long organizationId,
                                                                          @ApiParam(value = "type code", required = true)
                                                                          @PathVariable String typeCode) {
        return Optional.ofNullable(lookupValueService.queryLookupValueByCode(organizationId, typeCode))
                .map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                .orElseThrow(() -> new CommonException("error.lookupValueList.get"));
    }

}