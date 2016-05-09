package com.meetu.photos.dao;

import org.apache.ibatis.annotations.Param;

import com.meetu.photos.domain.MeetuTradingRecord;

public interface MeetuTradingRecordDao {

	public void insertOper(MeetuTradingRecord tradingRecord);
	public void updateResult(@Param("bill_no") String bill_no, 
			@Param("result") Integer result);
	public Integer selectTradeResult(@Param("bill_no") String bill_no, @Param("user_id") String user_id);
	public Integer selectTotalFee(@Param("bill_no") String bill_no);
	public MeetuTradingRecord selectAll(@Param("bill_no") String out_trade_no);
}
