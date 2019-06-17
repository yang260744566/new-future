/**
 * 
 */
package com.xwl.task.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import com.xwl.task.entity.NwlSettle;
import com.xwl.task.service.DownMService;
import com.xwl.utlis.DBCPUtil;
import com.xwl.utlis.NewFurureUtils;
import com.xwl.utlis.PropertiesUtil;

/**
 * @author yang
 *
 */
@Service
public class DownMServiceImpl implements DownMService {
	public static void main(String[] args) {
		updateForDownM20();
	}

	static String getOpeninfo = PropertiesUtil.getValue("getOpeninfo");
	static String savesinfo = PropertiesUtil.getValue("savesinfo");
	static int price = Integer.parseInt(PropertiesUtil.getValue("price"));
	static Connection con = null;

	@Override
	public void updateForDownM() throws Exception {
		updateForDownM20();
	}

	public static void updateForDownM20() {
		con = DBCPUtil.getConnection();
		String sql0="select * from xwlsettle where status = 20 and money >"+price+" order by updt";
		List<NwlSettle> array0 = NewFurureUtils.getAllByStatus(con, sql0);
		int size0 = array0.size();
		if(size0>0) {
			int limit = (size0/10) * 10;
			sql0+=" limit "+limit;
			List<NwlSettle> array = NewFurureUtils.getAllByStatusLimit(con,sql0,limit);
			int size = array.size();
			if (size != 0 && size % 10 == 0) {
				int i = 1;
				String sql = "update xwlsettle set money=money-" + price + ",status=?,updt=? where status = 20 and suid=?";
				int ci = 0;
				for (NwlSettle nwlSettle : array) {
					String suid = nwlSettle.getSuid();
					try {
						String no = "";
						while(true) {
							String url = getOpeninfo + Math.random();
							String page = NewFurureUtils.sendPost(url, "", "uid="+suid);
							int a = page.indexOf("time1\":");
							int b = page.indexOf(",\"",a+7);
							if(a != -1 && b != 1) {
								String time = page.substring(a+7, b);
								if(Integer.parseInt(time)>10 || ci>0) {
									int aa = page.indexOf("issue3\":\"");
									int bb = page.indexOf("\",",aa+9);
									no = page.substring(aa+9, bb);
									break;
								}else {
									System.out.println("等待开奖..");
									Thread.sleep(5000);
								}
							}else {
								System.out.println("开奖中..");
								Thread.sleep(1000);
							}
						}
						String c = NewFurureUtils.sendPost(savesinfo,"issue2="+no+"&chooseinfo="+ci+",;,;,;,&choosenums=1&mons="+price+"&mode=16&buytype=1&random="+Math.random(), "uid="+suid);
						try {
							int a = c.indexOf("rtmsg\":\"");
							int b = c.indexOf("\"}",a+8);
							c = c.substring(a+8, b);
						} catch (Exception e) {
						}
						System.out.println("期号:"+no+"    注号:"+ci+"   i:"+i+"    结果:"+c);
						if(ci<9) {
							ci++;
						}else {
//							Thread.sleep(5000);
							ci = 0;
							System.out.println("------------------------------------------------------------------------------------------------");
						}
						PreparedStatement pst = con.prepareStatement(sql);
						pst.setInt(1, 30);
						pst.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
						pst.setString(3, suid);
						pst.executeUpdate();
						Thread.sleep(100);
					} catch (Exception e) {
						Scanner scan = new Scanner(System.in);
						System.out.println("程序异常 按任意键继续。。");
						scan.next();
						scan.close();
						e.printStackTrace();
					}
				}
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("条件不满足");
			}
		}else {
			System.out.println("满足数"+size0);
		}
		
	}

}
