package com.boot.demo.tools;

public class CacheUtil {

	public static String getSMSKey(String mobile) {
		return createKey(Constant.CACHE_PREFIX_SMS, mobile);
	}

	public static String getRegisterKey(String mobile) {
		return createKey(Constant.CACHE_PREFIX_REGISTER, mobile);
	}

	public static String getLoginKey(String uid) {
		return createKey(Constant.CACHE_PREFIX_LOGIN, uid);
	}

	/**
	 * <p>Title: 生成key</p>  
	 * <p>Description: </p>  
	 * @param prefix 前缀
	 * @param str  字符串
	 * @return
	 */
	private static String createKey(String prefix, String str) {
		String key = String.format("%s:%s", prefix, str);
		if(Constant.config.getEnvironment() == Constant.ENVIRONMENT_DEV) {
			//区别出测试和正式环境
			key += Constant.ENVIRONMENT_DEV;
		}
		return key;
	}
	
	
}
