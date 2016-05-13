package com.meetu.biu.domain;

import java.util.Date;


public class Biu {

	
	private String id;
	private String created_by;
	private String chat_tags;
	private Date last_date;
	private Integer status;
	private Integer agree_biu;
	private Integer accept_num;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getChat_tags() {
		return chat_tags;
	}
	public void setChat_tags(String chat_tags) {
		this.chat_tags = chat_tags;
	}
	public Date getLast_date() {
		return last_date;
	}
	public void setLast_date(Date last_date) {
		this.last_date = last_date;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getAgree_biu() {
		return agree_biu;
	}
	public void setAgree_biu(Integer agree_biu) {
		this.agree_biu = agree_biu;
	}
	public Integer getAccept_num() {
		return accept_num;
	}
	public void setAccept_num(Integer accept_num) {
		this.accept_num = accept_num;
	}
	public Biu() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Biu(String id, String created_by, String chat_tags, Date last_date,
			Integer status, Integer agree_biu, Integer accept_num) {
		super();
		this.id = id;
		this.created_by = created_by;
		this.chat_tags = chat_tags;
		this.last_date = last_date;
		this.status = status;
		this.agree_biu = agree_biu;
		this.accept_num = accept_num;
	}
	@Override
	public String toString() {
		return "Biu [id=" + id + ", created_by=" + created_by + ", chat_tags="
				+ chat_tags + ", last_date=" + last_date + ", status=" + status
				+ ", agree_biu=" + agree_biu + ", accept_num=" + accept_num
				+ "]";
	}
	
	
}
