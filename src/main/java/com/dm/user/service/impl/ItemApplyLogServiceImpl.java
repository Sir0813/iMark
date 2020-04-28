package com.dm.user.service.impl;

import com.dm.user.entity.ItemApplyLog;
import com.dm.user.mapper.ItemApplyLogMapper;
import com.dm.user.service.ItemApplyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemApplyLogServiceImpl implements ItemApplyLogService {

    @Autowired
    private ItemApplyLogMapper itemApplyLogMapper;

    @Override
    public void insertLog(String createdBy, Integer applyId, Date date, Integer status, String nodeName) throws Exception {
        ItemApplyLog itemApplyLog = new ItemApplyLog();
        itemApplyLog.setApplyid(applyId);
        itemApplyLog.setCreatedDate(date);
        itemApplyLog.setStatus(String.valueOf(status));
        itemApplyLog.setCreatedBy(createdBy);
        itemApplyLog.setNodeName(nodeName);
        itemApplyLogMapper.insertSelective(itemApplyLog);
    }

    @Override
    public List<ItemApplyLog> historyNode(int applyid) throws Exception {
        return itemApplyLogMapper.historyNode(applyid);
    }

    @Override
    public ItemApplyLog selectByApplyIdAndStatus(Integer applyid, int code) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyId", applyid);
        map.put("code", code);
        return itemApplyLogMapper.selectByApplyIdAndStatus(map);
    }

    @Override
    public void updateById(ItemApplyLog itemApplyLog) throws Exception {
        itemApplyLogMapper.updateById(itemApplyLog);
    }
}
