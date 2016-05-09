package com.meetu.tags.dao;

import java.util.List;
import java.util.Map;

import com.meetu.tags.domain.MeetuInterestTags;

public interface MeetuInterestTagsDao {

	public void insertOper(MeetuInterestTags interestTags);

	public List<Map<String, Object>> selectAll();

	public List<Map<String, Object>> selectTags(String[] array);
}
