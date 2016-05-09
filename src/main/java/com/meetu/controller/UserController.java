package com.meetu.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import com.meetu.auth.service.UserAuthorityService;
import com.meetu.core.base.BaseController;
import com.meetu.service.UserService;

@Controller
public class UserController extends BaseController{

	@Resource
	private UserService userService;
	@Autowired
	private UserAuthorityService userAuthorityService;
	
	@Override
	public void save(HttpServletResponse paramHttpServletResponse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void del(HttpServletResponse paramHttpServletResponse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(HttpServletResponse paramHttpServletResponse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String add(HttpServletResponse paramHttpServletResponse) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String show(HttpServletResponse paramHttpServletResponse) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void query(Integer paramInteger1, Integer paramInteger2,
			HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse, ModelMap paramModelMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String index(HttpServletResponse paramHttpServletResponse,
			ModelMap paramModelMap) {
		// TODO Auto-generated method stub
		return null;
	}
}
