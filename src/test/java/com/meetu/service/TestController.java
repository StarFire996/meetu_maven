package com.meetu.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meetu.util.ApiService;

public class TestController {


	@Test
	public void run() {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("");
		ApiService apiService = context.getBean(ApiService.class);
		
		String url = "http://101.200.150.226:8080/meetu_push/api/push/pushMsgToDevices";
		Map<String, Object> map = new HashMap<String, Object>();
		String conditions = "{\"channelId\":\"3688275956332016984\",\"_dt\":\"3\",\"appStatus\":\"0\",\"mess\":\"1\",\"sound\":\"1\"}";
		String message = "{\"icon_thumbnailUrl\":\"111\",\"nickname\":\"小星\",\"sex\":\"2\",\"age\":\"18\",\"starsign\":\"天蝎座\",\"isgraduated\":\"1\",\"school\":\"110000_110114\",\"company\":\"1\",\"career\":\"1\",\"user_code\":\"12880\",\"time\":\"1461897887900\",\"chat_id\":\"\",\"reference_id\":\"\",,\"messageType\":\"0\"}";

		map.put("conditions", conditions);
		map.put("message", message);
		
		apiService.doPost(url, map);
	}

}
