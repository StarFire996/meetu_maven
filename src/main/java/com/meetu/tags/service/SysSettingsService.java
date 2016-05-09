package com.meetu.tags.service;

import com.meetu.tags.domain.SysSettings;

public interface SysSettingsService {
	public void insertOper(SysSettings settings) throws Exception;
	public SysSettings selectDataByKey(String parameter) throws Exception;
}
