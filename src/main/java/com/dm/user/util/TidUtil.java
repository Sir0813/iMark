package com.dm.user.util;

import com.dm.app.tid.sdk.model.form.RegisterForm;
import com.dm.app.tid.sdk.model.form.UpdateTidForm;
import com.dm.app.tid.sdk.service.TIDService;
import com.dm.app.tid.sdk.service.impl.TIDServiceImpl;
import com.dm.fchain.sdk.msg.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TidUtil {

    private static Logger logger = LoggerFactory.getLogger(TidUtil.class);

    /**
     * 链上注册用户身份
     *
     * @param userName 手机号
     * @param password 密码
     */
    public static boolean register(String userName, String password) throws Exception {
        TIDService tidService = new TIDServiceImpl();
        String appCode = ShaUtil.readProperty("appCode");
        String passWord = ShaUtil.readProperty("passWord");
        RegisterForm registerForm = new RegisterForm();
        registerForm.setAppCode(appCode);
        registerForm.setPassword(password);
        registerForm.setMobile(appCode + userName);
        registerForm.setAppPassword(passWord);
        Result register = tidService.register(registerForm);
        if (!"200".equals(register.getCode())) {
            logger.info("用户身份上链注册失败!");
            return true;
        } else {
            logger.info("用户身份上链注册成功!");
            return false;
        }
    }

    /**
     * 修改链上身份密码
     *
     * @param userName    手机号
     * @param oldPassWord 旧密码
     * @param newPassword 新密码
     */
    public static boolean updatePassword(String userName, String oldPassWord, String newPassword) throws Exception {
        TIDService tidService = new TIDServiceImpl();
        UpdateTidForm tidForm = new UpdateTidForm();
        String appCode = ShaUtil.readProperty("appCode");
        String passWord = ShaUtil.readProperty("passWord");
        tidForm.setCode(appCode + userName);
        tidForm.setPassword(oldPassWord);
        tidForm.setNewPassword(newPassword);
        Result result = tidService.updateTid(tidForm);
        if ("200".equals(result.getCode())) {
            logger.info("链上身份信息修改成功!");
            return true;
        } else {
            logger.error("身份信息修改失败!");
            return false;
        }
    }

    /**
     * 查询修改历史记录
     *
     * @param userName
     */
    public static String history(String userName) throws Exception {
        TIDService tidService = new TIDServiceImpl();
        String appCode = ShaUtil.readProperty("appCode");
        String history = tidService.history(appCode + userName);
        return history;
    }

}
