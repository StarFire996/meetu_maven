package com.meetu.tags.domain;

import java.util.Date;

public class MeetuChatList implements java.io.Serializable{
	
	private static final long serialVersionUID = 2889407814313654162L;
	private String id;
	private String hxid;
	private String from_user_id;
	private String to_user_id;
	private String chat_tags;
	private Date start_date;
	private Date agree_date;
	private Integer delete_status;
	private Integer state;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHxid() {
		return hxid;
	}
	public void setHxid(String hxid) {
		this.hxid = hxid;
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
	public String getChat_tags() {
		return chat_tags;
	}
	public void setChat_tags(String chat_tags) {
		this.chat_tags = chat_tags;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getAgree_date() {
		return agree_date;
	}
	public void setAgree_date(Date agree_date) {
		this.agree_date = agree_date;
	}
	public Integer getDelete_status() {
		return delete_status;
	}
	public void setDelete_status(Integer delete_status) {
		this.delete_status = delete_status;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	
}
