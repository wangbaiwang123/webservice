package com.rongwei.cron.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rongwei.cron.dao.WafDataStatusDao;
import com.rongwei.cron.dao.WafInterfaceAddressDao;
import com.rongwei.cron.domain.WafDataStatusDO;
import com.rongwei.cron.domain.WafInterfaceAddressDO;
import com.rongwei.cron.service.CronService;
import com.rongwei.zj4a.dao.WafAcOrgan2organDao;
import com.rongwei.zj4a.dao.WafAcOrganBaseDao;
import com.rongwei.zj4a.dao.WafLogsDao;
import com.rongwei.zj4a.dao.WsemployeeDao;
import com.rongwei.zj4a.domain.*;
import com.rongwei.zj4a.service.impl.WsEmployeeServiceImpl;
import com.rongwei.zj4a.vo.Zj4aWsemployeeVO;
import com.rongwei.zj4a.webservices.impl.UserWebServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author Administrator
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
@Service
public class CronServiceImpl implements CronService
{
	private static final Logger logger = LoggerFactory.getLogger(CronServiceImpl.class);

	@Autowired
	private WafDataStatusDao wafDataStatusDao;

	/**
	 * 账户信息表
	 */
	@Autowired
	private WsemployeeDao wsemployeeDao;

	/**
	 * 组织机构基本信息
	 */
	@Autowired
	private WafAcOrganBaseDao wafAcOrganBaseDao;

	/**
	 * 组织机构行政隶属信息
	 */
	@Autowired
	private WafAcOrgan2organDao acOrgan2organDao;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private WafInterfaceAddressDao wafInterfaceAddressDao;

	@Autowired
	private WafLogsDao wafLogsDao;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserWebServiceImpl userWebService;

	/**
	 * 账号信息
	 */
	private final String USER = "user";

	/**
	 * 组织信息
	 */
	private final String ORG_BASE = "org/base";

	/**
	 * 组织机构行政隶属信息表
	 */
	private final String ORG_2ORG = "org2org";


	/**
	 * <p>
	 * 1.把未同步的数据，更新或插入数据到waf_data_status表中
	 * 2.从waf_data_status中查询未同步的数据，调用对应的接口同步数据
	 * <p>
	 * TABLE_TYPE:关联表的类型,1：waf_wsemployee,2:waf_ac_organ,3:waf_ac_organ2organ,4:waf_ac_organ2biz
	 */
	@Override
	public void run()
	{
		/*更新数据同步状态*/
		this.updateStatusOfOrg(); // 组织机构基本信息表
		this.updateStatusOfOrgan2organ(); // 组织机构行政隶属信息表
		this.updateStatusOfEmployee(); // 账户信息表

		/*调用接口，同步数据*/
		this.callInterface();
	}


	/**
	 * 账户信息表
	 * waf_wsemployee中未同步的数据，更新或插入到waf_data_status中
	 */
	public void updateStatusOfEmployee()
	{
		Map<String, Object> map = new HashMap<>(10);
		// handlestatus:操作状态，N：未操作，Y:已操作
		// needTransmission：Y：需要推送到EOS,N:不需要推送到EOS
		map.put("handlestatus", "N");
		map.put("needTransmission", "Y");

		// 查询未同步，并且需要推送到EOS中的数据
		List<Zj4aWsemployeeVO> employees = wsemployeeDao.list(map);

		// 查询所有要调用的接口地址
		List<WafInterfaceAddressDO> addressDOS = this.getAllInterfaces(map);
		for (Zj4aWsemployeeVO employee : employees)
		{
			// tableType关联表的类型,1：waf_wsemployee,2:waf_ac_organ,3:waf_ac_organ2organ,4:waf_ac_organ2biz
			this.saveOrUpdateDataStatus(String.valueOf(employee.getId()), 1, addressDOS);
			employee.setHandlestatus("Y");
			wsemployeeDao.update(employee);
		}
	}

	/**
	 * 更具oid获取父级部门是4级的组织机构
	 *
	 * @param oid
	 * @return
	 */
	public String getFourLevelOrgId(String oid)
	{
		logger.info("原本OID：" + oid);
		Map<String, Object> params = new HashMap<>(8);
		params.put("oid", oid);
		List<WafAcOrganBaseDO> wafAcOrganBaseDOS = wafAcOrganBaseDao.list(params);
		if (wafAcOrganBaseDOS == null || wafAcOrganBaseDOS.size() == 0)
		{
			return oid;
		}

		logger.info("原本等级：" + wafAcOrganBaseDOS.get(0).getGrade());
		if (wafAcOrganBaseDOS.get(0).getGrade().intValue() <= 4)
		{
			return oid;
		}

		WafAcOrganBaseDO wafAcOrganBaseDO = userWebService.getFourLevelOrg(wafAcOrganBaseDOS.get(0));
		logger.info("四级OID：" + wafAcOrganBaseDO.getOid() + ",等级:" + wafAcOrganBaseDO.getGrade());
		return wafAcOrganBaseDO.getOid();
	}


	/**
	 * 组织机构信息表
	 * waf_ac_organ_base中未同步的数据，更新或插入到waf_data_status中
	 */
	public void updateStatusOfOrg()
	{
		Map<String, Object> map = new HashMap<>(16);
		map.put("handlestatus", "N");
		map.put("needTransmission", "Y");
		// 查询未同步的数据
		List<WafAcOrganBaseDO> organDOS = wafAcOrganBaseDao.list(map);

		// 查询所有要调用的接口地址
		List<WafInterfaceAddressDO> addressDOS = this.getAllInterfaces(map);

		for (WafAcOrganBaseDO organDO : organDOS)
		{
			// tableType关联表的类型,1：waf_wsemployee,2:waf_ac_organ_base,3:waf_ac_organ2organ
			this.saveOrUpdateDataStatus(organDO.getO2bid(), 2, addressDOS);
			organDO.setHandlestatus("Y");
			wafAcOrganBaseDao.update(organDO);
		}
	}

	/**
	 * 组织机构行政隶属信息表
	 * waf_ac_organ2organ中未同步的数据，更新或插入到waf_data_status中
	 */
	public void updateStatusOfOrgan2organ()
	{

		Map<String, Object> map = new HashMap<>(5);
		map.put("handlestatus", "N");
		// 查询未同步的数据
		List<WafAcOrgan2organDO> organ2organDOS = acOrgan2organDao.list(map);

		// 查询所有要调用的接口地址
		List<WafInterfaceAddressDO> addressDOS = this.getAllInterfaces(map);

		for (WafAcOrgan2organDO organ2organDO : organ2organDOS)
		{
			// tableType关联表的类型,1：waf_wsemployee,2:waf_ac_organ_base,3:waf_ac_organ2organ
			this.saveOrUpdateDataStatus(organ2organDO.getO2oid(), 3, addressDOS);
			organ2organDO.setHandlestatus("Y");
			acOrgan2organDao.update(organ2organDO);
		}
	}

	/**
	 * 获取所有要调用的接口地址
	 *
	 * @param map
	 * @return
	 */
	public List<WafInterfaceAddressDO> getAllInterfaces(Map<String, Object> map)
	{
		// 从waf_interface_address表中，查询要调用的接口地址
		map.clear();
		map.put("dicttypeid", "REMOTE_CALL_ADDRESS");
		return wafInterfaceAddressDao.list(map);
	}


	/**
	 * 更新或保存到waf_data_status(数据状态表)中
	 *
	 * @param relationId
	 * @param tableType
	 * @param addressDOS
	 */
	public void saveOrUpdateDataStatus(String relationId, int tableType, List<WafInterfaceAddressDO> addressDOS)
	{
		if (addressDOS == null)
		{
			return;
		}

		for (WafInterfaceAddressDO addressDO : addressDOS)
		{
			WafDataStatusDO dataStatusDO = new WafDataStatusDO();
			dataStatusDO.setRelationId(relationId);

			// 关联表的类型,1：waf_wsemployee,2:waf_ac_organ_base,3:waf_ac_organ2organ
			dataStatusDO.setTableType(tableType);
			dataStatusDO.setInterfaceType(addressDO.getDictid());

			BeanMap beanMap = BeanMap.create(dataStatusDO);
			List<WafDataStatusDO> dataStatusDOS = wafDataStatusDao.list(beanMap);
			dataStatusDO.setDataStatus("N");

			// 已经存在 --> 更新
			if (dataStatusDOS != null && dataStatusDOS.size() > 0)
			{
				dataStatusDO.setId(dataStatusDOS.get(0).getId());
				dataStatusDO.setUpdateTime(new Date());
				wafDataStatusDao.update(dataStatusDO);
			}
			else // 不存在 --> 插入
			{
				dataStatusDO.setInsertTime(new Date());
				dataStatusDO.setUpdateTime(new Date());
				wafDataStatusDao.save(dataStatusDO);
			}
		}
	}


	/**
	 * 调用接口，同步数据
	 */
	public void callInterface()
	{

		Map<String, Object> map = new HashMap<>(10);
		map.put("dataStatus", "N");
		// 查询所有未同步的数据
		List<WafDataStatusDO> list = wafDataStatusDao.list(map);
		logger.info("要推送的数据条数：" + (list == null ? "null" : list.size()));

		// 调用接口同步数据
		String temp, fouLevelId, temp1, temp2;
		for (WafDataStatusDO dataStatusDO : list)
		{
			String interfaceType = dataStatusDO.getInterfaceType();
			map.clear();
			map.put("dicttypeid", "REMOTE_CALL_ADDRESS");
			map.put("dictid", interfaceType);
			// 获取接口地址
			List<WafInterfaceAddressDO> addressDOS = wafInterfaceAddressDao.list(map);

			// 获取对应的数据
			// 1：waf_wsemployee,2:waf_ac_organ_base,3:waf_ac_organ2organ
			int tableType = dataStatusDO.getTableType();
			// 1：waf_wsemployee 账号信息
			if (tableType == 1)
			{
				WsemployeeDO wsemployeeDO = wsemployeeDao.get(Integer.valueOf(dataStatusDO.getRelationId()));
				logger.info("推送的账号信息：" + wsemployeeDO == null ? "null" : wsemployeeDO.toString());

				// 把当前人归到父部门是4级的机构
				temp = wsemployeeDO.getOfficedepid();
				fouLevelId = this.getFourLevelOrgId(temp);
				wsemployeeDO.setOfficedepid(fouLevelId);

				// 兼职部门处理
				temp1 = this.getSubDepts(wsemployeeDO.getSubdepts(), 1);
				wsemployeeDO.setSubdepts(temp1);
				temp2 = this.getSubDepts(wsemployeeDO.getAttribute1(), 2);
				wsemployeeDO.setAttribute1(temp2);

				if (addressDOS != null && addressDOS.size() > 0 && wsemployeeDO != null)
				{
					String interfaceAddress = addressDOS.get(0).getDictname();

					Map<String, Object> params = new HashMap<>(10);
					params.put("data", JSON.toJSONString(wsemployeeDO));
					// 调用接口
					logger.info("地址：" + interfaceAddress + USER);
					logger.info("params：" + params);
					logger.info("dataStatusDO：" + dataStatusDO);
					this.postForEntity(interfaceAddress + USER, params, dataStatusDO);
				}
			}

			// 2:waf_ac_organ_base 组织基本信息  NeedTransmission:数据是否需要传输
			if (tableType == 2)
			{
				WafAcOrganBaseDO wafAcOrganBaseDO = wafAcOrganBaseDao.get(dataStatusDO.getRelationId());
				logger.info("推送的组织基本信息 ：" + wafAcOrganBaseDO == null ? "null" : wafAcOrganBaseDO.toString());
				if (addressDOS != null && addressDOS.size() > 0 && wafAcOrganBaseDO != null)
				{
					String interfaceAddress = addressDOS.get(0).getDictname();

					Map<String, Object> params = new HashMap<>(10);
					params.put("data", JSON.toJSONString(wafAcOrganBaseDO));
					// 调用接口
					logger.info("地址：" + interfaceAddress + ORG_BASE);
					this.postForEntity(interfaceAddress + ORG_BASE, params, dataStatusDO);
				}
			}

			// 3:waf_ac_organ2organ 组织机构行政隶属信息
			if (tableType == 3)
			{
				WafAcOrgan2organDO wafAcOrgan2organDO = acOrgan2organDao.get(dataStatusDO.getRelationId());
				logger.info("推送组织机构行政隶属信息 ：" + wafAcOrgan2organDO == null ? "null" : wafAcOrgan2organDO.toString());
				if (addressDOS != null && addressDOS.size() > 0 && wafAcOrgan2organDO != null)
				{
					String interfaceAddress = addressDOS.get(0).getDictname();

					Map<String, Object> params = new HashMap<>(10);
					params.put("data", JSON.toJSONString(wafAcOrgan2organDO));
					// 调用接口
					logger.info("地址：" + interfaceAddress + ORG_2ORG);
					this.postForEntity(interfaceAddress + ORG_2ORG, params, dataStatusDO);
				}
			}
		}
	}

	/**
	 * 添加日志信息到waf_logs中
	 *
	 * @param url
	 * @param params
	 * @param result
	 */
	@SuppressWarnings("AlibabaUndefineMagicConstant")
	public void addLogs(int tableType, String url, Map<String, Object> params, ResponseEntity<String> result)
	{
		// 用于插入日志表
		WafLogsDO wafLogsDO = (WafLogsDO) applicationContext.getBean("WafLogsDO");
		String interfaceDescribe = "";
		// 1：waf_wsemployee 账号信息
		if (tableType == 1)
		{
			interfaceDescribe = "调用接口，同步账号信息";
		}
		// 2:waf_ac_organ_base 组织基本信息
		if (tableType == 2)
		{
			interfaceDescribe = "调用接口，同步组织机构基本信息";
		}
		// 3:waf_ac_organ2organ 组织机构行政隶属信息表
		if (tableType == 3)
		{
			interfaceDescribe = "调用接口，同步组织机构行政隶属信息";
		}

		wafLogsDO.setInterfaceType("callInterface");
		wafLogsDO.setInterfaceName(url);
		wafLogsDO.setInterfaceDescribe(interfaceDescribe);
		wafLogsDO.setInputValue(params.toString());

		JSONObject returnJson = JSON.parseObject(result.getBody());
		wafLogsDO.setOutputValue(returnJson.toString());
		wafLogsDO.setState(returnJson.getString("status"));
		wafLogsDO.setMessage(returnJson.getString("message"));
		wafLogsDO.setInsertTime(new Date());
		wafLogsDao.save(wafLogsDO);

	}

	/**
	 * 调用具体的接口
	 *
	 * @param url
	 * @param params
	 * @param dataStatusDO
	 * @return
	 */
	@SuppressWarnings("AlibabaUndefineMagicConstant")
	public ResponseEntity<String> postForEntity(String url, Map<String, Object> params, WafDataStatusDO dataStatusDO)
	{
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);

		// 传递给接口的数据
		HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, requestHeaders);
		// 调用接口
		ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);
		logger.info("接口返回的数据：" + result.getBody());

		// 添加日志到表中
		this.addLogs(dataStatusDO.getTableType(), url, params, result);

		// 更新数据状态
		JSONObject returnJson = JSON.parseObject(result.getBody());
		if (result.getStatusCode().value() == 200 && "000".equals(returnJson.getString("status")))
		{
			dataStatusDO.setDataStatus("Y");
			dataStatusDO.setSynchTime(new Date());
			wafDataStatusDao.update(dataStatusDO);
		}

		return result;
	}

	/**
	 * 添加兼职部门转换
	 *
	 * @param subdepts 兼职部门字符串，“兼职所在部门ID|岗位类别ID|岗位ID |岗位名称|排序号” 多条兼职循环拼串
	 *                 attrbute1: 兼职所在部门ID||岗位类别ID||排序号
	 * @return
	 */
	public String getSubDepts(String subdepts, int type)
	{
		if (subdepts != null && !"".equals(subdepts.trim()))
		{
			String[] values = type == 1 ? subdepts.split("\\|") : subdepts.split("\\|\\|");
			int groupNum = type == 1 ? values.length / 5 : values.length / 3;
			if (groupNum == 0)
			{
				return null;
			}

			String subdept = "";
			Map<String, Object> params = new HashMap<>(5);
			while (groupNum > 0)
			{
				int subdeptPlace = type == 1 ? (groupNum - 1) * 5 : (groupNum - 1) * 3;
				subdept = values[subdeptPlace];
				params.put("oid", subdept);
				List<WafAcOrganBaseDO> organizationDOS = wafAcOrganBaseDao.list(params);
				if (organizationDOS != null && organizationDOS.size() > 0)
				{
					values[subdeptPlace] = organizationDOS.get(0).getOid();
				}
				groupNum--;
			}
			String result = type == 1 ? String.join("|", values) : String.join("||", values);
			return result;
		}
		return null;
	}
}