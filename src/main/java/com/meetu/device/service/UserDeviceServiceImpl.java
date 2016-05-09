package com.meetu.device.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.device.dao.UserDeviceDao;
import com.meetu.device.domain.UserDevice;

@Service
public class UserDeviceServiceImpl implements UserDeviceService {

	@Autowired
	private UserDeviceDao userDeviceDao;
	
	
	@Override
	public UserDevice selectById(String Id) throws Exception {
		
		return userDeviceDao.selectUserById(Id);
	}

	@Override
	public void insert(UserDevice userDevice) throws Exception {
		
		userDeviceDao.insertOper(userDevice);
	}

	@Override
	public void deleteByPrimaryKey(String userDeviceId) throws Exception {
		// TODO Auto-generated method stub
		userDeviceDao.deleteByPrimaryKey(userDeviceId);
	}

}
