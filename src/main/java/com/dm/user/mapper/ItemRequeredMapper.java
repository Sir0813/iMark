package com.dm.user.mapper;

import com.dm.user.entity.ItemRequered;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cui
 * @date 2020-01-02
 */
@Mapper
public interface ItemRequeredMapper {

    /**
     * 根据主键查询
     *
     * @param requeredid
     * @return
     */
    ItemRequered selectByPrimaryKey(Integer requeredid);

    /**
     * 根据项目ID查询所需规则材料
     *
     * @param itemId
     * @return
     */
    List<ItemRequered> selectByItemId(int itemId);

    /**
     * 查询所需材料
     *
     * @param applyid
     * @return
     */
    List<ItemRequered> selectByRequeredIdAndApplyId(Integer applyid);

    /**
     * 草稿其他材料
     *
     * @param itemid
     * @return
     */
    ItemRequered selectOtherFile(Integer itemid);
}