package com.xwl.utlis;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;


public class DBCPUtil {
	// BasicDataSource 也就是DBCP所使用数据源
    private static BasicDataSource basicDataSource;
   // public static Connection con = null;
	public static ResultSet res = null;
    /**
    * 读取配置文件，并且初始化连接池
    */
    private static void init(){
        // 根据上面所放类路径读取配置文件
        InputStream inputStream = DBCPUtil.class.getClassLoader().getResourceAsStream("datasource.properties");
        Properties properties = new Properties();
        try {
            // 加载properties文件
            properties.load(inputStream);

            // 新建一个BasicDataSource
            basicDataSource = new BasicDataSource();

            // 设置对应的参数
            basicDataSource.setUrl(properties.getProperty("jdbc.mysql.url"));
            basicDataSource.setDriverClassName(properties.getProperty("jdbc.mysql.driverClassName"));
            basicDataSource.setUsername(properties.getProperty("jdbc.mysql.username"));
            basicDataSource.setPassword(properties.getProperty("jdbc.mysql.password"));
            basicDataSource.setInitialSize(Integer.parseInt(properties.getProperty("dataSource.initialSize")));
            basicDataSource.setMaxIdle(Integer.parseInt(properties.getProperty("dataSource.maxIdle")));
            basicDataSource.setMinIdle(Integer.parseInt(properties.getProperty("dataSource.minIdle")));
            basicDataSource.setMaxWaitMillis(Integer.parseInt(properties.getProperty("dataSource.maxWait")));
            basicDataSource.setMaxTotal(Integer.parseInt(properties.getProperty("dataSource.maxActive")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得Connection
     * @return Connection
     */
     public synchronized static Connection getConnection(){
            if (basicDataSource == null){
                init();
            }
            try {
                return basicDataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
      }
     // 查询
     public static ResultSet  Search(Connection con,String sql, String str[]) {
         try {
             PreparedStatement pst =con.prepareStatement(sql);
             if (str != null) {
                 for (int i = 0; i < str.length; i++) {
                     pst.setString(i + 1, str[i]);
                 }
             }
             res = pst.executeQuery();
             
         } catch (Exception e) {
             e.printStackTrace();
         }
         return res;
     }

     // 增删修改
     public static int AddU(Connection con,String sql, String str[]) {
         int a = 0;
         try {
             PreparedStatement pst = con.prepareStatement(sql);
             if (str != null) {
                 for (int i = 0; i < str.length; i++) {
                     pst.setString(i + 1, str[i]);
                 }
             }
             a = pst.executeUpdate();
         } catch (Exception e) {
             e.printStackTrace();
         }
         return a;
     }
}
