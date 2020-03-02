package com.dm.user.mapper;

import com.dm.user.entity.OrgUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrgUserMapper {

    /**
     * 主键查询
     *
     * @param userid
     * @return
     */
    OrgUser selectByPrimaryKey(Integer userid);

}