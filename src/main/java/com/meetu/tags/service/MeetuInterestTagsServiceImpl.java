package com.meetu.tags.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.tags.dao.MeetuInterestTagsDao;
import com.meetu.tags.domain.MeetuInterestTags;

@Service
public class MeetuInterestTagsServiceImpl implements MeetuInterestTagsService {

	@Autowired
	private MeetuInterestTagsDao chatInterestDao;

	@Override
	public void insert(MeetuInterestTags interestTags) throws Exception {
		// TODO Auto-generated method stub
		chatInterestDao.insertOper(interestTags);
	}

	@Override
	public List<Map<String, Object>> selectAll() throws Exception{
		// TODO Auto-generated method stub
		return chatInterestDao.selectAll();
	}

	@Override
	public List<Map<String, Object>> selectTags(String[] array) throws Exception {
		// TODO Auto-generated method stub
		return chatInterestDao.selectTags(array);
	}

	
}
