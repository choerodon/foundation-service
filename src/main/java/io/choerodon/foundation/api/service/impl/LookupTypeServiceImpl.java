package io.choerodon.foundation.api.service.impl;


import io.choerodon.foundation.api.dto.LookupTypeDTO;
import io.choerodon.foundation.api.service.LookupTypeService;
import io.choerodon.foundation.infra.mapper.LookupTypeMapper;
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
public class LookupTypeServiceImpl implements LookupTypeService {

    @Autowired
    private LookupTypeMapper lookupTypeMapper;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<LookupTypeDTO> listLookupType(Long organizationId) {
        return modelMapper.map(lookupTypeMapper.selectAll(), new TypeToken<List<LookupTypeDTO>>() {
        }.getType());
    }

}
