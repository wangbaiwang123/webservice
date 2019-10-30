package com.rongwei.zj4a.dao;

import com.rongwei.zj4a.domain.WafAcOrgan2organDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangshen
 * @email wangshen@irongwei.com
 * @date 2019-01-23 18:04:31
 */
@Mapper
public interface WafAcOrgan2organDao
{

	/**
	 * 更具主键获取
	 *
	 * @param o2oid
	 * @return
	 */
	WafAcOrgan2organDO get(String o2oid);

	/**
	 * 条件查询
	 *
	 * @param map
	 * @return
	 */
	List<WafAcOrgan2organDO> list(Map<String, Object> map);

	/**
	 * 条件计数
	 *
	 * @param map
	 * @return
	 */
	int count(Map<String, Object> map);

	/**
	 * 保存
	 *
	 * @param wafAcOrgan2organ
	 * @return
	 */
	int save(WafAcOrgan2organDO wafAcOrgan2organ);

	/**
	 * 更新
	 *
	 * @param wafAcOrgan2organ
	 * @return
	 */
	int update(WafAcOrgan2organDO wafAcOrgan2organ);

	/**
	 * 删除
	 *
	 * @param o2oid
	 * @return
	 */
	int remove(String o2oid);

	/**
	 * 批量删除
	 *
	 * @param o2oids
	 * @return
	 */
	int batchRemove(String[] o2oids);
}
