package com.dm.user.mapper;

import com.dm.user.entity.ApplyFee;
import com.dm.user.entity.ItemCharge;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ItemChargeMapper {

    ItemCharge selectByName(String name);

    List<ApplyFee> selectByOrgIdAndState(Map<String, Object> map);
}