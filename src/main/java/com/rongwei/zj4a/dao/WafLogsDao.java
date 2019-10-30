package com.rongwei.zj4a.dao;

import com.rongwei.zj4a.domain.WafLogsDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author wangshen
 * @email 1992lcg@163.com
 * @date 2019-01-25 18:22:03
 */
@Mapper
public interface WafLogsDao {

	WafLogsDO get(Long id);
	
	List<WafLogsDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(WafLogsDO wafLogs);
	
	int update(WafLogsDO wafLogs);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
