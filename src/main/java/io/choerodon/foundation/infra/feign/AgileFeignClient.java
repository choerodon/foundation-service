package io.choerodon.foundation.infra.feign;

import io.choerodon.foundation.infra.feign.dto.DataLogCreateDTO;
import io.choerodon.foundation.infra.feign.dto.DataLogDTO;
import io.choerodon.foundation.infra.feign.fallback.AgileFeignClientFallback;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author shinan.chen
 * @since 2019/6/12
 */
@FeignClient(value = "agile-service", fallback = AgileFeignClientFallback.class)
@Component
public interface AgileFeignClient {

    /**
     * 创建自定义字段的DataLog
     *
     * @param projectId
     * @param createDTO
     * @return
     */
    @PostMapping("/v1/projects/{project_id}/data_log")
    ResponseEntity<DataLogDTO> createDataLog(@ApiParam(value = "项目id", required = true)
                                             @PathVariable(name = "project_id") Long projectId,
                                             @ApiParam(value = "data log object", required = true)
                                             @RequestBody DataLogCreateDTO createDTO);
}
