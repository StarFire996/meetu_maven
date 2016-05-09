package com.meetu.tags.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.tags.dao.MeetuFriendsRelDao;
import com.meetu.tags.domain.MeetuFriendsRel;

@Service
public class MeetuFriendsRelServiceImpl implements MeetuFriendsRelService {

	@Autowired
	MeetuFriendsRelDao relDao;
	@Override
	public void insertOper(MeetuFriendsRel friendsRel) throws Exception {
		// TODO Auto-generated method stub
		relDao.insertOper(friendsRel);
	}
	@Override
	public MeetuFriendsRel isFriends(Map<String, Object> ids) throws Exception {
		// TODO Auto-generated method stub
		return relDao.isFriends(ids);
	}
	@Override
	public List<Map<String, Object>> selectFriendsListByUserId(String userid) {
		return relDao.selectFriendsListByUserId(userid);
	}
	@Override
	public void deleteByUserIds(String user_id1, String user_id2)
			throws Exception {
		// TODO Auto-generated method stub
		relDao.deleteByUserIds(user_id1, user_id2);
	}

}
