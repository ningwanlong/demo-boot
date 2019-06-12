package com.boot.demo.global;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boot.demo.core.controller.BaseController;
import com.boot.demo.tools.ResultCode;
import com.boot.demo.tools.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
* <p>Title: 全局异常处理，未知异常</p>  
* <p>Description: </p>  
* @author nwl  
* @date 2018年9月29日
 */
@Configuration
public class ExceptionHandler extends BaseController implements HandlerExceptionResolver {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public ModelAndView resolveException(HttpServletRequest request,  
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mv = new ModelAndView();
		try {
			//强转HandlerMethod类，才能详细获取错误具体位置
			HandlerMethod hm=(HandlerMethod) handler;
			logger.error(hm.getMethod()+"产生异常:",ex);
			//使用response返回
			response.setStatus(HttpStatus.OK.value()); //设置状态码
			response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType
			response.setCharacterEncoding("UTF-8"); //避免乱码
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			rm = new ResultModel();
			rm.add(ResultCode.SERVER_BUSY);
			response.getWriter().write(super.toJson());
		} catch (IOException e) {
			logger.error("与客户端通讯异常:"+ e.getMessage(), e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return mv;
	}
	
	
	
	
	
	
	
	
	
	
	
}
