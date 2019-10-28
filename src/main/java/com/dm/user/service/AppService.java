package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.App;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface AppService {

    /**
     * 获取最新app版本
     *
     * @param app
     * @return app实体
     * @throws Exception
     */
    Result getAppVersion(App app) throws Exception;

}
