package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.*;
import com.dm.user.mapper.ApplySupplementMapper;
import com.dm.user.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.EditorKit;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApplySupplementServiceImpl implements ApplySupplementService {

    @Autowired
    private ApplySupplementMapper applySupplementMapper;

    @Autowired
    private ItemApplyService itemApplyService;
    @Autowired
    ItemApplyFilesService itemApplyFilesService;

    @Autowired
    private BizItemApplyService  bizItemApplyService;
    @Autowired
    private ApplyFileLogService applyFileLogService;


    @Override
    public Result add(ApplySupplement applySupplement) throws Exception {
        applySupplement.setCreateTime(new Date());
        applySupplementMapper.insertSelective(applySupplement);
        itemApplyService.updateById(applySupplement.getApplyid());
        return ResultUtil.success();
    }

    @Override
    public Result userApplySupplement(ApplySupplementVo applySupplementVo) throws Exception {
        List<Map<String, Object>> editList = applySupplementVo.getEidtList();
        List<Map<String, Object>> supplementList = applySupplementVo.getSupplementList();
        Integer applyId = applySupplementVo.getApplyId();
        BizItemApply bia = new BizItemApply();
        bia.setApplyid(applyId);
        bia.setAddFileStatus(0);
        bizItemApplyService.update(bia);
        try {
            if(editList.size()>0){
                for (Map<String, Object> editMap : editList) {
                    ItemApplyFiles iaf = new ItemApplyFiles();
                    Integer fileId = (Integer)editMap.get("fileIds");
                    Integer fileLogId = (Integer)editMap.get("fileLogId");
                    iaf.setApplyid(applyId);
                    iaf.setFileid(fileId);
                    iaf.setIsDel(1);
                    iaf.setCreatedDate(new Date());
                    iaf.setFileTypes(6);
                    iaf.setFileLogId(fileLogId);
                    itemApplyFilesService.insert(iaf);

                }


            }

            if(supplementList.size()>0){
                 for (Map<String, Object> suppleMap : supplementList) {
                    ItemApplyFiles iaf = new ItemApplyFiles();
                    String fileIds = (String)suppleMap.get("fileids");
                    Integer supplementId = (Integer)suppleMap.get("supplementId");
                    iaf.setApplyid(applyId);
                    iaf.setFileTypes(5);
                    iaf.setIsDel(1);
                    iaf.setSupplementId(supplementId);
                    String[] split = fileIds.split(",");
                    if(split.length>0){
                        for (int i = 0; i < split.length; i++) {
                            iaf.setFileid(Integer.valueOf(split[i]));
                            iaf.setCreatedDate(new Date());
                            itemApplyFilesService.insert(iaf);
                        }
                    }
                }
            }


            return ResultUtil.success("success");
        } catch (Exception e){

            e.printStackTrace();
            return ResultUtil.error("fail");

        }


    }

}
