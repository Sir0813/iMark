package com.dm.user.util;

import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.user.LoginUserHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QRCodeUtil {

    private static Map<String, Object> loginUserMap = new ConcurrentHashMap<>();

    /**
     * 手机端授权之后存储手机号并返回pc服務器端拿手机号模拟登录
     *
     * @param code
     * @throws Exception
     */
    public static void saveUserName(String code) throws Exception {
        if (null == loginUserMap.get(code)) {
            loginUserMap.put(code, LoginUserHelper.getUserName());
        } else {
            loginUserMap.remove(code);
        }
    }

    public static Result getUserName(String code) throws Exception {
        if (null == loginUserMap.get(code)) {
            Thread.sleep(1500);
            return ResultUtil.error("用户未扫码");
        } else {
            Object o = loginUserMap.get(code);
            return ResultUtil.success(o);
        }
    }
}
