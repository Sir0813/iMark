package com.dm.user.mapper;

import com.dm.user.entity.BizItemAgreementWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BizItemAgreementMapper {

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    BizItemAgreementWithBLOBs selectByPrimaryKey(Integer id);

    /**
     * 根据公证处ID查询
     *
     * @return
     */
    BizItemAgreementWithBLOBs selectByOrgId(Integer orgId);
}