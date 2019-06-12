package io.choerodon.foundation.config;

import io.choerodon.foundation.infra.feign.AgileFeignClient;
import io.choerodon.foundation.infra.feign.IamFeignClient;
import io.choerodon.foundation.infra.feign.dto.DataLogCreateDTO;
import io.choerodon.foundation.infra.feign.dto.DataLogDTO;
import io.choerodon.foundation.infra.feign.dto.UserDO;
import io.choerodon.foundation.infra.feign.fallback.AgileFeignClientFallback;
import io.choerodon.foundation.infra.feign.fallback.IamFeignClientFallback;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

/**
 * @author shinan.chen
 * @since 2019/6/11
 */
@Configuration
public class FeignConfigure {
    @Bean
    @Primary
    IamFeignClient iamFeignClient() {
        IamFeignClient iamFeignClient = Mockito.mock(IamFeignClientFallback.class);
        UserDO userDO = new UserDO();
        userDO.setId(1L);
        userDO.setRealName("test");
        Mockito.when(iamFeignClient.listUsersByIds(Matchers.anyObject(), Matchers.anyBoolean())).thenReturn(new ResponseEntity<>(Arrays.asList(userDO), HttpStatus.OK));
        return iamFeignClient;
    }

    @Bean
    @Primary
    AgileFeignClient agileFeignClient() {
        AgileFeignClient agileFeignClient = Mockito.mock(AgileFeignClientFallback.class);
        Mockito.when(agileFeignClient.createDataLog(Matchers.anyLong(), Matchers.any(DataLogCreateDTO.class))).thenReturn(new ResponseEntity<>(new DataLogDTO(), HttpStatus.OK));
        return agileFeignClient;
    }
}