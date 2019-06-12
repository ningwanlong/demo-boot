package com.boot.demo.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class GeneralUtil {

	/**
	 * 获取指定长度随机数
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomCode(int length) {
		Random rm = new Random();

		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, length);

		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);

		// 返回固定的长度的随机数
		return fixLenthString.substring(1, length + 1);
	}

	/**
	 * List按分隔符转化成String
	 * 
	 * @param list
	 * @param separator
	 * @return
	 */
	public static <T> String listToString(List<T> list, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).toString()).append(separator);
		}
		return sb.toString().substring(0, sb.toString().length() - separator.length());
	}

	/**
	 * 获取Unix时间戳
	 * 
	 * @return
	 */
	public static long getUnixTimeStamp() {
		return System.currentTimeMillis() / 1000L;
	}

	/**
	 * 获取32位16进制的MD5字符串
	 * 
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	/*public static String getMD5(String str) throws NoSuchAlgorithmException {
		MessageDigest md = null;
		md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update(str.getBytes());
		byte[] bytes = md.digest();
		String result = "";
		for (byte b : bytes) {
			// byte转换成16进制
			result += String.format("%02x", b);
		}
		return result.toUpperCase();
	}*/
	public static String getMD5(String str) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");  
		md5.update((str).getBytes("UTF-8"));  
		byte b[] = md5.digest();
		  
		int i;  
		StringBuffer buf = new StringBuffer("");  
		  
		for(int offset=0; offset<b.length; offset++){  
		    i = b[offset];  
		    if(i<0){  
		        i+=256;  
		    }  
		    if(i<16){  
		        buf.append("0");  
		    }  
		    buf.append(Integer.toHexString(i));  
		}  
		  
		return buf.toString().toUpperCase();
	}

	/**
	 * 创建图片名
	 * @param id
	 * @return
	 */
	public static String createPiceName(int id) {
		StringBuilder sb = new StringBuilder();
		sb.append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss sss")).append("/").append(System.currentTimeMillis()).append("_").append(id)
				.append(".jpg");
		return sb.toString();
	}
}
