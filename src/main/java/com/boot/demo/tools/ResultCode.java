package com.boot.demo.tools;

public enum ResultCode {
	/**0, "成功"*/
	SUCCESS(0, "成功"),
	/**1001, "服务端内部错误"*/
	EXCEPTION(1001, "服务端内部错误"),
	/**1002, "网络IO错误"*/
	IO_ERROR(1002, "网络IO错误"),
	/**1003, "返回结果解析错误"*/
	PARSE_ERROR(1003, "返回结果解析错误"),
	/**1004, "网络不可用"*/
	NO_NETWORK(1004, "网络不可用"),
	/**1005, "系统错误"*/
	SYSTEM_ERROR(1005, "系统错误"),
	/**10111, "系统错误"*/
	UNKNOWN_ERROR(1006, "未知错误"),
	/**1007, "参数异常，请检查参数是否合法"*/
	ERROR_PARAM(1007, "参数异常，请检查参数是否合法"),
	/**1008, "参数无效，查看详细错误信息"*/
	INVALID_PARAM(1008, "参数无效"),
	/**1009, "操作失败"*/
	FAIL(1009, "操作失败,请稍后再试"),
	/**1010, "没有数据"*/
	NO_DATA(1010, "没有数据"),
	/**1011, "无效签名"*/
	INVALID_SIGN(1011, "无效签名"),
	/**1012, "360服务异常"*/
	CAMERA_360_ERROR(1012, "360服务异常"),
	/**1014, "无效权限"*/
	INVALID_AUTH(1014, "无效权限"),
	/**1090, "服务器繁忙。请稍后再试"*/
	SERVER_BUSY(1090, "服务器繁忙,请稍后再试"),
	
	/**2000, "用户不存在"*/
	USER_NOT_EXSIT(2000, "用户不存在"),
	/**2001, "用户未注册"*/
	ACCOUNT_NOT_EXSIT(2001, "用户未注册"),
	/**2002, "用户未激活"*/
	ACCOUNT_NOT_ACTIVE(2002, "用户未激活"),
	/**2003, "账户被冻结"*/
	ACCOUNT_FROZEN(2003, "账户被冻结"),
	/**2004, "账户异常"*/
	ACCOUNT_EXCEPTION(2004, "账户异常"),
	/**2005, "登陆过期"*/
	LOGIN_EXPIRE(2005, "登陆过期"),
	/**2006, "登陆校验失败"*/
	LOGIN_CHECK_FAIL(2006, "登陆校验失败"),
	/**4001, 正在通话中"*/
	CALL_ING(4001, "正在通话中"),
	/**4002, "目标不存在"*/
	OBJECT_NOT_EXSIT(4002, "目标不存在"),
	/**4003, "禁止访问"*/
	OBJECT_NO_ACCESS(4003, "禁止访问"),
	/**4004, "创建数已达上限"*/
	CREATE_OBJ_FULL(4004, "创建数已达上限"),
	/**4005, "摄像机正在被商家使用，请先去商家删除！"*/
	CAMERA_IN_BUSINESS(4005, "摄像机正在被商家使用，请先去商家删除！"),
	
	/**2007, "账户已存在"*/
	ACCOUNT_EXISTS(2007, "该手机号已被注册");
	
	
	private String msg;
	private int code;

	private ResultCode(int code, String msg) {
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public int getCode() {
		return code;
	}

}
