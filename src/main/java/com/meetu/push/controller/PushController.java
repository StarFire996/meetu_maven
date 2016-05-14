package com.meetu.push.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.meetu.biu.service.BiuService;
import com.meetu.config.Constants;
import com.meetu.core.base.BaseController;
import com.meetu.domain.User;
import com.meetu.grabbiu.domain.GrabBiu;
import com.meetu.grabbiu.service.GrabBiuService;
import com.meetu.push.service.PushService;
import com.meetu.service.MeetuAuthService;
import com.meetu.service.UserService;
import com.meetu.tags.domain.MeetuUserSettings;
import com.meetu.tags.service.MeetuChatListService;
import com.meetu.tags.service.MeetuReferencesService;
import com.meetu.tags.service.MeetuUserSettingsService;
import com.meetu.util.Common;
import com.meetu.util.LoggerUtils;
import com.meetu.util.RedisUtil;
import com.meetu.util.StsService;

@Controller
@RequestMapping(value = "app/push")
public class PushController extends BaseController {

	public static Logger LOGGER = LoggerFactory.getLogger(PushController.class);

	@Autowired
	private MeetuUserSettingsService settingsService;

	@Autowired
	private UserService userService;

	@Autowired
	private MeetuChatListService chatlistService;

	@Autowired
	private PushService pushService;

	@Autowired
	private MeetuReferencesService referencesService;

	@Autowired
	private MeetuAuthService authService;

	@Autowired
	private BiuService biuService;

	@Autowired
	private GrabBiuService grabBiuService;

	/**
	 * 发biu推送
	 * 
	 * @param userId
	 * @param chat_tags
	 * @return
	 */
	@RequestMapping(value = "pushMsgToDevices", method = RequestMethod.POST)
	public ResponseEntity<Void> pushMsgToDevices(
			@RequestParam("userId") String userId,
			@RequestParam("chat_tags") String chat_tags,
			@RequestParam("iu_biu_id") String iu_biu_id) {
		try {
			Long start = System.currentTimeMillis();
			Map<String, Object> debugMap = new HashMap<String, Object>();
			debugMap.put("userId", userId);
			debugMap.put("chat_tags", iu_biu_id);
			debugMap.put("iu_biu_id", iu_biu_id);

			List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();

			MeetuUserSettings settings = settingsService.selectByUserId(userId);// 用户设置

			User user = userService.selectUserById(userId);// 用户信息

			String u_city = user.getCityf();
			Integer s_age_down = settings.getAge_down();
			Integer s_age_up = settings.getAge_up();
			String s_city = settings.getCity();
			String s_sex = settings.getSex();

			Map<String, Object> map = new HashMap<String, Object>();
			if (s_city.equals("1")) { // 同城（0未知,1同城,2不限）
				map.put("city", u_city);
			}
			if (s_sex.equals("1")) { // 性别："0"-未知，"1"-男，"2"-女
				map.put("sex", "1");
			} else if (s_sex.equals("2")) {
				map.put("sex", "2");
			}

			map.put("sex2", user.getSex() == null ? "2" : user.getSex());

			if (s_age_down != s_age_up && s_age_up != 0) { // 年龄范围
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.YEAR, -s_age_down);
				map.put("age_up", cal.getTime());
				cal.add(Calendar.YEAR, s_age_down - s_age_up);
				map.put("age_down", cal.getTime());
			}
			map.put("from_user_id", userId);

			Integer sendBiuNumbers = authService
					.getSettingByKey(Constants.sendBiuNumbers) == null ? Constants.sendBiuNumbers_default
					: authService.getSettingByKey(Constants.sendBiuNumbers);

			map.put("num", sendBiuNumbers);
			// 缓存中取该用户分页查询的最后一位用户,作为这一次查询的起点
			map.put("latestCode",
					RedisUtil.hget(Constants.redis_online,
							Constants.redis_online_latestCode.concat(userId)) == null ? 10000
							: Integer.parseInt(RedisUtil.hget(
									Constants.redis_online,
									Constants.redis_online_latestCode
											.concat(userId))));

			debugMap.put("设置匹配数据时间", System.currentTimeMillis() - start);

			users = userService.selectBiu(map);

			debugMap.put("匹配用户时间1", System.currentTimeMillis() - start);
			int age = Common.getAge(user.getBirth_date());

			int revMatch = Common.revMatch(u_city, age, users);

			debugMap.put("反向匹配人数1", revMatch);
			debugMap.put("反向匹配时间1", System.currentTimeMillis() - start);
			if (users == null) {
				users = new ArrayList<Map<String, Object>>();
			}
			// 使用的滚动查询,如果查询到的数量小于要求的数量,那么就从数据库表头开始查询
			if (users.size() < sendBiuNumbers) {
				map.put("num", sendBiuNumbers - users.size());
				map.put("latestCode", 10000);

				List<Map<String, Object>> users2 = userService.selectBiu(map);
				debugMap.put("匹配用户时间2", System.currentTimeMillis() - start);

				if (users2 != null && users2.size() > 0) {

					int revMatch2 = Common.revMatch(u_city, age, users2);

					debugMap.put("反向匹配人数2", revMatch2);
					debugMap.put("反向匹配时间2", System.currentTimeMillis() - start);
					// 设置lastestCode的值
					users.addAll(users2);
				}
			}

			// 发biu时，若该用户存在未被抢的chatlist则更新该信息,更新话题标签时间

			JSONObject json = new JSONObject();
			JSONObject userInfo = new JSONObject();
			userInfo.put(
					"icon_thumbnailUrl",
					user.getIcon_url() == null ? " " : StsService
							.generateCircleUrl(user.getIcon_url().toString()));
			userInfo.put("nickname", user.getNickname());
			userInfo.put("sex", user.getSex());
			userInfo.put("age", Common.getAgeByBirthday(user.getBirth_date()));
			userInfo.put("starsign",
					user.getStarsign() == null ? " " : user.getStarsign());
			userInfo.put("isgraduated", user.getIsgraduated());
			userInfo.put("school",
					user.getSchool() == null ? " " : user.getSchool());
			userInfo.put("company",
					user.getCompany() == null ? " " : user.getCompany());
			userInfo.put("career",
					user.getCareer() == null ? " " : user.getCareer());
			userInfo.put("user_code", user.getCode().toString());
			userInfo.put("time", new Date());
			userInfo.put("chat_tags", chat_tags);
			userInfo.put("iu_biu_id", iu_biu_id);

			// 三种推送，"0"收到的biu，"1"通知用户你的biu被抢了
			userInfo.put("messageType", "0");

			if (users != null && users.size() > 0) {
				// 设置latestCode的值
				RedisUtil.hset(Constants.redis_online,
						Constants.redis_online_latestCode.concat(user.getId()),
						users.get(users.size() - 1).get("code").toString());
				for (int i = 0; i < users.size(); i++) {
					if (!json.containsKey(users.get(i).get("code").toString())) {

						json.put(users.get(i).get("code").toString(), i);
						// 计算匹配度 推送
						JSONObject matchJson = authService.handleMatchByUserID(
								userId, users.get(i).get("id").toString());

						userInfo.putAll(matchJson);
						// 推送消息
						pushService.pushMsgToSingleDevice(users.get(i)
								.get("id").toString(), userInfo);

					}
				}

				debugMap.put("推送数量", json.size());
				json = null;
			}

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("pushMsgToDevices: {}", debugMap);
			}

			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 
	 * 抢biu推送
	 * 
	 * @param userId
	 * @param chat_id
	 * @param founderId
	 * @param founderCode
	 * @return
	 */
	@RequestMapping(value = "getMsgToDevices", method = RequestMethod.POST)
	public ResponseEntity<Void> getMsgToDevices(
			@RequestParam("userId") String userId,
			@RequestParam("chat_id") String chat_id,
			@RequestParam("founderId") String founderId,
			@RequestParam("founderCode") String founderCode) {
		try {
			User user = userService.selectUserById(userId);// 用户信息
			if (user != null) {
				// 向biu创建者发推送
				JSONObject ms = new JSONObject();
				ms.put("user_code", user.getCode().toString());
				ms.put("nickname", user.getNickname());
				ms.put("icon_thumbnailUrl",
						StsService.generateCircleUrl(user.getIcon_url()));
				ms.put("chat_id", chat_id);
				ms.put("time", new Date());
				// 三种推送，"0"收到的biu，"1"通知用户你的biu被抢了，"2"通知用户推荐给你的biu已经被抢了
				ms.put("messageType", "1");
				pushService.pushMsgToSingleDevice(founderId, ms);

				// 向所有收到过改biu的用户发推送
				// System.out.println("grabAddFriends____chat_id__"+chat_id);
				List<Map<String, Object>> refs = referencesService
						.selectListByChatId(chat_id);
				if (refs != null && refs.size() > 0) {
					// System.out.println("grabAddFriends______"+refs.toString());
					JSONObject userids = new JSONObject();
					for (int k = 0; k < refs.size(); k++) {
						if (refs.get(k).get("USER_ID2") != null
								&& !userids.containsKey(refs.get(k)
										.get("USER_ID2").toString())) {
							userids.put(refs.get(k).get("USER_ID2").toString(),
									k);

							JSONObject refsMs = new JSONObject();
							refsMs.put("time", new Date());
							refsMs.put("chat_id", chat_id);
							refsMs.put("user_code", founderCode);
							refsMs.put("messageType", "2");
							pushService.pushMsgToSingleDevice(
									refs.get(k).get("USER_ID2").toString(),
									refsMs);
						}
					}
					// 如果删除会对匹配造成影响，如果不删除获取列表时会出现垃圾数据
					referencesService.deleteListByChatId(chat_id);
				}

			}

			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 
	 * 向发biu用户推送抢biu用户信息
	 * 
	 * @param grab_user_id
	 * @param send_user_id
	 * @param iu_biu_id
	 * @return
	 */
	@RequestMapping(value = "pushGrabBiuMessage", method = RequestMethod.POST)
	public ResponseEntity<Void> pushGrabBiuMessage(
			@RequestParam("grab_user_code") Integer grab_user_code,
			@RequestParam("send_user_id") String send_user_id,
			@RequestParam("iu_biu_id") String iu_biu_id) {
		try {
			GrabBiu grabBiu = grabBiuService.selectGrabBiuByBiuIdAndUserCode(
					iu_biu_id, grab_user_code);
			if (grabBiu != null) {

				// 向biu创建者发推送
				JSONObject ms = new JSONObject();

				ms.put("age", grabBiu.getAge());
				ms.put("icon_thumbnailUrl", grabBiu.getIcon_thumbnailUrl());
				ms.put("nickname", grabBiu.getName());
				ms.put("school", grabBiu.getSchool());
				ms.put("sex", grabBiu.getSex());
				ms.put("starsign", grabBiu.getStarsign());
				ms.put("user_code", grabBiu.getUser_code());

				pushService.pushMsgToSingleDevice(send_user_id, ms);

				return ResponseEntity.status(HttpStatus.OK).build();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
		// TODO Auto-generated method stub

	}

}
