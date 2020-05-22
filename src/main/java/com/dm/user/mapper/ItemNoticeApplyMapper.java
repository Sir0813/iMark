package com.dm.user.mapper;

import com.dm.user.entity.ItemNoticeApply;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemNoticeApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemNoticeApply record);

    int insertSelective(ItemNoticeApply record);

    ItemNoticeApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemNoticeApply record);

    int updateByPrimaryKey(ItemNoticeApply record);
}