package com.dm.user.mapper;

import com.dm.user.entity.ApplyFee;
import com.dm.user.entity.ChargeDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ChargeDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ChargeDetail record);

    int insertSelective(ChargeDetail record);

    ChargeDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChargeDetail record);

    int updateByPrimaryKey(ChargeDetail record);

    void updateIsDel(Integer applyid);

    void insertList(List<ChargeDetail> chargeDetails);

    ChargeDetail selectByChargeIdAndApplyId(Map<String, Object> map);

    List<ApplyFee> selectByApplyId(int applyId);

    ApplyFee selectByApplyIdAndFileNum(Integer applyId);

    void updateByIdAndApplyId(Map<String, Object> chargeMap);

    List<ApplyFee> selectByApplyIdAndStatus(int applyid);

    void updatePayStatus(Integer id);
}