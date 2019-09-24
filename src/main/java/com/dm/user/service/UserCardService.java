package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.UserCard;

public interface UserCardService {

    Result authentication(UserCard userCard) throws Exception;

    Result realInfo() throws Exception;

    UserCard selectByUserId(String toString) throws Exception;
}
