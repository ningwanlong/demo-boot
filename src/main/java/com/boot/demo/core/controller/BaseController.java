package com.boot.demo.core.controller;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.boot.demo.core.service.AsyncService;
import com.boot.demo.tools.ResultModel;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

public abstract class BaseController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	protected MongoTemplate mongoTemplate;
	@Resource
	protected AsyncService<Object> asyncService;

	protected ResultModel rm;

	private static ValueFilter filter = new ValueFilter() {
		@Override
		public Object process(Object obj, String s, Object v) {
			if (v instanceof ObjectId){
				return ((ObjectId) v).toHexString();
			}else if(v instanceof Date){
				return ((Date) v).getTime();
			}
			return v;
		}
	};

	public String toJson() {
		int code = rm.getCode();
		String msg = rm.getMsg();
		JSONObject result = new JSONObject();
		result.put("code", code);
		result.put("msg", msg);
		return result.toString();
	}
	
	public String toCodeJson(String verifCode) {
		int code = rm.getCode();
		String msg = rm.getMsg();
		JSONObject result = new JSONObject();
		result.put("code", code);
		result.put("msg", msg);
		result.put("verifCode", verifCode);
		return result.toString();
	}

	public String toJson(String data) {
		int code = rm.getCode();
		String msg = rm.getMsg();
		JSONObject result = new JSONObject();
		result.put("code", code);
		result.put("msg", msg);
		if (data != null) {
			result.put("data", data);
		}
		return result.toString();
	}

	public String toJson(Object data) {
		int code = rm.getCode();
		String msg = rm.getMsg();
		JSONObject result = new JSONObject();
		result.put("code", code);
		result.put("msg", msg);
		if (data != null) {
			result.put("data", data);
		}

		System.out.println(result.toJSONString());

		return JSONObject.toJSONString(result, filter,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullNumberAsZero,
				SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.PrettyFormat);
	}


}
