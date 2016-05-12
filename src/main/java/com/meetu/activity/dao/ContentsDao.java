package com.meetu.activity.dao;

import com.meetu.activity.domain.Contents;

public interface ContentsDao {

	public String selectContents();
	public void insertContents(Contents contents);

}
