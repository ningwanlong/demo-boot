package com.boot.demo.doc;

import java.util.Date;
import java.util.List;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
/**
* <p>Title: 后台管理员角色</p>  
 */
@Data
@Document(collection = "admin_role")
public class AdminRole {
	@Transient
	public static final String collectionName = "admin_role";
	
	@Id
	private String id;
	private int status; //通用状态：0-正常、1-删除
	private boolean leave;	//是否官方角色：true/是，官方角色有一些官方专属权限。
	private String name;	//角色名称
	private List<ObjectId> createRoleIds;//创建该角色的上级角色id数组
	private ObjectId createRoleId;//创建该角色的上级角色id
	private int[] auths;	//菜单权限
	private Date createDate;	//操作时间


}
