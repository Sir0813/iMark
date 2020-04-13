package com.dm.user.mapper;

import com.dm.user.entity.BizMsg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BizMsgMapper {

    List<BizMsg> msgList(String userId);

    List<BizMsg> msgDetails(Map<String, Object> map);
}