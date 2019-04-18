package io.choerodon.foundation.api.controller

import io.choerodon.foundation.IntegrationTestConfiguration
import io.choerodon.foundation.api.dto.*
import io.choerodon.foundation.api.service.ObjectSchemeFieldService
import io.choerodon.foundation.infra.enums.FieldType
import io.choerodon.foundation.infra.enums.ObjectSchemeCode
import io.choerodon.foundation.infra.enums.ObjectSchemeFieldContext
import io.choerodon.foundation.infra.enums.PageCode
import io.choerodon.foundation.infra.repository.ObjectSchemeFieldRepository
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
import spock.lang.Stepwise

/**
 * @author shinan.chen
 * @since 2019/4/11
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(IntegrationTestConfiguration)
@ActiveProfiles("test")
@Stepwise
class FieldValueControllerSpec extends Specification {

    @Autowired
    TestRestTemplate restTemplate
    @Autowired
    ObjectSchemeFieldService objectSchemeFieldService

    @Shared
    Long organizationId = 1L

    @Shared
    Long projectId = 1L

    @Shared
    ObjectSchemeFieldDetailDTO field

    @Autowired
    ObjectSchemeFieldRepository objectSchemeFieldRepository

    def url = '/v1/projects/{project_id}/field_value'

    def setup() {
        println "执行初始化"
        ObjectSchemeFieldCreateDTO createDTO = new ObjectSchemeFieldCreateDTO()
        createDTO.code = 'csn'
        createDTO.context = ObjectSchemeFieldContext.GLOBAL
        createDTO.description = 'csn'
        createDTO.fieldType = FieldType.INPUT
        createDTO.name = 'csn'
        createDTO.schemeCode = ObjectSchemeCode.AGILE_ISSUE
        field = objectSchemeFieldService.create(organizationId, projectId, createDTO)
    }

    def cleanup() {
        objectSchemeFieldService.delete(organizationId, projectId, field.id)
    }

    def "queryPageFieldViewList"() {
        given: '准备'
        PageFieldViewParamDTO paramDTO = new PageFieldViewParamDTO()
        paramDTO.context = context
        paramDTO.schemeCode = schemeCode
        paramDTO.pageCode = pageCode

        when: '界面上获取字段列表，带有字段选项'
        ParameterizedTypeReference<List<PageFieldViewDTO>> typeRef = new ParameterizedTypeReference<List<PageFieldViewDTO>>() {
        }
        HttpEntity<PageFieldViewParamDTO> httpEntity = new HttpEntity<>(paramDTO)
        def entity = restTemplate.exchange(url + "/list?organizationId=" + organizationId, HttpMethod.POST, httpEntity, typeRef, projectId)

        then: '状态码为200，调用成功'
        def actRequest = false
        def actResponse = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null) {
                    if (entity.getBody().size() > 0) {
                        actResponse = true
                    }
                }
            }
        }
        actRequest == expRequest
        actResponse == expResponse

        where: '测试用例：'
        context                       | schemeCode                   | pageCode                    || expRequest | expResponse
        ObjectSchemeFieldContext.EPIC | ObjectSchemeCode.AGILE_ISSUE | PageCode.AGILE_ISSUE_CREATE || true       | true
    }

    def "queryPageFieldViewListWithInstanceId"() {
        given: '准备'
        def id = 1L
        PageFieldViewParamDTO paramDTO = new PageFieldViewParamDTO()
        paramDTO.context = context
        paramDTO.schemeCode = schemeCode
        paramDTO.pageCode = pageCode

        when: '根据实例id从界面上获取字段列表，带有字段值、字段选项'
        ParameterizedTypeReference<List<PageFieldViewDTO>> typeRef = new ParameterizedTypeReference<List<PageFieldViewDTO>>() {
        }
        HttpEntity<PageFieldViewParamDTO> httpEntity = new HttpEntity<>(paramDTO)
        def entity = restTemplate.exchange(url + "/list/{instance_id}?organizationId=" + organizationId, HttpMethod.POST, httpEntity, typeRef, projectId, id)

        then: '状态码为200，调用成功'
        def actRequest = false
        def actResponse = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null) {
                    if (entity.getBody().size() > 0) {
                        actResponse = true
                    }
                }
            }
        }
        actRequest == expRequest
        actResponse == expResponse

        where: '测试用例：'
        context                       | schemeCode                   | pageCode                    || expRequest | expResponse
        ObjectSchemeFieldContext.EPIC | ObjectSchemeCode.AGILE_ISSUE | PageCode.AGILE_ISSUE_CREATE || true       | true
    }

    def "createFieldValues"() {
        given: '准备'
        def instanceId = 1L
        def schemeCode = ObjectSchemeCode.AGILE_ISSUE
        List<FieldValueUpdateDTO> fieldValues = new ArrayList<>()
        FieldValueUpdateDTO updateDTO = new FieldValueUpdateDTO()
        List<PageFieldViewCreateDTO> list = new ArrayList<>()
        PageFieldViewCreateDTO createDTO = new PageFieldViewCreateDTO()
        updateDTO.stringValue = 'string'
        fieldValues.add(updateDTO)
        createDTO.fieldId = field.id
        createDTO.fieldType = FieldType.INPUT
        createDTO.value = 'test'
        list.add(createDTO)

        when: '创建实例时，批量创建字段值'
        HttpEntity<List<PageFieldViewCreateDTO>> httpEntity = new HttpEntity<>(list)
        def entity = restTemplate.exchange(url + "/{instance_id}?schemeCode=" + schemeCode + "&&organizationId=" + organizationId, HttpMethod.POST, httpEntity, Object, projectId, instanceId)

        then: '状态码为200，调用成功'
        def actRequest = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
            }
        }
        expect: '测试用例：'
        actRequest == true
    }

    def "createFieldValuesWithQuickCreate"() {
        given: '准备'
        def instanceId = 1L
        PageFieldViewParamDTO paramDTO = new PageFieldViewParamDTO()
        paramDTO.context = ObjectSchemeFieldContext.EPIC
        paramDTO.schemeCode = ObjectSchemeCode.AGILE_ISSUE
        paramDTO.pageCode = PageCode.AGILE_ISSUE_CREATE

        when: '快速创建实例时，批量创建字段值（默认值）'
        HttpEntity<PageFieldViewParamDTO> httpEntity = new HttpEntity<>(paramDTO)
        def entity = restTemplate.exchange(url + "/quick_create/{instance_id}?organizationId=" + organizationId, HttpMethod.POST, httpEntity, Object, projectId, instanceId)

        then: '状态码为200，调用成功'
        def actRequest = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
            }
        }
        expect: '测试用例：'
        actRequest == true
    }

    def "updateFieldValue"() {
        given: '准备'
        def instanceId = 1L
        def fieldId = field.id
        def schemeCode = ObjectSchemeCode.AGILE_ISSUE
        PageFieldViewUpdateDTO updateDTO = new PageFieldViewUpdateDTO()
        updateDTO.value = 'test'
        updateDTO.fieldType = FieldType.INPUT

        when: '保存值/修改值'
        ParameterizedTypeReference<List<FieldValueDTO>> typeRef = new ParameterizedTypeReference<List<FieldValueDTO>>() {
        }
        HttpEntity<PageFieldViewUpdateDTO> httpEntity = new HttpEntity<>(updateDTO)
        def entity = restTemplate.exchange(url + "/update/{instance_id}?schemeCode=" + schemeCode + "&&organizationId=" + organizationId + "&&fieldId=" + fieldId, HttpMethod.POST, httpEntity, typeRef, projectId, instanceId)

        then: '状态码为200，调用成功'
        def actRequest = false
        def actResponse = false
        if (entity != null) {
            if (entity.getStatusCode().is2xxSuccessful()) {
                actRequest = true
                if (entity.getBody() != null) {
                    if (entity.getBody().size() > 0) {
                        actResponse = true
                    }
                }
            }
        }
        actRequest == expRequest
        actResponse == expResponse

        where: '测试用例：'
        expRequest | expResponse
        true       | true
    }
}