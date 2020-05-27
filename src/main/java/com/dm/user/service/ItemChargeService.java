package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.ItemCharge;

import java.util.Map;

public interface ItemChargeService {

    /**
     * 按照名字查询
     *
     * @param name
     */
    ItemCharge selectByName(String name);

    /**
     * 维护收费列表
     *
     * @param map
     * @return
     */
    Result selectByOrgIdAndState(Map<String, Object> map);
}
