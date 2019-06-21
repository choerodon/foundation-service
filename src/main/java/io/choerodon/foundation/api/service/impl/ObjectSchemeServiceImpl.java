package io.choerodon.foundation.api.service.impl;

import com.github.pagehelper.PageInfo;

import io.choerodon.foundation.api.dto.ObjectSchemeDTO;
import io.choerodon.foundation.api.dto.ObjectSchemeSearchDTO;
import io.choerodon.foundation.api.service.ObjectSchemeService;
import io.choerodon.foundation.domain.ObjectScheme;
import io.choerodon.foundation.infra.mapper.ObjectSchemeMapper;

import com.github.pagehelper.PageHelper;

import io.choerodon.base.domain.PageRequest;
import io.choerodon.foundation.infra.utils.PageUtil;

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
    public PageInfo<ObjectSchemeDTO> pageQuery(Long organizationId, PageRequest pageRequest, ObjectSchemeSearchDTO searchDTO) {
        PageInfo<ObjectScheme> page = PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize(),
                PageUtil.sortToSql(pageRequest.getSort())).doSelectPageInfo(() -> objectSchemeMapper.fulltextSearch(organizationId, searchDTO));
        return PageUtil.buildPageInfoWithPageInfoList(page,
                modelMapper.map(page.getList(), new TypeToken<List<ObjectSchemeDTO>>() {
                }.getType()));
    }
}
