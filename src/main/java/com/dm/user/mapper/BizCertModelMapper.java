package com.dm.user.mapper;

import com.dm.user.entity.BizCertModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface BizCertModelMapper {

    /**
     * itemid查询
     *
     * @param itemid
     * @return
     */
    BizCertModel selectByItemId(Integer itemid);

    /**
     * 占位符信息查询
     *
     * @param applyId
     */
    Map<String, Object> selectInfoByApplyId(Integer applyId);
}