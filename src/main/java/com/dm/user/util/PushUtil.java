package com.dm.user.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
public class PushUtil{

	private static PushUtil instance;
	
    private JPushClient jpushClient;

    private static Logger logger = LoggerFactory.getLogger(PushUtil.class);

    /**
     * 极光账户初始化
     * @throws Exception
     */
    private PushUtil() throws Exception {
    	String appKey = null;
    	String masterSecret = null;
    	Yaml yaml = new Yaml();
        String url = PushUtil.class.getClassLoader().getResource("config/application-dev.yml").getPath();
        Map<String,Object> map = yaml.loadAs(new FileInputStream(url), Map.class);
        appKey = ((Map<String, Object>) map.get("jpush")).get("appKey").toString();
        masterSecret = ((Map<String, Object>) map.get("jpush")).get("masterSecret").toString();
    	if (appKey == null || masterSecret == null) {
            throw new Exception("极光推送账户初始化失败");
        }
        jpushClient = new JPushClient(masterSecret, appKey);
    }

    public void bindMobil(String registrationId,String mobile){
        try {
            jpushClient.bindMobile(registrationId, mobile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static PushUtil getInstance() throws Exception {
        if (null == instance) {
            synchronized (PushUtil.class) {
                if (null == instance) {
                    instance = new PushUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 推送给指定设备标识参数的用户（自定义消息通知）
     * @param alias 设备标识 用户ID 别名
     * @param msg_title 消息内容标题
     * @param msg_content 消息内容
     * @return 0推送失败，1推送成功
     */
    public int sendToRegistrationId(String alias, String msg_title, String msg_content) {
        int result = 0;
        try {
        	//pushPayload = this.buildPushObjectAlertWithTitle(alias, msg_content, extrasparam);
            PushPayload pushPayload = null;
            pushPayload = this.buildPushObjectMessageWithTitle(alias, msg_title, msg_content);
            PushResult pushResult = jpushClient.sendPush(pushPayload);
            if(pushResult.getResponseCode() == 200&&pushResult.statusCode==0){
                result=1;
            }
            logger.info("[极光推送]PushResult result is " + pushResult);
        } catch (APIConnectionException e) {
            logger.error("[极光推送]Connection error. Should retry later. ", e.getMessage());
        } catch (APIRequestException e) {
            logger.error("[极光推送]HTTP Status: " + e.getStatus());
            logger.error("[极光推送]Error Code: " + e.getErrorCode());
            logger.error("[极光推送]Error Message: " + e.getErrorMessage());
        }
        return result;
    }
 
    /**
     * 推送自定义消息
     * @param alias 设备标识 用户ID 别名
     * @param msg_title 消息内容标题
     * @param msg_content 消息内容
     * @return
     */
    private PushPayload buildPushObjectMessageWithTitle(String alias, String msg_title, String msg_content) {
        //创建一个IosAlert对象，可指定APNs的alert、title等字段
        //IosAlert iosAlert =  IosAlert.newBuilder().setTitleAndBody("title", "alert body").build();
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.android())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.alias(alias))
                //.setAudience(Audience.all()) //所有人
                //.setAudience(Audience.registrationId(alias)) //注册ID
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                /*.setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(msg_content)
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("url", extrasparam)
                                .build())
                        //指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(msg_content)
                                //直接传alert
                                //此项是指定此推送的badge自动加1
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("sound.caf")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("url", extrasparam)
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)
                                .build())
                        .build())*/
                //Platform指定了哪些平台就会向指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                // [通知与自定义消息有什么区别？]了解通知和自定义消息的区别
                .setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)//消息内容本身
                        .setTitle(msg_title)//消息标题
                        .build())
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(true)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天； 秒为单位
                        .setTimeToLive(1 * 60 * 60 * 24)
                        .build())
                .build();
    }
    
    /**
     * 推送通知
     * @param alias
     * @param msg_content
     * @param extravalue
     * @return
     */
	private PushPayload buildPushObjectAlertWithTitle(String alias,
    		String msg_content, String extravalue) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(null==alias?Audience.all():Audience.registrationId(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(msg_content)//通知内容
                                .addExtra("key", extravalue)//自定义json信息
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .setSendno(1)
                        .setTimeToLive(1 * 60 * 60 * 24)
                        .build())
                .build();
    }
}
