package com.meetu.tags.domain;

import java.util.Date;

public class MeetuNoLongerMatch implements java.io.Serializable{

	private static final long serialVersionUID = 4826784108239762910L;
	
	private String id;
	private String from_user_id;
	private String to_user_id;
	private Date date;
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
}
