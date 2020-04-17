package com.dm.user.mapper;

import com.dm.user.entity.PubArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PubArticleMapper {

    PubArticle selectByPrimaryKey(Integer id);

    List<PubArticle> articleList();

}