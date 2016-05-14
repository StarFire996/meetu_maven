package com.meetu.grabbiu.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.grabbiu.dao.GrabBiuDao;
import com.meetu.grabbiu.domain.GrabBiu;

@Service
public class GrabBiuServiceImpl implements GrabBiuService{
	
	@Autowired
	private GrabBiuDao grabBiuDao;

	@Override
	public List<GrabBiu> selectGrabBiuListByBiuId(String iu_biu_id) {
		return this.grabBiuDao.selectGrabBiuListByBiuId(iu_biu_id);
	}

	@Override
	public void insertOper(GrabBiu grabBiu) {
		this.grabBiuDao.insertOper(grabBiu);
	}

	@Override
	public void updateGrabBiuStatus(GrabBiu grabBiu) {
		this.grabBiuDao.updateGrabBiuStatus(grabBiu);
	}

	public GrabBiu selectGrabBiuByBiuIdAndUserCode(String iu_biu_id,
			Integer user_code) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("iu_biu_id", iu_biu_id);
		map.put("user_code", user_code);
		return null;
	}
}
