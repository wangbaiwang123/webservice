package com.rongwei.cron.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 字典表
 *
 * @author wangshen
 * @email wangshen@irongwei.com
 * @date 2019-02-20 11:56:55
 */
@Data
public class WafInterfaceAddressDO implements Serializable
{
	private static final long serialVersionUID = 1L;

	//
	private String dicttypeid;
	//
	private String dictid;
	//
	private String dictname;
	//状态
	private Integer status;
	//排序号
	private Integer sortno;
	//是否排序
	private Integer rank;
	//父节点Id
	private String parentid;
	//序号
	private String seqno;
	//
	private String filter1;
	//
	private String filter2;
}
