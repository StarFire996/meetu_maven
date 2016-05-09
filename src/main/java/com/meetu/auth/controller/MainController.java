package com.meetu.auth.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetu.auth.service.UserAuthorityService;
import com.meetu.core.base.BaseController;
import com.meetu.domain.User;
import com.meetu.photos.service.SysMenusService;
import com.meetu.service.UserService;

/**
 * 首页控制器
 * 
 * @author liuzengming
 */
@Controller
public class MainController extends BaseController{
	
	@Autowired
	private UserAuthorityService userAuthorityService;
	@Autowired
	private UserService userService;
	@Autowired
	private SysMenusService sysMenus;
	
	/**
	 * 登录页面
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login")
	public String login(HttpServletResponse response, ModelMap model){
		
		
		String root = this.getCdnURL(request);
		request.setAttribute("command", root + "/main");
		
		request.setAttribute("root", root);
		response.setDateHeader("expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("pragma", "no-cache");

		return "login";
	}
	
	/**
	 * 登录方法
	 * @throws Exception 
	 */
	@RequestMapping(value = "/main", method = RequestMethod.POST)
	public String main(HttpServletResponse response, ModelMap model) throws Exception { 
		String loginName = request.getParameter("loginName");
		String password = request.getParameter("password");
		
		String root = this.getCdnURL(request);
		request.setAttribute("root", root);
		
		JSONObject ret = userAuthorityService.userLogin(loginName, password);
	
		if(ret.getString("code").equals("300")){
			this.setAttr("page", "login");
			this.setAttr("message", ret.get("error"));
			this.setAttr("loginfailed", "loginfailed");
			
			return login(response, model);
		}else{
			List<Map<String, Object>> menus = sysMenus.findAll("select * from sys_menus");
			if(menus!=null&&menus.size()>0){
				List<Map<String, Object>> heads = fechMenu(menus, "1");
				this.setAttr("navMenus", heads);
				this.setAttr("navLen", heads.size());
				this.setAttr("menuChild", fechMenu(menus, "2"));
			}
		}
		
		request.setAttribute("loginName", loginName);
		
		return "default"; 
	}
	
	/**
	 * 登出
	 * @throws IOException 
	 */
	@RequestMapping(value = "/logout")  
	@ResponseBody
	public String appLoginOut(HttpServletResponse response, ModelMap model) throws IOException{
		String root = this.getCdnURL(request);
		request.setAttribute("root", root);
		this.setAttr("page", "login");

		response.sendRedirect("login.html");
		
		return login(response, model);
	}

	
	
	/**
	 * 个人工作首页
	 */
	@RequestMapping(value = "/main/index", method = RequestMethod.GET)
	@Override
	public String index(HttpServletResponse response, ModelMap model) {
		return "index";
	}

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
	
	/**
	 * 查找一级菜单
	 * 
	 * @param menus
	 * @return
	 */
	private List<Map<String, Object>> fechMenu(List<Map<String, Object>> menus, String level) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int size = menus.size();
		for (int i = 0; i < size; i++) {
			Map<String, Object> menu = menus.get(i);
			if(menu.get("LEVEL").equals(level)){
				list.add(menu);
			}
		}
		return list;
	}

}
