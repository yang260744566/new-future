/**
 */
package com.xwl.task.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.xwl.utlis.DBCPUtil;
import com.xwl.utlis.NewFurureUtils;

/**
 * @author yang
 *
 */
public class AddMJob {
	
	static String path ="F:\\xinweilai\\xinweilai.txt";
	static Connection con = null;
	
	 public static void main(String[] args) {
		add();
	 }
	 /**
	  * 数据入库
	  */
	 public static void add() {
		 //得到txt的数据
			String data = NewFurureUtils.getData(path);
			String[] array = data.split("uid=");
			//StringBuffer buffer = new StringBuffer();
			Set<String> set = new HashSet<String>();
			System.out.println(array.length -1 );
			for (String str : array) {
				if("".equals(str))continue;
				set.add(str);
		    }
			System.out.println(set.size());
			Connection con = DBCPUtil.getConnection();
			String sql="insert into xwlsettle (suid,money,status,crdt,updt) values(?,?,?,?,?)";
			for (String suid : set) {
				if("".equals(suid))continue;
	            int a = 0;
	            
	            try {
	                PreparedStatement pst = con.prepareStatement(sql);
	                pst.setString(1, suid);
	                pst.setDouble(2, 0.0);
	                pst.setInt(3, 10);
	                pst.setTimestamp(4, new Timestamp(new java.util.Date().getTime())); 
	                pst.setTimestamp(5, new Timestamp(new java.util.Date().getTime())); 
	                a = pst.executeUpdate();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            System.out.println("插入"+suid);
			}
			
			//buffer.append(url+",");
			//NewFurureUtils.setResult(path, buffer.toString());
	 }
	 
	 
	
	 
}
