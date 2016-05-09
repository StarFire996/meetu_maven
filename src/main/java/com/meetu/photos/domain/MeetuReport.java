package com.meetu.photos.domain;

import java.util.Date;

public class MeetuReport implements java.io.Serializable{

	private static final long serialVersionUID = 8852137352688688452L;

	private String id;
	private String from_user_id;
	private String to_user_id;
	private Date date;
	private String reason;
	private Integer ischecked;
	private Date check_date;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFrom_user_id() {
		return from_user_id;
	}
	public void setFrom_user_id(String from_user_id) {
		this.from_user_id = from_user_id;
	}
	public String getTo_user_id() {
		return to_user_id;
	}
	public void setTo_user_id(String to_user_id) {
		this.to_user_id = to_user_id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getIschecked() {
		return ischecked;
	}
	public void setIschecked(Integer ischecked) {
		this.ischecked = ischecked;
	}
	public Date getCheck_date() {
		return check_date;
	}
	public void setCheck_date(Date check_date) {
		this.check_date = check_date;
	}
	
}
