package io.choerodon.foundation.api.service.impl;

import io.choerodon.foundation.api.dto.PageDTO;
import io.choerodon.foundation.api.dto.PageSearchDTO;
import io.choerodon.foundation.api.service.PageService;
import io.choerodon.foundation.domain.Page;
import io.choerodon.foundation.infra.mapper.PageMapper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/4/1
 */
@Component
public class PageServiceImpl implements PageService {
    @Autowired
    private PageMapper pageMapper;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public io.choerodon.core.domain.Page<PageDTO> pageQuery(Long organizationId, PageRequest pageRequest, PageSearchDTO searchDTO) {
        io.choerodon.core.domain.Page<Page> page = PageHelper.doPageAndSort(pageRequest, () -> pageMapper.fulltextSearch(organizationId, searchDTO));
        io.choerodon.core.domain.Page<PageDTO> dtoPage = new io.choerodon.core.domain.Page<>();
        dtoPage.setContent(modelMapper.map(page.getContent(), new TypeToken<List<PageDTO>>() {
        }.getType()));
        dtoPage.setSize(page.getSize());
        dtoPage.setNumber(page.getNumber());
        dtoPage.setTotalElements(page.getTotalElements());
        dtoPage.setTotalPages(page.getTotalPages());
        return dtoPage;
    }
}
