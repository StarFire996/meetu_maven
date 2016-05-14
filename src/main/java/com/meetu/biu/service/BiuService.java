package com.meetu.biu.service;

import com.meetu.biu.domain.Biu;

public interface BiuService {

	public Biu selectBiuById(String biuId);
	
	public Biu selectBiuByUserId(String userId);
	
	public void insertOper(Biu biu);
	
	public void updateBiu(Biu biu);
	
}
