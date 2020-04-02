package com.dm.user.mapper;

import com.dm.user.entity.OrgItems;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author cui
 * @date 2020-01-02
 */
@Mapper
public interface OrgItemsMapper {

    /**
     * 根据主键查询
     *
     * @param itemid
     * @return
     */
    OrgItems selectByPrimaryKey(Integer itemid);

    /**
     * 公证处业务项目列表
     *
     * @return
     */
    List<OrgItems> itemList(Map<String, Object> map);

    /**
     * 上架的公正类型
     *
     * @param map
     */
    List<OrgItems> selectByOrgIdAndStatus(Map<String, Object> map);
}