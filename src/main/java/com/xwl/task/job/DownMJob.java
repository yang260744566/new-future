/**
 */
package com.xwl.task.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xwl.task.service.DownMService;

/**
 * @author yang
 *
 */
public class DownMJob {
	
	private static final Logger log = LoggerFactory.getLogger(DownMJob.class);
	@Autowired
	private DownMService downMService; 
	public void run() throws Exception{
		log.info("DownMJob start.");
		this.downMService.updateForDownM();
		log.info("DownMJob end.");
	}

}
