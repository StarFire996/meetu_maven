package com.meetu.grabbiu.domain;



public class GrabBiu {

	
	private String id;
	private String iu_biu_id;
	private Integer user_code;
	private String icon_thumbnailUrl;
	private String name;
	private String sex;
	private String school;
	private String starsign;
	private Integer status;
	private Integer age;
	public GrabBiu() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GrabBiu(String id, String iu_biu_id, Integer user_code,
			String icon_thumbnailUrl, String name, String sex, String school,
			String starsign, Integer status, Integer age) {
		super();
		this.id = id;
		this.iu_biu_id = iu_biu_id;
		this.user_code = user_code;
		this.icon_thumbnailUrl = icon_thumbnailUrl;
		this.name = name;
		this.sex = sex;
		this.school = school;
		this.starsign = starsign;
		this.status = status;
		this.age = age;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIu_biu_id() {
		return iu_biu_id;
	}
	public void setIu_biu_id(String iu_biu_id) {
		this.iu_biu_id = iu_biu_id;
	}
	public Integer getUser_code() {
		return user_code;
	}
	public void setUser_code(Integer user_code) {
		this.user_code = user_code;
	}
	public String getIcon_thumbnailUrl() {
		return icon_thumbnailUrl;
	}
	public void setIcon_thumbnailUrl(String icon_thumbnailUrl) {
		this.icon_thumbnailUrl = icon_thumbnailUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getStarsign() {
		return starsign;
	}
	public void setStarsign(String starsign) {
		this.starsign = starsign;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "GrabBiu [id=" + id + ", iu_biu_id=" + iu_biu_id
				+ ", user_code=" + user_code + ", icon_thumbnailUrl="
				+ icon_thumbnailUrl + ", name=" + name + ", sex=" + sex
				+ ", school=" + school + ", starsign=" + starsign + ", status="
				+ status + ", age=" + age + "]";
	}
	
	
	
}
