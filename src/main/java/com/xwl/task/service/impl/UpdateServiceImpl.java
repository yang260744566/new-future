/**
 * 
 */
package com.xwl.task.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xwl.task.entity.NwlSettle;
import com.xwl.task.service.UpdateMService;
import com.xwl.utlis.DBCPUtil;
import com.xwl.utlis.NewFurureUtils;
import com.xwl.utlis.PropertiesUtil;

/**
 * @author yang
 *
 */
@Service
public class UpdateServiceImpl implements UpdateMService {
	private static final Logger log = LoggerFactory.getLogger(UpdateServiceImpl.class);
	static int price = Integer.parseInt(PropertiesUtil.getValue("price"));
	static double Allmoney = 0;
	static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	@Override
	public void updateForSelectM10(int status) throws Exception {
		updateByStatus(status);
	}

	@Override
	public void updateForSelectM30(int status) throws Exception {
		updateByStatus(status);
	}

	static Connection con;
	// 同时并发执行的线程数
	public static int threadTotal = 10;

	public static AtomicInteger count = new AtomicInteger(0);

	public static void main(String[] args) {
		updateByStatus(30);
	}

	/**
	 * 并修改数据库
	 */
	public static void updateByStatus(int status) {
		String name = NewFurureUtils.hqym();
		con = DBCPUtil.getConnection();
		ExecutorService executorService = Executors.newCachedThreadPool();// 获取线程池
		String sql0 = "select * from xwlsettle where status =" + status;
		List<NwlSettle> array = NewFurureUtils.getAllByStatus(con, sql0);
		final Semaphore semaphore = new Semaphore(threadTotal);// 定义信号量
		final CountDownLatch countDownLatch = new CountDownLatch(array.size());
		for (NwlSettle nwlSettle : array) {
			String suid = nwlSettle.getSuid();
			Integer place = nwlSettle.getPlace();
			executorService.execute(() -> {
				double money = 0;
				try {
					String q = NewFurureUtils.sendPost(name + "/getUserInfo?random=" + Math.random(), "",
							"uid=" + suid);
					int a = q.indexOf("monsid\":\"");
					int b = q.indexOf("\",", a + 9);
					money = Double.parseDouble(q.substring(a + 9, b));
					Allmoney = Allmoney + money;
					System.out.println(nwlSettle.getId()+ "/" + array.size() + "\t金额：" + money + "\t总金额：" + Allmoney +"位置： "+place);
					if(money>80) {
						System.err.println(df.format(new Date())+"\t金额：" + money + "\t总金额：" + Allmoney +"\t位置： "+place +"\tsuid:"+suid);
					}
				} catch (Exception e) {
				}
				try {
					semaphore.acquire();
					update(status, money, suid, place);
					semaphore.release();
				} catch (Exception e) {
					log.error("exception", e);
				}
				countDownLatch.countDown();
			});
		}
		try {
			countDownLatch.await();
			try {
				con.close();
				Allmoney = 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executorService.shutdown();
		log.info("count:{}", count.get());

	}

	private static synchronized void update(int status, double money, String suid, int place) {
		String sql = "update xwlsettle set money=?,status=?,updt=? where status = 10 and suid=?";
		String sql2 = "insert into xwlsettle_temp (suid,money,status,place,crdt) values (?,?,?,?,?)";
		try {
			if (status == 10) {
				PreparedStatement pst = con.prepareStatement(sql);
				pst.setDouble(1, money);
				pst.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
				pst.setString(4, suid);
				if (money >= price & money < 23) {
					pst.setInt(2, 20);
				} else if (money > 80) {
					pst.setInt(2, 30);
				} else {
					pst.setInt(2, 10);
				}
				pst.executeUpdate();
			} else if (status == 30) {
				if (money > 80) {
					PreparedStatement pst2 = con.prepareStatement(sql2);
					pst2.setString(1, suid);
					pst2.setDouble(2, money);
					pst2.setInt(3, 20);
					pst2.setInt(4, place);
					pst2.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
					int executeUpdate = pst2.executeUpdate();
				System.err.println("插入临时表"+executeUpdate+"\tsuid:"+suid);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getLocalizedMessage()); 
		}
	}

}
