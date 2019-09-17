package com.dm.user.service;

import com.dm.frame.jboot.msg.Result;
import com.dm.user.entity.App;

public interface AppService {
    Result appVersion(App app) throws Exception;
}
