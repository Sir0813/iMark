package com.dm.user.mapper;

import com.dm.user.entity.OrgUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrgUserMapper {

    /**
     * 主键查询
     *
     * @param userid
     * @return
     */
    OrgUser selectByPrimaryKey(Integer userid);

    /**
     * 查询公正管理员集合
     *
     * @return
     */
    List<OrgUser> selectAdminList();
}