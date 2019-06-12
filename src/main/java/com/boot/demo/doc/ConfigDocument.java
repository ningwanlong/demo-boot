package com.boot.demo.doc;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 配置文件对应集合
 * @author nwl15
 *
 */
@Data
@Document(collection = "config")
public class ConfigDocument {
	@Transient
	public static final String collectionName = "config";
	@Id
	private String id;//配置文件id
	
	private int environment = 0;//环境：0-测试环境、1-正式环境
	private double homeMxdDist = 500000.0D;//附近最大查询距离:米,附近动态允许查看最大距离
	private int getSmsMaxNumPer = 50;//每个ip地址指定时间getSmsPerTime能获取短验证码信次数:次数，
	private int getSmsPerTime = 3600;//每个ip地址能获取短验证码信次数  的 指定时间周期：秒数

	private int adminLoginFailNum = 5;//后台管理员指定时间内允许登陆密码错误失败次数
	private int adminLoginFailTime = 10;//后台管理员指定时间内允许登陆密码错误失败次数的时间范围：分钟

	

	
}
