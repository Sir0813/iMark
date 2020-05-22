package com.dm.user.mapper;

import com.dm.user.entity.ItemCharge;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemChargeMapper {

    ItemCharge selectByName(String name);
}