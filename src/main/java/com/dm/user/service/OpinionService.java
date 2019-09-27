package com.dm.user.service;

import com.dm.user.entity.Opinion;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface OpinionService {

    /**
     * 意见反馈提交
     * @param opinion
     * @throws Exception
     */
    void opinionSubmit(Opinion opinion) throws Exception;
}
