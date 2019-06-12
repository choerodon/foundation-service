package io.choerodon.foundation.infra.feign.fallback;

import io.choerodon.core.exception.FeignException;
import io.choerodon.foundation.infra.feign.AgileFeignClient;
import io.choerodon.foundation.infra.feign.dto.DataLogCreateDTO;
import io.choerodon.foundation.infra.feign.dto.DataLogDTO;
import org.springframework.http.ResponseEntity;

/**
 * @author shinan.chen
 * @since 2019/6/12
 */
public class AgileFeignClientFallback implements AgileFeignClient {
    @Override
    public ResponseEntity<DataLogDTO> createDataLog(Long projectId, DataLogCreateDTO createDTO) {
        throw new FeignException("error.agileFeign.createDataLog");
    }
}
