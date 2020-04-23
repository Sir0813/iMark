package com.dm.user.service;

import com.dm.user.entity.PushMsg;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface PushMsgService {

    /**
     * 新增
     *
     * @param pm
     * @throws Exception
     */
    void insertSelective(PushMsg pm) throws Exception;

    /**
     * 根据发送人和状态查询
     *
     * @param username
     * @return
     * @throws Exception
     */
    List<PushMsg> selectByReceiveAndState(String username) throws Exception;

    /**
     * 修改
     *
     * @param pm
     * @throws Exception
     */
    void updateByPrimaryKeySelective(PushMsg pm) throws Exception;

    /**
     * 历史消息
     *
     * @param pageNum
     * @return
     * @throws Exception
     */
    PageInfo<PushMsg> historyInfo(Integer pageNum) throws Exception;

    /**
     * 已读状态修改
     *
     * @param pushId
     * @throws Exception
     */
    void readInfo(String pushId) throws Exception;

    /**
     * 根据ID删除推送记录
     *
     * @param parseInt
     * @throws Exception
     */
    void deleteByCertId(String parseInt) throws Exception;
}
