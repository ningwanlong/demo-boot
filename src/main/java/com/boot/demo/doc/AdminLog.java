package com.boot.demo.doc;

import java.util.Date;

import com.mongodb.BasicDBObject;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 后台操作日志
 */
@Data
@Document(collection = "admin_log")
public class AdminLog {
	@Id
	private String id;
	private int status;			//状态：0-成功，1-失败
	private ObjectId adminId;	//操作者用户id
	private String remarks;		//操作备注
	private ObjectId oid;		//被操作对象id
	private BasicDBObject obj;	//被操作的对象
	private Date createDate;	//操作时间
	

	
}
