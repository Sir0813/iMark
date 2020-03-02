package com.dm.user.service;

import com.dm.user.entity.OrgUser;

public interface OrgUserService {

    /**
     * 主键查询
     *
     * @param userId
     * @return
     */
    OrgUser selectByUserId(int userId);
}
