package com.boot.demo.doc;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * nwl  后台管理员信息
 */
@Data
@Document(collection = "admin_base_info")
public class AdminBaseInfo implements Serializable {
	@Transient
	private static final long serialVersionUID = -4553002102697843628L;
	@Transient
	public static final String collectionName = "admin_base_info";
	@Id
	private String	id;
	private	int status;	//通用状态：0-正常，1-关闭删除
	private boolean lock; //是否锁定，锁定不能登陆
	private String	loginName;	//登录帐号
	private String password; //登录密码，MD5(登录帐号+密码)
	private String	name;	//名称
	private String	phone;	//联系电话
	private Date createDate;	//创建日期
	private Date loginDate;  //上次登录时间
	private String token;  //登陆使用的token
	private ObjectId roleId; //所属角色id
	
	/**每次登陆出错允许次数*/
	@Transient
	public static final int ERROR_COUNT = 6;
	/**密码加密盐值*/
	@Transient
	public static final String SALT_PASSWORD = "FGHJK(YT*%$Gs";
	/**管理员登陆session里的key值*/
	@Transient
	public static final String ADMIN_LOGIN_SESSION_KEY = "ADMIN_LOGIN_SESSION_KEY";

	
}
