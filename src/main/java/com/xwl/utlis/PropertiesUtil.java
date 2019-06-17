/**
 * 
 */
package com.xwl.utlis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author yang
 *
 */
public class PropertiesUtil {
	
	public static String getValue(String key){
		Properties prop = new Properties();
		//通过当前线程的类加载器
	    //文件夹下的的东西都会被加载到bin下面，因为这两个文件被配置为了source
		File file = new File(Thread.currentThread().getContextClassLoader().getResource("parameters.properties").getFile());
		try {
			//装载配置文件
			prop.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//返回获取的值
		return prop.getProperty(key);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String value = getValue("index");
		System.out.println(value);
	}

}
