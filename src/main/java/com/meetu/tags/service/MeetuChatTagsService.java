package com.meetu.tags.service;

import java.util.List;
import java.util.Map;

import com.meetu.tags.domain.MeetuChatTags;

public interface MeetuChatTagsService {

	public List<Map<String, Object>> selectAll() throws Exception;
	public void insert(MeetuChatTags chatTags) throws Exception;
}
