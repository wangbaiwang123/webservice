package com.rongwei.cron.dao;

import com.rongwei.cron.domain.WafInterfaceAddressDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 字典表
 *
 * @author wangshen
 * @email wangshen@irongwei.com
 * @date 2019-02-20 11:56:55
 */
@Mapper
public interface WafInterfaceAddressDao
{

	WafInterfaceAddressDO get(String dicttypeid);

	List<WafInterfaceAddressDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(WafInterfaceAddressDO wafInterfaceAddress);

	int update(WafInterfaceAddressDO wafInterfaceAddress);

	int batchRemove(String[] dicttypeids);
}
