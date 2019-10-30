package com.rongwei.cron.config;

import com.rongwei.cron.service.CronService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author wangshen
 */
@Component
@Configurable
@EnableScheduling
public class CronConfig
{
	private static final Logger logger = LoggerFactory.getLogger(CronConfig.class);

	@Autowired
	private CronService cronService;

	/**
	 * 每5秒执行一次:0/5 * * * * ?   2分钟：00 0/2 * * * ?
	 */
//	@Scheduled(cron = "0/5 * * * * ?")
	public void reportCurrentByCron()
	{
		logger.info("定时任务开始.....");
		cronService.run();
		logger.info("定时任务结束.....");
	}
}
