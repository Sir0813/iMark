package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.BizMsg;
import com.dm.user.mapper.BizMsgMapper;
import com.dm.user.service.BizMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class BizMsgServiceImpl implements BizMsgService {

    @Autowired
    private BizMsgMapper bizMsgMapper;

    @Override
    public Result msgList() throws Exception {
        String userId = LoginUserHelper.getUserId();
        List<BizMsg> bizMsgList = bizMsgMapper.msgList(userId);
        return ResultUtil.success(bizMsgList);
    }

    @Override
    public Result msgDetails(String chatId, String createTime) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("chatId", chatId);
        map.put("createTime", createTime);
        List<BizMsg> bizMsgList = bizMsgMapper.msgDetails(map);
        return ResultUtil.success(bizMsgList);
    }
}
