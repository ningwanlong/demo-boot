package com.boot.demo.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Title:
 * @author: chenmingyue
 * @date: 2018/3/16 12:01
 * Description:配置URLInterceptor拦截器，以及拦截路径
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new CommonInterceptor())
        	.addPathPatterns("/*")
        	.addPathPatterns("/*/*")
        	.addPathPatterns("/*/*/*")
        	.addPathPatterns("/*/*/*/*")
        	.addPathPatterns("/*/*/*/*/*")
        	.addPathPatterns("/*/*/*/*/*/*")
        	.addPathPatterns("/*/*/*/*/*/*/*")
        	.excludePathPatterns("/excludeurl/*")
        	.excludePathPatterns("/excludeurl/*/*")
        	;
        //registry.addInterceptor(new AllAppCommonInterceptor()).addPathPatterns("/allApp/user/*").addPathPatterns("/allApp/user/*/*");
        
        super.addInterceptors(registry);
    }
}

