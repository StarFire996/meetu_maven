package com.meetu.activity.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.meetu.activity.dao.ActivityDao;
import com.meetu.activity.dao.ContentsDao;
import com.meetu.activity.domain.Activity;
import com.meetu.activity.domain.Contents;
import com.meetu.core.base.BaseController;

@Controller
@RequestMapping("activity")
public class ActivityController extends BaseController {

	@Autowired
	private ActivityDao activityDao;
	
	@Autowired
	private ContentsDao contentsDao;

	/**
	 * 设置活动内容
	 * 
	 * @param body
	 * @param update_date
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "setActivity", method = RequestMethod.POST)
	public ResponseEntity<Void> setActivity(
			@RequestParam("activity") String body,
			@RequestParam("update_date") String update_date)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

		Date date = sdf.parse(update_date);

		Activity activity = new Activity();
		activity.setId("1");
		activity.setBody(body);
		activity.setUpdate_date(date.getTime());
		activity.setCreate_date(new Date());

		activityDao.insertActivity(activity);

		return ResponseEntity.ok().build();
	}

	/**
	 * 获取活动内容
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getActivity", method = RequestMethod.POST)
	@ResponseBody
	public void getActivity(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		JSONObject json2 = new JSONObject();
		try {
			String newToken = (String) request.getAttribute("token");

			Map<String, Object> map = activityDao.selectActivity();
			JSONObject jsonDate = (JSONObject) JSONObject.parse(map.get("body")
					.toString());
			jsonDate.put("updateAt", map.get("update_date"));

			json2.put("activity", jsonDate);
			json2.put("token", newToken);

			json.put("data", json2);
			json.put("state", "200");

		} catch (Exception e) {
			json.put("state", "300");
			json.put("error", e.getMessage() + " 请联系管理员！");
			e.printStackTrace();
		}
		this.renderJson(response, json.toString());
	}
	
	/**
	 * 设置内容
	 * @param body
	 * @return
	 */
	@RequestMapping(value = "setContents", method = RequestMethod.POST)
	public ResponseEntity<Void> setActivity(@RequestParam("body") String body) {
		Contents contents = new Contents("2", body, new Date());
		this.contentsDao.insertContents(contents);
		return ResponseEntity.ok().build();
	}
	
	/**
	 * 获取活动内容
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getContents", method = RequestMethod.POST)
	@ResponseBody
	public void getContents(HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		JSONObject json2 = new JSONObject();
		try {
			String newToken = (String) request.getAttribute("token");

			String contents = this.contentsDao.selectContents();

			json2.put("contents", contents);
			json2.put("token", newToken);

			json.put("data", json2);
			json.put("state", "200");

		} catch (Exception e) {
			json.put("state", "300");
			json.put("error", e.getMessage() + " 请联系管理员！");
			e.printStackTrace();
		}
		this.renderJson(response, json.toString());
	}


	@Override
	public String index(HttpServletResponse paramHttpServletResponse,
			ModelMap paramModelMap) {
		// TODO Auto-generated method stub
		return null;
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

	}
}
