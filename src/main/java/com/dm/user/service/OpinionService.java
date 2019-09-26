package com.dm.user.service;

import com.dm.user.entity.Opinion;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface OpinionService {

    void opinionSubmit(Opinion opinion) throws Exception;
}
