package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.user.entity.BizCertModel;
import com.dm.user.entity.BookView;
import com.dm.user.entity.ItemApplyFiles;
import com.dm.user.entity.WfInstAuditTrack;
import com.dm.user.mapper.BizCertModelMapper;
import com.dm.user.msg.ItemApplyEnum;
import com.dm.user.msg.ItemFileTypeEnum;
import com.dm.user.service.BizCertService;
import com.dm.user.service.ItemApplyFilesService;
import com.dm.user.service.ItemApplyLogService;
import com.dm.user.service.WfInstAuditTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class BizCertServiceImpl implements BizCertService {

    @Autowired
    private BizCertModelMapper bizCertModelMapper;

    @Autowired
    private ItemApplyFilesService itemApplyFilesService;

    @Autowired
    private WfInstAuditTrackService wfInstAuditTrackService;

    @Autowired
    private ItemApplyLogService itemApplyLogService;

    @Override
    public BizCertModel selectByItemId(Integer itemid) {
        BizCertModel bizCertModel = bizCertModelMapper.selectByItemId(itemid);
        return bizCertModel;
    }

    @Override
    public Map<String, Object> certInfo(Integer applyId) {
        return bizCertModelMapper.selectInfoByApplyId(applyId);
    }

    @Override
    public Result submit(ItemApplyFiles itemApplyFiles) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyId", itemApplyFiles.getApplyid());
        map.put("state", ItemFileTypeEnum.OPINION_FILE.getCode());
        ItemApplyFiles itemApplyFiles1 = itemApplyFilesService.selectByApplyIdAndState(map);
        if (null == itemApplyFiles1) {
            itemApplyFiles.setCreatedDate(new Date());
            itemApplyFiles.setFileTypes(ItemFileTypeEnum.OPINION_FILE.getCode());
            itemApplyFiles.setIsDel(ItemFileTypeEnum.FILE_EXISTENCE.getCode());
            itemApplyFilesService.insertData(itemApplyFiles);
        } else {
            itemApplyFiles1.setFileString(itemApplyFiles.getFileString());
            itemApplyFilesService.updateData(itemApplyFiles1);
        }
        return ResultUtil.success(itemApplyFiles.getFileString());
    }

    @Override
    public Result reviewSubmit(ItemApplyFiles itemApplyFiles) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyid", itemApplyFiles.getApplyid());
        map.put("fileString", itemApplyFiles.getFileString());
        map.put("userId", LoginUserHelper.getUserId());
        WfInstAuditTrack wfInstAuditTrack = wfInstAuditTrackService.selectByInstanId(map);
        if (null == wfInstAuditTrack) {
            wfInstAuditTrackService.insertNewData(map);
        } else {
            wfInstAuditTrack.setFileString(itemApplyFiles.getFileString());
            wfInstAuditTrackService.updateData(wfInstAuditTrack);
        }
        return ResultUtil.success(itemApplyFiles.getFileString());
    }

    @Override
    public Result certView(BookView bookView) throws Exception {
        if (bookView.getType() == 0) {
            ItemApplyFiles itemApplyFiles = itemApplyFilesService.selectById(bookView.getId());
            return ResultUtil.success(itemApplyFiles.getFileString());
        } else if (bookView.getType() == 1) {
            WfInstAuditTrack wfInstAuditTrack = wfInstAuditTrackService.selectById(bookView.getId());
            return ResultUtil.success(wfInstAuditTrack.getFileString());
        }
        return ResultUtil.success();
    }

    @Override
    public Result realCert(ItemApplyFiles itemApplyFiles) throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        map.put("applyId", itemApplyFiles.getApplyid());
        map.put("state", ItemFileTypeEnum.SEAL_OPINION_FILE.getCode());
        ItemApplyFiles itemApplyFiles1 = itemApplyFilesService.selectByApplyIdAndState(map);
        if (null == itemApplyFiles1) {
            itemApplyFiles.setCreatedDate(new Date());
            itemApplyFiles.setFileTypes(ItemFileTypeEnum.SEAL_OPINION_FILE.getCode());
            itemApplyFiles.setIsDel(1);
            itemApplyFilesService.insertData(itemApplyFiles);
            itemApplyLogService.insertLog(LoginUserHelper.getUserId(), itemApplyFiles.getApplyid(), new Date(), ItemApplyEnum.FILE_MAKE.getCode(), ItemApplyEnum.FILE_MAKE.getDesc());
        } else {
            itemApplyFiles1.setFileString(itemApplyFiles.getFileString());
            itemApplyFilesService.updateData(itemApplyFiles1);
        }
        return ResultUtil.success();
    }
}
