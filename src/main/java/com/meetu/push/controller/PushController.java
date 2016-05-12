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

import org.apache.log4j.Logger;
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
import com.meetu.config.Constants;
import com.meetu.core.base.BaseController;
import com.meetu.domain.User;
import com.meetu.photos.dao.SysMenusDao;
import com.meetu.photos.service.MeetuReportService;
import com.meetu.photos.service.MeetuTradingRecordService;
import com.meetu.photos.service.SysMenusService;
import com.meetu.push.service.PushService;
import com.meetu.service.MeetuAuthService;
import com.meetu.service.UserService;
import com.meetu.tags.domain.MeetuChatList;
import com.meetu.tags.domain.MeetuReferences;
import com.meetu.tags.domain.MeetuUserSettings;
import com.meetu.tags.domain.SysSettings;
import com.meetu.tags.service.MeetuChatListService;
import com.meetu.tags.service.MeetuFriendsRelService;
import com.meetu.tags.service.MeetuNoLongerMatchService;
import com.meetu.tags.service.MeetuReferencesService;
import com.meetu.tags.service.MeetuUserSettingsService;
import com.meetu.util.Common;
import com.meetu.util.LoggerUtils;
import com.meetu.util.RedisUtil;
import com.meetu.util.StsService;

@Controller
@RequestMapping(value = "app/push")
public class PushController extends BaseController {

	public static Logger log = Logger.getLogger(PushController.class);

	public static org.slf4j.Logger LOGGER = LoggerFactory
			.getLogger(PushController.class);

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
			@RequestParam("chat_tags") String chat_tags) {
		try {
			List<String> debugList = new ArrayList<String>();
			debugList.add(userId);
			debugList.add(chat_tags);

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
			Integer timeInterval = authService
					.getSettingByKey(Constants.timeInterval) == null ? Constants.timeInterval_default
					: authService.getSettingByKey(Constants.timeInterval);

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, -timeInterval);
			// 用户多长时间不能接受新的biu
			map.put("latest", cal.getTime());

			Integer timeIntervalT = authService
					.getSettingByKey(Constants.timeIntervalT) == null ? Constants.timeIntervalT_default
					: authService.getSettingByKey(Constants.timeIntervalT);

			// Calendar cal2 = Calendar.getInstance();
			cal.add(Calendar.SECOND, timeInterval - timeIntervalT);
			// 用户多长时间不能接受该用户新的biu
			map.put("latestT", cal.getTime());

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

			users = userService.selectBiu(map);

			int age = this.getAge(user.getBirth_date());

			this.revMatch(u_city, age, users);

			if (users == null) {
				users = new ArrayList<Map<String, Object>>();
			}
			// 使用的滚动查询,如果查询到的数量小于要求的数量,那么就从数据库表头开始查询
			if (users.size() < sendBiuNumbers) {
				map.put("num", sendBiuNumbers - users.size());
				map.put("latestCode", 10000);
				
				List<Map<String, Object>> users2 = userService.selectBiu(map);
				
				
				if (users2 != null && users2.size() > 0) {
					
					this.revMatch(u_city, age, users2);
					// 设置lastestCode的值
					users.addAll(users2);
				}
			}

			// 发biu时，若该用户存在未被抢的chatlist则更新该信息,更新话题标签时间
			String from_user_id = chatlistService
					.selectUngrabbedChatByUserId(user.getId());
			MeetuChatList chatList = new MeetuChatList();
			if (from_user_id != null) {
				chatList.setId(from_user_id);
				chatList.setChat_tags(chat_tags);
				chatList.setStart_date(new Date());
				chatlistService.updateChatListById(chatList);
			} else {
				chatList.setId(Common.generateId());
				chatList.setChat_tags(chat_tags);
				chatList.setFrom_user_id(user.getId());
				chatList.setStart_date(new Date());
				chatlistService.insertOper(chatList);
			}

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
			userInfo.put("chat_id", chatList.getId());

			// 三种推送，"0"收到的biu，"1"通知用户你的biu被抢了，"2"通知用户推荐给你的biu已经被抢了
			userInfo.put("messageType", "0");

			if (users != null && users.size() > 0) {
				// 设置latestCode的值
				RedisUtil.hset(Constants.redis_online,
						Constants.redis_online_latestCode.concat(user.getId()),
						users.get(users.size() - 1).get("code").toString());
				for (int i = 0; i < users.size(); i++) {
					if (!json.containsKey(users.get(i).get("code").toString())) {

						json.put(users.get(i).get("code").toString(), i);
						MeetuReferences references = new MeetuReferences();
						references.setId(Common.generateId());
						references.setChat_id(chatList.getId());
						references.setDate(new Date());
						references.setUser_id1(user.getId());
						references.setUser_id2(users.get(i).get("id")
								.toString());
						references.setUser_code2((Integer) users.get(i).get(
								"code"));
						referencesService.insertOper(references);

						userInfo.put("reference_id", references.getId());

						// 推送消息
						pushService.pushMsgToSingleDevice(users.get(i)
								.get("id").toString(), userInfo);

					}
				}

				debugList.add("个数：" + json.size());
				json = null;
			}

			LoggerUtils.setLogger("pushMsgToDevices", debugList);

			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	/**
	 * 反向筛选用户
	 * 
	 * @param u_city
	 * @param age
	 * @param users
	 */
	private void revMatch(String u_city, int age,
			List<Map<String, Object>> users) {
		List<Map<String, Object>> removeList = new ArrayList<Map<String, Object>>();
		int count = 0;
		for (Map<String, Object> map : users) {
			String s_city = map.get("s_city").toString();
			String cityf = map.get("cityf").toString();
			int s_age_down = (int) map.get("s_age_down");
			int s_age_up = (int) map.get("s_age_up");
			//只有用户同城限制打开的时候,并且两个人城市不相同,才向移除列表中添加
			if (s_city.equals("1") && !cityf.equals(u_city)) {
				removeList.add(map);
				count++;
				continue;
			}
			//当不满足用户年龄范围限制的时候,向移除列表中添加
			if (s_age_down != s_age_up && s_age_up != 0) {
				if (age > s_age_up || age < s_age_down) {
					removeList.add(map);
					count++;
					continue;
				}
			}
		}
		users.removeAll(removeList);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("反向匹配移除用户数 :{}", count);
		}
	}

	/**
	 * 通过出生日期计算年龄
	 * 
	 * @param birthDate
	 * @return
	 */
	private int getAge(Date birthDate) {

		if (birthDate == null)
			throw new RuntimeException("出生日期不能为null");

		int age = 0;

		Date now = new Date();

		SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		SimpleDateFormat format_M = new SimpleDateFormat("MM");

		String birth_year = format_y.format(birthDate);
		String this_year = format_y.format(now);

		String birth_month = format_M.format(birthDate);
		String this_month = format_M.format(now);

		// 初步，估算
		age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);

		// 如果未到出生月份，则age - 1
		if (this_month.compareTo(birth_month) < 0)
			age -= 1;
		if (age < 0)
			age = 0;
		return age;
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
