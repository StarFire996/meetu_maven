package com.meetu.tags.domain;

import java.util.Date;

public class MeetuUserSettings implements java.io.Serializable {

	private static final long serialVersionUID = 3445083957849036448L;

	private String id;
	private String user_id;
	private String sex;
	private String city;
	private Integer age_down;
	private Integer age_up;
	private String personalized_tags;
	private Date update_date;
	private Integer message;
	private Integer sound;
	private Integer vibration;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getAge_down() {
		return age_down;
	}
	public void setAge_down(Integer age_down) {
		this.age_down = age_down;
	}
	public Integer getAge_up() {
		return age_up;
	}
	public void setAge_up(Integer age_up) {
		this.age_up = age_up;
	}
	public String getPersonalized_tags() {
		return personalized_tags;
	}
	public void setPersonalized_tags(String personalized_tags) {
		this.personalized_tags = personalized_tags;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getMessage() {
		return message;
	}
	public void setMessage(Integer message) {
		this.message = message;
	}
	public Integer getSound() {
		return sound;
	}
	public void setSound(Integer sound) {
		this.sound = sound;
	}
	public Integer getVibration() {
		return vibration;
	}
	public void setVibration(Integer vibration) {
		this.vibration = vibration;
	}
	
}
