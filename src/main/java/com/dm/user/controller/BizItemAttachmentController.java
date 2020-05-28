package com.dm.user.controller;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.BizCertFilesVo;
import com.dm.user.entity.BizItemAttachment;
import com.dm.user.service.BizItemAttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController

@RequestMapping("bizItemAttachment")
public class BizItemAttachmentController {
    @Autowired
    private BizItemAttachmentService bizItemAttachmentService;

    /**
     * 查询公证项目附件列表
     * @param applyId
     * @return
     */
    @PostMapping("/getBizItemAttachment")
    public Result getBizItemAttachmentInfo(Integer applyId){
        List<BizCertFilesVo> vo =  bizItemAttachmentService.getBizItemAttachmentInfo(applyId);
        return ResultUtil.success(vo);
    }

    /**
     * 文件公证项目关联表保存数据
     * @param bizItemAttachment
     * @return
     */
    @PostMapping("/saveAttachment")
    public Result saveAttachment(@RequestBody BizItemAttachment bizItemAttachment){
            try{
                bizItemAttachment.setCreateTime(new Date());
                int insert = bizItemAttachmentService.insertSelective(bizItemAttachment);
                return ResultUtil.success(insert);
            }catch (Exception e){
                return ResultUtil.error("fail");
            }
    }
}
