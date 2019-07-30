package com.dm.user.msg;

public final class StateMsg {
	
	//----------------存证状态----------------
	public static final int noCert = 0;//未存证 草稿
	
	public static final int toCert = 1;//存证中
	
	public static final int certSuccess = 2;//存证成功
	
	public static final int certFail = 3;//存证失败
	
	public static final int othersConfirm = 4;//待他人确认

	public static final int isReturn = 5;//已退回
	
	public static final int isRevoke = 6;//已撤回
	
	//----------------确认状态----------------
	public static final int isConfirm = 0;//已确认
	
	public static final int noConfirm = 1;//未确认
	
	public static final int returnConfirm = 2;//已退回
	
	public static final int revokeConfirm = 3;//已撤回
	
	public static final int originator = 4;//发起人
	
	//----------------分页每页条数----------------
	public static final int pageSize = 10;
}
