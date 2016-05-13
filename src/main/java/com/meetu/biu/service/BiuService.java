package com.meetu.biu.service;

import com.meetu.biu.domain.Biu;

public interface BiuService {

	public Biu selectBiuById(String userid);
	public void insertOper(Biu biu);
	public void updateBiu(Biu biu);

}
