package com.meetu.photos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.photos.dao.MeetuReportDao;
import com.meetu.photos.domain.MeetuReport;

@Service
public class MeetuReportServiceImpl implements MeetuReportService {

	@Autowired
	private MeetuReportDao reportDao;
	
	@Override
	public void insertOper(MeetuReport report) throws Exception {
		// TODO Auto-generated method stub
		reportDao.insertOper(report);
	}

	@Override
	public void deleteById(String id) throws Exception {
		// TODO Auto-generated method stub
		reportDao.deleteById(id);
	}

}
