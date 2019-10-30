package com.rongwei.zj4a.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author wangshen
 * @email 1992lcg@163.com
 * @date 2019-01-25 18:22:03
 */
@Data
public class WafLogsDO implements Serializable
{
	private static final long serialVersionUID = 1L;

	//主键
	private Long id;
	//接口类别
	private String interfaceType;
	//接口名
	private String interfaceName;
	//接口描述
	private String interfaceDescribe;
	//放置些重要的信息，比如数据的id、code等
	private String importantValue;
	//4A传递过来的数据
	private String inputValue;
	//返回给4A的数据
	private String outputValue;
	//数据操作状态码
	private String state;
	//返回信息,成功、失败等
	private String message;
	//数据插入时间
	private Date insertTime;
}
