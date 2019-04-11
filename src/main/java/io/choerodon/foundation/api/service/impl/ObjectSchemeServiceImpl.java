package io.choerodon.foundation.api.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.foundation.api.dto.ObjectSchemeDTO;
import io.choerodon.foundation.api.dto.ObjectSchemeSearchDTO;
import io.choerodon.foundation.api.service.ObjectSchemeService;
import io.choerodon.foundation.domain.ObjectScheme;
import io.choerodon.foundation.infra.mapper.ObjectSchemeMapper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/3/29
 */
@Component
public class ObjectSchemeServiceImpl implements ObjectSchemeService {
    @Autowired
    private ObjectSchemeMapper objectSchemeMapper;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public Page<ObjectSchemeDTO> pageQuery(Long organizationId, PageRequest pageRequest, ObjectSchemeSearchDTO searchDTO) {
        Page<ObjectScheme> page = PageHelper.doPageAndSort(pageRequest, () -> objectSchemeMapper.fulltextSearch(organizationId, searchDTO));
        Page<ObjectSchemeDTO> dtoPage = new Page<>();
        dtoPage.setContent(modelMapper.map(page.getContent(), new TypeToken<List<ObjectSchemeDTO>>() {
        }.getType()));
        dtoPage.setSize(page.getSize());
        dtoPage.setNumber(page.getNumber());
        dtoPage.setTotalElements(page.getTotalElements());
        dtoPage.setTotalPages(page.getTotalPages());
        return dtoPage;
    }
}
