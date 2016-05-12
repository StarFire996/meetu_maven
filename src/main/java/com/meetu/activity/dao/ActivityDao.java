package com.meetu.activity.dao;

import java.util.Map;

import com.meetu.activity.domain.Activity;

public interface ActivityDao {

	public Map<String,Object> selectActivity();
	public void insertActivity(Activity activity);

}
