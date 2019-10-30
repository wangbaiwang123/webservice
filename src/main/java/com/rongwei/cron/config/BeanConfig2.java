package com.rongwei.cron.config;

import com.rongwei.cron.domain.WafDataStatusDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 配置类
 *
 * @author wangshen
 */
@Configuration
public class BeanConfig2
{
	@Bean("WafDataStatusDO")
	public WafDataStatusDO wafDataStatusDO()
	{
		return new WafDataStatusDO();
	}

	@Bean
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
}
