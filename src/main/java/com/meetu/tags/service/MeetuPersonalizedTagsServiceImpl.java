package com.meetu.tags.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.tags.dao.MeetuPersonalizedTagsDao;
import com.meetu.tags.domain.MeetuPersonalizedTags;

@Service
public class MeetuPersonalizedTagsServiceImpl implements
		MeetuPersonalizedTagsService {
	
	@Autowired
	private MeetuPersonalizedTagsDao personalizedTagsDao;
	
	@Override
	public void insert(MeetuPersonalizedTags personalizedTags) throws Exception {
		personalizedTagsDao.insertOper(personalizedTags);
	}

	@Override
	public List<Map<String, Object>> selectAll(String sex) throws Exception {
		// TODO Auto-generated method stub
		return personalizedTagsDao.selectAll(sex);
	}

	@Override
	public List<Map<String, Object>> selectTags(String[] intArray) throws Exception{
		// TODO Auto-generated method stub
		return personalizedTagsDao.selectTags(intArray);
	}


}
