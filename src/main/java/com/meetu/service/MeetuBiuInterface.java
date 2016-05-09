package com.meetu.service;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.easemob.server.api.IMUserAPI;
import com.easemob.server.jersey.EasemobIMUsers;
import com.meetu.config.Constants;
import com.meetu.core.base.BaseController;
import com.meetu.domain.User;
import com.meetu.photos.dao.SysMenusDao;
import com.meetu.photos.domain.MeetuReport;
import com.meetu.photos.domain.MeetuTradingRecord;
import com.meetu.photos.service.MeetuReportService;
import com.meetu.photos.service.MeetuTradingRecordService;
import com.meetu.photos.service.SysMenusService;
import com.meetu.tags.domain.MeetuChatList;
import com.meetu.tags.domain.MeetuFriendsRel;
import com.meetu.tags.domain.MeetuNoLongerMatch;
import com.meetu.tags.domain.MeetuReferences;
import com.meetu.tags.domain.MeetuUserSettings;
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
@RequestMapping(value = "app/biubiu")
public class MeetuBiuInterface extends BaseController {

    public static Logger log = Logger.getLogger(MeetuBiuInterface.class);
    
    public static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MeetuBiuInterface.class);

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
        
        List<Long> debugList = new ArrayList<Long>();

        JSONObject json = new JSONObject();

        User user = new User();
        String chat_tags = "";
        try {

            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");
            // String userid = "c6f3de0f76de4b25b371c4afcc43f7b8";
            // 判断这个时刻该用户是否可以发BIU
            if (authService.checkSendBiu(userid)) {

                chat_tags = data.getString("chat_tags");

                user = userService.selectUserById(userid);// 用户信息

                authService.handleBiuList(userid, chat_tags);
                
                debugList.add(System.currentTimeMillis()-start);
                

                JSONObject json2 = new JSONObject();
                // biu币
                Integer vc2 = userService.selectVC(userid);


                // 发送成功之后奖励biu币
                Integer vc = authService.getSettingByKey(Constants.sendRewards) == null ? Constants.sendRewards_default
                        : authService.getSettingByKey(Constants.sendRewards);

                debugMap.put("Auth_getSettingByKey", System.currentTimeMillis() - start);

                if ((vc2 == null ? 0 : vc2) < 100) {
                    user.setVirtual_currency(vc);
                    userService.addVC(user);

                    debugMap.put("User_addVC", System.currentTimeMillis() - start);

                    json2.put("virtual_currency", vc2 == null ? vc : (vc2 + vc));
                } else {
                    json2.put("virtual_currency", vc2 == null ? 0 : vc2);
                }
                json2.put("token", newToken);

                json.put("data", json2);
                json.put("state", "200");
            } else {
                json.put("state", "304");
                json.put("error", "两次发biu时间间隔不足90s");
            }

        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        } finally {
//            // 分别向MEETU_CHATLIST、MEETU_REFERENCES表中插入数据
//            try {
//                authService.handleBiuList(users, user, chat_tags);
//                
//                debugList.add(System.currentTimeMillis() - start);
//                debugMap.put("Auth_ghandleBiuList", System.currentTimeMillis() - start);
//
//            } catch (Exception e) {
//                log.info("发送推送: " + e.getMessage());
//                e.printStackTrace();
//            }

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("sendBiu_times : {}", debugMap);
        }

        String loggerName = this.getClass().getCanonicalName() + "sendBiu";
        debugList.add(System.currentTimeMillis() - start);
        LoggerUtils.setLogger(loggerName, debugList);
        // Long end = new Date().getTime();
        // System.out.println("发biu耗时(ms)："+(end-start));
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
        String chat_id = "";
        try {
        	//System.out.println("getMsgToDevices__1");
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");
            // String userid = "4abfdd58dc1f44b19ce394e45dc5ea5e";

            chat_id = data.getString("chat_id");
            // 判断BIU是否被抢
            String isGrabbed = chatlistService.checkIsGrabbed(chat_id);
            if (isGrabbed != null) {
            	
                // 判断biu币是否充足可以抢biu
                Integer vc = authService.getSettingByKey(Constants.grabRewards) == null ? Constants.grabRewards_default
                        : authService.getSettingByKey(Constants.grabRewards);
                if (userService.isEnough(userid, vc)) {
                    // 抢biu，加好友
                    // 更新chat_id对应信息
                    MeetuChatList chatlist = new MeetuChatList();
                    chatlist.setId(chat_id);
                    chatlist.setHxid("");
                    chatlist.setAgree_date(new Date());
                    chatlist.setTo_user_id(userid);
                    //有线程不安全的隐患
                    chatlistService.updateGrabInfo(chatlist);
                    JSONObject ret = authService.grabAddFriendsV2(userid, chat_id, vc);
                    if (ret.getBooleanValue("state")) {// success
                        User user = (User) ret.get("user");
                        authService.grabAddFriends_f(user, chat_id, ret.getString("founderId"),ret.getInteger("founderCode"));

                        //System.out.println("getMsgToDevices__3");
                        Long end = new Date().getTime();
                        // System.out.println(userid+"_抢biu耗时(ms)："+(end-start));

                        json.put("state", "200");
                        JSONObject json2 = new JSONObject();
                        json2.put("token", newToken);
                        json2.put("message", "1");
                        json.put("data", json2);
                    } else {
                        json.put("state", "300");
                        json.put("error", ret.get(""));
                    }
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
                json.put("error", "已被抢！");
            }

        } catch (Exception e) {

            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /***
     * 头像状态
     * */
    @RequestMapping(value = "/updateIconStatus", method = RequestMethod.POST)
    @ResponseBody
    public void updateIconStatus(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            User user = userService.selectUserById(userid);// 用户信息
            String iconStatus = user.getIcon_is_validate();
            if (iconStatus.equals("2")) {
                menuDao.updateSQL("update sys_user set icon_is_validate=? where id=?", new Object[] { "3",
                        userid });
            } else if (iconStatus.equals("4")) {
                menuDao.updateSQL("update sys_user set icon_is_validate=? where id=?", new Object[] { "5",
                        userid });
            } else if (iconStatus.equals("6")) {
                StsService.delphoto(user.getIcon_url());
                menuDao.updateSQL(
                        "update sys_user set icon_url=original_icon_url,original_icon_url='',icon_is_validate=? where id=?",
                        new Object[] { "3", userid });
            }

            json.put("state", "200");
            json2.put("token", newToken);
            json.put("data", json2);

        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());

    }

    /**
     * 获取biubiu推荐
     * 
     * @param 2016-03-19
     * */
    @RequestMapping(value = "/biubiuList", method = RequestMethod.POST)
    @ResponseBody
    public void biubiuList(HttpServletRequest request, HttpServletResponse response) {

        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            Integer biubiuListNumbers = authService.getSettingByKey(Constants.biubiuListNumbers) == null ? Constants.biubiuListNumbers_default
                    : authService.getSettingByKey(Constants.biubiuListNumbers);
            Integer biuExpire = authService.getSettingByKey(Constants.biuExpire) == null ? Constants.biuExpire_default
                    : authService.getSettingByKey(Constants.biuExpire);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, -biuExpire);
            List<Map<String, Object>> list = referencesService.selectReferencesList(userid,
                    biubiuListNumbers, cal.getTime());

            // System.out.println(new Date().toString()+"____biubiuList___list: " +
            // list.toString());
            JSONArray arr = new JSONArray();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    JSONObject jb = new JSONObject();
                    jb.put("icon_thumbnailUrl",
                            list.get(i).get("icon_url") == null ? "" : StsService.generateCircleUrl(list
                                    .get(i).get("icon_url").toString()));
                    jb.put("user_code", list.get(i).get("code").toString());
                    jb.put("already_seen", list.get(i).get("state") == null ? "0" : list.get(i).get("state")
                            .toString());
                    jb.put("nickname", list.get(i).get("nickname") == null ? "" : list.get(i).get("nickname")
                            .toString());
                    jb.put("age", Common.getAgeByBirthday((Date) list.get(i).get("birth_date")));
                    jb.put("sex", list.get(i).get("sex") == null ? "" : list.get(i).get("sex").toString());
                    jb.put("starsign", list.get(i).get("starsign") == null ? "" : list.get(i).get("starsign")
                            .toString());
                    jb.put("isgraduated",
                            list.get(i).get("isgraduated") == null ? "" : list.get(i).get("isgraduated")
                                    .toString());
                    jb.put("school", list.get(i).get("school") == null ? "" : list.get(i).get("school")
                            .toString());
                    jb.put("company", list.get(i).get("company") == null ? "" : list.get(i).get("company")
                            .toString());
                    jb.put("carrer", list.get(i).get("career") == null ? "" : list.get(i).get("career")
                            .toString());
                    jb.put("chat_id", list.get(i).get("chat_id").toString());
                    jb.put("reference_id", list.get(i).get("id").toString());
                    jb.put("time", list.get(i).get("date"));
                    arr.add(jb);
                }
            }
            // biu币
            Integer vc = userService.selectVC(userid);
            // 最新被抢的biu
            JSONObject mylatestbiu = new JSONObject();
            List<Map<String, Object>> chatlist = chatlistService.selectLatestChatByuserId(userid);
            if (chatlist != null && chatlist.size() > 0) {
                // System.out.println(new Date().toString()+"____biubiuList___chatlist: "
                // +chatlist.toString());
                for (int j = 0; j < 1; j++) {
                    mylatestbiu.put("icon_thumbnailUrl", chatlist.get(j).get("icon_url") == null ? ""
                            : StsService.generateCircleUrl(chatlist.get(j).get("icon_url").toString()));
                    mylatestbiu.put("user_code", chatlist.get(j).get("code").toString());
                    mylatestbiu.put("already_seen", chatlist.get(j).get("state") == null ? "0" : chatlist
                            .get(j).get("state").toString());
                    mylatestbiu.put("nickname", chatlist.get(j).get("nickname") == null ? "" : chatlist
                            .get(j).get("nickname").toString());
                    mylatestbiu.put("age", Common.getAgeByBirthday((Date) chatlist.get(j).get("birth_date")));
                    mylatestbiu.put("sex",
                            chatlist.get(j).get("sex") == null ? "" : chatlist.get(j).get("sex").toString());
                    mylatestbiu.put("starsign", chatlist.get(j).get("starsign") == null ? "" : chatlist
                            .get(j).get("starsign").toString());
                    mylatestbiu.put("isgraduated", chatlist.get(j).get("isgraduated") == null ? "" : chatlist
                            .get(j).get("isgraduated").toString());
                    mylatestbiu.put("school", chatlist.get(j).get("school") == null ? "" : chatlist.get(j)
                            .get("school").toString());
                    mylatestbiu.put("company", chatlist.get(j).get("company") == null ? "" : chatlist.get(j)
                            .get("company").toString());
                    mylatestbiu.put("carrer", chatlist.get(j).get("career") == null ? "" : chatlist.get(j)
                            .get("career").toString());
                    mylatestbiu.put("chat_id", chatlist.get(j).get("id"));
                    mylatestbiu.put("time", chatlist.get(j).get("agree_date"));
                }
                json2.put("mylatestbiu", mylatestbiu);
            }
            User user = userService.selectUserById(userid);// 用户信息
            String tk = RedisUtil.updateToken(newToken, userid);
            json2.put("token", tk);
            json2.put("users", arr);
            json2.put("virtual_currency", vc == null ? 0 : vc);
            json2.put("iconStatus", user.getIcon_is_validate());

            json.put("data", json2);
            json.put("state", "200");
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 更新chatLiat状态
     * 
     * */
    @RequestMapping(value = "/updateChatListState", method = RequestMethod.POST)
    @ResponseBody
    public void updateChatListState(HttpServletRequest request, HttpServletResponse response) {

        JSONObject json = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");

            String chat_id = data.getString("chat_id");
            if (chat_id != null) {
                chatlistService.updateState(chat_id);
                JSONObject json2 = new JSONObject();
                json2.put("token", newToken);
                json.put("data", json2);
                json.put("state", "200");
            } else {
                json.put("state", "300");
                json.put("error", "参数缺失！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 点击头像查看详细信息
     * 
     * @param
     * 
     *        2016-03-19
     * */
    @RequestMapping(value = "/biuDetails", method = RequestMethod.POST)
    @ResponseBody
    public void biuDetails(HttpServletRequest request, HttpServletResponse response) {

        JSONObject json = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String chat_id = data.getString("chat_id");
            String user_code = data.getString("user_code");// 发biu的人code
            String reference_id = data.getString("reference_id");
            if (chat_id != null && !chat_id.equals("") && user_code != null && !user_code.equals("")
                    && reference_id != null && !reference_id.equals("")) {
                User user_from = userService.selectUserByCode(Integer.parseInt(user_code));// 发biu的人--
                User user_to = userService.selectUserById(userid);// 我的信息
                MeetuUserSettings settings_from = settingsService.selectByUserId(userid);// 发biu的人--用户设置

                String s_pTags = settings_from.getPersonalized_tags() == null ? "" : settings_from
                        .getPersonalized_tags();

                JSONObject match = authService
                        .handleMatch(user_from, s_pTags, user_to, chat_id, reference_id);
                // 更新推荐是否被看过字段
                referencesService.updateState(reference_id);

                if (match.size() > 0) {
                    // biu币
                    Integer vc = userService.selectVC(userid);
                    match.put("havevc", vc);
                    // 抢biu需要多少biu币
                    match.put(
                            "needvc",
                            authService.getSettingByKey(Constants.grabRewards) == null ? Constants.grabRewards_default
                                    : authService.getSettingByKey(Constants.grabRewards));
                    // 根据chat_id获取话题标签
                    MeetuChatList chatlist = chatlistService.selectChatListById(chat_id);
                    if (chatlist != null) {
                        match.put("chatTag", chatlist.getChat_tags() == null ? " " : chatlist.getChat_tags());
                        if (chatlist.getAgree_date() == null && chatlist.getTo_user_id() == null) {
                            match.put("isGrabbed", 0);
                        } else {
                            match.put("isGrabbed", 1);
                        }
                    } else {
                        match.put("chatTag", " ");
                        match.put("isGrabbed", 0);
                    }

                    match.put("token", newToken);
                    json.put("data", match);
                    json.put("state", "200");
                } else {
                    json.put("state", "300");
                    json.put("error", "获取数据失败，请联系管理员！");
                }
            } else {
                json.put("state", "300");
                json.put("error", "参数缺失！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 不想再看到TA
     * 
     * @param
     * 
     *        2016-03-16
     * */
    @RequestMapping(value = "/nolongerMatch", method = RequestMethod.POST)
    @ResponseBody
    public void nolongerMatch(HttpServletRequest request, HttpServletResponse response) {

        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String user_code = data.getString("user_code");
            if (user_code != null) {

                String user_id2 = userService.selectIdByCode(Integer.parseInt(user_code));

                MeetuNoLongerMatch noLongerMatch = new MeetuNoLongerMatch();
                noLongerMatch.setFrom_user_id(userid);
                noLongerMatch.setTo_user_id(user_id2);
                noLongerMatch.setDate(new Date());
                noLongerMatch.setId(Common.generateId());

                nlmService.insertOper(noLongerMatch);

                json2.put("token", newToken);
                json.put("data", json2);
                json.put("state", "200");
            } else {
                json.put("state", "200");
                json.put("error", "参数错误！");

            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 更新缓存中的channelId和deviceType
     * 
     * @param 2016-03-20
     * */
    @RequestMapping(value = "/updateChannelIdAndDeviceType", method = RequestMethod.POST)
    @ResponseBody
    public void updateSettings(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String channelId = data.getString("channelId");
            String deviceType = data.getString("deviceType");
            if (channelId != null && !channelId.equals("") && deviceType != null && !deviceType.equals("")) {
                RedisUtil.hset(Constants.redis_online, Constants.redis_online_channelId.concat(userid),
                        channelId);
                RedisUtil.hset(Constants.redis_online, Constants.redis_online_deviceType.concat(userid),
                        deviceType);
                json.put("state", "200");
                JSONObject json2 = new JSONObject();
                json2.put("token", newToken);
                json.put("data", json2);
            } else {
                json.put("state", "300");
                json.put("error", "参数缺失！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 获取好友列表
     * 
     * @param 2016-03-23
     * */
    @RequestMapping(value = "/getFriendsList", method = RequestMethod.POST)
    @ResponseBody
    public void getFriendsList(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            // Integer index = data.getInteger("index");
            // Integer numbers = data.getInteger("numbers");
            List<Map<String, Object>> friends = friendsRelService.selectFriendsListByUserId(userid);

            if (friends != null && friends.size() > 0) {
                JSONArray ja = new JSONArray();
                for (int j = 0; j < friends.size(); j++) {
                    JSONObject jb = new JSONObject();

                    jb.put("icon_thumbnailUrl",
                            friends.get(j).get("icon_url") == null ? "" : StsService
                                    .generateCircleUrl(friends.get(j).get("icon_url").toString()));
                    jb.put("user_code", friends.get(j).get("code").toString());
                    jb.put("nickname",
                            friends.get(j).get("nickname") == null ? "" : friends.get(j).get("nickname")
                                    .toString());
                    jb.put("age", Common.getAgeByBirthday((Date) friends.get(j).get("birth_date")));
                    jb.put("sex", friends.get(j).get("sex") == null ? "" : friends.get(j).get("sex")
                            .toString());
                    jb.put("starsign",
                            friends.get(j).get("starsign") == null ? "" : friends.get(j).get("starsign")
                                    .toString());
                    jb.put("isgraduated", friends.get(j).get("isgraduated") == null ? "" : friends.get(j)
                            .get("isgraduated").toString());
                    jb.put("school", friends.get(j).get("school") == null ? "" : friends.get(j).get("school")
                            .toString());
                    jb.put("company",
                            friends.get(j).get("company") == null ? "" : friends.get(j).get("company")
                                    .toString());
                    jb.put("carrer", friends.get(j).get("career") == null ? "" : friends.get(j).get("career")
                            .toString());
                    jb.put("time", friends.get(j).get("agree_date"));
                    ja.add(jb);
                }
                json2.put("users", ja);
            }
            json2.put("token", newToken);
            json.put("data", json2);
            json.put("state", "200");
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 删除好友
     * 
     * @param 2016-03-23
     * */
    @RequestMapping(value = "/removeFriend", method = RequestMethod.POST)
    @ResponseBody
    public void removeFriend(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String user_code2 = data.getString("user_code");
            String user_code = userService.selectCodeById(userid).toString();
            if (user_code2 != null) {
                String user_id2 = userService.selectIdByCode(Integer.parseInt(user_code2));

                friendsRelService.deleteByUserIds(userid, user_id2);
                // 环信
                IMUserAPI userAPIImpl = new EasemobIMUsers();
                userAPIImpl.deleteFriendSingle(user_code, user_code2);
                userAPIImpl.deleteFriendSingle(user_code2, user_code);

                json2.put("token", newToken);
                json.put("data", json2);
                json.put("state", "200");

            } else {
                json.put("state", "300");
                json.put("error", "参数有误！");
            }

        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 举报
     * 
     * @param 2016-03-24
     * */
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    public void report(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String user_code2 = data.getString("user_code");// 被举报的人
            String reason = data.getString("reason");
            if (user_code2 != null) {
                String user_id2 = userService.selectIdByCode(Integer.parseInt(user_code2));

                MeetuReport report = new MeetuReport();
                report.setId(Common.generateId());
                report.setFrom_user_id(userid);
                report.setTo_user_id(user_id2);
                report.setDate(new Date());
                report.setReason(reason);
                report.setIschecked(0);
                reportService.insertOper(report);

                json2.put("token", newToken);
                json.put("data", json2);
                json.put("state", "200");

            } else {
                json.put("state", "300");
                json.put("error", "参数有误！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 创建订单
     * 
     * @param 2016-03-25
     * */
    @RequestMapping(value = "/createBill", method = RequestMethod.POST)
    @ResponseBody
    public void createBill(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String bill_type = data.getString("bill_type");
            String channel = data.getString("channel");
            String title = data.getString("title");
            Integer totalfee = data.getInteger("totalfee");
            Integer totalnum = data.getInteger("totalnum");

            Integer code = userService.selectCodeById(userid);

            if (bill_type != null && channel != null && title != null && totalfee != null && code != null
                    && totalnum != null) {
                Date date = new Date();
                MeetuTradingRecord tradingRecord = new MeetuTradingRecord();
                tradingRecord.setId(Common.generateId());
                tradingRecord.setBill_no("" + code + date.getTime());
                tradingRecord.setBill_type(bill_type);
                tradingRecord.setTitle(title);
                tradingRecord.setChannel(channel);
                tradingRecord.setTotalfee(totalfee);
                tradingRecord.setTotalnum(totalnum);
                tradingRecord.setDate(date);
                tradingRecord.setResult(0);
                tradingRecord.setUser_id(userid);
                tradingRecordService.insertOper(tradingRecord);

                json2.put("bill_no", tradingRecord.getBill_no());
                json2.put("token", newToken);
                json.put("data", json2);
                json.put("state", "200");

            } else {
                json.put("state", "300");
                json.put("error", "参数有误！");
            }

        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 查询订单状态
     * 
     * @param 2016-03-25
     * */
    @RequestMapping(value = "/checkBill", method = RequestMethod.POST)
    @ResponseBody
    public void checkBill(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String bill_no = data.getString("bill_no");
            if (bill_no != null) {
                // 查询支付结果
                Integer result = tradingRecordService.selectTradeResult(bill_no, userid);
                if (result != null) {
                    if (result == 1) {
                        json2.put("result", "1");
                    } else {
                        json2.put("result", "0");
                    }
                    // biu币
                    Integer vc = userService.selectVC(userid);
                    json2.put("virtual_currency", vc == null ? 0 : vc);
                    json2.put("token", newToken);
                    json.put("data", json2);
                    json.put("state", "200");
                } else {
                    json.put("state", "300");
                    json.put("error", "订单不存在！");
                }
            } else {
                json.put("state", "300");
                json.put("error", "参数有误！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }
    
    /**
     * 更改订单状态
     * 
     * @param 2016-05-05
     * */
    @RequestMapping(value = "/updateBill", method = RequestMethod.POST)
    @ResponseBody
    public void updateBill(HttpServletRequest request, HttpServletResponse response) {
    	
    	Map<String, Object> debugMap = new HashMap<String, Object>();
    	
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String bill_no = data.getString("bill_no");
            
            debugMap.put("userid", userid);
            debugMap.put("bill_no", bill_no);
            
            //支付成功更改数据库
            if (bill_no != null) {
            	MeetuTradingRecord meetuTradingRecord = tradingRecordService.selectAll(bill_no);
            	
            	if (meetuTradingRecord != null) {
            		//获取增加的Umi
					Integer totalnum = meetuTradingRecord.getTotalnum();
					//更改订单状态,更改用户U米数量
					authService.updateVC(bill_no, userid, totalnum);
					
					Integer vc = userService.selectVC(userid);
                    json2.put("virtual_currency", vc == null ? 0 : vc);
                    json2.put("token", newToken);
                    json.put("data", json2);
                    json.put("state", "200");
				}else {
                    json.put("state", "300");
                    json.put("error", "订单不存在！");
                    debugMap.put("error", "订单不存在！");
                }
            } else {
                json.put("state", "300");
                json.put("error", "bill_no参数有误！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            debugMap.put("error", e.getMessage() + " 请联系管理员");
        }
        
        if (LOGGER.isInfoEnabled()) {
			LOGGER.info("updateBill : {}", debugMap);
		}
        
        this.renderJson(response, json.toString());
    }
    
    
    
    @Override
    public String index(HttpServletResponse paramHttpServletResponse, ModelMap paramModelMap) {
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
            HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse,
            ModelMap paramModelMap) {
        // TODO Auto-generated method stub

    }
}
