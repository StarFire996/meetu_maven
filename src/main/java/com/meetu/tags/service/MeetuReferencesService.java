package com.meetu.tags.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.meetu.tags.domain.MeetuReferences;

public interface MeetuReferencesService {

	public void insertOper(MeetuReferences references) throws Exception;

	public List<Map<String, Object>> selectReferencesList(String userid,
			Integer biubiuListNumbers, Date date) throws Exception;
	public void updateState(String id) throws Exception;
	
	public List<Map<String, Object>> selectListByChatId(String chat_id);
	public List<Map<String, Object>> selectReferencesListUnlogin(Integer num, Date date) throws Exception;

	public void deleteListByChatId(String chat_id);
	public Date selectFoundDateById(String id) throws Exception;
}
