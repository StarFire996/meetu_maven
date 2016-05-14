package com.meetu.grabbiu.service;

import java.util.List;

import com.meetu.grabbiu.domain.GrabBiu;

public interface GrabBiuService {

	public List<GrabBiu> selectGrabBiuListByBiuId(String iu_biu_id);

	public void insertOper(GrabBiu grabBiu);

	public void updateGrabBiuStatus(GrabBiu grabBiu);
	
	public GrabBiu selectGrabBiuByBiuIdAndUserCode(String iu_biu_id,Integer user_code);

}
