package com.meetu.tags.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meetu.tags.dao.MeetuChatListDao;
import com.meetu.tags.domain.MeetuChatList;

@Service
@Transactional
public class MeetuChatListServiceImpl implements MeetuChatListService {

	@Autowired
	private MeetuChatListDao chatlistDao;
	
	@Override
	public List<Map<String, Object>> selectChatList(String userid)
			throws Exception {
		return chatlistDao.selectChatList(userid);
	}

	@Override
	public void insertOper(MeetuChatList chatList) {
		chatlistDao.insertOper(chatList);
	}

	@Override
	public String checkIsGrabbed(String chatlist_id) throws Exception {
		// TODO Auto-generated method stub
		return chatlistDao.checkIsGrabbed(chatlist_id);
	}

	@Override
	public Integer selectFounderCode(String chat_id) throws Exception {
		// TODO Auto-generated method stub
		return chatlistDao.selectFounderCode(chat_id);
	}

	@Override
	public void updateGrabInfo(MeetuChatList chatlist) throws Exception {
		// TODO Auto-generated method stub
		chatlistDao.updateGrabInfo(chatlist);
	}

	@Override
	public String selectFounderId(String chat_id) throws Exception {
		// TODO Auto-generated method stub
		return chatlistDao.selectFounderId(chat_id);
	}

	@Override
	public Date selectFoundDateById(String id) throws Exception {
		// TODO Auto-generated method stub
		return chatlistDao.selectFoundDateById(id);
	}

	@Override
	public MeetuChatList selectChatListById(String chat_id) {
		// TODO Auto-generated method stub
		return chatlistDao.selectChatListById(chat_id);
	}

	@Override
	public Date selectChatDate(String from_user_id, String to_user_id) {
		// TODO Auto-generated method stub
		return chatlistDao.selectChatDate(from_user_id, to_user_id);
	}

	@Override
	public String selectUngrabbedChatByUserId(String userid) {
		// TODO Auto-generated method stub
		return chatlistDao.selectUngrabbedChatByUserId(userid);
	}

	@Override
	public void updateChatListById(MeetuChatList chatlist){
		// TODO Auto-generated method stub
		chatlistDao.updateChatListById(chatlist);
	}


	@Override
	public List<Map<String, Object>> selectLatestChatByuserId(String from_user_id) {
		// TODO Auto-generated method stub
		return chatlistDao.selectLatestChatByuserId(from_user_id);
	}

	@Override
	public void updateState(String id) throws Exception {
		// TODO Auto-generated method stub
		chatlistDao.updateState(id);
	}

	@Override
	public Date checkSendBiu(String from_user_id) {
		// TODO Auto-generated method stub
		return chatlistDao.checkSendBiu(from_user_id);
	}

}
