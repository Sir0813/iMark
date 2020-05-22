package com.dm.user.mapper;

import com.dm.user.entity.Org;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author cui
 * @date 2020-01-02
 */
@Mapper
public interface OrgMapper {

    /**
     * 根据主键查询
     *
     * @param itemid
     * @return
     */
    Org selectByPrimaryKey(Integer itemid);

    /**
     * 组织公证处列表
     *
     * @return
     */
    List<Org> orgList();

    /**
     * 查询
     *
     * @param applyid
     * @return
     */
    Org selectByApplyId(int applyid);
}