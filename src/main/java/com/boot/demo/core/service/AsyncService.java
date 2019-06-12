package com.boot.demo.core.service;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public interface AsyncService<T>{
	
	/**
	 * 异步插入文档
	 * @param	 t	文档对象
	 */
	public void insert(T t);
	
	/**
	 * 异步修改文档（一条）
	 * @param	 query	查询条件
	 * @param	 update	修改内容
	 * @param	 collectionName	集合名称
	 */
	public void updateFirst(Query query, Update update, String collectionName);

	/**
	 * 异步修改符合条件的文档
	 * @param	 query	查询条件
	 * @param	 update	修改内容
	 * @param	 collectionName	集合名称
	 */
	public void updateMulti(Query query, Update update, String collectionName);
	
	/**
	 * 异步修改文档（一条）
	 * @param	 id
	 * @param	 collectionName	集合名称
	 */
	public void addHotByID(String id, String collectionName);
	/**
	 * 异步删除文档，一个条件，删满足条件的
	 * @param	 filed	条件
	 * @value	条件的值
	 * @param	 collectionName	集合名称
	 */
	public void deleteByFiled(String filed, Object value, String collectionName);

}
