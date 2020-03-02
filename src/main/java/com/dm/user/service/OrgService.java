package com.dm.user.service;

import com.dm.user.entity.Org;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * @author cui
 * @date 2019-12-25
 */
public interface OrgService {

    /**
     * 公证处列表
     *
     * @param page
     * @throws Exception
     */
    PageInfo<Org> orgList(Page<Org> page) throws Exception;

    /**
     * 公正须知
     *
     * @param orgId
     * @return
     */
    String notice(int orgId) throws Exception;
}
