package com.dm.user.service;

import com.dm.user.entity.OrgUser;

import java.util.List;

public interface OrgUserService {

    /**
     * 主键查询
     *
     * @param userId
     * @return
     */
    OrgUser selectByUserId(int userId);

    /**
     * 查询所有管理员
     *
     * @return
     */
    List<OrgUser> selectAdminList();
}
