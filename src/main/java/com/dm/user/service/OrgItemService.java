package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.OrgItems;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface OrgItemService {

    /**
     * 公正业务项目列表
     *
     * @param pageNum
     * @return
     * @throws Exception
     */
    PageInfo<OrgItems> itemList(Integer pageNum, int orgId, String itemName) throws Exception;

    /**
     * 申请公正内容填充展示
     *
     * @param itemId
     * @return
     * @throws Exception
     */
    Map<String, Object> content(int itemId) throws Exception;

    /**
     * 主键ID查询数据
     *
     * @param itemid
     * @return
     */
    OrgItems selectByPrimaryKey(Integer itemid) throws Exception;

    /**
     * 公正类型
     *
     * @return
     */
    Result typeList() throws Exception;

    /**
     * 公证处ID和状态查询
     *
     * @param map
     * @return
     * @throws Exception
     */
    List<OrgItems> selectByOrgIdAndStatus(Map<String, Object> map) throws Exception;

    /**
     * 根据itemCode查询最新上线公正
     *
     * @param itemCode
     * @return
     * @throws Exception
     */
    OrgItems selectByItemCode(String itemCode) throws Exception;
}
