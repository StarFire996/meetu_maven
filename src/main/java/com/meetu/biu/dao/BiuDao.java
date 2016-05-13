package com.meetu.biu.dao;

import com.meetu.biu.domain.Biu;


public interface BiuDao {
	
	public Biu selectBiuById(String id);
	
	public void insertOper(Biu biu);
	
	public void updateBiu(Biu biu);

}
