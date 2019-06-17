/**
 * com.sgyj.web.define.HttpConstants.java
 * ==================================================================================================
 * Copyright 2018 xxxx ，All Rights Reserved.
 * ==================================================================================================
 * 日期         				作者 						动作
 * 2018年1月5日 下午3:38:02     			huxc      		创建
 * ==================================================================================================
 * 项目名称：xyjjw-1.0
 * ==================================================================================================
 * 描述：
 *	TODO
 */
package com.xwl.base;

import com.xwl.utlis.PropertiesUtil;

/**
 * Title:HttpConstants.java
 * Description: 
 * Copyright: Copyright (c) 2018
 * 
 * @author huxc
 * @email 985815621@qq.com
 * @version 1.0 
 * TODO
 */
public class HttpConstants {
	public final static String AUTH_SESSION_USER = "SESSION_USER";
	public final static String RABBITMQ_ROOTING_KEY = "smsKey";
	public  final static String UPLOAD_PATH_IP = "http://39.108.12.86";
	public  final static String UPLOAD_PATH = "/uploads"; 
	
	String value = PropertiesUtil.getValue("index1");
}
