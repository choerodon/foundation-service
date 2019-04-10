package io.choerodon.foundation.infra.mapper;


import io.choerodon.foundation.domain.LookupTypeWithValues;
import io.choerodon.foundation.domain.LookupValue;
import io.choerodon.mybatis.common.BaseMapper;

public interface LookupValueMapper extends BaseMapper<LookupValue> {

    LookupTypeWithValues queryLookupValueByCode(String typeCode);
}
