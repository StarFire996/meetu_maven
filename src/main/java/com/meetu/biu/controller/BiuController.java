package com.meetu.biu.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetu.biu.domain.Biu;
import com.meetu.biu.service.BiuService;
import com.meetu.config.Constants;
import com.meetu.core.base.BaseController;
import com.meetu.domain.User;
import com.meetu.photos.dao.SysMenusDao;
import com.meetu.photos.service.MeetuReportService;
import com.meetu.photos.service.MeetuTradingRecordService;
import com.meetu.photos.service.SysMenusService;
import com.meetu.service.MeetuAuthService;
import com.meetu.service.UserService;
import com.meetu.tags.domain.MeetuUserSettings;
import com.meetu.tags.service.MeetuChatListService;
import com.meetu.tags.service.MeetuFriendsRelService;
import com.meetu.tags.service.MeetuNoLongerMatchService;
import com.meetu.tags.service.MeetuReferencesService;
import com.meetu.tags.service.MeetuUserSettingsService;
import com.meetu.util.Common;
import com.meetu.util.RedisUtil;
import com.meetu.util.StsService;

@Controller
@RequestMapping("app/biu")
public class BiuController extends BaseController {

	@Autowired
	private BiuService biuService;

	public static Logger LOGGER = LoggerFactory.getLogger(BiuController.class);

	@Autowired
	private MeetuUserSettingsService settingsService;

	@Autowired
	private UserService userService;

	@Autowired
	private MeetuChatListService chatlistService;

	@Autowired
	private MeetuNoLongerMatchService nlmService;

	@Autowired
	private MeetuAuthService authService;

	@Autowired
	private MeetuReferencesService referencesService;

	@Autowired
	private MeetuFriendsRelService friendsRelService;

	@Autowired
	private MeetuReportService reportService;

	@Autowired
	private MeetuTradingRecordService tradingRecordService;

	@Autowired
	private SysMenusService menuService;

	@Autowired
	private SysMenusDao menuDao;

	/**
	 * 已注册用户发送BIU 2016-03-16
	 * */
	@RequestMapping(value = "/sendBiu", method = RequestMethod.POST)
	@ResponseBody
	public void sendBiu(HttpServletRequest request, HttpServletResponse response) {
		Long start = new Date().getTime();

		Map<String, Object> debugMap = new HashMap<String, Object>();

		JSONObject json = new JSONObject();

		User user = new User();
		String chat_tags = "";
		try {

			JSONObject data = JSONObject.parseObject(this.getPara("data"));
			String newToken = (String) request.getAttribute("token");
			String userid = (String) request.getAttribute("userid");
			chat_tags = data.getString("chat_tags");

			Biu biu = biuService.selectBiuByUserId(userid);

			// 判读是否有biu 如果没有则新建一个biu
			if (biu == null) {
				biu.setId(Common.generateId());
				biu.setAccept_num(0);
				biu.setAgree_biu(1);
				biu.setChat_tags(chat_tags);
				biu.setCreated_by(userid);
				biu.setLast_date(new Date());
				biu.setStatus(1);
				biuService.insertOper(biu);
			}

			// 判断是否同意发biu,并且biu的状态是已结束
			if (1 == biu.getAgree_biu() && 1 == biu.getStatus()) {

				// 更新话题,最后发biu时间,biu的状态
				biu.setChat_tags(chat_tags);
				biu.setLast_date(new Date());
				biu.setStatus(0);
				biu.setAccept_num(0);

				user = userService.selectUserById(userid);// 用户信息

				authService.handleBiuList(userid, chat_tags, biu.getId());

				JSONObject json2 = new JSONObject();
				// biu币
				Integer vc2 = userService.selectVC(userid);
				json2.put("iu_biu_id", biu.getId());
				json2.put("iu_biu_date", biu.getLast_date().getTime());

				// 发送成功之后奖励biu币
				Integer vc = authService.getSettingByKey(Constants.sendRewards) == null ? Constants.sendRewards_default
						: authService.getSettingByKey(Constants.sendRewards);

				debugMap.put("Auth_getSettingByKey", System.currentTimeMillis()
						- start);

				if ((vc2 == null ? 0 : vc2) < 100) {
					user.setVirtual_currency(vc);
					userService.addVC(user);

					debugMap.put("User_addVC", System.currentTimeMillis()
							- start);

					json2.put("virtual_currency", vc2 == null ? vc : (vc2 + vc));
				} else {
					json2.put("virtual_currency", vc2 == null ? 0 : vc2);
				}
				json2.put("token", newToken);

				json.put("data", json2);
				json.put("state", "200");
			} else {
				json.put("state", "304");
				json.put("error", "上一次的biu未结束");
			}

		} catch (Exception e) {
			json.put("state", "300");
			json.put("error", e.getMessage() + " 请联系管理员");
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(e.getMessage());
			}
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("sendBiu_times : {}", debugMap);
		}

		this.renderJson(response, json.toString());
	}

	/**
	 * 抢BIU
	 * 
	 * @param
	 * 
	 *        2016-03-16
	 * */
	@RequestMapping(value = "/grabBiu", method = RequestMethod.POST)
	@ResponseBody
	public void grabBiu(HttpServletRequest request, HttpServletResponse response) {
		Long start = new Date().getTime();
		JSONObject json = new JSONObject();
		String iu_biu_id = "";
		try {
			JSONObject data = JSONObject.parseObject(this.getPara("data"));
			String newToken = (String) request.getAttribute("token");
			String userid = (String) request.getAttribute("userid");

			iu_biu_id = data.getString("iu_biu_id");

			Biu biu = this.biuService.selectBiuById(iu_biu_id);

			// 要抢的biu没有结束
			if (biu != null && 0 == biu.getStatus()) {

				// 判断biu币是否充足可以抢biu
				Integer vc = authService.getSettingByKey(Constants.grabRewards) == null ? Constants.grabRewards_default
						: authService.getSettingByKey(Constants.grabRewards);
				if (userService.isEnough(userid, vc)) {

					// 向发biu用户推送一条信息
					this.authService.pushGrabBiuMessage(userid,
							biu.getCreated_by(), iu_biu_id, vc);

					json.put("state", "200");
					JSONObject json2 = new JSONObject();
					json2.put("token", newToken);
					json2.put("message", "1");
					json.put("data", json2);
				} else {
					json.put("state", "200");
					JSONObject json2 = new JSONObject();
					json2.put("token", newToken);
					json2.put("message", "0");
					json.put("data", json2);
					json.put("error", "biu币不足，请及时充值！");
				}
			} else {
				json.put("state", "200");
				JSONObject json2 = new JSONObject();
				json2.put("token", newToken);
				json2.put("message", "2");
				json.put("data", json2);
				json.put("error", "要抢的biu已结束");
			}
		} catch (Exception e) {

			json.put("state", "300");
			json.put("error", e.getMessage() + " 请联系管理员");
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(e.getMessage());
			}
		}
		this.renderJson(response, json.toString());
	}

	/**
	 * 接受抢biu请求
	 * 
	 * @param
	 * 
	 * */
	@RequestMapping(value = "/acceptBiu", method = RequestMethod.POST)
	@ResponseBody
	public void acceptBiu(HttpServletRequest request,
			HttpServletResponse response) {
		Long start = new Date().getTime();
		JSONObject json = new JSONObject();
		String iu_biu_id = "";
		String grab_user_code = "";
		try {
			JSONObject data = JSONObject.parseObject(this.getPara("data"));
			String newToken = (String) request.getAttribute("token");
			String send_user_id = (String) request.getAttribute("userid");

			iu_biu_id = data.getString("iu_biu_id");
			grab_user_code = data.getString("grab_user_code");

			Biu biu = this.biuService.selectBiuById(iu_biu_id);

			// 要抢的biu没有结束
			if (biu != null && 0 == biu.getStatus()) {

				// 判断biu币是否充足可以抢biu
				Integer vc = authService
						.getSettingByKey(Constants.acceptBiuCost) == null ? Constants.acceptBiuCost_default
						: authService.getSettingByKey(Constants.acceptBiuCost);
				vc = vc * biu.getAccept_num();

				if (userService.isEnough(send_user_id, vc)) {

					authService.acceptBiu(biu,
							Integer.parseInt(grab_user_code), vc);

					json.put("state", "200");
					JSONObject json2 = new JSONObject();
					json2.put("token", newToken);
					json2.put("message", "1");
					json.put("data", json2);
				} else {
					json.put("state", "200");
					JSONObject json2 = new JSONObject();
					json2.put("token", newToken);
					json2.put("message", "0");
					json.put("data", json2);
					json.put("error", "biu币不足，请及时充值！");
				}
			} else {
				json.put("state", "200");
				JSONObject json2 = new JSONObject();
				json2.put("token", newToken);
				json2.put("message", "2");
				json.put("data", json2);
				json.put("error", "本次biu已结束");
			}
		} catch (Exception e) {

			json.put("state", "300");
			json.put("error", e.getMessage() + " 请联系管理员");
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(e.getMessage());
			}
		}
		this.renderJson(response, json.toString());
	}

	/**
	 * 获取潜在发biu对象,按照发biu时间排序
	 * */
	@RequestMapping(value = "/sendBiu", method = RequestMethod.POST)
	@ResponseBody
	public void getTargetBiuList(HttpServletRequest request,
			HttpServletResponse response) {
		Long start = new Date().getTime();

		Map<String, Object> debugMap = new HashMap<String, Object>();

		JSONObject json = new JSONObject();

		User user = new User();

		try {

			JSONObject data = JSONObject.parseObject(this.getPara("data"));
			String newToken = (String) request.getAttribute("token");
			String userId = (String) request.getAttribute("userid");

			JSONObject json2 = new JSONObject();

			List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();

			MeetuUserSettings settings = settingsService.selectByUserId(userId);// 用户设置

			user = userService.selectUserById(userId);// 用户信息

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

			Integer targetBiuNumbers = authService
					.getSettingByKey(Constants.targetBiuNumbers) == null ? Constants.targetBiuNumbers_default
					: authService.getSettingByKey(Constants.targetBiuNumbers);

			map.put("num", targetBiuNumbers);

			debugMap.put("设置匹配数据时间", System.currentTimeMillis() - start);

			users = userService.selectBiu(map);

			debugMap.put("匹配用户时间1", System.currentTimeMillis() - start);
			int age = Common.getAge(user.getBirth_date());
			
			JSONArray jsonArray = new JSONArray();
			if (users != null) {

				int revMatch = Common.revMatch(u_city, age, users);
				debugMap.put("反向匹配人数", revMatch);
				debugMap.put("反向匹配时间", System.currentTimeMillis() - start);
				for (Map<String, Object> target : users) {
					JSONObject json1 = new JSONObject();
					json1.put("user_code", map.get("code"));
					json1.put("icon_thumbnailUrl", StsService.generateCircleUrl(map.get("icon_url").toString()));
					json1.put("nickname", map.get("nickname"));
					json1.put("school", map.get("school"));
					json1.put("sex", map.get("sex"));
					json1.put("chat_tags", map.get("chat_tags"));
					json1.put("age", Common.getAge((Date) map.get("birth_date")));
					
					JSONObject hm = authService.handleMatchByUserID(userId, map.get("id").toString());
					json1.putAll(hm);
					jsonArray.add(json1);
				}

			}

			json2.put("token", newToken);
			json2.put("users", jsonArray);

			json.put("data", json2);
			json.put("state", "200");

		} catch (Exception e) {
			json.put("state", "300");
			json.put("error", e.getMessage() + " 请联系管理员");
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(e.getMessage());
			}
		}

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("sendBiu_times : {}", debugMap);
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
		// TODO Auto-generated method stub

	}
}
