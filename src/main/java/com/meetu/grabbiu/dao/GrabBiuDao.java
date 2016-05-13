package com.meetu.grabbiu.dao;

import java.util.HashMap;
import java.util.List;

import com.meetu.grabbiu.domain.GrabBiu;



public interface GrabBiuDao {
	
	public List<GrabBiu> selectGrabBiuListByBiuId(String iu_biu_id);
	
	public void insertOper(GrabBiu grabBiu);
	
	public void updateGrabBiuStatus(HashMap<String, Object> map);
 
}
