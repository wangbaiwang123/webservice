CREATE TABLE waf_wsemployee 
(
  id int not null auto_increment primary key comment '表主键',
  empcode varchar(10) NOT NULL COMMENT '人员编码',
  attribute1 varchar(4000)  DEFAULT NULL COMMENT '扩展字段1',
  attribute2 varchar(500)  DEFAULT NULL COMMENT '扩展字段2',
  birthday varchar(50) DEFAULT NULL COMMENT '生日',
  certno varchar(20)  NOT NULL COMMENT '证件号',
  certtype varchar(10)  DEFAULT NULL COMMENT '证件类型',
  email varchar(100)  DEFAULT NULL COMMENT '邮箱',
  empsort varchar(20) DEFAULT NULL COMMENT '用工类型',
  empstatus varchar(5) DEFAULT NULL COMMENT '用户状态',
  enname varchar(50) DEFAULT NULL COMMENT '英文名称',
  entrytime varchar(50) DEFAULT NULL COMMENT '本企业入职时间',
  fax varchar(50) DEFAULT NULL COMMENT '传真',
  hrempcode varchar(20)  DEFAULT NULL COMMENT '显示编码',
  jobname varchar(50) DEFAULT NULL COMMENT '主职岗位名称',
  jobtype varchar(50) DEFAULT NULL COMMENT '主职岗位类别',
  name varchar(50)  DEFAULT NULL COMMENT '姓名',
  namespell varchar(500) DEFAULT NULL COMMENT '姓名全拼',
  nation varchar(10) DEFAULT NULL COMMENT '民族',
  nationnality varchar(10) DEFAULT NULL COMMENT '国籍',
  officeNum varchar(50) DEFAULT NULL COMMENT '办公室号',
  officedepid varchar(50) DEFAULT NULL COMMENT '主职所在部门ID',
  password varchar(100)  DEFAULT NULL COMMENT '密码',
  phone varchar(100)  DEFAULT NULL COMMENT '手机',
  positiongrade varchar(50) DEFAULT NULL COMMENT '最高职级',
  postionname varchar(500) DEFAULT NULL COMMENT '职务名称串',
  postions varchar(500) DEFAULT NULL COMMENT '职务ID串',
  sex varchar(10) DEFAULT NULL COMMENT '性别',
  sno varchar(50) DEFAULT NULL COMMENT '人员主职部门内排序号',
  subdepts varchar(4000)  DEFAULT NULL COMMENT '兼职所在部门、岗位类别、岗位名称及排序',
  tel varchar(100) DEFAULT NULL COMMENT '座机',
  userLogin varchar(256)  DEFAULT NULL COMMENT '登录账户',
  createtime datetime  NOT NULL COMMENT '创建时间',
  updatetime datetime  NOT NULL COMMENT '修改时间',
  isvalid varchar(10) DEFAULT NULL COMMENT '禁用状态0启用 1禁用',
  handlestatus varchar(10) NOT NULL COMMENT '操作状态' ,
  need_transmission VARCHAR (1) COMMENT '是否需要传输到其他系统,Y:需要,N:不需要'
) COMMENT '账户信息表';

CREATE TABLE WAF_AC_ORGAN_BASE 
(
	O2BID VARCHAR (50) NOT NULL PRIMARY KEY COMMENT '主键',
	BIZTYPE VARCHAR (20) NOT NULL COMMENT '组织业务类型',
	OID VARCHAR (50) NOT NULL COMMENT '机构id',
	POID VARCHAR (50) NOT NULL COMMENT '上级机构id',
	GPOID VARCHAR (50) NOT NULL COMMENT '分组时的上级机构ID',
	ORULE VARCHAR (500) NOT NULL COMMENT '机构规则码',
	GRULE VARCHAR (500) COMMENT '分组机构规则码',
	`TYPE` CHAR (1) NOT NULL COMMENT '机构类型',
	TYPEEXT VARCHAR (50) NOT NULL COMMENT '机构分类',
	MRUT VARCHAR (50) COMMENT '最近更新时间',
	SNO INT NOT NULL COMMENT '序号',
	`NAME` VARCHAR (150) NOT NULL COMMENT '全称',
	SHORTNAME VARCHAR (200) NOT NULL COMMENT '简称',
	OCODE VARCHAR (50) COMMENT '组织编码',
	COID VARCHAR (50) NOT NULL COMMENT '隶属单位',
	CROSSORGAN CHAR (1) NOT NULL COMMENT '拥有兼管职能',
	GOID VARCHAR (50) COMMENT '分组所属的实体机构ID',
	`STATUS` CHAR (1) NOT NULL COMMENT '启用（是否）1:启用 2：临时机构 3:停用 4：撤销',
	GRADE TINYINT (22) NOT NULL COMMENT '层级',
	OPER VARCHAR (50) COMMENT '操作时间',
	NOTE VARCHAR (200) COMMENT '备注',
	TEMORGANNAME VARCHAR (50) COMMENT '临时机构名称',
	ORGPROVINCE VARCHAR (500) COMMENT '机构所在地',
	CAREA VARCHAR (50) COMMENT '国家地区',
	TERRITORYPRO VARCHAR (50) COMMENT '地域属性',
	BIZDOMAIN VARCHAR (50) COMMENT '业务领域',
	ENTCLASS VARCHAR (50) COMMENT '企业分类',
	ORGGRADE VARCHAR (50) COMMENT '机构层级',
	PROJECTSCALE VARCHAR (50) COMMENT '项目规模',
	PROJECTMANTYPE VARCHAR (50) COMMENT '项目管理类型',
	PROJECTTYPE VARCHAR (50) COMMENT '项目类型',
	STARTDATE VARCHAR (50) COMMENT '开始时间',
	ORGANEMP VARCHAR (50) COMMENT '负责人',
	ORGANGRADE VARCHAR (50) COMMENT '机构层级',
	ROWN CHAR (1) COMMENT '独立授权',
	ROID VARCHAR (50) COMMENT '授权机构',
	GLOBAL_SNO VARCHAR (500) COMMENT '全部机构的大流水号',
	QYGRADE VARCHAR (50) COMMENT '股权层级',
	CREATETIME DATETIME COMMENT '插入时间',
	UPDATETIME DATETIME COMMENT '更新时间',
	handlestatus CHAR (1) NOT NULL COMMENT '操作状态,Y:已处理,N:未处理',
	need_transmission VARCHAR (1) COMMENT '是否需要传输到其他系统,Y:需要,N:不需要'
) COMMENT '组织机构信息';


create table WAF_AC_ORGAN2ORGAN
(
  o2oid      VARCHAR(32) not null COMMENT '主键',
  oid        VARCHAR(32) not null COMMENT '被兼管机构',
  poid       VARCHAR(32) not null COMMENT '兼管机构',
  gpoid      VARCHAR(32) not null COMMENT '目前同poid',
  iscascaded VARCHAR(20) COMMENT '是否级联(新增)；是：显示被兼管机构下级的机构，否：不显示',
  sno        INT(10) not null COMMENT '排序号（目前都是1）',
  orule      VARCHAR(500) not null COMMENT '兼管机构orule+被兼管机构oid',
  mrut       VARCHAR(19) COMMENT '隶属单位',
  createtime datetime  NOT NULL COMMENT '创建时间',
  updatetime datetime DEFAULT NULL COMMENT '更新时间',
  oper       VARCHAR(50) COMMENT '机构规则码',
  handlestatus CHAR(1) not null COMMENT '操作状态,Y:已处理,N:未处理',
  PRIMARY KEY (o2oid)
) COMMENT '组织机构行政隶属信息表';


-- 接口日志表
CREATE TABLE waf_logs 
(
	id BIGINT NOT NULL PRIMARY KEY auto_increment COMMENT '主键',
    interface_type VARCHAR (50) COMMENT '接口类别',
	interface_name VARCHAR (50) COMMENT '接口名',
	interface_describe VARCHAR (5000) COMMENT '接口描述',
	important_value VARCHAR (50) COMMENT '放置些重要的信息，比如数据的id、code等',
	input_value VARCHAR (10000) COMMENT '4A传递过来的数据',
	output_value VARCHAR (1000) COMMENT '返回给4A的数据',
	state VARCHAR (10) COMMENT '数据操作状态码',
	message VARCHAR (1000) COMMENT '返回信息,成功、失败等',
	insert_time datetime COMMENT '数据插入时间'
) COMMENT '接口日志表';

-- 数据状态
CREATE TABLE WAF_DATA_STATUS (
	ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '表主键',
	RELATION_ID VARCHAR(20) NOT NULL COMMENT '关联其他表的ID',
	TABLE_TYPE TINYINT NOT NULL COMMENT '1：waf_wsemployee,2:waf_ac_organ,3:waf_ac_organ2organ,4:waf_ac_organ2biz',
	INTERFACE_TYPE VARCHAR(20) NOT NULL COMMENT '要同步数据的接口,取waf_interface_address表中DICTID字段',
	DATA_STATUS CHAR (1) DEFAULT 'N' COMMENT '此条数据的状态,Y:已同步,N：未同步',
	INSERT_TIME DATETIME COMMENT '数据插入时间',
	UPDATE_TIME DATETIME COMMENT '数据更新时间',
	SYNCH_TIME DATETIME COMMENT '数据同步到其他系统时间'
) COMMENT '数据状态，是否已经同步了';

-- 接口地址表
CREATE TABLE `waf_interface_address` 
(
  `DICTTYPEID` varchar(128) NOT NULL,
  `DICTID` varchar(128) NOT NULL,
  `DICTNAME` varchar(255) DEFAULT NULL,
  `STATUS` int(11) DEFAULT NULL COMMENT '状态',
  `SORTNO` int(11) DEFAULT NULL COMMENT '排序号',
  `RANK` int(11) DEFAULT NULL COMMENT '是否排序',
  `PARENTID` varchar(255) DEFAULT NULL COMMENT '父节点Id',
  `SEQNO` varchar(255) DEFAULT NULL COMMENT '序号',
  `FILTER1` varchar(1000) DEFAULT NULL,
  `FILTER2` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DICTTYPEID`,`DICTID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='接口地址表';

INSERT INTO `zj4a`.`waf_interface_address` (`DICTTYPEID`, `DICTID`, `DICTNAME`, `STATUS`, `SORTNO`, `RANK`, `PARENTID`, `SEQNO`, `FILTER1`, `FILTER2`) VALUES ('REMOTE_CALL_ADDRESS', 'OA', 'http://10.17.52.28:8001/', NULL, NULL, NULL, NULL, NULL, 'OA-对OA进行人员新增、修改等，以及对组织结构进行操作的的地址前缀', NULL);
