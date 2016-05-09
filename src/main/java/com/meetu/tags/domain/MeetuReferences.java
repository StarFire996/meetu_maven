package com.meetu.tags.domain;

import java.util.Date;

public class MeetuReferences implements java.io.Serializable{

	private static final long serialVersionUID = -2651366809624562434L;
	private String id;
	private String user_id1;
	private String user_id2;
	private Integer user_code2;
	private String chat_id;
	private Integer state;
	private Date date;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id1() {
		return user_id1;
	}
	public void setUser_id1(String user_id1) {
		this.user_id1 = user_id1;
	}
	public String getUser_id2() {
		return user_id2;
	}
	public void setUser_id2(String user_id2) {
		this.user_id2 = user_id2;
	}
	public Integer getUser_code2() {
		return user_code2;
	}
	public void setUser_code2(Integer user_code2) {
		this.user_code2 = user_code2;
	}
	public String getChat_id() {
		return chat_id;
	}
	public void setChat_id(String chat_id) {
		this.chat_id = chat_id;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
