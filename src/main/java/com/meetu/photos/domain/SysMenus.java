package com.meetu.photos.domain;

import com.meetu.core.db.Table;


@Table(name = "SYS_MENUS", pk = "id")
public class SysMenus implements java.io.Serializable{

	private static final long serialVersionUID = 3089894085481295951L;
	
	private String id;
	private String level;
	private String name;
	private String path;
	private String icon;
	private String parent_id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	
}
