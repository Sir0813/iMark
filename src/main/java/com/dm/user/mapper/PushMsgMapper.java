package com.dm.user.mapper;

import com.dm.user.entity.PushMsg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cui
 * @date 2019-09-26
 */
@Mapper
public interface PushMsgMapper {

    /**
     * 删除
     *
     * @param pushId
     * @return
     */
    int deleteByPrimaryKey(Integer pushId);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insert(PushMsg record);

    /**
     * 新增
     *
     * @param record
     * @return
     */
    int insertSelective(PushMsg record);

    /**
     * 根据ID查询
     *
     * @param pushId
     * @return
     */
    PushMsg selectByPrimaryKey(Integer pushId);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(PushMsg record);

    /**
     * 修改
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(PushMsg record);

    /**
     * 根据状态和手机号查询
     *
     * @param username
     * @return
     */
    List<PushMsg> selectByReceiveAndState(String username);

    /**
     * 用户历史消息
     *
     * @param userId 用户ID
     * @return
     */
    List<PushMsg> historyInfo(String userId);

    /**
     * 修改成已读
     *
     * @param pushId
     */
    void updateByRead(String pushId);
}