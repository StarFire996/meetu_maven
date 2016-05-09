package com.meetu.photos.service;

import com.meetu.photos.domain.MeetuTradingRecord;

public interface MeetuTradingRecordService {

	public void insertOper(MeetuTradingRecord tradingRecord) throws Exception;
	public void updateResult(String bill_no, Integer result) throws Exception;
	public Integer selectTradeResult(String bill_no, String userid);
	public Integer selectTotalFee(String out_trade_no);
	public MeetuTradingRecord selectAll(String out_trade_no);
}
