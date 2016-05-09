package com.meetu.tags.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.tags.dao.MeetuChatTagsDao;
import com.meetu.tags.domain.MeetuChatTags;

@Service
public class MeetuChatTagsServiceImpl implements MeetuChatTagsService {

	@Autowired
	private MeetuChatTagsDao chatTagsDao;
	
	@Override
	public void insert(MeetuChatTags chatTags) throws Exception {
		chatTagsDao.insertOper(chatTags);
	}

	@Override
	public List<Map<String, Object>> selectAll() throws Exception {
		// TODO Auto-generated method stub
		return chatTagsDao.selectAll();
	}

	
}
