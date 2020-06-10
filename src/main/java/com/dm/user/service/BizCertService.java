package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.BizCertModel;
import com.dm.user.entity.BookView;
import com.dm.user.entity.ItemApplyFiles;

import java.util.Map;

public interface BizCertService {

    /**
     * 根据itemid查询
     *
     * @param itemid
     * @return
     */
    BizCertModel selectByItemId(Integer itemid);

    /**
     * 文件占位符
     *
     * @param applyId
     */
    Map<String, Object> certInfo(Integer applyId);

    /**
     * 制证第一次提交
     *
     * @param itemApplyFiles
     * @return
     */
    Result submit(ItemApplyFiles itemApplyFiles) throws Exception;

    /**
     * 审批意见书保存
     *
     * @param itemApplyFiles
     * @return
     */
    Result reviewSubmit(ItemApplyFiles itemApplyFiles) throws Exception;

    /**
     * 查看建议意见书
     *
     * @param bookView
     * @return
     */
    Result certView(BookView bookView) throws Exception;

    /**
     * 制证
     *
     * @param itemApplyFiles
     * @return
     */
    Result realCert(ItemApplyFiles itemApplyFiles) throws Exception;
}
