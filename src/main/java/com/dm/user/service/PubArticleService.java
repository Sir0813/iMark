package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.PubArticle;
import com.dm.user.entity.PubArticleDetail;

import java.util.List;

public interface PubArticleService {

    /**
     * 普法专栏前三条
     *
     * @return
     */
    List<PubArticle> articleTop() throws Exception;

    /**
     * 普法详情
     *
     * @return
     */
    PubArticleDetail articleDetail(Integer id) throws Exception;

    /**
     * 普法专栏列表
     *
     * @param pageNum
     * @return
     */
    Result articleList(Integer pageNum) throws Exception;
}
