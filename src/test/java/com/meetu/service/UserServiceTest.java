package com.meetu.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.meetu.baseTest.SpringTestCase;
import com.meetu.domain.User;

public class UserServiceTest extends SpringTestCase {

	@Autowired
	private UserService userService;
	
	@Test
	public void selectUserByIdTest() throws Exception{
		
		User user = userService.selectUserById("1");
		System.out.println(user.getName()+":"+user.getPassword());
	}
}
