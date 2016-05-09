package com.meetu.tags.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.tags.dao.MeetuUserSettingsDao;
import com.meetu.tags.domain.MeetuUserSettings;

@Service
public class MeetuUserSettingsServiceImpl implements MeetuUserSettingsService {

	@Autowired
	private MeetuUserSettingsDao settingsDao;
	
	@Override
	public void insertOper(MeetuUserSettings settings) throws Exception {
		settingsDao.insertOper(settings);
	}

	@Override
	public MeetuUserSettings selectByUserId(String user_id){
		return settingsDao.selectByUserId(user_id);
	}

	@Override
	public void updateSettingsByUserId(MeetuUserSettings settings) throws Exception {
		// TODO Auto-generated method stub
		settingsDao.updateSettingsByUserId(settings);
	}

	@Override
	public void deleteByPrimaryKey(String settingsId) {
		// TODO Auto-generated method stub
		settingsDao.deleteByPrimaryKey(settingsId);
	}

	@Override
	public String selectSexByUserId(String userid) {
		// TODO Auto-generated method stub
		return settingsDao.selectSexByUserId(userid);
	}

}
