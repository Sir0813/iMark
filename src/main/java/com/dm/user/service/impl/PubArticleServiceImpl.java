package com.dm.user.service.impl;

import com.dm.user.entity.PubArticle;
import com.dm.user.entity.PubArticleDetail;
import com.dm.user.mapper.PubArticleMapper;
import com.dm.user.service.PubArticleDetailService;
import com.dm.user.service.PubArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PubArticleServiceImpl implements PubArticleService {

    @Autowired
    private PubArticleMapper articleMapper;

    @Autowired
    private PubArticleDetailService articleDetailService;

    @Override
    public List<PubArticle> articleList() throws Exception {
        List<PubArticle> articleList = articleMapper.articleList();
        return articleList;
    }

    @Override
    public PubArticleDetail articleDetail(Integer id) throws Exception {
        return articleDetailService.articleDetail(id);
    }
}
