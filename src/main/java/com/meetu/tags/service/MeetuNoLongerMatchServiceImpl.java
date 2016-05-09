package com.meetu.tags.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.tags.dao.MeetuNoLongerMatchDao;
import com.meetu.tags.domain.MeetuNoLongerMatch;

@Service
public class MeetuNoLongerMatchServiceImpl implements MeetuNoLongerMatchService {

	@Autowired
	private MeetuNoLongerMatchDao noLongerMatchDao;
	
	@Override
	public void insertOper(MeetuNoLongerMatch noLongerMatch) throws Exception {
		// TODO Auto-generated method stub
		noLongerMatchDao.insertOper(noLongerMatch);
	}

}
