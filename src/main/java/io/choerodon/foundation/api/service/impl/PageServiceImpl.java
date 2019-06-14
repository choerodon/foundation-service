package io.choerodon.foundation.api.service.impl;

import io.choerodon.foundation.api.dto.PageDTO;
import io.choerodon.foundation.api.dto.PageSearchDTO;
import io.choerodon.foundation.api.service.PageService;
import io.choerodon.foundation.domain.Page;
import io.choerodon.foundation.infra.mapper.PageMapper;
import io.choerodon.base.domain.PageRequest;
import io.choerodon.foundation.infra.utils.PageUtil;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public PageInfo<PageDTO> pageQuery(Long organizationId, PageRequest pageRequest, PageSearchDTO searchDTO) {
        PageInfo<Page> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(),
                pageRequest.getSort().toSql()).doSelectPageInfo(() -> pageMapper.fulltextSearch(organizationId, searchDTO));
        return PageUtil.buildPageInfoWithPageInfoList(page,
                modelMapper.map(page.getList(), new TypeToken<List<PageDTO>>() {
                }.getType()));
    }
}
