package com.meetu.device.dao;

import com.meetu.device.domain.UserDevice;

public interface UserDeviceDao {
	
	public UserDevice selectUserById(String userId);
	public void insertOper(UserDevice userDevice);
	public void deleteByPrimaryKey(String id);
	
}
