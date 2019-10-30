package com.rongwei.zj4a.dao;

import com.rongwei.zj4a.domain.WsemployeeDO;

import java.util.List;
import java.util.Map;

import com.rongwei.zj4a.vo.Zj4aWsemployeeVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wangshen
 * @email 1992lcg@163.com
 * @date 2018-12-25 14:53:47
 */
@Mapper
public interface WsemployeeDao
{

	/**
	 * 根据主键查询
	 *
	 * @param id
	 * @return
	 */
	WsemployeeDO get(int id);

	/**
	 * 条件查询
	 *
	 * @param map
	 * @return
	 */
	List<Zj4aWsemployeeVO> list(Map<String, Object> map);

	/**
	 * 保存
	 *
	 * @param wsemployee
	 * @return
	 */
	int save(WsemployeeDO wsemployee);

	/**
	 * 更新
	 *
	 * @param wsemployee
	 * @return
	 */
	int update(WsemployeeDO wsemployee);

	/**
	 * 删除
	 *
	 * @param empcode
	 * @return
	 */
	int remove(String empcode);

	/**
	 * 批量删除
	 *
	 * @param empcodes
	 * @return
	 */
	int batchRemove(String[] empcodes);
}
