package com.meetu.photos.dao;

import java.util.List;
import java.util.Map;

import com.meetu.photos.domain.MeetuPhotos;

public interface MeetuPhotosDao {

	public void insertOper(MeetuPhotos photos);

	public void deleteById(String photoId);
	public List<Map<String, Object>> selectByUserCode(Integer code);
}
