package com.dm.user.service;

import com.dm.user.entity.ItemCharge;

public interface ItemChargeService {

    /**
     * 按照名字查询
     *
     * @param name
     */
    ItemCharge selectByName(String name);
}
