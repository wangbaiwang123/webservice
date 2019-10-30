package com.rongwei.zj4a.controller;

import com.rongwei.zj4a.service.WsEmployeeService;
import com.rongwei.zj4a.vo.Zj4aWsemployeeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("/WsEmployee")
public class WsEmployeeController
{
	@Autowired
	private WsEmployeeService wsEmployeeService;

	@ResponseBody
	@RequestMapping("/getAllEmployees")
	public List<Zj4aWsemployeeVO> getAllEmployees(Map<String, Object> map)
	{
		List<Zj4aWsemployeeVO> list = wsEmployeeService.getAllEmployees(map);
		return list;
	}

	@ResponseBody
	@RequestMapping("/updateNeedTramissionStatus")
	public void getAllEmployees()
	{
		wsEmployeeService.updateNeedTramissionStatus();
	}
}
