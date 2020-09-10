package com.dm.user;


import com.dm.app.did.sdk.model.form.BindForm;
import com.dm.app.did.sdk.model.form.RegisterForm;
import com.dm.app.did.sdk.model.form.SimpleForm;
import com.dm.app.did.sdk.service.DataService;
import com.dm.app.did.sdk.service.impl.DataServiceImpl;
import com.dm.fchain.sdk.msg.Result;

import java.util.UUID;

/**
 * @Description
 * @Package src.main.java.com.dm.baas
 * @Author bin
 * @DATE 2019/7/31
 */
public class AppTest {
    public static void main(String[] args) throws Exception {
        /*CIDService chainCertService = new CIDServiceImpl();
        Result result = chainCertService.save("123r1sdfs12312321df234143", "1311231232312312312", DateUtil.timeToString2(new Date()), "123123");
        System.out.println(result.getCode());*/
        /**
         * 注册临时身份
         */
//        register("1880808080856757", "9651e04928c2568e8a8b70d643bd9790");
        /**
         * 注册实名身份
         */
//        addTid("user000209");
        /**
         * 绑定实名身份
         */
//        addBind("1880808080856757", "9651e04928c2568e8a8b70d643bd9790", "user000209");
        /**
         * 历史信息
         */
        /*String history = history("1880808080856757");
        System.out.println(history);*/

        /*DataService dataService = new DataServiceImpl();
        Result result = dataService.queryTID("user000209");
        System.out.println(result);*/

//        checkTid("1880808080856757", "user000209");
//        TIDService tidService = new TIDServiceImpl();
//        String iMark18808080808123123 = tidService.history("iMark18811012959");
//        System.out.println(iMark18808080808123123);
//        Result result = tidService.queryTID("131182199408131233");
//        System.out.println(result.getCode());
        /*JSONObject jsonObject = JSONObject.parseObject(iMark18811014321);
        String data = jsonObject.get("data").toString();
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        Object parent = jsonObject1.get("parent");
        System.out.println(parent);
        System.out.println(iMark18811014321);*/
//        updatePassword("18910500312", "11b0ac1a8ca68fad3f5e73858dc59070", "e6bb8eae2959e050997c58d15ea00577");
//        String history = history("18910500312");
//        System.out.println(history);

        String s = UUID.randomUUID().toString().replace("-", "");
        System.out.println(s);


    }


    private static String appCode = "zgc_app_616_1_code ";
    private static String passWord = "zgc_app_616_1_password";

    /**
     * 链上注册用户身份
     *
     * @param userName 手机号
     * @param password 密码
     */
    public static void register(String userName, String password) throws Exception {
        DataService dataService = new DataServiceImpl();
        RegisterForm registerForm = new RegisterForm();
        registerForm.setAppCode(appCode);
        registerForm.setAppPassword(passWord);
        registerForm.setCode(appCode + userName);
        registerForm.setPassword(password);
        Result register = dataService.register(registerForm);
        if (!"200".equals(register.getCode())) {
            System.out.println("用户身份上链注册失败!");
        } else {
            System.out.println("注册成功!");
        }
    }

    /**
     * 实名身份注册
     *
     * @param userCard
     * @throws Exception
     */
    public static void addTid(String userCard) throws Exception {
        DataService dataService = new DataServiceImpl();
        SimpleForm simpleForm = new SimpleForm();
        simpleForm.setCode(userCard);
        simpleForm.setPassword(passWord);
        Result result = dataService.registerApp(simpleForm);
        System.out.println(result.getCode());
    }

    /**
     * 修改链上身份密码
     *
     * @param userName    手机号
     * @param oldPassWord 旧密码
     * @param newPassword 新密码
     */
    public static void updatePassword(String userName, String oldPassWord, String newPassword) throws Exception {
        /*TIDService tidService = new TIDServiceImpl();
        UpdateTidForm tidForm = new UpdateTidForm();
        tidForm.setCode(appCode + userName);
        tidForm.setPassword(oldPassWord);
        tidForm.setNewPassword(newPassword);
        Result result = tidService.updateTid(tidForm);
        if ("200".equals(result.getCode())) {
            System.out.println("身份修改成功!");
        } else {
            System.out.println("身份信息修改失败!");
        }*/
    }

    /**
     * 查询修改历史记录
     *
     * @param userName
     */
    public static String history(String userName) throws Exception {
        DataService didService = new DataServiceImpl();
        String history = didService.history(appCode + userName);
        return history;
    }

    /**
     * 检测实名信息
     *
     * @param userName
     * @param userCard
     * @throws Exception
     */
    public static void checkTid(String userName, String userCard) throws Exception {
        DataService dataService = new DataServiceImpl();
        Result result = dataService.checkTID(appCode + userName, userCard);
        System.out.println(result.getCode());
        if ("201".equals(result.getCode())) {
            // 账号未实名认证 可以实名
        } else if ("203".equals(result.getCode())) {
            // 实名身份和证件号码不匹配  (多次实名时 )
        } else if ("200".equals(result.getCode())) {
            // 实名身份与证件号码匹配 (可以实名)
        } else if ("404".equals(result.getCode())) {

        }
    }

    public static void addBind(String userName, String userPassword, String userCard) throws Exception {
        DataService dataService = new DataServiceImpl();
        BindForm bindForm = new BindForm();
        bindForm.setUserNo(appCode + userName);
        bindForm.setPassword(userPassword);
        bindForm.setToUserNo(userCard);
        bindForm.setToUserPassword(passWord);
        Result result = dataService.addBind(bindForm);
        System.out.println(result.getCode());
    }

}
