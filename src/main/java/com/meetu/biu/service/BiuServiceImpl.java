package com.meetu.biu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.biu.dao.BiuDao;
import com.meetu.biu.domain.Biu;

@Service
public class BiuServiceImpl implements BiuService{
	
	@Autowired
	private BiuDao biuDao;

	@Override
	public Biu selectBiuById(String userid) {
		return this.biuDao.selectBiuById(userid);
	}

	@Override
	public void insertOper(Biu biu) {
		this.insertOper(biu);
	}

	@Override
	public void updateBiu(Biu biu) {
		this.updateBiu(biu);
	}

	
	
}
