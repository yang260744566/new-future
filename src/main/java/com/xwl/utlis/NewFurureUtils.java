package com.xwl.utlis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xwl.task.entity.NwlSettle;

public class NewFurureUtils {
	private static final Logger log = LoggerFactory.getLogger(NewFurureUtils.class);
	public static String data; // 链接数据 由程序获取
	public final static String dataPath = "E:\\action\\data.txt"; // 链接文件地址
	private static BufferedReader br;
	private static BufferedWriter out;
	
	/**
	 * @author 黄艳鹏
	 * @date 2019-03-09
	 * @param data
	 */
	public static String getData(String path) {
		StringBuffer sb = new StringBuffer();
		try {
			FileReader reader = new FileReader(path); 
			br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(sb == null || sb.length()<=0) {
			dd("链接获取异常  按回车键继续...");
		}
		return sb.toString();
	}

	/**
	 * @author 黄艳鹏
	 * @date 2019-03-09
	 * @param data
	 * @param param
	 * @param cookie
	 */
	public static String sendPost(String url,String param,String cookie) throws Exception {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
			conn.setRequestProperty("Cookie", cookie);
            conn.connect();
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
            throw e;
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
	
	/**
	 * @author 黄艳鹏
	 * @date 2019-03-09
	 * @param data
	 * @param param
	 * @param cookie
	 */
	public static String sendGet(String url, String param,String cookie) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Cookie", cookie);
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

	/**
	 * @author 黄艳鹏
	 * @date 2019-03-09
	 * @param ip
	 * @param post
	 */
	public static void dl(String ip,String post) {
		System.setProperty("http.proxyHost", ip);
		System.setProperty("http.proxyPort", post);
		System.setProperty("https.proxyHost", ip);
		System.setProperty("https.proxyPort", post);
	}

	/**
	 * @author 黄艳鹏
	 * @date 2019-03-10
	 * @param url
	 */
	public static String sendPostGetSession(String url) throws Exception {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String session = "";
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = null;
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0");
			conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
			conn.connect();
			Map<String, List<String>> map = conn.getHeaderFields();
			List<String> list = map.get("Set-Cookie");
			int a = list.get(1).indexOf("JSESSIONID=");
			int b = list.get(1).indexOf(";",a+11);
			session = list.get(1).subSequence(a+11, b).toString();
		} catch (Exception e) {
			System.out.println("获得session 请求出现异常！"+e);
		}
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return session;
	}
	
	/**
	 * 将指定文本内容放到指定路径内
	 * @author hyp
	 */
	public static void setResult(String path,String data) {
		try {
			File writeName = new File(path);
			FileWriter writer = new FileWriter(writeName);
			out = new BufferedWriter(writer);
			out.write(data);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * @author 黄艳鹏
	 * @date 2019-03-09
	 * @param data
	 */
	public static void getData() {
		StringBuffer sb = new StringBuffer();
		try {
			FileReader reader = new FileReader(dataPath); 
			br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(sb == null || sb.length()<=0) {
			dd("链接获取异常  按回车键继续...");
		}
		data = sb.toString();
	}

	/**
	 * 将指定文本内容放到指定路径内
	 * @author hyp
	 */
	public static void setResult(String path,String data,boolean isClean) {
		try {
			File writeName = new File(path);
			FileWriter writer = new FileWriter(writeName,isClean);
			out = new BufferedWriter(writer);
			out.write(data);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void distinct(String path1,String path2) {
		String data = NewFurureUtils.getData(path1);
		String[] array = data.split(",");
		System.out.println(array.length);
		Set<String> set = new HashSet<String>();
		for (String str : array) {
			set.add(str);
	    }
		System.out.println(set.size());
		StringBuffer buffer = new StringBuffer();
		for (String url : set) {
			if("".equals(url))continue;
			buffer.append(url+",");
		}
		setResult(path2, buffer.toString());
		
	}
	
	public static void getUids(String path1,String path2) {
		String data = NewFurureUtils.getData(path1);
		String[] array = data.split("http://");
		StringBuffer buffer = new StringBuffer();
		for (String url : array) {
			if("".equals(url))continue;
			int a = url.indexOf("uid=");
			int b = url.indexOf("&", a + 4);
			String uid = url.substring(a+4, b);
			buffer.append(uid+",");
		}
		setResult(path2, buffer.toString());
	}
	public static String hqym() {
		System.out.println("开始获取域名");
		String url = PropertiesUtil.getValue("hqymUrl");
		try {
			String path = NewFurureUtils.sendGet(url, "", "");
			int a = path.indexOf("f='h");
			if (a != -1) {
				int b = path.indexOf("'", a + 3);
				return path.substring(a + 3, b - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	 //根据status 查找数据
    public static List<NwlSettle> getAllByStatus(Connection con,String sql){
    	log.info(sql);
        List<NwlSettle> list=new ArrayList<NwlSettle>();
        try {
            ResultSet rs= DBCPUtil.Search(con,sql, null);
            while (rs.next()) {
                int id=rs.getInt("id");
                String suid=rs.getString("suid");
                double money = rs.getDouble("money");
                int status=rs.getInt("status");
                int place=rs.getInt("place");
                Date crdt = rs.getDate("crdt");
                Date updt = rs.getDate("updt");
                list.add(new NwlSettle( id, suid, money, status,place, crdt, updt));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
  //根据status 查找数据
    public static List<NwlSettle> getAllByStatusLimit(Connection con,String sql,int limit){
    	log.info(sql+"============"+limit);
        List<NwlSettle> list=new ArrayList<NwlSettle>();
        try {
            ResultSet rs= DBCPUtil.Search(con,sql, null);
            while (rs.next()) {
                int id=rs.getInt("id");
                String suid=rs.getString("suid");
                double money = rs.getDouble("money");
                int status=rs.getInt("status");
                int place=rs.getInt("place");
                Date crdt = rs.getDate("crdt");
                Date updt = rs.getDate("updt");
                list.add(new NwlSettle( id, suid, money, status,place, crdt, updt));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
	public static void dd(String content) {
		System.out.println(content);
		try {
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		System.out.println(	hqym());
	}
}
