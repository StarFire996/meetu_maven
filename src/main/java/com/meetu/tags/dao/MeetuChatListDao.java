package com.meetu.tags.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.meetu.tags.domain.MeetuChatList;

public interface MeetuChatListDao {

	public void insertOper(MeetuChatList chatList);

	public List<Map<String, Object>> selectChatList(String userid);

	public String checkIsGrabbed(String chatlist_id);

	public Integer selectFounderCode(String id);

	public void updateGrabInfo(MeetuChatList chatlist);

	public String selectFounderId(String id);
	
	public Date selectFoundDateById(String id);

	public MeetuChatList selectChatListById(String id);

	public Date selectChatDate(@Param("from_user_id") String userid1, @Param("to_user_id") String userid2);

	public String selectUngrabbedChatByUserId(@Param("from_user_id") String userid);
	
	public void updateChatListById(MeetuChatList chatlist);

	public List<Map<String, Object>> selectLatestChatByuserId(String from_user_id);
	
	public void updateState(String id);
	
	public Date checkSendBiu(String from_user_id);
}
