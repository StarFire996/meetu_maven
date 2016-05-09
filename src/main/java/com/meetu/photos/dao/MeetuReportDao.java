package com.meetu.photos.dao;

import com.meetu.photos.domain.MeetuReport;

public interface MeetuReportDao {

	public void insertOper(MeetuReport report);
	public void deleteById(String id);
}
