package io.choerodon.foundation.api.controller

import com.github.pagehelper.PageInfo
import io.choerodon.foundation.IntegrationTestConfiguration
import io.choerodon.foundation.api.dto.ObjectSchemeDTO
import io.choerodon.foundation.api.dto.ObjectSchemeSearchDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification

/**
 * @author shinan.chen
 * @since 2019/4/11
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(IntegrationTestConfiguration)
@ActiveProfiles("test")
class ObjectSchemeControllerSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate

    @Shared
    Long organizationId = 1L

    def url = '/v1/organizations/{organization_id}/object_scheme'

    def "pageQuery"() {
        when: '分页查询对象方案列表'
        ParameterizedTypeReference<PageInfo<ObjectSchemeDTO>> typeRef = new ParameterizedTypeReference<PageInfo<ObjectSchemeDTO>>() {
        }
        HttpEntity<ObjectSchemeSearchDTO> httpEntity = new HttpEntity<>(new ObjectSchemeSearchDTO())
        def entity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, typeRef, organizationId)

        then: '状态码为200，调用成功'
        def actRequest = false
        def actResponseSize = 0
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null) {
                    actResponseSize = entity.getBody().size
                }
            }
        }
        actRequest == expRequest
        actResponseSize == expResponseSize

        where: '测试用例：'
        expRequest | expResponseSize
        true       | 1
    }
}