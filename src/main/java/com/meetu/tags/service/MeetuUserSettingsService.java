package com.meetu.tags.service;

import com.meetu.tags.domain.MeetuUserSettings;

public interface MeetuUserSettingsService {

	public void insertOper(MeetuUserSettings settings) throws Exception;

	public MeetuUserSettings selectByUserId(String user_id);
	
	public void updateSettingsByUserId(MeetuUserSettings settings) throws Exception;

	public void deleteByPrimaryKey(String settingsId);

	public String selectSexByUserId(String userid);
}
