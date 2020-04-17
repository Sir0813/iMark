package com.dm.user.mapper;

import com.dm.user.entity.PubArticleDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PubArticleDetailMapper {

    PubArticleDetail selectByPrimaryKey(Integer id);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    PubArticleDetail articleDetail(Integer id);
}