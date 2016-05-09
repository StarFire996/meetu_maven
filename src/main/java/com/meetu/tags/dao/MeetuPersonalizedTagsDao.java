package com.meetu.tags.dao;

import java.util.List;
import java.util.Map;

import com.meetu.tags.domain.MeetuPersonalizedTags;

public interface MeetuPersonalizedTagsDao {

	public void insertOper(MeetuPersonalizedTags personalizedTags);
	public List<Map<String, Object>> selectAll(String sex);
	public List<Map<String, Object>> selectTags(String[] intArray);
}
