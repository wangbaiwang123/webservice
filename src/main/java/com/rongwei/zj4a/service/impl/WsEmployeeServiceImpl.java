package com.rongwei.zj4a.service.impl;

import com.rongwei.zj4a.dao.WsemployeeDao;
import com.rongwei.zj4a.domain.WsemployeeDO;
import com.rongwei.zj4a.service.WsEmployeeService;
import com.rongwei.zj4a.vo.Zj4aWsemployeeVO;
import com.rongwei.zj4a.webservices.impl.UserWebServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
public class WsEmployeeServiceImpl implements WsEmployeeService
{
	private final Logger logger = LoggerFactory.getLogger(UserWebServiceImpl.class);

	@Autowired
	private WsemployeeDao wsEmployeeDao;

	@Autowired
	private UserWebServiceImpl userWebService;

	/**
	 * 获取所有的员工信息
	 *
	 * @return
	 */
	@Override
	public List<Zj4aWsemployeeVO> getAllEmployees(Map<String, Object> map)
	{
		List<Zj4aWsemployeeVO> list = wsEmployeeDao.list(map);

		return list;
	}

	/**
	 * 保存
	 *
	 * @param wsemployee
	 * @return
	 */
	@Override
	public int save(WsemployeeDO wsemployee)
	{
		int rows = wsEmployeeDao.save(wsemployee);
		return rows;
	}

	/**
	 * 手动更新 是否传输这个状态
	 */
	@Override
	public void updateNeedTramissionStatus()
	{
		logger.info("手动更新开始...");
		Map<String, Object> params = new HashMap<>(5);
		List<Zj4aWsemployeeVO> list = wsEmployeeDao.list(params);
		String isNeedTrans = "";
		for (Zj4aWsemployeeVO zj4aWsemployeeVO : list)
		{
			isNeedTrans = userWebService.isNeedTransmission(zj4aWsemployeeVO);
			zj4aWsemployeeVO.setNeedTransmission(isNeedTrans);
			wsEmployeeDao.update(zj4aWsemployeeVO);
		}
		logger.info("手动更新结束...");
	}
}
