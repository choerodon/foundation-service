package io.choerodon.foundation.infra.mapper;


import io.choerodon.foundation.domain.LookupTypeWithValues;
import io.choerodon.foundation.domain.LookupValue;
import io.choerodon.mybatis.common.Mapper;

public interface LookupValueMapper extends Mapper<LookupValue> {

    LookupTypeWithValues queryLookupValueByCode(String typeCode);
}
