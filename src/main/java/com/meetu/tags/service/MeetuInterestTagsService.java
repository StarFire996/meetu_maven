package com.meetu.tags.service;

import java.util.List;
import java.util.Map;

import com.meetu.tags.domain.MeetuInterestTags;

public interface MeetuInterestTagsService {

	public void insert(MeetuInterestTags interestTags) throws Exception;

	public List<Map<String, Object>> selectAll() throws Exception;

	public List<Map<String, Object>> selectTags(String[] array) throws Exception;
}
