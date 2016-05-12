package com.meetu.activity.domain;

import java.util.Date;


public class Activity {

	
	private String id;
	private String body;
	private Date create_date;
	private Long update_date;
	public Activity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Activity(String id, String body, Date create_date, Long update_date) {
		super();
		this.id = id;
		this.body = body;
		this.create_date = create_date;
		this.update_date = update_date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Long getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Long update_date) {
		this.update_date = update_date;
	}
	@Override
	public String toString() {
		return "Activity [id=" + id + ", body=" + body + ", create_date="
				+ create_date + ", update_date=" + update_date + "]";
	}
	
}
