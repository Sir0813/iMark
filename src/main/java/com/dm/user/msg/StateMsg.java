package com.dm.user.msg;

/**
 * @author cui
 * @date 2019-09-26
 */
public final class StateMsg {

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

	/** 存证不删除 */
	public static final int CERT_NOT_DELETE = 1;

	/** 存证删除 物理删除 */
	public static final int CERT_IS_DELETE = 0;

	/** 存证文件删除 */
	public static final String CERT_FILE_IS_DELETE = "0";

	/** 存证文件保留 */
	public static final String CERT_FILE_NOT_DELETE = "1";

	/** 待自己确认 */
	public static final String CONFIRM_TO_ME = "6";
}
