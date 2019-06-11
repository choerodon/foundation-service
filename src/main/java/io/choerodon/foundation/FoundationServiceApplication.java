package io.choerodon.foundation;

import io.choerodon.resource.annoation.EnableChoerodonResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author shinan.chen
 * @date 2019/3/29
 */
@SpringBootApplication
@EnableEurekaClient
@EnableChoerodonResourceServer
public class FoundationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoundationServiceApplication.class, args);
    }
}

