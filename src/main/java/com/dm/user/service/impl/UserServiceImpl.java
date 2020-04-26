package com.dm.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dm.frame.jboot.locale.I18nUtil;
import com.dm.frame.jboot.msg.Result;
import com.dm.frame.jboot.msg.ResultUtil;
import com.dm.frame.jboot.security.LoginUserDetailsService;
import com.dm.frame.jboot.security.token.JwtTokenProvider;
import com.dm.frame.jboot.user.LoginUserHelper;
import com.dm.frame.jboot.user.model.LoginUserDetails;
import com.dm.frame.jboot.user.service.LoginUserService;
import com.dm.frame.jboot.util.DateUtil;
import com.dm.frame.jboot.util.MD5Util;
import com.dm.user.entity.*;
import com.dm.user.mapper.ContactMapper;
import com.dm.user.mapper.UserMapper;
import com.dm.user.msg.StateMsg;
import com.dm.user.service.*;
import com.dm.user.util.HttpSendUtil;
import com.dm.user.util.PushUtil;
import com.dm.user.util.RandomCode;
import com.dm.user.util.TidUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * @author cui
 * @date 2019-09-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private InformationService informationService;

    @Autowired
    private UserCardService userCardService;

    @Autowired
    private PushMsgService pushMsgService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private LoginUserDetailsService loginUserDetailsService;

    @Autowired
    private CertConfirmService certConfirmService;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private OrgUserService orgUserService;

    @Autowired
    private TidUtil tidUtil;

    @Value("${email.emailContent}")
    private String emailContent;

    @Value("${email.expired}")
    private long expired;

    @Override
    public Result sendVeriCode(String phone, String type) throws Exception {
        AppUser u = userMapper.findByName(phone);
        switch (type) {
            case StateMsg.REGISTER:
                if (null != u) {
                    return ResultUtil.info("register.has.name.code", "register.has.name.msg");
                }
                break;
            case StateMsg.FORGETPWD:
                if (null == u) {
                    return ResultUtil.info("login.account.no.code", "login.account.no.msg");
                }
                break;
            case StateMsg.LOGIN:
                if (null == u) {
                    return ResultUtil.info("login.account.no.code", "login.account.no.msg");
                }
                break;
            default:
                break;
        }
        Information info = new Information();
        Date date = new Date();
        Map<String, Object> map = new HashMap<>(16);
        map.put("email", phone);
        Information information = informationService.selectByPhone(map);
        if (null != information) {
            if (!date.after(new Date(information.getInfoSenddate().getTime() + 60000))) {
                return ResultUtil.info("error.code", "veri.send.frequent.msg");
            }
        }
        long randomNumber = RandomCode.generateRandomNumber(6);
        String replaceContent = emailContent.replace("$code$", String.valueOf(randomNumber));
        info.setInfoCode(String.valueOf(randomNumber));
        info.setInfoMsg(replaceContent);
        info.setInfoPhone(phone);
        info.setInfoSenddate(date);
        info.setInfoExpireddate(new Date(date.getTime() + expired));
        info.setInfoState("0");
        informationService.insertSelective(info);
        return ResultUtil.success(String.valueOf(randomNumber));
    }

    @Override
    public Result userRegister(AppUser user) throws Exception {
        AppUser u = userMapper.findByName(user.getUsername());
        if (null != u) {
            return ResultUtil.info("register.has.name.code", "register.has.name.msg");
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("email", user.getUsername());
        map.put("veriCode", user.getUsercode());
        Information information = informationService.selectByPhone(map);
        Result result = checkVeriCode(information, map);
        if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
            return result;
        }
        information.setInfoState("1");
        user.setSalt(RandomCode.getCode());
        user.setUsercode("");
        String md5Password = MD5Util.encode(user.getPassword() + user.getSalt());
        user.setPassword(md5Password);
        user.setCreatedDate(DateUtil.timeToString2(new Date()));
        informationService.updateByPrimaryKeySelective(information);
        /* 注册用户身份上链  start **/
        boolean register = tidUtil.register(user.getUsername(), md5Password);
        if (!register) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error();
        }
        /* 注册用户身份上链  end **/
        userMapper.userRegister(user);
        // 待自己确认 需要更新注册用户ID
        List<CertConfirm> list = certConfirmService.selectByPhone(user.getUsername());
        if (list.size() > 0) {
            list.forEach(cc -> {
                cc.setUserId(user.getUserid());
                certConfirmService.updateByPrimaryKeySelective(cc);
            });
        }
        /* 出证发送给我的添加用户ID **/
        List<Contact> contacts = contactMapper.selectByPhone(user.getUsername());
        if (contacts.size() > 0) {
            contacts.forEach(contact -> {
                contact.setUserId(user.getUserid());
                contactMapper.updateByPrimaryKey(contact);
            });
        }
        /* 历史消息添加用户ID */
        List<PushMsg> pushMsgs = pushMsgService.selectByReceiveAndState(user.getUsername());
        if (pushMsgs.size() > 0) {
            for (int i = 0; i < pushMsgs.size(); i++) {
                PushMsg pushMsg = pushMsgs.get(i);
                pushMsg.setUserId(user.getUserid());
                pushMsgService.updateByPrimaryKeySelective(pushMsg);
            }
        }
        /* 注册成功直接登录 **/
        Collection<GrantedAuthority> authorities = this.loginUserDetailsService.loadAuthority(user.getUsername());
        LoginUserDetails loginUserDetails = new LoginUserDetails();
        loginUserDetails.setUsername(user.getUsername());
        loginUserDetails.setUserid(user.getUserid().toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginUserDetails, md5Password, authorities);
        String token = this.jwtTokenProvider.createToken(authentication);
        return ResultUtil.success(token);
    }


    @Override
    public Result resetPassword(Map<String, Object> map) throws Exception {
        LoginUserDetails user = loginUserService.getUserByUsername(LoginUserHelper.getUserName());
        String md5Password = MD5Util.encode(map.get("oldPassword").toString() + user.getSalt());
        if (!md5Password.equals(user.getPassword())) {
            return ResultUtil.info("user.reset.password.error.code", "user.reset.password.error.msg");
        }
        String newPwd = MD5Util.encode(map.get("newPassword").toString() + user.getSalt());
        if (newPwd.equals(user.getPassword())) {
            return ResultUtil.info("user.reset.equal.password.code", "user.reset.equal.password.msg");
        } else {
            map.put("userid", user.getUserid());
            map.put("password", newPwd);
            userMapper.updateById(map);
        }
        /* 修改密码修改链上对应身份密码信息 start **/
        boolean b = tidUtil.updatePassword(user.getUsername(), md5Password, newPwd);
        if (!b) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error();
        }
        /* 修改密码修改链上对应身份密码信息 end **/
        return ResultUtil.success();
    }

    @Override
    public Result userInfo() throws Exception {
        Map<String, Object> map = new HashMap<>(16);
        AppUser user = userMapper.findByName(LoginUserHelper.getUserName());
        UserCard userCard = userCardService.selectByUserId(user.getUserid().toString(), null);
        OrgUser orgUser = orgUserService.selectByUserId(user.getUserid());
        map.put("isAdmin", null == orgUser ? false : true);
        map.put("email", StringUtils.isBlank(user.getEmail()) ? "" : user.getEmail());
        map.put("userName", LoginUserHelper.getUserName());
        map.put("realName", null == userCard ? "" : userCard.getRealName());
        map.put("headPhoto", StringUtils.isBlank(user.getHeadPhoto()) ? "" : user.getHeadPhoto());
        map.put("realState", null == userCard ? "" : userCard.getRealState());
        map.put("sex", user.getSex());
        map.put("nickName", null == user.getDescribe() ? "" : user.getDescribe());
        map.put("userid", user.getUserid());
        map.put("address", user.getAddress());
        return ResultUtil.success(map);
    }

    @Override
    public Result getPushMsg() throws Exception {
        List<PushMsg> pmList = pushMsgService.selectByReceiveAndState(LoginUserHelper.getUserName());
        if (pmList.size() > 0) {
            pmList.forEach(pm -> {
                String json = new Gson().toJson(pm);
                try {
                    String aliases = HttpSendUtil.getData("aliases", LoginUserHelper.getUserName());
                    JSONObject jsonObject = JSONObject.parseObject(aliases);
                    String registrationIds = jsonObject.get("registration_ids").toString();
                    if (!"[]".equals(registrationIds)) {
                        int resout = PushUtil.getInstance().sendToRegistrationId(LoginUserHelper.getUserName(), pm.getTitle(), json);
                        if (1 == resout) {
                            pm.setState("1");
                            pushMsgService.updateByPrimaryKeySelective(pm);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return ResultUtil.success();
    }

    @Override
    public String getRegistrationId(Map<String, Object> map) throws Exception {
        String registrationId = map.get("registrationId").toString();
        AppUser user = userMapper.findByName(LoginUserHelper.getUserName());
        user.setUsercode(registrationId);
        userMapper.updateByPrimaryKeySelective(user);
        JSONObject json = new JSONObject();
        json.put("alias", user.getUsername());
        HttpSendUtil.postData("devices/" + registrationId, json);
        return user.getUserid().toString();
    }

    private Result checkVeriCode(Information info, Map<String, Object> map) throws Exception {
        if (null == info || !map.get("veriCode").toString().equals(info.getInfoCode())) {
            return ResultUtil.info("email.code.error.code", "email.code.error.msg");
        }
        Date date = new Date();
        if (date.after(info.getInfoExpireddate())) {
            return ResultUtil.info("email.code.expire.code", "email.code.expire.msg");
        }
        return ResultUtil.success();
    }

    @Override
    public Result userUpdate(AppUser user) throws Exception {
        userMapper.updateByPrimaryKeySelective(user);
        return ResultUtil.success();
    }

    @Override
    public Result retrievePwd(Map<String, Object> map) throws Exception {
        AppUser user = userMapper.findByName(map.get("phone").toString());
        if (null == user) {
            return ResultUtil.info("login.account.no.code", "login.account.no.msg");
        }
        String oldPassword = user.getPassword();
        user.setSalt(RandomCode.getCode());
        String newPassword = MD5Util.encode(map.get("password").toString() + user.getSalt());
        user.setPassword(newPassword);
        userMapper.updateByPrimaryKeySelective(user);
        /* 找回密码修改链上身份信息 start */
        boolean b = tidUtil.updatePassword(user.getUsername(), oldPassword, newPassword);
        if (!b) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error();
        }
        /* 找回密码修改链上身份信息 start */
        return ResultUtil.success();
    }

    @Override
    public Result nextOperate(Map<String, Object> map) throws Exception {
        map.put("email", map.get("phone").toString());
        map.put("veriCode", map.get("veriCode").toString());
        Information information = informationService.selectByPhone(map);
        Result result = checkVeriCode(information, map);
        if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
            return result;
        }
        information.setInfoState("1");
        informationService.updateByPrimaryKeySelective(information);
        return ResultUtil.success();
    }

    @Override
    public Result dynamicLogin(Map<String, Object> map) throws Exception {
        AppUser user = userMapper.findByName(map.get("username").toString());
        if (null == user) {
            return ResultUtil.info("login.account.no.code", "login.account.no.msg");
        }
        map.put("email", map.get("username").toString());
        map.put("veriCode", map.get("password").toString());
        Information information = informationService.selectByPhone(map);
        Result result = checkVeriCode(information, map);
        if (!result.getCode().equals(I18nUtil.getMessage("success.code"))) {
            return result;
        }
        Collection<GrantedAuthority> authorities = this.loginUserDetailsService.loadAuthority(map.get("username").toString());
        LoginUserDetails loginUserDetails = new LoginUserDetails();
        loginUserDetails.setUsername(user.getUsername());
        loginUserDetails.setUserid(user.getUserid().toString());
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginUserDetails, user.getPassword() + user.getSalt(), authorities);
        String token = this.jwtTokenProvider.createToken(authentication);
        information.setInfoState("1");
        informationService.updateByPrimaryKeySelective(information);
        return ResultUtil.success(token);
    }

    @Override
    public AppUser findByName(String confirmPhone) throws Exception {
        return userMapper.findByName(confirmPhone);
    }

    @Override
    public AppUser selectByEamil(String email) throws Exception {
        return userMapper.selectByEamil(email);
    }

    @Override
    public void updateByPrimaryKeySelective(AppUser u) throws Exception {
        userMapper.updateByPrimaryKeySelective(u);
    }

    @Override
    public AppUser selectByPrimaryKey(Integer userId) throws Exception {
        return userMapper.selectByPrimaryKey(userId);
    }

}
