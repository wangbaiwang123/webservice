package com.rongwei.zj4a.config;

import com.rongwei.zj4a.domain.WafLogsDO;
import com.rongwei.zj4a.vo.MessageResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author Administrator
 */
@Configuration
public class BeanConfig
{
	@Bean("MessageResult")
	public MessageResult getMessageResult()
	{
		return new MessageResult();
	}

	@Bean("WafLogsDO")
	@Scope(value = "prototype")
	public WafLogsDO getWafLogsDO()
	{
		return new WafLogsDO();
	}
}
