/**
 */
package com.xwl.task.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xwl.task.service.LotteryMService;

/**
 * @author yang
 *
 */
public class LotteryMJob {
	private static final Logger log = LoggerFactory.getLogger(LotteryMJob.class);
	@Autowired
	private LotteryMService lotteryMService;
	public void run() throws Exception{
		log.info("LotteryMJob start.");
		this.lotteryMService.updateForLotteryM();
		log.info("LotteryMJob end.");
	}
	
}
