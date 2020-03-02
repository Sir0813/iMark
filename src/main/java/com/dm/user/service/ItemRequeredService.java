package com.dm.user.service;

import com.dm.user.entity.ItemRequered;

import java.util.List;

public interface ItemRequeredService {

    /**
     * 根据项目ID查询所需规则材料
     *
     * @param itemId
     * @return
     */
    List<ItemRequered> selectByItemId(int itemId) throws Exception;
}
