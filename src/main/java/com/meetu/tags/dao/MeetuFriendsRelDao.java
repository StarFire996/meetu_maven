package com.meetu.tags.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.meetu.tags.domain.MeetuFriendsRel;

public interface MeetuFriendsRelDao {

	public void insertOper(MeetuFriendsRel friendsRel);

	public MeetuFriendsRel isFriends(Map<String, Object> ids);
	
	public void deleteByUserIds(@Param("user_id1") String user_id1, @Param("user_id2") String user_id2);

	public List<Map<String, Object>> selectFriendsListByUserId(@Param("userid") String userid);
}
