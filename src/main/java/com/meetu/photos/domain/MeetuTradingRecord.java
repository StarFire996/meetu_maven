package com.meetu.photos.domain;

import java.util.Date;

public class MeetuTradingRecord implements java.io.Serializable{

	private static final long serialVersionUID = -7240219226993142937L;

	private String id;
	private String user_id;
	private String bill_no;
	private String bill_type;
	private String title;
	private String channel;
	private Integer totalnum;
	private Integer totalfee;
	private Integer result;
	private Integer refund_result;
	private Integer revert_result;
	private Date date;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getBill_no() {
		return bill_no;
	}
	public void setBill_no(String bill_no) {
		this.bill_no = bill_no;
	}
	public String getBill_type() {
		return bill_type;
	}
	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Integer getTotalfee() {
		return totalfee;
	}
	public void setTotalfee(Integer totalfee) {
		this.totalfee = totalfee;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public Integer getRefund_result() {
		return refund_result;
	}
	public void setRefund_result(Integer refund_result) {
		this.refund_result = refund_result;
	}
	public Integer getRevert_result() {
		return revert_result;
	}
	public void setRevert_result(Integer revert_result) {
		this.revert_result = revert_result;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(Integer totalnum) {
		this.totalnum = totalnum;
	}
	
}
