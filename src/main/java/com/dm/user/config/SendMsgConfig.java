package com.dm.user.config;

public class SendMsgConfig {

	/**
	 * url前半部分
	 */
	public static final String BASE_URL = "https://openapi.miaodiyun.com/distributor/sendSMS";

	/**
	 * 开发者注册后系统自动生成的账号，可在官网登录后查看
	 */
	public static final String ACCOUNT_SID = "3ff432721fcda2c0c2cb447259e27f1d";

	/**
	 * 开发者注册后系统自动生成的TOKEN，可在官网登录后查看
	 */
	public static final String AUTH_TOKEN = "320b3cdebc2f00ea0758870a6a266c7e";

	/**
	 * 响应数据类型, JSON或XML
	 */
	public static final String RESP_DATA_TYPE = "JSON";

	/**
	 * 短信内容
	 */
	public static final String SMSCONTENT = "【北京迪曼森科技有限公司】您的验证码为#num，该验证码5分钟内有效。请勿泄漏于他人。";
	
}
