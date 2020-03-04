package com.dm.user.mapper;

import com.dm.user.entity.ItemApplyFiles;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemApplyFilesMapper {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(ItemApplyFiles record);

    ItemApplyFiles selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemApplyFiles record);

    List<ItemApplyFiles> selectByApplyIdAndRequeredId(Map<String, Object> m);

    void deleteByIds(String[] split);

    void deleteByApplyIdAndRequeredId(Map<String, Object> map);

    void deleteByApplyIdAndFileType(Map<String, Object> map);

    ItemApplyFiles selectByApplyIdAndState(Map<String, Object> fileMap);

    void updateDelState(Integer applyid);
}