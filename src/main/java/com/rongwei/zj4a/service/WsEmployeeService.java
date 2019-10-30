package com.rongwei.zj4a.service;

import com.rongwei.zj4a.domain.WsemployeeDO;
import com.rongwei.zj4a.vo.Zj4aWsemployeeVO;

import javax.jws.WebService;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@WebService
public interface WsEmployeeService
{
	/**
	 * 获取所有的员工信息
	 *
	 * @param map
	 * @return
	 */
	List<Zj4aWsemployeeVO> getAllEmployees(Map<String, Object> map);

	/**
	 * 把从4A传过来的数据，保存到zj4a库中
	 *
	 * @param wsemployee
	 * @return
	 */
	int save(WsemployeeDO wsemployee);


	/**
	 * 手动更新 是否传输这个状态
	 */
	void updateNeedTramissionStatus();
}
