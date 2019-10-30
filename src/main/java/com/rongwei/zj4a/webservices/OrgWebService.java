package com.rongwei.zj4a.webservices;

import com.rongwei.zj4a.domain.WafAcOrgan2organDO;
import com.rongwei.zj4a.domain.WafAcOrganBaseDO;
import com.rongwei.zj4a.vo.MessageResult;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * @author Administrator
 * <p>
 * targetNamespace：指定你想要的名称空间，通常使用使用包名反转
 */
@WebService(targetNamespace = "http://webservices.zj4a.rongwei.com")
public interface OrgWebService
{
	/**
	 * webSerbice接口，用于插入组织机构基本信息
	 *
	 * @param wfAcOrganDO
	 * @return
	 */
	@WebMethod
	@WebResult
	MessageResult insertWafAcOrgan(@WebParam(name = "organs") WafAcOrganBaseDO[] wfAcOrganDO);


	/**
	 * webSerbice接口，用于插入行政隶属关系信息
	 *
	 * @param wafAcOrgan2organDOS
	 * @return
	 */
	@WebMethod
	@WebResult
	MessageResult insertWafAcOrgan2organ(@WebParam(name = "org2orgs") WafAcOrgan2organDO[] wafAcOrgan2organDOS);
}
