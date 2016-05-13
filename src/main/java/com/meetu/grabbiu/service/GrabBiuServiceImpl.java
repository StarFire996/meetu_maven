package com.meetu.grabbiu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.grabbiu.dao.GrabBiuDao;

@Service
public class GrabBiuServiceImpl implements GrabBiuService{
	
	@Autowired
	private GrabBiuDao grabBiuDao;
}
