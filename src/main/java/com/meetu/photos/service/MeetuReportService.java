package com.meetu.photos.service;

import com.meetu.photos.domain.MeetuReport;

public interface MeetuReportService {

	public void insertOper(MeetuReport report) throws Exception;
	public void deleteById(String id) throws Exception;
}
