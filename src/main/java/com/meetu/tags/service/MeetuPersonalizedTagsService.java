package com.meetu.tags.service;

import java.util.List;
import java.util.Map;

import com.meetu.tags.domain.MeetuPersonalizedTags;

public interface MeetuPersonalizedTagsService {
	
	public void insert(MeetuPersonalizedTags personalizedTags) throws Exception;

	public List<Map<String, Object>> selectAll(String sex) throws Exception;

	public List<Map<String, Object>> selectTags(String[] array) throws Exception;

}
