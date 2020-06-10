package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.ItemApplyFiles;
import com.dm.user.mapper.ItemApplyFilesMapper;
import com.dm.user.service.ItemApplyFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class ItemApplyFilesServiceImpl implements ItemApplyFilesService {

    @Autowired
    private ItemApplyFilesMapper itemApplyFilesMapper;


    @Override
    public void insert(ItemApplyFiles itemApplyFiles) throws Exception {
        itemApplyFilesMapper.insertSelective(itemApplyFiles);
    }

    @Override
    public List<ItemApplyFiles> selectByApplyIdAndRequeredId(Map<String, Object> m) throws Exception {
        List<ItemApplyFiles> itemApplyFilesList = itemApplyFilesMapper.selectByApplyIdAndRequeredId(m);
        return itemApplyFilesList;
    }

    @Override
    public void deleteByApplyIdAndRequeredId(Integer applyid, String requeredid) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(16);
        map.put("applyid", applyid);
        map.put("requeredid", requeredid);
        itemApplyFilesMapper.deleteByApplyIdAndRequeredId(map);
    }

    @Override
    public void deleteByApplyIdAndFileType(Map<String, Object> map) throws Exception {
        itemApplyFilesMapper.deleteByApplyIdAndFileType(map);
    }

    @Override
    public Result notes(Map<String, Object> map) throws Exception {
        itemApplyFilesMapper.updateByPrimaryKeySelective(map);
        return ResultUtil.success();
    }

    @Override
    public ItemApplyFiles selectByApplyIdAndState(Map<String, Object> fileMap) throws Exception {
        ItemApplyFiles itemApplyFiles = itemApplyFilesMapper.selectByApplyIdAndState(fileMap);
        return itemApplyFiles;
    }

    @Override
    public void updateDelState(Integer applyid) throws Exception {
        itemApplyFilesMapper.updateDelState(applyid);
    }

    @Override
    public void insertData(ItemApplyFiles itemApplyFiles) {
        itemApplyFilesMapper.insertSelective(itemApplyFiles);
    }

    @Override
    public void updateData(ItemApplyFiles itemApplyFiles1) {
        itemApplyFilesMapper.updateData(itemApplyFiles1);
    }

    @Override
    public ItemApplyFiles selectById(Integer id) {
        return itemApplyFilesMapper.selectByPrimaryKey(id);
    }
}
