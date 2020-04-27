package com.dm.user.mapper;

import com.dm.user.entity.ItemApplyLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemApplyLogMapper {

    int insertSelective(ItemApplyLog record);

    ItemApplyLog selectByPrimaryKey(Integer id);

    List<ItemApplyLog> historyNode(int applyid);

    ItemApplyLog selectByApplyIdAndStatus(Map<String, Object> map);

    void updateById(ItemApplyLog itemApplyLog);
}