package com.dm.user.service.impl;

import com.dm.user.entity.PubArticleDetail;
import com.dm.user.mapper.PubArticleDetailMapper;
import com.dm.user.service.PubArticleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PubArticleDetailServiceImpl implements PubArticleDetailService {

    @Autowired
    private PubArticleDetailMapper articleDetailMapper;

    @Override
    public PubArticleDetail articleDetail(Integer id) {
        return articleDetailMapper.articleDetail(id);
    }
}
