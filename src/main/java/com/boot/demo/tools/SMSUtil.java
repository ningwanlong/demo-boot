package com.boot.demo.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMSUtil {
	private static Logger logger = LoggerFactory.getLogger("SMSUtil");
	private static final String APP_SIGN = "【联合应急宝】";//APP短信平台签名（短信前缀）
	/**
	 * <p>Title: 语音验证码</p>  
	 * <p>Description: </p>  
	 * @param mobile
	 * @param code
	 * @return
	 */
	public static boolean sendVoiceSms(String mobile, String code) {
		boolean sendStatus = false;
		if (!isMobile(mobile)) {
			return false;
		}
		logger.info("发送语音验证码："+mobile+":"+code);
		String path = "https://zapi.253.com/msg/HttpBatchSendSM";
		String account = "V6230026";// 账号
		String pswd = "MIiU74Dsg8e7e6";// 密码
		String msg = "您好，您的验证码是"+code;// 短信内容
		boolean needstatus = false;// 是否需要状态报告，需要true，不需要false
		String extno = "555";// 扩展码

		try {
			String returnString = batchSend(path, account, pswd, mobile, msg, needstatus, extno);
			System.out.println(returnString);
			
			if(returnString.split("\n")[0].split(",")[1].equals("0")) {
				sendStatus = true;
			}
		} catch (Exception e) {
			logger.error("发送语音验证码异常:"+e);
		}
		return sendStatus;
	}
	/**
	 * @param url 应用地址，类似于http://ip:port/msg/
	 * @param account 账号
	 * @param pswd 密码
	 * @param mobile 手机号码，多个号码使用","分割
	 * @param msg 短信内容
	 * @param needstatus 是否需要状态报告，需要true，不需要false
	 * @return 返回值定义参见HTTP协议文档
	 * @throws Exception
	 */
	public static String batchSend(String url, String account, String pswd, String mobile, String msg,
			boolean needstatus, String extno) throws Exception {
		HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
		GetMethod method = new GetMethod();
		try {
			URI base = new URI(url, false);
			method.setURI(new URI(base, "HttpBatchSendSM", false));
			method.setQueryString(new NameValuePair[] { 
					new NameValuePair("account", account),
					new NameValuePair("pswd", pswd), 
					new NameValuePair("mobile", mobile),
					new NameValuePair("needstatus", String.valueOf(needstatus)), 
					new NameValuePair("msg", msg),
					new NameValuePair("extno", extno), 
			});
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				return URLDecoder.decode(baos.toString(), "UTF-8");
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * 发送验证码短信
	 * @param mobile
	 * @param code
	 * @return
	 * 登陆网址：zz.253.com 账号：18985411371 密码：abc18985411371
	 */
	public static boolean sendCodeSms(String mobile, String code) {
		boolean sendStatus = false;
		try {
			if (!isMobile(mobile)) {
				return false;
			}
			logger.info("发送短信验证码："+mobile+":"+code);
			String path = "http://smssh1.253.com/msg/send/json";
			String account = "N5532746";
			String password = "l7hxSGfpiZ8de8";
			JSONObject postContentJSON = new JSONObject();
			postContentJSON.put("account", account);
			postContentJSON.put("password", password);
			postContentJSON.put("msg", APP_SIGN+"您的验证码是 :"+code);
			postContentJSON.put("phone", mobile);
			postContentJSON.put("report", "true");
			postContentJSON.put("sendtime", DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmm"));
			postContentJSON.put("extend", "555");
			postContentJSON.put("uid", mobile);
			
			String result = sendSmsByPost(path, postContentJSON.toString());
			logger.info(result);

			JSONObject resultJSON = JSONObject.parseObject(result);
			if (resultJSON.getString("code").equals("0")) {
				sendStatus = true;
			} 
		} catch (Exception e) {
			logger.error("发送短信验证码异常:"+e);
		}
		return sendStatus;
	
	}
	
	/**
	 * <p>Title: 给多个手机号发送模版短信，手机号用英文";"隔开</p>  
	 * <p>Description: 单参数，即同样的短信内容,变量+内容</p>  
	 * @param mobiles 手机号,每个必须用英文的“;”结尾
	 * @param modle 模版参数值（此处也是）
	 * @param sMsContent 模版固定内容，必须包含{$var}内容
	 * @return
	 */
	public static boolean sendModeSms(String mobiles, String modle, String sMsContent) {
		boolean sendStatus = false;
		try {
			logger.info(mobiles + "发送短信:\n" + modle + sMsContent);
			String path = "http://smssh1.253.com/msg/variable/json";
			String account = "N5532746";
			String password = "l7hxSGfpiZ8de8";
			
			String params = mobiles.replaceAll(";", ","+modle+";");
			
			JSONObject postContentJSON = new JSONObject();
			postContentJSON.put("account", account);
			postContentJSON.put("password", password);
			postContentJSON.put("msg", APP_SIGN+sMsContent);
			postContentJSON.put("params", params);
			postContentJSON.put("report", "true");
			postContentJSON.put("sendtime",DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmm"));
			postContentJSON.put("extend", "555");
			postContentJSON.put("uid", mobiles);
			
			String result = sendSmsByPost(path, postContentJSON.toString());
			logger.info(result);

			JSONObject resultJSON = JSONObject.parseObject(result);
			if (resultJSON.getString("code").equals("0")) {
				sendStatus = true;
			} 
		} catch (Exception e) {
			logger.error("发送广告短信异常:"+e);
		}
		return sendStatus;
	
	}

	/**
	 * 发送广告短信
	 * 
	 * @param mobile
	 * @param content
	 * @return
	 */
	public static boolean sendAdSms(String mobile, String content) {
		boolean sendStatus = false;
		try {
			if (!isMobile(mobile)) {
				return false;
			}
			String path = "http://smssh1.253.com/msg/send/json";
			String account = "M5442772";
			String password = "tC8dJ6nPhzecb1";
			//postContent模版{"account":"M0013352","msg":"【253云通讯】你好,你的验证码是123456","password":"0Q9t1PjNou0b8e","phone":"15186400260","report":"true"}
			JSONObject postContentJSON = new JSONObject();
			postContentJSON.put("account", account);
			postContentJSON.put("password", password);
			postContentJSON.put("msg", APP_SIGN+content);
			postContentJSON.put("phone", mobile);
			postContentJSON.put("report", "true");

			//System.out.println(postContentJSON.toString());
			//result模版{"time":"20180419142321","msgId":"18041914232139885","errorMsg":"","code":"0"}
			String result = sendSmsByPost(path, postContentJSON.toString());
			logger.info(result);

			JSONObject resultJSON = JSONObject.parseObject(result);
			if (resultJSON.getString("code").equals("0")) {
				sendStatus = true;
			} 
		} catch (Exception e) {
			logger.error("发送广告短信异常:"+e);
		}
		return sendStatus;
	
	}
	/**
	 * @author nwl
	 * @Description 
	 * @param path :api接口地址
	 * postContent如：{"account":"M0013352","msg":"【253云通讯】你好,你的验证码是123456","password":"0Q9t1PjNou0b8e","phone":"15186400260","report":"true"}
	 * @param postContent 参数json内容
	 * @return String 返回发送返回结果
	 */
	private static String sendSmsByPost(String path, String postContent) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
			httpURLConnection.setReadTimeout(10000);//读取超时 单位毫秒
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");

			//			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			//			printWriter.write(postContent);
			//			printWriter.flush();

			httpURLConnection.connect();
			OutputStream os=httpURLConnection.getOutputStream();
			os.write(postContent.getBytes("UTF-8"));
			os.flush();

			StringBuilder sb = new StringBuilder();
			int httpRspCode = httpURLConnection.getResponseCode();
			if (httpRspCode == HttpURLConnection.HTTP_OK) {
				// 开始获取数据
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				return sb.toString();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private static boolean isMobile(String mobile) {
		if(mobile.length() == 11){
			return true;
		}
		return false;
		//return mobile.matches("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$");
	}
}
