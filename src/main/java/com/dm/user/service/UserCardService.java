package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.UserCard;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface UserCardService {

    Result authentication(UserCard userCard) throws Exception;

    Result realInfo() throws Exception;

    UserCard selectByUserId(String toString) throws Exception;
}
