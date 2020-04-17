package com.dm.user.service;

import com.dm.user.entity.PubArticle;
import com.dm.user.entity.PubArticleDetail;

import java.util.List;

public interface PubArticleService {

    /**
     * 普法专栏前五条
     *
     * @return
     */
    List<PubArticle> articleList() throws Exception;

    /**
     * 普法详情
     *
     * @return
     */
    PubArticleDetail articleDetail(Integer id) throws Exception;
}
