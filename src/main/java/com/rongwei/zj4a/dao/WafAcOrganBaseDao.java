package com.rongwei.zj4a.dao;

import java.util.List;
import java.util.Map;

import com.rongwei.zj4a.domain.WafAcOrganBaseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织机构信息
 *
 * @author wangshen
 * @email wangshen@irongwei.com
 * @date 2019-02-25 10:58:53
 */
@Mapper
public interface WafAcOrganBaseDao
{

	WafAcOrganBaseDO get(String o2bid);

	List<WafAcOrganBaseDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(WafAcOrganBaseDO wafAcOrganBase);

	int update(WafAcOrganBaseDO wafAcOrganBase);

	int batchRemove(String[] o2bids);
}
