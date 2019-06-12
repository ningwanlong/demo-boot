package com.boot.demo.doc;

import java.util.Date;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**                                      
* <p>Title: APP用户</p>  
* <p>Description: </p>  
* @author nwl
* @date 2018年10月10日
 */
@Data
@Document(collection = "user_base_info")
public class UserBaseInfo {
	@Transient
	private static final long serialVersionUID = -4553002102697789456L;
	
	@Transient
	public static final String collectionName = "user_base_info";
	@Id
	private String id;
	private long weId;//我们自定义生成的id
	private int status; // 账号状态	1-未激活 2-正常 3-冻结 4-离线
	private String realname;	//真实姓名
	private String mobile;	//手机号
	private String avatar;	//头像
	private String intro;	//介绍
	private String[] lable;	//个人标签
	private int gender;		//性别	0-女、1-男、其他-未知
	private String token;	//签名用
	private int devType;	//登陆的设备类型 1-ios 2-android
	private int registerDevType;	//用户注册设备来源类型 1-ios、2-android、3-H5、4-导入
	private String devNo;	//设备编号
	private String impw;	//聊天密码
	private Date createDate;	//注册时间
	private String password;	//登陆用户密码
	private String xylinkMobile;//小鱼注册的手机号
	private long frozenTimeStap; //冻结结束时间，status为冻结时：大于0为待解冻时间戳、小于等于0为永久冻结。


}