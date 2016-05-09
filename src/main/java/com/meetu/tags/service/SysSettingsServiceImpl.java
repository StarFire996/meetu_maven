package com.meetu.tags.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.tags.dao.SysSettingsDao;
import com.meetu.tags.domain.SysSettings;

@Service
public class SysSettingsServiceImpl implements SysSettingsService {

	@Autowired
	private SysSettingsDao sysSettingsDao;
	
	@Override
	public void insertOper(SysSettings settings) throws Exception {
		sysSettingsDao.insertOper(settings);
	}

	@Override
	public SysSettings selectDataByKey(String key) throws Exception {
		return sysSettingsDao.selectDataByKey(key);
	}

}
