/**
 * 
 */
package com.xwl.task.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xwl.task.entity.NwlSettle;
import com.xwl.task.service.LotteryMService;
import com.xwl.utlis.DBCPUtil;
import com.xwl.utlis.NewFurureUtils;
import com.xwl.utlis.PropertiesUtil;

/**
 * @author yang
 *
 */
@Service
public class LotteryMServiceImpl implements LotteryMService {
	private static final Logger log = LoggerFactory.getLogger(LotteryMServiceImpl.class);
	static Connection con;
	static int num = 0;
	// 同时并发执行的线程数
	public static int threadTotal = 5;
	static String sql = "update xwlsettle set money=money+1,status=?,updt=? where status = 30 and suid=?";
	public static AtomicInteger count = new AtomicInteger(0);

	public static void main(String[] args) {
		updateForLotteryM10();
	}
	@Override
	public void updateForLotteryM() throws Exception {
		updateForLotteryM10();
	}
	public static void updateForLotteryM10() {
		//String name = NewFurureUtils.hqym();
		con = DBCPUtil.getConnection();
		ExecutorService executorService = Executors.newCachedThreadPool();// 获取线程池
		String sql0="select * from xwlsettle where status =30";
		List<NwlSettle> array = NewFurureUtils.getAllByStatus(con, sql0);
		final Semaphore semaphore = new Semaphore(threadTotal);// 定义信号量
		final CountDownLatch countDownLatch = new CountDownLatch(array.size());
		int size = array.size();
		for (NwlSettle nwlSettle : array) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String suid = nwlSettle.getSuid();
			executorService.execute(() -> {
				double money = 0;
				try {
					String urr = PropertiesUtil.getValue("indexdd88fomday")+Math.random();
					// 抽奖页面
					String q = NewFurureUtils.sendGet(urr,"", "uid="+suid+";nickname="+UUID.randomUUID());
					int a = q.indexOf("s=\"num\">");
					int b = q.indexOf("</sp", a + 8);
					//Thread.sleep(7000);
					//  抽奖
					if (a == -1 || b == -1) {
						q = NewFurureUtils.sendGet(PropertiesUtil.getValue("getPackday")+Math.random(), "","uid="+suid+";nickname=\""+UUID.randomUUID()+"\"");
						a = q.indexOf("s=\"num\">");
						b = q.indexOf("</sp", a + 8);
					}
					try {
						money = Double.parseDouble(q.substring(a + 8, b));
						if(money<0.6 && money>0.4) {
							Thread.sleep(7000);
						}
					} catch (Exception e) {
						
					}
					System.out.println(nwlSettle.getId()+"/"+size+"    "+money+"     " + num);
				} catch (Exception e) {
				}
				try {
					semaphore.acquire();
					update(money, suid);
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executorService.shutdown();
		log.info("count:{}", count.get());
		
		
	}
	public static synchronized void update(double money, String suid) {
		if(money >=1) {
			try {
				PreparedStatement pst;
				pst = con.prepareStatement(sql);
				pst.setInt(1, 10);
				pst.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
				pst.setString(3, suid);
				pst.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
