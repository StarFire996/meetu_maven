package com.meetu.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.meetu.domain.User;
import com.meetu.service.UserService;

/**
 * 用户权限服务
 * @author Lzm
 * @date 2016-2-24
 * @version 1.0
 * @description 用户权限服务管理
 */
@Service
public class UserAuthorityService {
	
	@Autowired
	private UserService userService;
	
	
	/**
	 * @throws Exception 
	 * 用户登录
	 * @param loginName
	 * @param password
	 * @return {code:"响应码(成功:200,失败:300)",
	 * 			token:"Token，失败时候可省略!",
	 * 			error:"错误信息，成功时候可省略!"}
	 * @throws  
	 */
	public JSONObject userLogin(String loginName,String password) throws Exception{
			
		JSONObject jb = new JSONObject();
		User user = userService.selectUserByLoginName(loginName);
		if(user == null){//Not Registered
			jb.put("code", "300");
			jb.put("error", "The subscriber don't have permiss !");
			return jb;
		}else if(!user.getPassword().equals(password)){
			jb.put("code", "300");
			jb.put("error", "Password Error !");
			return jb;
		}
		
		jb.put("code", "200");
		return jb;
	};
	

}
