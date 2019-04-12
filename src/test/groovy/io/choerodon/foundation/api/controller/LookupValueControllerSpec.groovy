package io.choerodon.foundation.api.controller

import io.choerodon.foundation.IntegrationTestConfiguration
import io.choerodon.foundation.api.dto.LookupTypeWithValuesDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification

/**
 * @author shinan.chen
 * @since 2019/4/12
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(IntegrationTestConfiguration)
@ActiveProfiles("test")
class LookupValueControllerSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    @Shared
    Long organizationId = 1L

    def url = '/v1/organizations/{organization_id}/lookup_values'

    def "queryLookupValueByCode"() {
        given:'准备'
        def typeCode = 'field_type'
        when: '根据type code查询其下的value值'
        def entity = restTemplate.exchange(url+"/{typeCode}", HttpMethod.GET, null,LookupTypeWithValuesDTO.class, organizationId,typeCode)

        then: '状态码为200，调用成功'
        def actRequest = false
        def actResponseContent = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null) {
                    if (entity.getBody().lookupValues.size() > 0) {
                        actResponseContent = true
                    }
                }
            }
        }
        actRequest == expRequest
        actResponseContent == expResponseContent

        where: '测试用例：'
        expRequest | expResponseContent
        true       | true
    }
}