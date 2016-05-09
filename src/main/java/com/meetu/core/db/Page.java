package com.meetu.core.db;

import java.io.Serializable;
import java.util.List;

public class Page <T> implements Serializable{

	private static final long serialVersionUID = 8523638928644402324L;
	private List<T> listT;
	private int Object;
	private int p2;
	private int p3;
	private long o00000;

	public Page(List<T> paramList, int paramInt1, int paramInt2, int paramInt3, long paramLong)
	{
		listT = paramList;
		Object = paramInt1;
	    p2 = paramInt2;
	    p3 = paramInt3;
	    o00000 = paramLong;
	}
	public List<T> getList(){
		return listT;
	}
	public int getPageNumber(){
		return Object;
	}
	public int getPageSize(){
		return p2;
	}
	public int getTotalPage(){
		return p3;
	}
	public long getTotalRow(){
		return o00000;
	}
}
