package com.meetu.tags.dao;

import java.util.List;
import java.util.Map;

import com.meetu.tags.domain.MeetuChatTags;

public interface MeetuChatTagsDao {

	public void insertOper(MeetuChatTags chatTags);

	public List<Map<String, Object>> selectAll();

}
