package com.boot.demo.core.redis;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisCache<T> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	//@Resource
    private RedisTemplate<String, T> redisTemplate;
    RedisSerializer<String> kSerializer;
    RedisSerializer<T> vSerializer;
	
	@SuppressWarnings("unchecked")
	public RedisCache(RedisTemplate<String, T> template) {
		this.redisTemplate = template;
		this.kSerializer = redisTemplate.getStringSerializer();
		this.vSerializer = (RedisSerializer<T>) redisTemplate.getDefaultSerializer();
	}

    /**
     * 添加缓存数据	暂时不用
     * @param key
     * @param obj
     * @return
     * @throws Exception
     */
    public boolean putCache(String key, T obj) throws Exception {
        final byte[] bkey = kSerializer.serialize(key);
        final byte[] bvalue = vSerializer.serialize(obj);
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(bkey, bvalue);
                return true;
            }
        });
        return result;
    }

    /**
     * 添加缓存数据，设定缓存失效时间
     * @param key
     * @param obj
     * @param expireTime（秒）
     * @throws Exception
     */
    public void putCacheWithExpireTime(String key, T obj, final long expireTime) throws Exception {
        final byte[] bkey = kSerializer.serialize(key);
        final byte[] bvalue = vSerializer.serialize(obj);
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setEx(bkey, expireTime, bvalue);
                return true;
            }
        });
    }
    
    /**
     * 更改失效时间
     * @param key
     * @param expireTime（秒）
     * @throws Exception
     */
    public void expireTime(String key,final long expireTime) throws Exception {
        final byte[] bkey = kSerializer.serialize(key);
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
            	return connection.expire(bkey, expireTime);
            }
        });
    }

    /**
     * 根据key取缓存数据
     * @param key
     * @return
     * @throws Exception
     */
    public T getCache(final String key) throws Exception {
        byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.get(kSerializer.serialize(key));
            }
        });
        if (result == null) {
            return null;
        }
        return vSerializer.deserialize(result);
    }
    
    /**
     * 根据key获取剩余过期时间
     * @param key
     * @return 秒 	如果key不存在返回-2，如果key存在但是已经过期返回-1 
     * @throws Exception
     */
    public long getTTL(final String key) throws Exception {
    	long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ttl(kSerializer.serialize(key));
            }
        });
        return result;
    }
    
    /**
     * 根据key删除
     * @param key
     * @return 	0-失败(key对应的值不存在)、1-成功
     * @throws Exception
     */
    public long delete(final String key) throws Exception {
    	long result = redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.del(kSerializer.serialize(key));
            }
        });
        return result;
    }
    
    /**
     * 添加消息队列缓存数据，设定缓存失效时间
     * @param key
     * @param obj
     * @param expireTime（秒）
     * @throws Exception
     */
    public void putListCacheWithExpireTime(String key, List<T> obj, final long expireTime) throws Exception {
        final byte[] bkey = kSerializer.serialize(key);
        final byte[][] bvalues = new byte[obj.size()][];
        
        for(int i=0;i<obj.size();i++){
        	bvalues[i] = vSerializer.serialize(obj.get(i));
        }
        
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.lPush(bkey, bvalues);
                connection.expire(bkey, expireTime);
                return true;
            }
        });
    }
    
    /**
     * 取出消息队列缓存数据
     * @param key
     * @throws Exception
     */
    public T popList(String key) throws Exception {
    	byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.rPop(kSerializer.serialize(key));
            }
        });
        if (result == null) {
            return null;
        }
        return vSerializer.deserialize(result);
    }
    
	 /**
     * 获取唯一Id，第一次会自动生成
     * @param key key值
     * @param hashKey 区别key的hash值
     * @param delta 增加量（不传采用1）
     * @return
     */
    public Long incrementHash(String key,String hashKey,Long delta){
        try {
            if (null == delta) {
                delta=1L;
            }
            return redisTemplate.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {//redis宕机时采用uuid的方式生成唯一id
        	logger.error("redis生成id异常",e);
            int first = new java.util.Random(10).nextInt(8) + 1;
            int randNo = UUID.randomUUID().toString().hashCode();
            if (randNo < 0) {
                randNo = -randNo;
            }
            return Long.valueOf(first + randNo);
        }
    }
}
