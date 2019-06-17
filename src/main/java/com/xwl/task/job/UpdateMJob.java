/**
 */
package com.xwl.task.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xwl.task.service.UpdateMService;

/**
 * @author yang
 *
 */
public class UpdateMJob {
	private static final Logger log = LoggerFactory.getLogger(UpdateMJob.class);
	@Autowired
	private UpdateMService updateMService;

	public void run10() throws Exception {
		log.info("UpdateMJob run10 start.");
		this.updateMService.updateForSelectM10(10);
		log.info("UpdateMJob run10 end.");
	}
	
	public void run30() throws Exception {
		  log.info("UpdateMJob run30 start.");
		  this.updateMService.updateForSelectM30(30);
		  log.info("UpdateMJob run30 end.");
	}
	 

}
