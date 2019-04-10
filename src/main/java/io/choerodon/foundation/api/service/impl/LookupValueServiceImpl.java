package io.choerodon.foundation.api.service.impl;

import io.choerodon.foundation.api.dto.LookupTypeWithValuesDTO;
import io.choerodon.foundation.api.dto.LookupValueDTO;
import io.choerodon.foundation.api.service.LookupValueService;
import io.choerodon.foundation.domain.LookupTypeWithValues;
import io.choerodon.foundation.infra.mapper.LookupValueMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/8
 */
@Service
public class LookupValueServiceImpl implements LookupValueService {

    @Autowired
    private LookupValueMapper lookupValueMapper;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public LookupTypeWithValuesDTO queryLookupValueByCode(Long organizationId, String typeCode) {
        LookupTypeWithValues typeWithValues = lookupValueMapper.queryLookupValueByCode(typeCode);
        LookupTypeWithValuesDTO result = modelMapper.map(typeWithValues, LookupTypeWithValuesDTO.class);
        result.setLookupValues(modelMapper.map(typeWithValues.getLookupValues(), new TypeToken<List<LookupValueDTO>>() {
        }.getType()));
        return result;
    }
}