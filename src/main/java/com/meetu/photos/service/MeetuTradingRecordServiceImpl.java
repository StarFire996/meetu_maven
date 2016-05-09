package com.meetu.photos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meetu.photos.dao.MeetuTradingRecordDao;
import com.meetu.photos.domain.MeetuTradingRecord;

@Service("tradingRecordService")
public class MeetuTradingRecordServiceImpl implements MeetuTradingRecordService {

	@Autowired
	private MeetuTradingRecordDao tradingRecordDao;
	@Override
	public void insertOper(MeetuTradingRecord tradingRecord) throws Exception{
		// TODO Auto-generated method stub
		tradingRecordDao.insertOper(tradingRecord);
	}

	@Override
	public void updateResult(String bill_no, Integer result) throws Exception{
		// TODO Auto-generated method stub
		tradingRecordDao.updateResult(bill_no, result);

	}

	@Override
	public Integer selectTradeResult(String bill_no, String userid) {
		// TODO Auto-generated method stub
		return tradingRecordDao.selectTradeResult(bill_no, userid);
	}

	@Override
	public Integer selectTotalFee(String out_trade_no) {
		// TODO Auto-generated method stub
		return tradingRecordDao.selectTotalFee(out_trade_no);
	}

	@Override
	public MeetuTradingRecord selectAll(String out_trade_no) {
		// TODO Auto-generated method stub
		return tradingRecordDao.selectAll(out_trade_no);
	}

}
