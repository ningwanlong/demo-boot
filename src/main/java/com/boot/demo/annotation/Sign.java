package com.boot.demo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Sign {

	/**
	 * APP密钥
	 */
	public final static int APP_KEY = 1;

	/**
	 * 用户密钥
	 */
	public final static int USER_KEY = 2;

	/**
	 * 不用验证
	 */
	public final static int NO_CHECK = 3;

	int keyType();
}
