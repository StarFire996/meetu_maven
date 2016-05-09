package com.meetu.photos.service;

import java.util.List;
import java.util.Map;

import com.meetu.photos.domain.MeetuPhotos;


public interface MeetuPhotosService {

	public void insert(MeetuPhotos meetuPhotos) throws Exception;

	public void deleteById(String photoId) throws Exception;
	
	public List<Map<String, Object>> selectByUserCode(Integer code) throws Exception;
}

