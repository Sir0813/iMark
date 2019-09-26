package com.dm.user.service.impl;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.user.entity.App;
import com.dm.user.mapper.AppMapper;
import com.dm.user.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cui
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;

    @Override
    public Result getAppVersion(App app) throws Exception {
        try {
            App appObject = appMapper.getAppVersion(app);
            return ResultUtil.success(appObject);
        } catch (Exception e) {
            throw new Exception();
        }
    }
}
