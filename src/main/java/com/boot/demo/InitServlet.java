package com.boot.demo;


import javax.annotation.Resource;
import javax.servlet.ServletContext;

import com.boot.demo.core.redis.RedisCache;
import com.boot.demo.doc.ConfigDocument;
import com.boot.demo.tools.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
/**
* <p>Title: 初始化spring，需要初始化操作的内容可以在本class完成</p>  
* <p>Description: </p>  
* @author nwl  
* @date 2018年9月29日
 */
@Component
public class InitServlet implements InitializingBean, ServletContextAware  {
  
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	RedisCache<Integer> redisCache;
	
	@Resource
	private MongoTemplate mongoTemplate;
	
	ServletContext application;
	
	@Override
	public void setServletContext(ServletContext arg0) {
		application = arg0;
		
	}  
	
	@Override  
    public void afterPropertiesSet() throws Exception {
    	try {
    		application.setAttribute("MYFILE_PREFIX_MAP",Constant.MYFILE_PREFIX_MAP);
    		ConfigDocument config = mongoTemplate.findOne(new Query(), ConfigDocument.class);
    		if(config != null) {
    			Constant.config = config;
    		}else {
    			config = Constant.config;
    			//没有配置文件则生成默认配置文件
    			mongoTemplate.insert(config);
    		}
    		//timerTaskService.startSask();
    		if(config.getEnvironment() == Constant.ENVIRONMENT_DEV){
				Constant.SIGN_APP_KEY = "TEST-74545BB-COMM-APP";
				Constant.THIRD_PARTY_PREFIX = "test-";
				Constant.JPUSH_PREFIX = "test_";
			}else if(config.getEnvironment() == Constant.ENVIRONMENT_PRO){
				Constant.SIGN_APP_KEY = "PROD-74545BB-COMM-APP";
				Constant.THIRD_PARTY_PREFIX = "prod-";
				Constant.JPUSH_PREFIX = "prod_";
			}
    		System.out.println("启动加载配置文件："+Constant.config.toString());
    		
		} catch (Exception e) {
			logger.error("", e);
		}
    }
}
