package com.meetu.tags.dao;

import com.meetu.tags.domain.SysSettings;

public interface SysSettingsDao {

	public void insertOper(SysSettings settings);

	public SysSettings selectDataByKey(String parameter);

}
