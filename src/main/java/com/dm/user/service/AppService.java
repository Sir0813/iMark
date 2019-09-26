package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.App;

/**
 * @author cui
 * @date 2019-09-26
 */
public interface AppService {

    Result getAppVersion(App app) throws Exception;

}
