package com.meetu.tags.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.tags.dao.MeetuReferencesDao;
import com.meetu.tags.domain.MeetuReferences;

@Service
public class MeetuReferencesServiceImpl implements MeetuReferencesService {

	@Autowired
	private MeetuReferencesDao referencesDao;
	
	@Override
	public void insertOper(MeetuReferences references) throws Exception{
		// TODO Auto-generated method stub
		referencesDao.insertOper(references);
	}

	@Override
	public List<Map<String, Object>> selectReferencesList(String userid,
			Integer biubiuListNumbers, Date date) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userid", userid);
		map.put("num", biubiuListNumbers);
		map.put("date", date);
		return referencesDao.selectReferencesList(map);
	}

	@Override
	public void updateState(String id) throws Exception {
		// TODO Auto-generated method stub
		referencesDao.updateState(id);
	}

	@Override
	public List<Map<String, Object>> selectListByChatId(String chat_id) {
		// TODO Auto-generated method stub
		return referencesDao.selectListByChatId(chat_id);
	}

	@Override
	public List<Map<String, Object>> selectReferencesListUnlogin(Integer num, Date date)
			throws Exception {
		// TODO Auto-generated method stub
		return referencesDao.selectReferencesListUnlogin(num, date);
	}

	@Override
	public void deleteListByChatId(String chat_id) {
		// TODO Auto-generated method stub
		referencesDao.deleteListByChatId(chat_id);
	}

	@Override
	public Date selectFoundDateById(String id) throws Exception {
		// TODO Auto-generated method stub
		return referencesDao.selectFoundDateById(id);
	}

}
