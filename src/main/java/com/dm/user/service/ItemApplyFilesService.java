package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.ItemApplyFiles;

import java.util.List;
import java.util.Map;

public interface ItemApplyFilesService {

    /**
     * 新增
     *
     * @param itemApplyFiles
     */
    void insert(ItemApplyFiles itemApplyFiles) throws Exception;

    /**
     * 查询公正提交的文件
     *
     * @param m
     * @return
     */
    List<ItemApplyFiles> selectByApplyIdAndRequeredId(Map<String, Object> m) throws Exception;

    /**
     * 草稿修改删除数据
     *
     * @param applyid
     * @param requeredid
     */
    void deleteByApplyIdAndRequeredId(Integer applyid, String requeredid) throws Exception;

    /**
     * 草稿删除
     *
     * @param map
     * @throws Exception
     */
    void deleteByApplyIdAndFileType(Map<String, Object> map) throws Exception;

    /**
     * 新增修改批注
     *
     * @param map
     * @return
     * @throws Exception
     */
    Result notes(Map<String, Object> map) throws Exception;

    /**
     * 申请ID和状态查意见书
     *
     * @param fileMap
     * @return
     */
    ItemApplyFiles selectByApplyIdAndState(Map<String, Object> fileMap) throws Exception;

    /**
     * 第一节点审批人退回意见书失效
     *
     * @param applyid
     */
    void updateDelState(Integer applyid) throws Exception;
}
