package com.dm.user.mapper;

import com.dm.user.entity.ProgressView;
import com.dm.user.entity.WfInstAuditTrack;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WfInstAuditTrackMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(WfInstAuditTrack record);

    WfInstAuditTrack selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WfInstAuditTrack record);

    void insertData(Map<String, Object> map);

    void insertApproved(Map<String, Object> map);

    List<ProgressView> selectIsProgress(Integer applyid);

    void insertNewData(Map<String, Object> map);

    WfInstAuditTrack selectByInstanId(Map<String, Object> map);

    void updateData(WfInstAuditTrack wfInstAuditTrack);

    WfInstAuditTrack selectLastReason(int applyid);

    WfInstAuditTrack selectEditData(Map<String, Object> map);

    WfInstAuditTrack selectByApplyIdAndUserId(Map<String, Object> map);

    WfInstAuditTrack selectByNodeId(Integer id);

    WfInstAuditTrack selectByApplyIdAndInstIdAndNodeId(Map<String, Object> map);
}