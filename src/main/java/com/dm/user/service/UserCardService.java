package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.UserCard;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface UserCardService {

    /**
     * 实名认证提交
     *
     * @param userCard
     * @return
     * @throws Exception
     */
    Result authentication(UserCard userCard) throws Exception;

    /**
     * 实名信息查看
     *
     * @return
     * @throws Exception
     */
    Result realInfo() throws Exception;

    /**
     * 根据用户ID查询
     *
     * @param userId
     * @return
     * @throws Exception
     */
    UserCard selectByUserId(String userId, String realState) throws Exception;

    /**
     * 修改用户认证状态
     *
     * @throws Exception
     */
    void updateRealState() throws Exception;

    /**
     * 查询实名
     *
     * @param userid
     * @return
     * @throws Exception
     */
    UserCard selectByUserIdAndStatus(Integer userid) throws Exception;
}
