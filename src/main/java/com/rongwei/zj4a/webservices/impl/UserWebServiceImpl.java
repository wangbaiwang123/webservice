package com.rongwei.zj4a.webservices.impl;


import com.rongwei.zj4a.dao.WafAcOrganBaseDao;
import com.rongwei.zj4a.dao.WafLogsDao;
import com.rongwei.zj4a.dao.WsemployeeDao;
import com.rongwei.zj4a.domain.WafAcOrganBaseDO;
import com.rongwei.zj4a.domain.WafLogsDO;
import com.rongwei.zj4a.domain.WsemployeeDO;
import com.rongwei.zj4a.vo.MessageResult;
import com.rongwei.zj4a.vo.Zj4aWsemployeeVO;
import com.rongwei.zj4a.webservices.UserWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.jws.WebService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * serviceName="UserService",//对外发布的服务名
 * targetNamespace：指定你想要的名称空间，通常使用使用包名反转
 * endpointInterface: 服务接口全路径, 指定做SEI（Service EndPoint Interface）服务端点接口
 *
 * @author wangshen
 */
@Component
@WebService(
		serviceName = "user",
		targetNamespace = "http://webservices.zj4a.rongwei.com",
		endpointInterface = "com.rongwei.zj4a.webservices.UserWebService")
public class UserWebServiceImpl implements UserWebService
{
	private static final Logger logger = LoggerFactory.getLogger(UserWebServiceImpl.class);

	@Autowired
	private WsemployeeDao wsEmployeeDao;


	@Autowired
	private WafLogsDao wafLogsDao;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 组织机构基本信息
	 */
	@Autowired
	private WafAcOrganBaseDao wafAcOrganBaseDao;


	/**
	 * 创建账户 表：waf_wsemployee
	 * <p>
	 * 000	String	创建账户成功
	 * 001	String	账户已存在
	 * 002	String	账户创建失败
	 * <p>
	 * 接口逻辑：
	 * 校验参数信息完整性，判断用户账户是否存在，如不存在，直接创建用户，返回用户创建成功。
	 * 如存在，再判断状态，如果状态为禁用，则修改为启用，返回用户创建成功；
	 * 如果状态为启用，则返回账户已存在信息，创建失败。
	 *
	 * @param wsemployeeDO
	 * @return
	 */
	@Override
	public MessageResult addAccount(WsemployeeDO wsemployeeDO)
	{
		logger.info("开始创建账户");
		logger.info("4A传递过来的数据：" + wsemployeeDO.toString());
		MessageResult messageResult = (MessageResult) applicationContext.getBean("MessageResult");

		// 用于插入日志表
		WafLogsDO wafLogsDO = (WafLogsDO) applicationContext.getBean("WafLogsDO");
		wafLogsDO.setInterfaceType("user");
		wafLogsDO.setInterfaceName("addAccount");
		wafLogsDO.setInterfaceDescribe("创建账户接口");
		wafLogsDO.setImportantValue(wsemployeeDO.getEmpcode());
		wafLogsDO.setInputValue(wsemployeeDO.toString());

		// 数据必填验证
		String message = this.validateAddAccountInfo(wsemployeeDO);
		if (!"".equals(message))
		{
			messageResult.setState("002");
			messageResult.setMessage(message);
		}
		else
		{
			Map<String, Object> map = new HashMap<>(10);
			map.put("empcode", wsemployeeDO.getEmpcode());
			List<Zj4aWsemployeeVO> list = wsEmployeeDao.list(map);

			// Isvalid:账号状态：0禁用 1启用
			wsemployeeDO.setIsvalid("1");
			wsemployeeDO.setHandlestatus("N");
			wsemployeeDO.setUpdatetime(new Date());

			// 存在
			if (list != null && list.size() > 0)
			{
				messageResult.setState("001");
				messageResult.setMessage("账户已存在");

				wsemployeeDO.setId(list.get(0).getId());
				try
				{
					wsEmployeeDao.update(wsemployeeDO);
				}
				catch (Exception e)
				{
					message += "更新失败：empcode-" + wsemployeeDO.getEmpcode() + "," + e.getMessage();
				}
			}
			else
			{
				wsemployeeDO.setCreatetime(new Date());

				// 有当前人对应的主职部门会被推送到EOS时，才说明此人的信息需要被推送到EOS
				wsemployeeDO.setNeedTransmission(this.isNeedTransmission(wsemployeeDO));

				try
				{
					wsEmployeeDao.save(wsemployeeDO);
					messageResult.setState("000");
					messageResult.setMessage("创建账户成功");
				}
				catch (Exception e)
				{
					message += "插入失败：empcode-" + wsemployeeDO.getEmpcode() + "," + e.getMessage();
				}
			}

			// message不为空，表示出现异常
			if (!"".equals(message))
			{
				messageResult.setState("002");
				messageResult.setMessage(message);
			}
		}
		// 写入日志表
		insertIntoWafLogs(wafLogsDO, messageResult);
		logger.info("返回信息：" + messageResult.toString());
		logger.info("创建账户结束");

		return messageResult;
	}

	/**
	 * 修改账户
	 * 100	String	修改账户成功
	 * 101	String	账户不存在
	 * 102	String	修改账户失败
	 *
	 * @param wsemployeeDO
	 * @return
	 */
	@Override
	public MessageResult modifyAccount(WsemployeeDO wsemployeeDO)
	{
		logger.info("修改账户开始");
		logger.info("A传递过来的数据：" + wsemployeeDO.toString());
		MessageResult messageResult = (MessageResult) applicationContext.getBean("MessageResult");

		// 用于插入日志表
		WafLogsDO wafLogsDO = (WafLogsDO) applicationContext.getBean("WafLogsDO");
		wafLogsDO.setInterfaceType("user");
		wafLogsDO.setInterfaceName("modifyAccount");
		wafLogsDO.setInterfaceDescribe("修改账户");
		wafLogsDO.setImportantValue(wsemployeeDO.getEmpcode());
		wafLogsDO.setInputValue(wsemployeeDO.toString());

		// 数据验证
		if (wsemployeeDO.getEmpcode() == null || "".equals(wsemployeeDO.getEmpcode()))
		{
			messageResult.setState("102");
			messageResult.setMessage("修改账户失败：人员编码(empcode)不能为空");
		}
		else
		{
			Map<String, Object> map = new HashMap<>(8);
			map.put("empcode", wsemployeeDO.getEmpcode());
			List<Zj4aWsemployeeVO> list = wsEmployeeDao.list(map);
			// 存在
			if (list != null && list.size() > 0)
			{
				// 有当前人对应的主职部门会被推送到EOS时，才说明此人的信息需要被推送到EOS
				wsemployeeDO.setNeedTransmission(this.isNeedTransmission(wsemployeeDO));

				wsemployeeDO.setUpdatetime(new Date());
				wsemployeeDO.setIsvalid(list.get(0).getIsvalid());
				wsemployeeDO.setHandlestatus("N");
				try
				{
					wsEmployeeDao.update(wsemployeeDO);
					messageResult.setState("100");
					messageResult.setMessage("修改账户成功");
				}
				catch (Exception e)
				{
					messageResult.setState("102");
					messageResult.setMessage("修改失败：empcode-" + wsemployeeDO.getEmpcode() + "," + e.getMessage());
				}
			}
			else
			{
				messageResult.setState("101");
				messageResult.setMessage("要修改的账户不存在");
			}
		}
		// 写入日志表
		insertIntoWafLogs(wafLogsDO, messageResult);
		logger.info("返回信息：" + messageResult.toString());
		logger.info("修改账户结束");

		return messageResult;
	}

	/**
	 * 禁用账户
	 * <p>
	 * 200	String	账户禁用成功
	 * 201	String	账户不存在
	 * 202	String	账户禁用失败
	 *
	 * @param empcode
	 * @return
	 */
	@Override
	public MessageResult disableAccount(String empcode)
	{
		logger.info("账户禁用开始");
		logger.info("4A传递过来的数据：empcode-" + empcode);
		MessageResult messageResult = (MessageResult) applicationContext.getBean("MessageResult");

		// 用于插入日志表
		WafLogsDO wafLogsDO = (WafLogsDO) applicationContext.getBean("WafLogsDO");
		wafLogsDO.setInterfaceType("user");
		wafLogsDO.setInterfaceName("disableAccount");
		wafLogsDO.setInterfaceDescribe("账户禁用");
		wafLogsDO.setImportantValue(empcode);
		wafLogsDO.setInputValue(empcode);

		if (empcode == null || "".equals(empcode))
		{
			messageResult.setState("202");
			messageResult.setMessage("账户禁用失败：人员编码(empcode)不能为空");
		}
		else
		{
			Map<String, Object> map = new HashMap<>(8);
			map.put("empcode", empcode);
			List<Zj4aWsemployeeVO> list = wsEmployeeDao.list(map);
			// 存在
			if (list != null && list.size() > 0)
			{
				WsemployeeDO wsemployeeDO = list.get(0);
				wsemployeeDO.setUpdatetime(new Date());

				messageResult.setState("200");
				messageResult.setMessage("账户禁用成功");
				// 状态变化时才更新
				if (wsemployeeDO.getIsvalid() != null && !"0".equals(wsemployeeDO.getIsvalid()))
				{
					// Isvalid:账号状态，0 禁用 1启用
					wsemployeeDO.setIsvalid("0");
					wsemployeeDO.setHandlestatus("N");

					try
					{
						wsEmployeeDao.update(wsemployeeDO);
					}
					catch (Exception e)
					{
						messageResult.setState("202");
						messageResult.setMessage("账户禁用失败,empcode-" + empcode);
					}
				}
			}
			else
			{
				messageResult.setState("201");
				messageResult.setMessage("账户禁用失败：要禁用的账户不存在,empcode:" + empcode);
			}
		}
		// 写入日志表
		insertIntoWafLogs(wafLogsDO, messageResult);
		logger.info("返回信息：" + messageResult.toString());
		logger.info("账户禁用结束");

		return messageResult;
	}

	/**
	 * 启用账户
	 * <p>
	 * 300	String	账户启用成功
	 * 301	String	账户不存在
	 * 302	String	账户启用失败
	 *
	 * @param empcode
	 * @return
	 */
	@Override
	public MessageResult enableAccount(String empcode)
	{
		logger.info("账户启用开始");
		logger.info("4A传递过来的数据：empcode-" + empcode);
		MessageResult messageResult = (MessageResult) applicationContext.getBean("MessageResult");

		// 用于插入日志表
		WafLogsDO wafLogsDO = (WafLogsDO) applicationContext.getBean("WafLogsDO");
		wafLogsDO.setInterfaceType("user");
		wafLogsDO.setInterfaceName("enableAccount");
		wafLogsDO.setInterfaceDescribe("账户启用");
		wafLogsDO.setImportantValue(empcode);
		wafLogsDO.setInputValue(empcode);

		if (empcode == null || "".equals(empcode))
		{
			messageResult.setState("302");
			messageResult.setMessage("账户启用失败：人员编码(empcode)不能为空");
		}
		else
		{
			Map<String, Object> map = new HashMap<>(8);
			map.put("empcode", empcode);
			List<Zj4aWsemployeeVO> list = wsEmployeeDao.list(map);
			// 存在
			if (list != null && list.size() > 0)
			{
				WsemployeeDO wsemployeeDO = list.get(0);
				wsemployeeDO.setUpdatetime(new Date());

				messageResult.setState("300");
				messageResult.setMessage("账户启用成功");

				// 状态变化时才更新
				if (wsemployeeDO.getIsvalid() != null && !"1".equals(wsemployeeDO.getIsvalid()))
				{
					// Isvalid：账号状态，0 禁用 1启用
					wsemployeeDO.setIsvalid("1");
					wsemployeeDO.setHandlestatus("N");

					try
					{
						wsEmployeeDao.update(wsemployeeDO);
					}
					catch (Exception e)
					{
						messageResult.setState("302");
						messageResult.setMessage("账户启用失败,empcode-" + empcode);
					}
				}
			}
			else
			{
				messageResult.setState("301");
				messageResult.setMessage("账户启用失败：要启用的账户不存在,empcode:" + empcode);
			}
		}
		// 写入日志表
		insertIntoWafLogs(wafLogsDO, messageResult);
		logger.info("返回信息：" + messageResult.toString());
		logger.info("账户启用结束");

		return messageResult;
	}

	/**
	 * 远程调用
	 *
	 * @param url
	 * @param jsonStr
	 * @return
	 */
	public String callRemoteAddress(String url, String jsonStr)
	{
		HttpHeaders httpHeaders = new HttpHeaders();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("jsonStr", jsonStr);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap, httpHeaders);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
		return responseEntity.getBody();
	}


	/**
	 * 验证增加用户时数据的正确性--必填项
	 * <p>
	 * empcode name officedepid  身份证号码，证件类型
	 *
	 * @param wsemployeeDO
	 * @return
	 */
	public String validateAddAccountInfo(WsemployeeDO wsemployeeDO)
	{
		String result = "";
		if (wsemployeeDO.getEmpcode() == null || "".equals(wsemployeeDO.getEmpcode().trim()))
		{
			result += "-人员编码(empcode)不能为空；";
		}
		if (wsemployeeDO.getName() == null || "".equals(wsemployeeDO.getName().trim()))
		{
			result += "-姓名(name)不能为空；";
		}
		if (wsemployeeDO.getCerttype() == null || "".equals(wsemployeeDO.getCerttype().trim()))
		{
			result += "-有效证件类型(certtype) 不能为空；";
		}
		if (wsemployeeDO.getCertno() == null || "".equals(wsemployeeDO.getCertno().trim()))
		{
			result += "-证件编号(certno)不能为空；";
		}
		if (wsemployeeDO.getOfficedepid() == null || "".equals(wsemployeeDO.getOfficedepid().trim()))
		{
			result += "-主职所在部门ID(officedepid)不能为空；";
		}

		return result;
	}

	/**
	 * 把接口返回信息，放入日志表
	 * 并把日期大小限制为500，要不数据太大了
	 *
	 * @param wafLogsDO
	 * @param messageResult
	 */
	public void insertIntoWafLogs(WafLogsDO wafLogsDO, MessageResult messageResult)
	{
		// 写入日志表
		wafLogsDO.setState(messageResult.getState());

		String importantValue = wafLogsDO.getImportantValue();
		importantValue = (importantValue != null && importantValue.length() > 50) ? importantValue.substring(0, 50) : importantValue;
		wafLogsDO.setImportantValue(importantValue);

		String inputValue = wafLogsDO.getInputValue();
		inputValue = (inputValue != null && inputValue.length() > 500) ? inputValue.substring(0, 500) : inputValue;
		wafLogsDO.setInputValue(inputValue);

		String message = messageResult.getMessage();
		message = (message != null && message.length() > 500) ? message.substring(0, 500) : message;
		wafLogsDO.setMessage(message);

		String temp = messageResult.toString();
		temp = temp.length() > 500 ? temp.substring(0, 500) : temp;
		wafLogsDO.setOutputValue(temp);
		wafLogsDO.setInsertTime(new Date());

		try
		{
			wafLogsDao.save(wafLogsDO);
		}
		catch (Exception e)
		{
			logger.info("插入日志表时报错：" + e.getMessage());
		}
	}

	/**
	 * 只有当前人对应的主职部门上级grade=4的数据被会被推送到EOS时，才说明此人的信息需要被推送到EOS
	 * 如果当前人的主职部门不会被传到EOS时，说明在EOS中无法显示，则不推送此人的信息
	 *
	 * @param wsemployeeDO
	 */
	public String isNeedTransmission(WsemployeeDO wsemployeeDO)
	{
		logger.info("=========");
		Map<String, Object> params = new HashMap<>(8);
		params.put("oid", wsemployeeDO.getOfficedepid());
		List<WafAcOrganBaseDO> wafAcOrganBaseDOS = wafAcOrganBaseDao.list(params);
		if (wafAcOrganBaseDOS == null || wafAcOrganBaseDOS.size() == 0)
		{
			return "N";
		}

		if (wafAcOrganBaseDOS.get(0).getGrade().intValue() < 4)
		{
			return "N";
		}

		String needTransmission = wafAcOrganBaseDOS.get(0).getNeedTransmission();
		boolean flag = wafAcOrganBaseDOS.get(0).getGrade().intValue() == 4 && (needTransmission == null || "N".equals(needTransmission) || "".equals(needTransmission.trim()));
		if (flag)
		{
			return "N";
		}

		if (wafAcOrganBaseDOS.get(0).getGrade().intValue() == 4 && "Y".equals(needTransmission))
		{
			return "Y";
		}

		logger.info("原本OID：" + wafAcOrganBaseDOS.get(0).getOid() + ",shortName：" + wafAcOrganBaseDOS.get(0).getShortname());
		WafAcOrganBaseDO wafAcOrganBaseDO = this.getFourLevelOrg(wafAcOrganBaseDOS.get(0));
		String needTransmission2 = wafAcOrganBaseDO.getNeedTransmission();
		String result = (wafAcOrganBaseDO == null || null == needTransmission2 || "".equals(needTransmission2.trim()) || "N".equals(needTransmission2)) ? "N" : "Y";
		logger.info("是否传输：" + result + ",OID:" + wafAcOrganBaseDO.getOid() + ",shortName：" + wafAcOrganBaseDO.getShortname());
		return result;
	}


	/**
	 * 根据部门DO,去找部门信息中 grade为4的数据
	 *
	 * @param baseDO
	 * @return
	 */
	public WafAcOrganBaseDO getFourLevelOrg(WafAcOrganBaseDO baseDO)
	{
		Map<String, Object> params = new HashMap<>(5);
		params.put("oid", baseDO.getPoid());
		List<WafAcOrganBaseDO> wafAcOrganBaseDOS = wafAcOrganBaseDao.list(params);
		if (wafAcOrganBaseDOS == null || wafAcOrganBaseDOS.size() == 0)
		{
			return null;
		}

		if (wafAcOrganBaseDOS.get(0).getGrade() == 4)
		{
			return wafAcOrganBaseDOS.get(0);
		}
		else
		{
			this.getFourLevelOrg(wafAcOrganBaseDOS.get(0));
		}
		return null;
	}
}
