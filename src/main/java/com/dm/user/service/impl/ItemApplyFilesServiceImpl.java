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
        try {
            itemApplyFilesMapper.insertSelective(itemApplyFiles);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public List<ItemApplyFiles> selectByApplyIdAndRequeredId(Map<String, Object> m) throws Exception {
        try {
            List<ItemApplyFiles> itemApplyFilesList = itemApplyFilesMapper.selectByApplyIdAndRequeredId(m);
            return itemApplyFilesList;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void deleteByApplyIdAndRequeredId(Integer applyid, String requeredid) throws Exception {
        try {
            Map<String, Object> map = new LinkedHashMap<>(16);
            map.put("applyid", applyid);
            map.put("requeredid", requeredid);
            itemApplyFilesMapper.deleteByApplyIdAndRequeredId(map);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void deleteByApplyIdAndFileType(Map<String, Object> map) throws Exception {
        try {
            itemApplyFilesMapper.deleteByApplyIdAndFileType(map);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public Result notes(ItemApplyFiles itemApplyFiles) throws Exception {
        try {
            itemApplyFilesMapper.updateByPrimaryKeySelective(itemApplyFiles);
            return ResultUtil.success();
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public ItemApplyFiles selectByApplyIdAndState(Map<String, Object> fileMap) throws Exception {
        try {
            ItemApplyFiles itemApplyFiles = itemApplyFilesMapper.selectByApplyIdAndState(fileMap);
            return itemApplyFiles;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void updateDelState(Integer applyid) throws Exception {
        try {
            itemApplyFilesMapper.updateDelState(applyid);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
