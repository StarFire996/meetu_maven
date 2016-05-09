package com.meetu.photos.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.photos.dao.MeetuPhotosDao;
import com.meetu.photos.domain.MeetuPhotos;

@Service
public class MeetuPhotosServiceImpl implements MeetuPhotosService {

	@Autowired
	private MeetuPhotosDao meetuPhotosDao;
	
	@Override
	public void insert(MeetuPhotos meetuPhotos) throws Exception {
		meetuPhotosDao.insertOper(meetuPhotos);
	}

	@Override
	public void deleteById(String photoId) throws Exception {
		meetuPhotosDao.deleteById(photoId);
	}

	@Override
	public List<Map<String, Object>> selectByUserCode(Integer code)
			throws Exception {
		// TODO Auto-generated method stub
		return meetuPhotosDao.selectByUserCode(code);
	}

}
