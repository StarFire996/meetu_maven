package com.meetu.tags.domain;

import java.util.Date;

public class MeetuInterestTags implements java.io.Serializable{
	
	private static final long serialVersionUID = -1552689550241764199L;
	private String id;
	private Integer code;
	private String name;
	private String type;
	private Date create_date;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
}
