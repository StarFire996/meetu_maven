package com.meetu.tags.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.meetu.tags.domain.MeetuChatList;

public interface MeetuChatListService {

	public List<Map<String, Object>> selectChatList(String userid) throws Exception;
	public void insertOper(MeetuChatList chatList);
	public String checkIsGrabbed(String chatlist_id) throws Exception;
	public Integer selectFounderCode(String chat_id) throws Exception;
	public void updateGrabInfo(MeetuChatList chatlist) throws Exception;
	public String selectFounderId(String chat_id) throws Exception;
	public Date selectFoundDateById(String id) throws Exception;
	public MeetuChatList selectChatListById(String chat_id);
	public Date selectChatDate(String from_user_id, String to_user_id);
	public String selectUngrabbedChatByUserId(String userid);
	public void updateChatListById(MeetuChatList chatlist);
	public List<Map<String, Object>> selectLatestChatByuserId(String from_user_id);
	public void updateState(String id) throws Exception;
	public Date checkSendBiu(String from_user_id);
}
