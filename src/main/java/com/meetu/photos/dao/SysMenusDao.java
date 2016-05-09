package com.meetu.photos.dao;

import org.springframework.stereotype.Repository;

import com.meetu.core.ibatis.MapperBaseDao;
import com.meetu.photos.domain.SysMenus;

@Repository("sysmenusdao")
public class SysMenusDao extends MapperBaseDao<SysMenus>{

//	public void insertOper(SysMenus menu);
//	public List<Map<String, Object>> selectAll();
//	public List<Map<String, Object>> paginate(@Param("start") int start, @Param("pageSize") int pageSize);
}
