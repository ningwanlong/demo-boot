package com.boot.demo.core.controller.user;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.boot.demo.annotation.Sign;
import com.boot.demo.core.controller.BaseController;
import com.boot.demo.core.redis.RedisCache;
import com.boot.demo.doc.UserBaseInfo;
import com.boot.demo.tools.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
@ResponseBody
public class UserController extends BaseController {
    @Resource
    RedisCache<Object> redisCache;


    //获取登陆验证码
    @RequestMapping("/getCode")
    @Sign(keyType = Sign.APP_KEY)
    public String getCode(HttpServletRequest request,
                          @RequestParam(required = true) String body) {
        try {
            rm = new ResultModel();

            JSONObject oBody = JSONObject.parseObject(body);
            String mobile = oBody.getString("mobile").trim();

            if(!mobile.matches("^1(3|4|5|6|7|8|9)\\d{9}$")){
                rm.add("不能识别的手机号，请检查");
                return super.toJson();
            }

            String code = "1234";
            boolean isSend = true;
            int mode = Constant.config.getEnvironment();
            if (Constant.ENVIRONMENT_PRO == mode && !mobile.equals("17888888888")) {
                code = GeneralUtil.getRandomCode(4);
                isSend = SMSUtil.sendCodeSms(mobile, code);
            }
            if (isSend) {
                String key = CacheUtil.getSMSKey(mobile);
                redisCache.putCacheWithExpireTime(key, code, 600L);
            } else {
                rm.add(ResultCode.FAIL);
            }
        }catch (JSONException | ClassCastException e) {
            logger.error("", e);
            rm.add(ResultCode.ERROR_PARAM);
            return toJson();
        }catch (Exception e) {
            logger.error("", e);
            rm.add(ResultCode.EXCEPTION);
        }
        return toJson();
    }

    //根据id获取用户信息
    @RequestMapping("/info")
    @ResponseBody
    @Sign(keyType = Sign.NO_CHECK)
    public String info(
            @RequestAttribute(required = true)JSONObject oBody
    ) {
        rm = new ResultModel();
        try {
            String uid = oBody.getString("uid");

            UserBaseInfo user = mongoTemplate.findById(uid, UserBaseInfo.class);

            System.out.println(user.getRealname());

            return toJson(user);
        } catch (Exception e) {
            logger.error("", e);
            rm.add(ResultCode.ERROR_PARAM);
        }
        return toJson();
    }

    //用户退出登陆，清空token
    @RequestMapping("/quitLogin")
    @ResponseBody
    @Sign(keyType = Sign.USER_KEY)
    public String quitLogin(
            @RequestAttribute(required = true)JSONObject oBody,
            @RequestAttribute("user")UserBaseInfo user
    ) {
        rm = new ResultModel();
        try {
            String redisKey = CacheUtil.getLoginKey(user.getId());//redis保存的key
            redisCache.delete(redisKey);
            Update update = new Update();
            update.set("token"  ,"");
            asyncService.updateFirst(
                    new Query(Criteria.where("_id").is(new ObjectId(user.getId()))),
                    update,
                    UserBaseInfo.collectionName
            );
            return toJson();
        } catch (Exception e) {
            logger.error("", e);
            rm.add(ResultCode.SERVER_BUSY);
        }
        return toJson();
    }





}
