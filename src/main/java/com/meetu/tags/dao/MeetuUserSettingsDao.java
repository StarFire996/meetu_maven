package com.meetu.tags.dao;

import com.meetu.tags.domain.MeetuUserSettings;

public interface MeetuUserSettingsDao {

	public void insertOper(MeetuUserSettings settings);
	public MeetuUserSettings selectByUserId(String user_id);
	public void updateSettingsByUserId(MeetuUserSettings settings);
	public void deleteByPrimaryKey(String settingsId);
	public String selectSexByUserId(String userid);

}
