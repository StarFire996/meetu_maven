package com.meetu.device.service;

import com.meetu.device.domain.UserDevice;

public interface UserDeviceService {

	public UserDevice selectById(String Id) throws Exception;
	public void insert(UserDevice userDevice) throws Exception;
	public void deleteByPrimaryKey(String userDeviceId) throws Exception;
}
