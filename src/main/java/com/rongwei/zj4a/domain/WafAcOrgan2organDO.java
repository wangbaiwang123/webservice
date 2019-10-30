package com.rongwei.zj4a.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author wangshen
 * @email wangshen@irongwei.com
 * @date 2019-01-23 18:04:31
 */
@Data
public class WafAcOrgan2organDO implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private String o2oid;

	/**
	 * 被兼管机构
	 */
	private String oid;

	/**
	 * 兼管机构
	 */
	private String poid;

	/**
	 * 目前同poid
	 */
	private String gpoid;

	/**
	 * 是否级联(新增)；是：显示被兼管机构下级的机构，否：不显示
	 */
	private String iscascaded;

	/**
	 * 排序号（目前都是1）
	 */
	private Integer sno;

	/**
	 * 兼管机构orule+被兼管机构oid
	 */
	private String orule;

	/**
	 * 隶属单位
	 */
	private String mrut;

	/**
	 * 创建时间
	 */
	private Date createtime;

	/**
	 * 更新时间
	 */
	private Date updatetime;

	/**
	 * 机构规则码
	 */
	private String oper;

	/**
	 * 操作状态
	 */
	private String handlestatus;
}
