package com.dm.user.mapper;

import com.dm.user.entity.ItemNotice;

import java.util.List;

public interface ItemNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemNotice record);

    int insertSelective(ItemNotice record);

    ItemNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemNotice record);

    int updateByPrimaryKey(ItemNotice record);

    List<ItemNotice> noticeList();
}