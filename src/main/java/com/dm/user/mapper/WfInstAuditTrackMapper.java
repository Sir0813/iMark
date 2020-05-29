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
}