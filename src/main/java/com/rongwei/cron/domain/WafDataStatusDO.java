package com.rongwei.cron.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 数据状态，是否已经同步了
 *
 * @author wangshen
 * @email wangshen@irongwei.com
 * @date 2019-02-20 14:56:39
 */
@Data
public class WafDataStatusDO implements Serializable
{
	private static final long serialVersionUID = 1L;

	//表主键
	private Integer id;
	//关联其他表的ID
	private String relationId;
	//关联表的类型,1：waf_wsemployee,2:waf_ac_organ2organ,3:waf_ac_organ2biz
	private Integer tableType;
	//要同步数据的接口,取waf_interface_address表中DICTID字段
	private String interfaceType;
	//此条数据的状态,Y:已同步,N：未同步
	private String dataStatus;
	//数据插入时间
	private Date insertTime;
	//数据更新时间
	private Date updateTime;
	//数据同步到其他系统时间
	private Date synchTime;


}
