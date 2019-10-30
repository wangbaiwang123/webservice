package com.rongwei.zj4a.config;

import com.rongwei.zj4a.webservices.OrgWebService;
import com.rongwei.zj4a.webservices.UserWebService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * @author Administrator
 */
@Configuration
public class CxfConfig
{
	@Autowired
	private Bus bus;

	@Autowired
	private UserWebService employeeWebServices;

	@Autowired
	private OrgWebService orgWebService;

	/**
	 * 此方法作用是改变项目中服务名的前缀名，此处127.0.0.1或者localhost不能访问时，请使用ipconfig查看本机ip来访问
	 * 此方法被注释后:wsdl访问地址为: http://127.0.0.1:8080/services/user?wsdl
	 * 去掉注释后：wsdl访问地址为：http://127.0.0.1:8080/soap/user?wsdl
	 *
	 * @return
	 * @Bean: 如果不写id，则默认方法名为bean的id，但dispatcherServlet和SpringMVC的核心控制器同名了，所以改了个名字
	 */
	@Bean
	public ServletRegistrationBean dispatcherServlet2()
	{
		return new ServletRegistrationBean(new CXFServlet(), "/pims/*");
	}

	/**
	 * JAX-WS
	 * 站点服务
	 **/
	@Bean
	public Endpoint endpoint()
	{
		EndpointImpl endpoint = new EndpointImpl(bus, employeeWebServices);
		endpoint.publish("/user");
		return endpoint;
	}

	/**
	 * JAX-WS
	 * 站点服务
	 **/
	@Bean
	public Endpoint endpoint2()
	{
		EndpointImpl endpoint = new EndpointImpl(bus, orgWebService);
		endpoint.publish("/org");
		return endpoint;
	}
}
