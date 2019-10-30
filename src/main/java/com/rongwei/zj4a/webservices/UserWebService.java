package com.rongwei.zj4a.webservices;


import com.rongwei.zj4a.domain.WsemployeeDO;
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
public interface UserWebService
{
	/**
	 * 创建账户
	 *
	 * @param wsEmployeeDO
	 * @return
	 */
	@WebMethod
	@WebResult
	MessageResult addAccount(@WebParam(name = "wsEmployeeDO") WsemployeeDO wsEmployeeDO);


	/**
	 * 修改账户
	 *
	 * @param wsEmployeeDO
	 * @return
	 */
	@WebMethod
	@WebResult
	MessageResult modifyAccount(@WebParam(name = "wsEmployeeDO") WsemployeeDO wsEmployeeDO);

	/**
	 * 禁用账户
	 *
	 * @param empcode
	 * @return
	 */
	@WebMethod
	@WebResult
	MessageResult disableAccount(@WebParam(name = "empcode") String empcode);

	/**
	 * 启用账户
	 *
	 * @param empcode
	 * @return
	 */
	@WebMethod
	@WebResult
	MessageResult enableAccount(@WebParam(name = "empcode") String empcode);

}
