package com.boot.demo.core.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boot.demo.annotation.Sign;
import com.boot.demo.core.controller.BaseController;
import com.boot.demo.doc.AdminBaseInfo;
import com.boot.demo.doc.AdminRole;
import com.boot.demo.global.PathConstant;
import com.boot.demo.tools.Constant;
import com.boot.demo.tools.GeneralUtil;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

/**
 * 管理员登陆相关
 */
@Controller
public class LoginController extends BaseController {
    /**管理员登陆保存用户名密码的cookie的key值*/
    private static final String ADMIN_LOGIN_COOKIE_KEY = "ADMIN_LOGIN_COOKIE_KEY";

    /**登陆成功跳到主页*/
    @RequestMapping("/home")
    public String home(
            HttpServletRequest request
    ){
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("admin") == null){

            return  PathConstant.REDIRECT + PathConstant.LOGIN + "?errorMsg=1";
        }
        logger.info("登陆信息："+session.getAttribute("admin").toString());
        logger.info ("权限信息："+session.getAttribute("role").toString());
        return PathConstant.HOME;
    }

    /**登陆或退出登陆*/
    @RequestMapping("/login")
    public String longPage(
            HttpServletRequest request,
            @RequestParam(defaultValue = "") String errorMsg,   //提示信息
            @RequestParam(defaultValue = "false") boolean quitLogin   //是否包含退出登陆到登陆页
    ){
        try {
            if("1".equals(errorMsg)){
                errorMsg = "登录过期";
            }
            if (quitLogin) {
                errorMsg = "退出登录成功";
                HttpSession session = request.getSession();
                if (session != null) {
                    session.removeAttribute(AdminBaseInfo.ADMIN_LOGIN_SESSION_KEY);
                }
            }
            Cookie[] cookies = request.getCookies();
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(ADMIN_LOGIN_COOKIE_KEY)) {
                    try {
                        JSONObject loginJsonObj = JSONObject.parseObject(cookie.getValue());
                        request.setAttribute("loginName", loginJsonObj.getString("loginName"));
                        request.setAttribute("password", loginJsonObj.getString("password"));
                        request.setAttribute("remember", true);
                    } catch (Exception e) {
                        logger.error("登陆cookie保存json格式不正确", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        request.setAttribute("errorMsg", errorMsg);
        return PathConstant.LOGIN;
    }

    /**验证密码*/
    @RequestMapping("/loginAuth")
    public String loginAuth(
            HttpServletResponse response,
            HttpServletRequest request,
            @RequestParam(required = true, value = "username") String loginName,   //用户名
            @RequestParam(required = true, value = "password") String password,   //密码
            @RequestParam(defaultValue = "false") boolean remember   //是否包记住密码
    ){
        try {
            //保存cookie
            try {
                if (remember) {
                    JSONObject loginJsonObj = new JSONObject();
                    loginJsonObj.put("loginName", loginName);
                    loginJsonObj.put("password", password);

                    Cookie[] cookies = request.getCookies();
                    Cookie cookie = new Cookie(ADMIN_LOGIN_COOKIE_KEY, loginJsonObj.toJSONString());
                    response.addCookie(cookie);
                } else {
                    Cookie[] cookies = request.getCookies();
                    for (int i = 0; i < cookies.length; i++) {
                        Cookie cookie = cookies[i];
                        if (cookie.getName().equals(ADMIN_LOGIN_COOKIE_KEY)) {
                            cookie.setMaxAge(0);
                        }
                    }
                }
                request.setAttribute("loginName",loginName);
                request.setAttribute("password",password);
                request.setAttribute("remember",remember);
            } catch (Exception e) {
                logger.error("ccookie操作失败", e);
            }
            //验证密码

            password = GeneralUtil.getMD5(loginName + AdminBaseInfo.SALT_PASSWORD + password);
            Criteria criteria = new Criteria();
            criteria.and("loginName").is(loginName);
            AdminBaseInfo admin = mongoTemplate.findOne(new Query(criteria), AdminBaseInfo.class);
            if(admin == null){
                request.setAttribute("errorMsg", "用户名或密码错误！");
            }else if(!admin.getPassword().equals(password)){
                request.setAttribute("errorMsg", "用户名或密码错误！");
            }else if(admin.getStatus() != Constant.COMM_STATUS_NORMAL){
                request.setAttribute("errorMsg", "用户已经被删除！");
            }else if(admin.isLock()){
                request.setAttribute("errorMsg", "用户被锁定，暂时不能登陆！");
            }else{
                admin.setPassword("");
                HttpSession session = request.getSession(true);
                session.setAttribute("admin", admin);
                AdminRole role = mongoTemplate.findById(admin.getRoleId(), AdminRole.class);
                session.setAttribute("role", role);
                return PathConstant.REDIRECT + PathConstant.HOME;
            }
        } catch (Exception e) {
           logger.error("", e);
            request.setAttribute("errorMsg", "服务器错误！");
        }
        return  PathConstant.LOGIN;
    }


}