package com.boot.demo.interceptor;

import java.io.IOException;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.boot.demo.annotation.Sign;
import com.boot.demo.core.redis.RedisCache;
import com.boot.demo.core.service.AsyncService;
import com.boot.demo.doc.AdminBaseInfo;
import com.boot.demo.doc.UserBaseInfo;
import com.boot.demo.tools.CacheUtil;
import com.boot.demo.tools.Constant;
import com.boot.demo.tools.GeneralUtil;
import com.boot.demo.tools.ResultCode;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
* <p>Title: 公共拦截器</p>  
* <p>Description: </p>  
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	RedisCache<UserBaseInfo> redisCacheUser;
	@Resource
	RedisCache<AdminBaseInfo> redisCacheAdmin;
	@Resource
	AsyncService<Object> asyncService;
	
	@Resource
	protected MongoTemplate mongoTemplate;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response,
			Object arg0) throws Exception {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		HandlerMethod handlerMethod = (HandlerMethod) arg0;
		Sign sign = handlerMethod.getMethodAnnotation(Sign.class);

		//把body直接解析成json参数使用
		String body = request.getParameter("body");
		if(StringUtils.isNotEmpty(body)) {
			try {
				JSONObject oBody = JSONObject.parseObject(body);
				request.setAttribute("oBody", oBody);
			} catch (Exception e) {
				logger.error("拦截器解析请求body为oBody失败",e);
			}
		}else {
			request.setAttribute("oBody", new JSONObject());
		}
		
		Enumeration<String> paramNames = request.getParameterNames();
		StringBuffer parameters = new StringBuffer("?");
		while(paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			String value = request.getParameter(paramName);
			parameters.append(paramName+"=").append(value+"&");
		}
		parameters.deleteCharAt(parameters.length()-1);
		
		logger.info("请求完整路径："+request.getRequestURL()+ parameters.toString());
		
		
		if (sign == null) {
			return true;
		} else if (sign.keyType() == Sign.USER_KEY) {
			
			//TODO 免签名
			/*UserBaseInfo user = mongoTemplate.findById(new ObjectId("5bbdb65db4100e0358427f54"), UserBaseInfo.class);
			request.setAttribute("user", user);
			if(user != null) {
				return true;
			}*/
		
			String key = chekLoginState(request, response);
			if (key == null) {
				return false;
			}
			if (!chekSign(key, request)) {
				writeResult(response, ResultCode.INVALID_SIGN);
				return false;
			}
		} else if (sign.keyType() == Sign.APP_KEY) {
			if (!chekSign(Constant.SIGN_APP_KEY, request)) {
				writeResult(response, ResultCode.INVALID_SIGN);
				return false;
			}
		}else if(sign.keyType() == Sign.NO_CHECK){
			return true;
			
		}/*{else
			
			//TODO 测试API免签名
			HttpSession session0 = request.getSession(true);
			AdminInfo admin0 = new AdminInfo(new ObjectId("5c46c97443c958b1d8a1d935"),"5bc19123ed0efb92649571fd", 0, "superadmin",
					"测试账号管理员", "10086", null );
			session0.setAttribute("admin", admin0);
			if("1".equals("1")) {
				return true;
			}
			
			//上面是APP或者不需要签名验证，本else验证后台管理权限
			//设置session同步问题
			sessionShare(request, response);
			HttpSession session = request.getSession(false);
			String ajax = request.getParameter("req_t");
			boolean isAjax = false;
			
			if (sign.keyType() == Sign.AUTH_ADMIN_APP_KEY) {
				if(!chekAdminSign(Constant.SIGN_MANAGE_KEY, request)) {
					writeResult(response, ResultCode.INVALID_SIGN);
					return false;
				}
			}else {
				String passWord = chekAppAdminPassWord(request, response);
				if(!chekAdminSign(passWord, request)) {
					writeResult(response, ResultCode.INVALID_SIGN);
					return false;
				}
				//如果是后台管理的网页
				if(StringUtils.isNotEmpty(ajax) && ajax.equals("ajax")) {
					isAjax = true;
				}
				if(session == null || session.getAttribute("admin") == null) {
					if(!isAjax) {
						response.sendError(500, "登陆过期");
					}else {
						writeResult(response, ResultCode.LOGIN_EXPIRE);
					}

					return false;
				}else if(sign.keyType() == Sign.AUTH_ADMIN_LOGIN_SUCCESS){
					//如果仅需要登陆权限就直接访问。
					return true;
				}
				AdminInfo admin = (AdminInfo) session.getAttribute("admin");
				if(Arrays.binarySearch(admin.getAuths(), sign.keyType()) < 0) {
					//如果包含权限返回true;
					if(!isAjax) {
						response.sendError(500, "非法操作");
					}else {
						writeResult(response, ResultCode.INVALID_AUTH);
					}
					return false;
				}
			}
		}*/

		return true;
	}
/*判断APP管理员密码是否有效，已经在respnose写入结果	private String chekAppAdminPassWord(HttpServletRequest request, HttpServletResponse respnose) throws Exception {
		try {
			String adminId = request.getParameter("adminId");
			AdminBaseInfoView admin = mongoTemplate.findById(adminId, AdminBaseInfoView.class);

			if(admin == null || admin.getStatus() == Constant.COMM_STATUS_CLOSE) {
				//用户不存在
				writeResult(respnose, ResultCode.USER_NOT_EXSIT);
			}else if(admin.isLock()){
				writeResult(respnose, ResultCode.ACCOUNT_FROZEN);
			}
			AdminInfo adminInfo = new AdminInfo(admin.getAreaId(), admin.getId(), admin.getLevel(), admin.getLoginName(),
					admin.getName(), admin.getPhone(), admin.getAuths());	//设置请求的用户数据
			request.getSession(true).setAttribute("admin", adminInfo);
			return admin.getToken();
		} catch (Exception e) {
			writeResult(respnose, ResultCode.SYSTEM_ERROR);
			logger.error("chekLoginState异常",e);
			return null;
		}
	}*/
	
	/**计算签名是否正确，时间戳相差不能超过1分钟，key：是用户的token，未写入respnose结果*/
	private boolean chekSign(String key, HttpServletRequest request) throws Exception {
		
		String body = request.getParameter("body");
		String timeStamp = request.getParameter("timeStamp");
		
		String sign = request.getParameter("sign");
		if (sign == null || sign.length() != 32 || body == null || timeStamp == null) {
			return false;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(key).append(timeStamp).append(body);
		
		//如果是开发环境，记录请求数据方便调试
		if(Constant.ENVIRONMENT_CURRENT == Constant.ENVIRONMENT_DEV){
			logger.info("服务器计算sign="+ GeneralUtil.getMD5(sb.toString()));
		}
		//判断传入的时间戳是否在允许范围
		long time = Long.parseLong(timeStamp);
		if (Constant.ENVIRONMENT_CURRENT == Constant.ENVIRONMENT_PRO 
				&& Math.abs(System.currentTimeMillis() - time) > 1000*60*1) {
			logger.error("签名传入时间戳误差超过1分钟");
			return false;
		} 
		return GeneralUtil.getMD5(sb.toString()).equals(sign);
	}
	/**判断用户token是否有效，已经在respnose写入结果*/
	private String chekLoginState(HttpServletRequest request, HttpServletResponse respnose) throws Exception {
		try {
			String uid = request.getParameter("uid");
			if (StringUtils.isEmpty(uid)) {
				writeResult(respnose, ResultCode.ERROR_PARAM);
				return null;
			}
			String redisKey = CacheUtil.getLoginKey(uid);//redis保存的key
			long expireTime  = 60L*60*24;	//redis过期时间
			UserBaseInfo user = redisCacheUser.getCache(redisKey);
			if(user == null) {
				user = mongoTemplate.findById(new ObjectId(uid), UserBaseInfo.class);
				if(user != null) {
					redisCacheUser.putCacheWithExpireTime(redisKey, user, expireTime);
				}
			}else {
				//重新设置过期时间
				redisCacheUser.expireTime(redisKey, expireTime);
			}

			if (user == null) {
				writeResult(respnose, ResultCode.USER_NOT_EXSIT);
				return null;
			}else if(user.getStatus() == Constant.USER_STATUS_FROZEN
					&& (user.getFrozenTimeStap() > System.currentTimeMillis()
					||user.getFrozenTimeStap() <= 0)) {
				writeResult(respnose, ResultCode.ACCOUNT_FROZEN);
				return null;
			}else if(user.getStatus() == Constant.USER_STATUS_FROZEN
					&& (user.getFrozenTimeStap() <= System.currentTimeMillis())) {
				//如果用户冻结时间结束则解冻
				Update update = new Update();
				update.set("status", Constant.USER_STATUS_COMM);
				Query query = new Query();
				query.addCriteria(where("_id").is(new ObjectId(uid)));
				asyncService.updateFirst(query, update, UserBaseInfo.collectionName);
			}
			//设置请求的用户数据
			request.setAttribute("user", user);
			return user.getToken();
		} catch (Exception e) {
			writeResult(respnose, ResultCode.SYSTEM_ERROR);
			logger.error("chekLoginState异常",e);
			return null;
		}
	}

	
	/**计算签名是否正确，时间戳相差不能超过1分钟，key：是管理员加密后的密码，未写入respnose结果*/
/*	private boolean chekAdminSign(String key, HttpServletRequest request) throws Exception {
		Enumeration<String> enumReqParames = request.getParameterNames();
		Map<String, String> map = new HashMap<String, String>();
		while(enumReqParames.hasMoreElements()) {
			//循环取出全部参数和值放入MAP
			String parameName = enumReqParames.nextElement();
			if(parameName.equals("req_t") || 
					parameName.equals("adminId") || 
					parameName.equals("sign") || 
					parameName.equals("timeStamp")) {
				continue;
			}
			String value = request.getParameter(parameName);
			map.put(parameName, value);
		}
		String parameStr = SortUtils.formatUrlParam(map, "UTF-8", false);
		String timeStamp = request.getParameter("timeStamp");
		
		String sign = request.getParameter("sign");
		if (sign == null || sign.length() != 32 || parameStr == null || timeStamp == null) {
			return false;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(key).append(timeStamp).append(parameStr);
		
		//如果是开发环境，记录请求数据方便调试
		if(Constant.ENVIRONMENT_CURRENT == Constant.ENVIRONMENT_DEV){
			logger.info("签名内容："+sb.toString());
			logger.info("服务器计算sign="+GeneralUtil.getMD5(sb.toString()));
		}
		//判断传入的时间戳是否在允许范围
		long time = Long.parseLong(timeStamp);
		if (Constant.ENVIRONMENT_CURRENT == Constant.ENVIRONMENT_PRO 
				&& Math.abs(System.currentTimeMillis() - time) > 1000*60*1) {
			logger.error("签名传入时间戳误差超过1分钟");
			return false;
		} 
		return GeneralUtil.getMD5(sb.toString()).equals(sign);
	}*/
	
	/**解决ajax请求session不一致问题，ajax请求加上两个ajax属性 ：xhrFields: { withCredentials: true },crossDomain: true,*/
	private void sessionShare(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "600");//session有效时间（s）
		response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,Access-Control-Allow-Headers");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed","1");
	}
	/**写入response返回结果*/
	private void writeResult(HttpServletResponse respnose, ResultCode ms) throws IOException {
		JSONObject result = new JSONObject();
		result.put("code", ms.getCode());
		result.put("msg", ms.getMsg());
		
		logger.error("拦截器返回错误："+result.toString());
		
		respnose.getWriter().write(result.toString());
		respnose.getWriter().close();
	}
}
