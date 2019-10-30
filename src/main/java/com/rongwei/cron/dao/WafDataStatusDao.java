package com.rongwei.cron.dao;

import com.rongwei.cron.domain.WafDataStatusDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 数据状态，是否已经同步了
 *
 * @author wangshen
 * @email wangshen@irongwei.com
 * @date 2019-02-19 17:40:31
 */
@Mapper
public interface WafDataStatusDao
{

	WafDataStatusDO get(Integer id);

	List<WafDataStatusDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(WafDataStatusDO wafDataStatus);

	int update(WafDataStatusDO wafDataStatus);

	int batchRemove(Integer[] ids);
}
