package com.rongwei.zj4a.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 组织机构信息
 *
 * @author wangshen
 * @email wangshen@irongwei.com
 * @date 2019-02-25 11:37:32
 */
@Data
public class WafAcOrganBaseDO implements Serializable
{
	private static final long serialVersionUID = 1L;

	//主键
	private String o2bid;
	//组织业务类型
	private String biztype;
	//机构id
	private String oid;
	//上级机构id
	private String poid;
	//分组时的上级机构ID
	private String gpoid;
	//机构规则码
	private String orule;
	//分组机构规则码
	private String grule;
	//机构类型
	private String type;
	//机构分类
	private String typeext;
	//最近更新时间
	private String mrut;
	//序号
	private Integer sno;
	//全称
	private String name;
	//简称
	private String shortname;
	//组织编码
	private String ocode;
	//隶属单位
	private String coid;
	//拥有兼管职能
	private String crossorgan;
	//分组所属的实体机构ID
	private String goid;
	//启用（是否）
	private String status;
	//层级
	private Integer grade;
	//操作时间
	private String oper;
	//备注
	private String note;
	//临时机构名称
	private String temorganname;
	//机构所在地
	private String orgprovince;
	//国家地区
	private String carea;
	//地域属性
	private String territorypro;
	//业务领域
	private String bizdomain;
	//企业分类
	private String entclass;
	//机构层级
	private String orggrade;
	//项目规模
	private String projectscale;
	//项目管理类型
	private String projectmantype;
	//项目类型
	private String projecttype;
	//开始时间
	private String startdate;
	//负责人
	private String organemp;
	//机构层级
	private String organgrade;
	//独立授权
	private String rown;
	//授权机构
	private String roid;
	//全部机构的大流水号
	private String globalSno;
	//股权层级
	private String qygrade;
	//插入时间
	private Date createtime;
	//更新时间
	private Date updatetime;
	//操作状态,Y:已处理,N:未处理
	private String handlestatus;
	// 是否需要传输到其他系统,Y:需要,N:不需要
	private String needTransmission;
}
