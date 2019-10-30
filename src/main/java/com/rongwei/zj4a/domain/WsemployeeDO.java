package com.rongwei.zj4a.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author wangshen
 * @email wangshen@irongwei.com
 * @date 2018-12-25 14:53:47
 */
@Data
public class WsemployeeDO implements Serializable
{
	private static final long serialVersionUID = 1L;

	// 主键
	private Integer id;

	//人员编码
	private String empcode;
	//扩展字段1
	private String attribute1;
	//扩展字段2
	private String attribute2;
	//生日
	private String birthday;
	//证件号
	private String certno;
	//证件类型
	private String certtype;
	//邮箱
	private String email;
	//用工类型
	private String empsort;
	//用户状态
	private String empstatus;
	//英文名称
	private String enname;
	//本企业入职时间
	private String entrytime;
	//传真
	private String fax;
	//显示编码
	private String hrempcode;
	//主职岗位名称
	private String jobname;
	//主职岗位类别
	private String jobtype;
	//姓名
	private String name;
	//姓名全拼
	private String namespell;
	//民族
	private String nation;
	//国籍
	private String nationnality;
	//办公室号
	private String officenum;
	//主职所在部门ID
	private String officedepid;
	//密码
	private String password;
	//手机
	private String phone;
	//最高职级
	private String positiongrade;
	//职务名称串
	private String postionname;
	//职务ID串
	private String postions;
	//性别
	private String sex;
	//人员主职部门内排序号
	private String sno;
	//兼职所在部门、岗位类别、岗位名称及排序
	private String subdepts;
	//座机
	private String tel;
	//登录账户
	private String userlogin;
	//创建时间
	private Date createtime;
	//修改时间
	private Date updatetime;

	/**
	 * 禁用状态0启用 1禁用
	 */
	private String isvalid;

	/**
	 * 是否需要传输到其他系统,Y:需要,N:不需要
	 */
	private String handlestatus;

	/**
	 * 是否需要传输到其他系统,Y:需要,N:不需要
	 */
	private String needTransmission;

}
