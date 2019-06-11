package io.choerodon.foundation.infra.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author shinan.chen
 * @since 2019/6/11
 */
@Configuration
@ConditionalOnProperty(prefix = "testScanIgnore", name = "enabled", havingValue = "false", matchIfMissing = true)
@EnableFeignClients("io.choerodon")
public class EnableFeignClientsConfiguration {
}