package com.meetu.activity.domain;

import java.util.Date;

public class Contents {

	
	private String id;
	private String body;
	private Date create_date;
	public Contents(String id, String body, Date create_date) {
		super();
		this.id = id;
		this.body = body;
		this.create_date = create_date;
	}
	public Contents() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Contents [id=" + id + ", body=" + body + ", create_date="
				+ create_date + "]";
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
	
}
