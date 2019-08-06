package com.dm.user.mapper;

import com.dm.user.entity.PushMsg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PushMsgMapper {
    int deleteByPrimaryKey(Integer pushId);

    int insert(PushMsg record);

    int insertSelective(PushMsg record);

    PushMsg selectByPrimaryKey(Integer pushId);

    int updateByPrimaryKeySelective(PushMsg record);

    int updateByPrimaryKey(PushMsg record);

    List<PushMsg> selectByReceiveAndState(String username);
}