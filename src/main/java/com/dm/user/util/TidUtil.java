package com.dm.user.util;

import com.alibaba.fastjson.JSONObject;
import com.dm.app.tid.sdk.model.form.BindForm;
import com.dm.app.tid.sdk.model.form.RegisterForm;
import com.dm.app.tid.sdk.model.form.SimpleForm;
import com.dm.app.tid.sdk.model.form.UpdateTidForm;
import com.dm.app.tid.sdk.service.TIDService;
import com.dm.fchain.sdk.msg.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TidUtil {

    private static Logger logger = LoggerFactory.getLogger(TidUtil.class);

    @Autowired
    private TIDService tidService;

    @Value("${tid.appCode}")
    private String appCode;

    @Value("${tid.passWord}")
    private String passWord;

    /**
     * 查询是否绑定
     *
     * @param userName
     * @return
     * @throws Exception
     */
    public boolean get(String userName) throws Exception {
        String s = tidService.get(appCode + userName);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String data = jsonObject.get("data").toString();
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        Object parent = jsonObject1.get("parent");
        if ("".equals(parent)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 注册用户绑定链上身份
     *
     * @param userName 手机号
     * @param password 密码
     */
    public boolean register(String userName, String password) throws Exception {
        RegisterForm registerForm = new RegisterForm();
        registerForm.setAppCode(appCode);
        registerForm.setPassword(password);
        registerForm.setMobile(appCode + userName);
        registerForm.setAppPassword(passWord);
        Result register = tidService.register(registerForm);
        if ("200".equals(register.getCode())) {
            logger.info("注册用户绑定链上身份成功!");
            return true;
        } else {
            logger.error(userName + "注册用户绑定链上身份失败!");
            logger.error(register.getCode());
            logger.error(register.getMsg());
            logger.error(String.valueOf(register.getData()));
            return false;
        }
    }

    /**
     * 实名认证身份证号绑定链上身份
     *
     * @param userCard
     * @throws Exception
     */
    public boolean addTid(String userCard) throws Exception {
        SimpleForm simpleForm = new SimpleForm();
        simpleForm.setCode(userCard);
        simpleForm.setPassword(passWord);
        Result result = tidService.addTid(simpleForm);
        if ("200".equals(result.getCode())) {
            logger.info("身份证号绑定链上身份成功");
            return true;
        } else {
            logger.error(userCard + "身份证号绑定链上身份失败");
            logger.error(result.getCode());
            logger.error(result.getMsg());
            logger.error(String.valueOf(result.getData()));
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
    public boolean updatePassword(String userName, String oldPassWord, String newPassword) throws Exception {
        UpdateTidForm tidForm = new UpdateTidForm();
        tidForm.setCode(appCode + userName);
        tidForm.setPassword(oldPassWord);
        tidForm.setNewPassword(newPassword);
        Result result = tidService.updateTid(tidForm);
        if ("200".equals(result.getCode())) {
            logger.info(userName + "链上身份信息修改成功!");
            return true;
        } else {
            logger.error(userName + "链上身份信息修改失败!");
            logger.error(result.getCode());
            logger.error(result.getMsg());
            logger.error(String.valueOf(result.getData()));
            return false;
        }
    }

    /**
     * 检测临时身份和实名身份绑定状态
     *
     * @param userName
     * @param userCard
     * @throws Exception
     */
    public String checkTid(String userName, String userCard) throws Exception {
        Result result = tidService.checkTID(appCode + userName, userCard);
        if ("200".equals(result.getCode())) {
            logger.info(userName + "已经绑定实名身份，且实名身份与证件号码相匹配");
        } else if ("201".equals(result.getCode())) {
            logger.info(userName + "未绑定实名身份，可以绑定");
        } else {
            logger.error(userName + "不能绑定实名身份");
            logger.error(result.getCode());
            logger.error(result.getMsg());
            logger.error(String.valueOf(result.getData()));
        }
        return result.getCode();
    }

    /**
     * 临时身份绑定身份证号身份
     *
     * @param userName
     * @param userPassword
     * @param userCard
     * @return
     * @throws Exception
     */
    public boolean addBind(String userName, String userPassword, String userCard) throws Exception {
        BindForm bindForm = new BindForm();
        bindForm.setUserNo(appCode + userName);
        bindForm.setPassword(userPassword);
        bindForm.setToUserNo(userCard);
        bindForm.setToUserPassword(passWord);
        Result result = tidService.addBind(bindForm);
        if ("200".equals(result.getCode())) {
            logger.info(userName + "临时身份绑定身份证号身份成功");
            return true;
        } else {
            logger.error(userName + "临时身份绑定身份证号身份失败");
            logger.error(result.getCode());
            logger.error(result.getMsg());
            logger.error(String.valueOf(result.getData()));
            return false;
        }
    }

    /**
     * 查询修改历史记录
     *
     * @param userName
     */
    public String history(String userName) throws Exception {
        String history = tidService.history(appCode + userName);
        return history;
    }

}
