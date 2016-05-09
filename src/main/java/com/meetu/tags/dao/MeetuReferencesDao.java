package com.meetu.tags.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.meetu.tags.domain.MeetuReferences;

public interface MeetuReferencesDao {

	public void insertOper(MeetuReferences references);

	public List<Map<String, Object>> selectReferencesList(
			Map<String, Object> map);
	
	public List<Map<String, Object>> selectReferencesListUnlogin(@Param("num") Integer num, @Param("date") Date date);

	public void updateState(String id);
	
	public List<Map<String, Object>> selectListByChatId(String chat_id);

	public void deleteListByChatId(String chat_id);

	public Date selectFoundDateById(String id);
}
