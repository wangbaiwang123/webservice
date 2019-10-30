package com.rongwei.cron.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.rongwei.cron.dao.WafDataStatusDao;
import com.rongwei.cron.domain.WafDataStatusDO;
import com.rongwei.cron.service.WafDataStatusService;


/**
 * @author Administrator
 */
@Service
public class WafDataStatusServiceImpl implements WafDataStatusService
{
	@Autowired
	private WafDataStatusDao wafDataStatusDao;

	@Override
	public WafDataStatusDO get(Integer id)
	{
		return wafDataStatusDao.get(id);
	}

	@Override
	public List<WafDataStatusDO> list(Map<String, Object> map)
	{
		return wafDataStatusDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map)
	{
		return wafDataStatusDao.count(map);
	}

	@Override
	public int save(WafDataStatusDO wafDataStatus)
	{
		return wafDataStatusDao.save(wafDataStatus);
	}

	@Override
	public int update(WafDataStatusDO wafDataStatus)
	{
		return wafDataStatusDao.update(wafDataStatus);
	}

	@Override
	public int batchRemove(Integer[] ids)
	{
		return wafDataStatusDao.batchRemove(ids);
	}

}
