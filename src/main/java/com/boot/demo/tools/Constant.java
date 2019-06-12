package com.boot.demo.tools;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.boot.demo.doc.ConfigDocument;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;

public class Constant {
	
	/**数据库配置doc内容*/
	public static ConfigDocument config = new ConfigDocument();
	
	public static String SIGN_APP_KEY = "";
	
	public static String THIRD_PARTY_PREFIX = "";	//第三方注册ID前缀
	public static String JPUSH_PREFIX = "";	//极光推送注册前缀
	
	public final static String THIRD_PARTY_PREFIX_USER = "user-";	//第三方注册ID中间名-用户
	public final static String THIRD_PARTY_PREFIX_VISITOR = "visitor-";	//第三方注册ID中间名-游客
	/**服务器存放文件文件夹路径，删除时候使用*/
	public final static String PC_SAVEPATH = "E:/static";
	/*部署环境 */
	/**开发环境*/
	public final static int ENVIRONMENT_DEV = 0;  //开发环境
	/**生产环境*/
	public final static int ENVIRONMENT_PRO = 1;  //生产环境
	public static int ENVIRONMENT_CURRENT = 0;//当前环境
	
	/**用户共享时的当前APPID*/
	public static final int USER_SHARE_APPID = 3;
	
	/**后台管理的app签名key*/
	public static String SIGN_MANAGE_KEY = "community#S1D$F5S*H";
	
	/* redis Key值前缀 */
	public final static String CACHE_PREFIX_SMS = "sms:community";
	
	public final static String CACHE_PREFIX_REGISTER = "register:community";
	
	public final static String CACHE_PREFIX_UPDATE_MOBILE = "update:community";
	
	public final static String CACHE_PREFIX_LOGIN = "login:community";
	
	public final static String CACHE_PREFIX_RED_PACKET = "redpacket:community";
	
	public final static String CACHE_USER_ID_HASHCODE = "redisuserid:community";

	/*用户状态*/
	public final static int USER_STATUS_NO_ACTIVE = 1;  //未激活
	public final static int USER_STATUS_COMM = 2;  //正常
	public final static int USER_STATUS_FROZEN = 3;  //冻结
	public final static int USER_STATUS_NOLINE = 4;  //离线
	
	//用户隐私设置,被呼叫的隐私设置：0-允许任何人拨打、1-仅允许通讯录好友拨打、2-不允许任何人拨打
	/**允许任何人拨打*/
	@Transient
	public final static int CALL_SECRET_ALL = 0;
	/**仅允许通讯录好友拨打*/
	@Transient
	public final static int CALL_SECRET_FRIEND = 1;
	/**不允许任何人拨打*/
	@Transient
	public final static int CALL_SECRET_NEVER = 2;
	
	/*首次登陆欢迎信息*/
	public final static String MSG_WELCOME = "";
	
	/**默认的返回的点赞头像的数量6*/
	public final static int LIKE_MSG_NUM = 6;
	/**默认展示的动态评论数量2*/
	public final static int COMM_MSG_NUM = 2;
	
	/*返回列表条数限制 */
	public final static int LIST_LIMIT_4 = 4;  //4条
	public final static int LIST_LIMIT_5 = 5;  //5条
	public final static int LIST_LIMIT_10 = 10;  //10条
	public final static int LIST_LIMIT_12 = 12;  //12条
	public final static int LIST_LIMIT_20 = 20;  //20条
	public final static int LIST_LIMIT_35 = 35;  //35条
	public final static int LIST_LIMIT_50 = 50;  //50条
	public final static int LIST_LIMIT_100 = 100;  //100条
	public final static int LIST_LIMIT_1000 = 1000;  //1000条
	
	/*操作日志status值 */
	public final static int LOG_STATUS_SUCCESS = 0;  //成功
	public final static int LOG_STATUS_ERROR = 1;  //失败

	/**很大的一个时间戳，拿来比较比他小的全部时间 9500053977312L*/
	public final static long BigTimestamp = 9999999999999L ;	
	/**地球半径，精确到米*/
	public final static double EarthRadius = 6378100.0;

	/*通用状态*/
	public final static int COMM_STATUS_NORMAL = 0;  //正常
	public final static int COMM_STATUS_CLOSE = 1;  //关闭
	
	/*通用审核状态*/
	public final static int COMM_AUDIT_STATUS_ING = 1;  	//审核中
	public final static int COMM_AUDIT_STATUS_SUCCESS = 2;  //审核通过
	public final static int COMM_AUDIT_STATUS_REFUSE = 3;   //审核拒绝
	public final static int COMM_AUDIT_STATUS_DELETE = 4;   //删除

	//通用角色
	/**通用角色：1-创建者*/
	@Transient
	public final static int ROLE_CREATOR = 1;
	/**通用角色：2-管理员*/
	@Transient
	public final static int ROLE_MANAGE = 2;
	/**通用角色：3-普通成员*/
	@Transient
	public final static int ROLE_MEMBER = 3;
	
	
	/*广告分类*/
	public final static int AD_TYPE_APP_START = 1;  //APP启动页广告
	public final static int AD_TYPE_APP_HOME_BANNER = 2;  //APP首页轮播广告
	public final static int AD_TYPE_LIVE_ROLL_TEXT = 3;  //直播跑马灯文字广播
	public final static int AD_TYPE_TVAPP_START = 4;  //APP启动页广告
	
	/*文件类型，0：无文件、1：图片、2：视频*/
	public final static int FILE_TYPE_NO = 0;
	public final static int FILE_TYPE_IMG = 1;
	public final static int FILE_TYPE_VIDEO = 2;
	
	
	/**一个过时的objectId,不能比较大小*/
	public final static String OBSOLETE_OBJECTID = "598d9d841fb5a10c6ceb2ac9";
	/**一个很大的objectId,弃用，造成查询时不是最大的查询数据丢失。*/
	@Deprecated
	public final static String MAX_OBJECTID = new ObjectId(new Date(System.currentTimeMillis()*100000)).toString();
	
	/**某ip每指定时间内可以获取验证码次数限,数据库获取*/
	public static int IP_GET_CODE_MAXNUM = 9;  
	/**某ip每次请求上限指定时间周期,单位：s*/
	public static long IP_GET_CODE_MAXTIME = 10*60;  
	
	/* 推送设备类型 */
	public final static int PUSH_DEVTYPE_IOS = 1; // ios
	public final static int PUSH_DEVTYPE_ANDROID = 2; // android
	
	public final static Map<String, String> MYFILE_PREFIX_MAP = new TreeMap<>();
	static {
		MYFILE_PREFIX_MAP.put("COMM_JS","http://file.aroundme.tv/webmodel");
	}
	
}
