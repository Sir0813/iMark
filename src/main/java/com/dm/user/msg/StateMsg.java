package com.dm.user.msg;

/**
 * @author cui
 * @date 2019-09-26
 */
public final class StateMsg {
	
	/** 未存证 草稿 */
	public static final int NO_CERT = 0;

	/** 存证中 */
	public static final int TO_CERT = 1;

	/** 存证成功 */
	public static final int CERT_SUCCESS = 2;

	/** 存证失败 */
	public static final int CERT_FAIL = 3;

	/** 待他人确认 */
	public static final int OTHERS_CONFIRM = 4;

	/** 已退回 */
	public static final int IS_RETURN = 5;

	/** 已撤回 */
	public static final int IS_REVOKE = 6;

	/** 已确认 */
	public static final int IS_CONFIRM = 0;

	/** 未确认 */
	public static final int NO_CONFIRM = 1;

	/** 已退回 */
	public static final int RETURN_CONFIRM = 2;

	/** 已撤回 */
	public static final int REVOKE_CONFIRM = 3;

	/** 发起人 */
	public static final int ORIGINATOR = 4;
	
	/** 每页条数 */
	public static final int PAGE_SIZE = 10;

	/** 操作系统开始字母 */
	public static final String OS_NAME = "win";

	/** 存证标题 */
	public static final String SAVE_CERT_TITLE = "存证→";

	/** 存证描述 */
	public static final String SAVE_CERT_CONTENT = "您有一条新的存证待查看→【certName】";

	/** 出证标题 */
	public static final String OUT_CERT_TITLE = "出证→";

	/** 出证描述 */
	public static final String OUT_CERT_CONTENT = "您有一条新的出证待查看→【outCertName】";

}
