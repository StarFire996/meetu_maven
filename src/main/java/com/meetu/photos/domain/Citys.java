package com.meetu.photos.domain;

import com.meetu.core.db.Table;


@Table(name = "SYS_CITYS", pk = "id")
public class Citys implements java.io.Serializable{

	private static final long serialVersionUID = -5370694141287861136L;

	private String id;
	private String province;
	private String province_num;
	private String city;
	private String city_num;
	private String town;
	private String town_num;
	private String city_town;
	private String city_town_num;
	
	public String getCity_town() {
		return city_town;
	}
	public void setCity_town(String city_town) {
		this.city_town = city_town;
	}
	public String getCity_town_num() {
		return city_town_num;
	}
	public void setCity_town_num(String city_town_num) {
		this.city_town_num = city_town_num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvince_num() {
		return province_num;
	}
	public void setProvince_num(String province_num) {
		this.province_num = province_num;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCity_num() {
		return city_num;
	}
	public void setCity_num(String city_num) {
		this.city_num = city_num;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getTown_num() {
		return town_num;
	}
	public void setTown_num(String town_num) {
		this.town_num = town_num;
	}
	
}
