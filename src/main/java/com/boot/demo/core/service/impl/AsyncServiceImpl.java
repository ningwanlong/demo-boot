package com.boot.demo.core.service.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.boot.demo.core.redis.RedisCache;
import com.boot.demo.core.service.AsyncService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mongodb.WriteConcern;


@Service
public class AsyncServiceImpl<T> implements AsyncService<T> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private MongoTemplate mongoTemplate;
	@Resource
	private RedisCache<Object> redisCache;

	@Async 
	public void insert(T t){
		try {
			mongoTemplate.insert(t);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	@Async 
	public void updateFirst(Query query,Update update,String collectionName){
		try {
			mongoTemplate.updateFirst(query, update, collectionName);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	@Async 
	public void addHotByID(String id,String collectionName){
		try {
				mongoTemplate.updateFirst(new Query(where("_id").is(new ObjectId(id)))
						, new Update().inc("hot", 1), collectionName);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	@Async
	public void deleteByFiled(String filed,Object value, String collectionName) {
		try {
			mongoTemplate.remove(new Query(where(filed).is(value)),
					collectionName);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	@Async
	public void updateMulti(Query query, Update update, String collectionName) {
		try {
			mongoTemplate.setWriteConcern(WriteConcern.ACKNOWLEDGED);
			mongoTemplate.updateMulti(query, update, collectionName);
		} catch (Exception e) {
			logger.error("", e);
		}
	}



}
