package com.meetu.photos.domain;

import com.meetu.core.db.Table;


@Table(name = "SYS_SCHOOLS", pk = "school_id")
public class Schools implements java.io.Serializable{

	private static final long serialVersionUID = -1438310122404399767L;

	private String school_id;
	private String school_name;
	private String province_id;
	public String getSchool_id() {
		return school_id;
	}
	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}
	public String getSchool_name() {
		return school_name;
	}
	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}
	public String getProvince_id() {
		return province_id;
	}
	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}
	
}
