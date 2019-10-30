package com.rongwei.zj4a.webservices.impl;

import com.rongwei.zj4a.dao.WafAcOrgan2organDao;
import com.rongwei.zj4a.dao.WafAcOrganBaseDao;
import com.rongwei.zj4a.dao.WafLogsDao;
import com.rongwei.zj4a.domain.WafAcOrgan2organDO;
import com.rongwei.zj4a.domain.WafAcOrganBaseDO;
import com.rongwei.zj4a.domain.WafLogsDO;
import com.rongwei.zj4a.vo.MessageResult;
import com.rongwei.zj4a.webservices.OrgWebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.util.*;

/**
 * serviceName="UserService",//对外发布的服务名
 * targetNamespace：指定你想要的名称空间，通常使用使用包名反转
 * endpointInterface: 服务接口全路径, 指定做SEI（Service EndPoint Interface）服务端点接口
 *
 * @author wangshen
 */
@Component
@WebService(
		serviceName = "org",
		targetNamespace = "http://webservices.zj4a.rongwei.com",
		endpointInterface = "com.rongwei.zj4a.webservices.OrgWebService")
public class OrgWebServiceImpl implements OrgWebService
{
	private static final Logger logger = LoggerFactory.getLogger(OrgWebServiceImpl.class);

	@Autowired
	private WafAcOrganBaseDao wafAcOrganBaseDao;

	@Autowired
	private WafAcOrgan2organDao wafAcOrgan2organDao;

	@Autowired
	private WafLogsDao wafLogsDao;

	@Autowired
	private ApplicationContext applicationContext;


	/**
	 * 组织机构基本信息接口
	 * 已存在 ---> 更新
	 * 不存在 ---> 插入
	 * <p>
	 * 100	机构增加成功
	 * 102	机构增加失败
	 *
	 * @param wfAcOrganDOS
	 * @return
	 */
	@Override
	public MessageResult insertWafAcOrgan(WafAcOrganBaseDO[] wfAcOrganDOS)
	{
		logger.info("组织机构基本信息接口,调用开始");
		logger.info("组织机构基本信息接口,4A传递过来的数据:" + Arrays.toString(wfAcOrganDOS));

		// 用于插入日志表
		WafLogsDO wafLogsDO = (WafLogsDO) applicationContext.getBean("WafLogsDO");
		wafLogsDO.setInterfaceType("org");
		wafLogsDO.setInterfaceName("insertWafAcOrgan");
		wafLogsDO.setInterfaceDescribe("组织机构基本信息");
		wafLogsDO.setInputValue(wfAcOrganDOS.toString());

		String message = "";
		String importantValues = "";
		for (WafAcOrganBaseDO value : wfAcOrganDOS)
		{
			importantValues += "->" + value.getO2bid();

			// 数据必填验证
			String validateResult = this.validateWafAcOrgan(value);
			if (!"".equals(validateResult))
			{
				message += validateResult;
				continue;
			}

			value.setUpdatetime(new Date());
			value.setHandlestatus("N");

			Map<String, Object> map = new HashMap<>(8);
			map.put("o2bid", value.getO2bid());
			List<WafAcOrganBaseDO> list = wafAcOrganBaseDao.list(map);

			// 数据存在 --> 更新
			if (list != null && list.size() > 0)
			{
				try
				{
					wafAcOrganBaseDao.update(value);
				}
				catch (Exception e)
				{
					message += "--更新失败：o2bid为 " + value.getO2bid() + "," + e.getMessage();
				}
			}
			else // 插入
			{
				try
				{
					value.setCreatetime(new Date());
					wafAcOrganBaseDao.save(value);
				}
				catch (Exception e)
				{
					message += "--插入失败：o2bid为 " + value.getO2bid() + "," + e.getMessage();
				}
			}
		}

		MessageResult messageResult = this.getMessageResult(message);
		wafLogsDO.setImportantValue(importantValues);

		// 写入日志表
		this.insertIntoWafLogs(wafLogsDO, messageResult);
		logger.info("返回信息:" + messageResult.toString());
		logger.info("组织机构基本信息接口,调用结束");

		return messageResult;
	}

	/**
	 * 验证：组织机构基本信息，中的必填字段
	 *
	 * @param value
	 * @return
	 */
	public String validateWafAcOrgan(WafAcOrganBaseDO value)
	{
		String result = "";

		result += value.getO2bid() == null || "".equals(value.getO2bid()) ? "-主键(o2bid)不能为空" : "";
		result += value.getBiztype() == null || "".equals(value.getBiztype()) ? "-组织业务类型(Biztype)不能为空" : "";
		result += value.getOid() == null || "".equals(value.getOid()) ? "-机构ID(Oid)不能为空" : "";
		result += value.getPoid() == null || "".equals(value.getPoid()) ? "-上级机构id(Poid)不能为空" : "";
		result += value.getGpoid() == null || "".equals(value.getGpoid()) ? "-分组时的上级机构(Gpoid)不能为空" : "";
		result += value.getOrule() == null || "".equals(value.getOrule()) ? "-机构规则码(Orule)不能为空" : "";
		result += value.getType() == null || "".equals(value.getType()) ? "-机构类型(type)不能为空" : "";
		result += value.getTypeext() == null || "".equals(value.getTypeext()) ? "-组织分类(typeext)不能为空" : "";
		result += value.getSno() == null || "".equals(value.getSno()) ? "-序号(Sno)不能为空" : "";
		result += value.getName() == null || "".equals(value.getName()) ? "-全称(Name)不能为空" : "";
		result += value.getShortname() == null || "".equals(value.getShortname()) ? "-简称(shortname)不能为空" : "";
		result += value.getCoid() == null || "".equals(value.getCoid()) ? "-隶属单位(Coid)不能为空" : "";
		result += value.getCrossorgan() == null || "".equals(value.getCrossorgan()) ? "-拥有兼管职能(Crossorgan)不能为空" : "";
		result += value.getStatus() == null || "".equals(value.getStatus()) ? "-启用状态(Status)不能为空" : "";
		result += value.getGrade() == null || "".equals(value.getGrade()) ? "-层级(grade)不能为空" : "";

		// 验证失败，并且o2bid存在时，把o2bid的值加在最前面，从而容易找打对应的数据
		if (value.getOid() != null && !"".equals(value.getOid()) && !"".equals(result))
		{
			result = "--主键o2bid:" + value.getO2bid() + result;
		}

		return result;
	}


	/**
	 * 兼管隶属关系信息接口
	 *
	 * @param wafAcOrgan2organDOS
	 * @return
	 */
	@Override
	public MessageResult insertWafAcOrgan2organ(WafAcOrgan2organDO[] wafAcOrgan2organDOS)
	{
		logger.info("兼管隶属关系信息接口，调用开始");
		logger.info("4A传递过来的数据:" + Arrays.toString(wafAcOrgan2organDOS));

		// 用于插入日志表
		WafLogsDO wafLogsDO = (WafLogsDO) applicationContext.getBean("WafLogsDO");
		wafLogsDO.setInterfaceType("org");
		wafLogsDO.setInterfaceName("insertWafAcOrgan2organ");
		wafLogsDO.setInterfaceDescribe("兼管隶属关系信息");
		wafLogsDO.setInputValue(wafAcOrgan2organDOS.toString());

		String message = "";
		String importantValues = "";
		for (WafAcOrgan2organDO value : wafAcOrgan2organDOS)
		{
			importantValues += "-" + value.getOid();

			// 数据必填验证
			String validateResult = this.validateWafAcOrgan2organ(value);
			if (!"".equals(validateResult))
			{
				message += validateResult;
				continue;
			}

			value.setUpdatetime(new Date());
			value.setHandlestatus("N");

			Map<String, Object> map = new HashMap<>(8);
			map.put("o2oid", value.getO2oid());
			List<WafAcOrgan2organDO> list = wafAcOrgan2organDao.list(map);
			if (list != null && list.size() > 0)
			{
				try
				{
					wafAcOrgan2organDao.update(value);
				}
				catch (Exception e)
				{
					message += "--更新失败：o2oid " + value.getOid() + "," + e.getMessage();
				}
			}
			else
			{
				try
				{
					value.setCreatetime(new Date());
					wafAcOrgan2organDao.save(value);
				}
				catch (Exception e)
				{
					message += "--插入失败：o2oid " + value.getOid() + "," + e.getMessage();
				}
			}
		}

		MessageResult messageResult = this.getMessageResult(message);

		wafLogsDO.setImportantValue(importantValues);

		// 写入日志表
		this.insertIntoWafLogs(wafLogsDO, messageResult);
		logger.info("返回信息:" + messageResult.toString());
		logger.info("兼管隶属关系信息接口，调用结束");

		return messageResult;
	}


	/**
	 * 验证：兼管隶属关系信息接口，中的必填字段
	 *
	 * @param value
	 * @return
	 */
	public String validateWafAcOrgan2organ(WafAcOrgan2organDO value)
	{
		String result = "";

		result += value.getO2oid() == null || "".equals(value.getO2oid()) ? "-主键D(o2oid)不能为空" : "";
		result += value.getOid() == null || "".equals(value.getOid()) ? "-被兼管机构(OID)不能为空" : "";
		result += value.getPoid() == null || "".equals(value.getPoid()) ? "-兼管机构id(poid)不能为空" : "";
		result += value.getGpoid() == null || "".equals(value.getGpoid()) ? "-(GPOID)不能为空" : "";
		result += value.getSno() == null || "".equals(value.getSno()) ? "-排序号(sno)不能为空" : "";
		result += value.getOrule() == null || "".equals(value.getOrule()) ? "-兼管机构orule+被兼管机构oid(orule)不能为空" : "";
		result += value.getOrule() == null || "".equals(value.getOrule()) ? "-机构规则码(ORULE)不能为空" : "";
		result += value.getSno() == null || "".equals(value.getSno()) ? "-序号(SNO)不能为空" : "";

		// 验证失败，并且oid存在时，报oid加在最前面，从而容易找打对应的数据
		if (value.getO2oid() != null && !"".equals(value.getO2oid()) && !"".equals(result))
		{
			result = "--主键(O2OID):" + value.getO2oid() + result;
		}

		return result;
	}

	/**
	 * 获取返回信息对象
	 *
	 * @param message
	 * @return
	 */
	public MessageResult getMessageResult(String message)
	{
		MessageResult messageResult = new MessageResult();
		if ("".equals(message))
		{
			messageResult.setState("100");
			messageResult.setMessage("全部成功");
		}
		else
		{
			messageResult.setState("102");
			messageResult.setMessage("失败的有：" + message);
		}
		return messageResult;
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
}



