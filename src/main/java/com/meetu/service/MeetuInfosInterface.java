package com.meetu.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.meetu.core.base.BaseController;
import com.meetu.domain.User;
import com.meetu.photos.dao.SysMenusDao;
import com.meetu.photos.domain.MeetuPhotos;
import com.meetu.photos.service.MeetuPhotosService;
import com.meetu.photos.service.SysMenusService;
import com.meetu.tags.domain.MeetuUserSettings;
import com.meetu.tags.service.MeetuInterestTagsService;
import com.meetu.tags.service.MeetuPersonalizedTagsService;
import com.meetu.tags.service.MeetuUserSettingsService;
import com.meetu.util.Common;
import com.meetu.util.LoggerUtils;
import com.meetu.util.RedisUtil;
import com.meetu.util.StsService;

@Controller
@RequestMapping(value = "app/info")
public class MeetuInfosInterface extends BaseController {
    public static Logger log = Logger.getLogger(MeetuInfosInterface.class);

    public static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MeetuInfosInterface.class);

    @Autowired
    private MeetuPhotosService meetuPhotosService;

    @Autowired
    private MeetuAuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private MeetuUserSettingsService settingsService;

    @Autowired
    private MeetuPersonalizedTagsService ptService;

    @Autowired
    private MeetuInterestTagsService itService;

    @Autowired
    private MeetuPhotosService photoService;

    @Autowired
    private SysMenusService menuService;

    @Autowired
    private SysMenusDao menuDao;

    /**
     * 保存图片 每次只保存一张照片 2016-03-09
     * 
     * @param token
     * @param preview
     * @param photo
     * @return state
     * @return id 数据库中唯一标识
     * @return token 若token超时，返回新的token
     * @return error 当state为300时，提示错误信息
     * 
     * */
    @RequestMapping(value = "/savePhoto", method = RequestMethod.POST)
    @ResponseBody
    public void savePhoto(HttpServletRequest request, HttpServletResponse response) {

        JSONObject json = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String preview = Common.formatNull(data.getString("preview"));
            String photo = data.getString("photo");
            if (photo != null && !photo.equals("")) {

                MeetuPhotos meetuPhotos = new MeetuPhotos();
                meetuPhotos.setId(Common.generateId());
                meetuPhotos.setUser_id(userid);
                meetuPhotos.setPreview(preview);
                meetuPhotos.setPhoto(photo);
                meetuPhotos.setCreate_date(new Date());
                meetuPhotosService.insert(meetuPhotos);

                JSONObject json2 = new JSONObject();
                json2.put("photo_code", meetuPhotos.getId());
                json2.put("photo_url", StsService.generateUrl(meetuPhotos.getPhoto()));
                json2.put("photo_thumbnailUrl", StsService.generateThumbnailUrl(meetuPhotos.getPhoto()));
                json2.put("photo_name", meetuPhotos.getPhoto());
                json2.put("token", newToken);

                json.put("data", json2);
                json.put("state", "200");
            } else {
                json.put("state", "300");
                json.put("error", "请检查数据！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员!");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 保存图片 每次只删除一张照片 2016-03-09
     * */
    @RequestMapping(value = "/delPhoto", method = RequestMethod.POST)
    @ResponseBody
    public void delPhoto(HttpServletRequest request, HttpServletResponse response) {

        JSONObject json = new JSONObject();
        try {

            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String photoId = data.getString("photo_code");
            String photoName = data.getString("photo_name");

            StsService.delphoto(photoName);
            meetuPhotosService.deleteById(photoId);

            JSONObject json2 = new JSONObject();

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
     * 更新用户头像
     * 
     * 2016-03-14
     * */
    @RequestMapping(value = "/updateIcon", method = RequestMethod.POST)
    @ResponseBody
    public void updateIcon(HttpServletRequest request, HttpServletResponse response) {

        JSONObject json = new JSONObject();
        try {

            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            User user = userService.selectUserById(userid);// 用户信息

            String icon_url = data.getString("icon_url");

            String icon_is_validate = user.getIcon_is_validate();
            if (icon_is_validate != null) {
                if (icon_is_validate.equals("2") || icon_is_validate.equals("3")) {
                    menuDao.updateSQL(
                            "update sys_user set original_icon_url=icon_url,icon_url=?,icon_is_validate=? where id=?",
                            new Object[] { icon_url, "1", userid });
                } else {
                    menuDao.updateSQL("update sys_user set icon_url=?,icon_is_validate=? where id=?",
                            new Object[] { icon_url, "1", userid });
                    if (user.getIcon_url() != null) {
                        StsService.delphoto(user.getIcon_url());
                    }
                }

            } else {
                menuDao.updateSQL("update sys_user set icon_url=?,icon_is_validate=? where id=?",
                        new Object[] { icon_url, "1", userid });
                if (user.getIcon_url() != null) {
                    StsService.delphoto(user.getIcon_url());
                }
            }

            JSONObject json2 = new JSONObject();
            json2.put("token", newToken);
            json2.put("icon_url", StsService.generateUrl(icon_url));
            json2.put("icon_thumbnailUrl", StsService.generateCircleUrl(icon_url));

            json.put("data", json2);
            json.put("state", "200");

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
     * 根据标签类型获取
     * 
     * @param type chat/interest/personalied
     * @author lzming 2016-03-10
     * */

    @RequestMapping(value = "/getTags", method = RequestMethod.POST)
    @ResponseBody
    public void getTags(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        String[] strs = { "chat", "interest", "personalied" };
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String type = data.getString("type");
            String sex = data.get("sex") == null ? "1" : data.get("sex").toString();

            if (Common.indexString(strs, type) > -1) {// type包含在strs中

                JSONArray ret = authService.getTags(type, sex);
                json2.put("tags", ret);
                json2.put("token", newToken);

                json.put("data", json2);
                json.put("state", "200");

            } else {
                json.put("state", "300");
                json.put("error", "请检查数据！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员！");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 根据code获取个人信息
     * 
     * @author lzming 2016-03-10
     * */

    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) {

        Long start = System.currentTimeMillis();
        Map<String, Object> debugMap = new HashMap<String, Object>();
        List<Long> debugList = new ArrayList<Long>();

        JSONObject json = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");
            Integer code = Integer.parseInt(data.getString("code"));

            User user = new User();
            user = userService.selectUserByCode(code);

            debugMap.put("User_selectUserByCode", System.currentTimeMillis() - start);
            debugList.add(System.currentTimeMillis() - start);

            if (user != null) {
            	     
            	
                JSONObject userInfo = new JSONObject();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                
                
                // 头像状态：0-待审核，1-审核中，2-审核通过，3-审核失败
                userInfo.put("photoStatus",
                        user.getIcon_is_validate() == null ? "" : user.getIcon_is_validate());
                userInfo.put("photoOrigin", StsService.generateUrl(user.getIcon_url()));
                userInfo.put("photoCircle", StsService.generateCircleUrl(user.getIcon_url()));// =================
                // 照片墙
                userInfo.put("photos", authService.handlePhotos(code));

                debugMap.put("Auth_handlePhotos", System.currentTimeMillis() - start);
                debugList.add(System.currentTimeMillis() - start);
                
                userInfo.put("description", user.getDescription() == null ? "" : user.getDescription());
                userInfo.put("nickname", user.getNickname() == null ? "" : user.getNickname());
                userInfo.put("sex", user.getSex() == null ? "0" : user.getSex());// 0未知，1男，2女
                userInfo.put("birth_date",
                        user.getBirth_date() == null ? "" : sdf.format(user.getBirth_date()));
                userInfo.put("starsign", user.getStarsign());
                userInfo.put("city", user.getCity() == null ? "" : user.getCity());
                userInfo.put("cityf", user.getCityf() == null ? "" : user.getCityf());
                userInfo.put("hometown", user.getHometown() == null ? "" : user.getHometown());
                userInfo.put("height", user.getHeight() == null ? 0 : user.getHeight());
                userInfo.put("weight", user.getWeight() == null ? 0 : user.getWeight());
                // 是否是学生：0-未知，1-是学生，2-已毕业
                userInfo.put("isgraduated", user.getIsgraduated() == null ? "" : user.getIsgraduated());
                userInfo.put("career", user.getCareer() == null ? "" : user.getCareer());
                userInfo.put("school", user.getSchool() == null ? "" : user.getSchool());
                userInfo.put("company", user.getCompany() == null ? "" : user.getCompany());
                userInfo.put("superman", user.getSuperman() == null ? 0 : user.getSuperman());
                // 个性标签
                if (user.getPersonality_tags() != null && !user.getPersonality_tags().equals("")) {
                    String[] pt_array = user.getPersonality_tags().split(",");
                    // int[] array = new int[pt.length];
                    // for(int i=0;i<pt.length;i++){
                    // array[i] = Integer.parseInt(pt[i]);
                    // }
                    List<Map<String, Object>> ret = ptService.selectTags(pt_array);

                    debugMap.put("PT_selectTags", System.currentTimeMillis() - start);
                    debugList.add(System.currentTimeMillis() - start);
                    if (ret != null) {
                        JSONArray ja = new JSONArray();
                        for (int k = 0; k < ret.size(); k++) {
                            JSONObject jb = new JSONObject();
                            jb.put("code", ret.get(k).get("id").toString());
                            jb.put("name", ret.get(k).get("name").toString());
                            ja.add(jb);
                        }
                        userInfo.put("personality_tags", ja);
                    } else {
                        userInfo.put("personality_tags", new JSONArray());
                    }
                } else {
                    userInfo.put("personality_tags", new JSONArray());
                }
                // 兴趣标签
                if (user.getInterested_tags() != null && !user.getInterested_tags().equals("")) {
                	//查询个人信息中的兴趣标识列表
                    String[] it_array = user.getInterested_tags().split(",");
                    List<Map<String, Object>> ret = itService.selectTags(it_array);
                    JSONArray ja = authService.handleInterestedTags(ret);
                    if (ja != null) {
                        userInfo.put("interested_tags", ja);
                    } else {
                        userInfo.put("interested_tags", new JSONArray());
                    }

                } else {
                    userInfo.put("interested_tags", new JSONArray());
                }
                // 若不是自己查看的,则计算匹配度,访问量加1
                if (!userid.equals(user.getId())) {
                    JSONObject hm = authService.handleMatchByUserID(userid, user.getId());

                    debugMap.put("Auth_handleMatchByUserID", System.currentTimeMillis() - start);
                    debugList.add(System.currentTimeMillis() - start);
                    hm.put("activity_time", user.getActivity_time());
                    userInfo.putAll(hm);
                    
                    //总访问量加1
                    user.setTotal_num(user.getTotal_num()+1);
                    //判断最后访问时间是否是今天
                    Date last_visit_date = user.getLast_visit_date();
                    if (last_visit_date!=null) {
						SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
						String user_last_date = sdFormat.format(last_visit_date);
						String today = sdFormat.format(new Date());
						if (today.equals(user_last_date)) {
							//最后访问时间是今天
							user.setToday_num(user.getToday_num()+1);
						}else {
							//最后访问时间不是今天
							user.setToday_num(1);
						}
					}else{
						//第一次被访问
						user.setToday_num(1);
					}
                    userService.updateUserInfo(user);
                    userInfo.put("today_num", user.getToday_num());
                    userInfo.put("total_num", user.getTotal_num());
                    
                }

                JSONObject json2 = new JSONObject();
                json2.put("userinfo", userInfo);
                json2.put("token", newToken);

                json.put("state", "200");
                json.put("data", json2);

            } else {
                json.put("state", "300");
                json.put("error", "请检查数据！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员！");
            e.printStackTrace();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getUserInfo_times : {}", debugMap);
        }

        String loggerName = this.getClass().getCanonicalName() + "getUserInfo";
        LoggerUtils.setLogger(loggerName, debugList);
        this.renderJson(response, json.toString());
    }

    /**
     * 更新个人信息
     * 
     * @author lzming 2016-03-10
     * */

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public void updateUserInfo(HttpServletRequest request, HttpServletResponse response) {

        JSONObject json = new JSONObject();
        try {

            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");
            String parameters = data.getString("parameters");

            if (parameters != null && !parameters.equals("")) {
                String[] pt_array = parameters.split(",");
                JSONArray parasArray = new JSONArray();
                for (int i = 0; i < pt_array.length; i++) {
                    parasArray.add(pt_array[i]);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                User user = new User();
                user.setId(userid);
                int k = 0;
                if (parasArray.contains("nickname")) {
                    user.setNickname(data.getString("nickname"));
                    k++;
                }
                if (parasArray.contains("sex")) {
                    user.setSex(data.getString("sex"));
                    k++;
                }
                if (parasArray.contains("birth_date")) {
                    user.setBirth_date(sdf.parse(data.getString("birth_date")));
                    k++;
                }
                if (parasArray.contains("city")) {
                    user.setCity(data.getString("city"));
                    k++;
                }
                if (parasArray.contains("cityname")) {
                    user.setCity(data.getString("cityname"));
                    k++;
                }
                if (parasArray.contains("cityf")) {
                    user.setCityf(data.getString("cityf"));
                    k++;
                }
                if (parasArray.contains("hometown")) {
                    user.setHometown(data.getString("hometown"));
                    k++;
                }
                if (parasArray.contains("height")) {
                    user.setHeight(data.getInteger("height"));
                    k++;
                }
                if (parasArray.contains("weight")) {
                    user.setWeight(data.getInteger("weight"));
                    k++;
                }
                if (parasArray.contains("isgraduated")) {
                    user.setIsgraduated(data.getString("isgraduated"));
                    k++;
                }
                if (parasArray.contains("career")) {
                    user.setCareer(data.getString("career"));
                    k++;
                }
                if (parasArray.contains("school")) {
                    user.setSchool(data.getString("school"));
                    k++;
                }
                if (parasArray.contains("schoolname")) {
                    user.setSchoolname(data.getString("schoolname"));
                    k++;
                }
                if (parasArray.contains("company")) {
                    user.setCompany(data.getString("company"));
                    k++;
                }
                if (parasArray.contains("personality_tags")) {
                    user.setPersonality_tags(data.getString("personality_tags"));
                    k++;
                }
                if (parasArray.contains("interested_tags")) {
                    user.setInterested_tags(data.getString("interested_tags"));
                    k++;
                }
                if (parasArray.contains("starsign")) {
                    user.setStarsign(data.getString("starsign"));
                    k++;
                }
                if (parasArray.contains("description")) {
                    user.setDescription(data.getString("description"));
                    k++;
                }
                if (parasArray.contains("activity_time")) {
                    user.setActivity_time(new Date());
                    k++;
                }
                if (parasArray.contains("app_status")) {
                    user.setApp_status(data.get("app_status").toString());
                    k++;
                }
                if (pt_array.length == k) {
                    userService.updateUserInfo(user);

                    JSONObject json2 = new JSONObject();
                    json2.put("token", newToken);

                    json.put("data", json2);
                    json.put("state", "200");
                } else {
                    json.put("state", "300");
                    json.put("error", "未传递正确的数据！");
                }
            } else {
                json.put("state", "300");
                json.put("error", "未传递正确的数据！");

            }

        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员！");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 获取设置
     * 
     * @author lzming 2016-03-11
     * */

    @RequestMapping(value = "/getSettings", method = RequestMethod.POST)
    @ResponseBody
    public void getSettings(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            MeetuUserSettings settings = settingsService.selectByUserId(userid);
            User user = userService.selectUserById(userid);// 用户信息
            if (settings != null && user != null) {
                JSONObject json2 = new JSONObject();
                json2.put("sex", Common.formatNull(settings.getSex()));
                json2.put("city", Common.formatNull(settings.getCity()));
                json2.put("age_down", settings.getAge_down());
                json2.put("age_up", settings.getAge_up());
                JSONArray tagArray = new JSONArray();
                if (settings.getPersonalized_tags() != null && !settings.getPersonalized_tags().equals("")) {
                    String[] ids = settings.getPersonalized_tags().split(",");
                    List<Map<String, Object>> ret = ptService.selectTags(ids);
                    if (ret != null) {
                        for (int k = 0; k < ret.size(); k++) {
                            JSONObject jb = new JSONObject();
                            jb.put("code", ret.get(k).get("id").toString());
                            jb.put("name", ret.get(k).get("name").toString());
                            tagArray.add(jb);
                        }
                    }
                }
                json2.put("personalized_tags", tagArray);
                json2.put("message", settings.getMessage());
                json2.put("sound", settings.getSound());
                json2.put("vibration", settings.getVibration());
                json2.put("sex2", user.getSex());
                json2.put("personalityTags", (user.getPersonality_tags() == null || user
                        .getPersonality_tags().equals("")) ? 0 : user.getPersonality_tags().split(",").length);

                json2.put("token", newToken);

                json.put("data", json2);
                json.put("state", "200");
            } else {
                json.put("state", "300");
                json.put("error", "数据不存在，请联系管理员！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员！");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 更新设置
     * 
     * @author lzming 2016-03-11
     * */
    @RequestMapping(value = "/updateSettings", method = RequestMethod.POST)
    @ResponseBody
    public void updateSettings(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            String parameters = data.getString("parameters");

            if (parameters != null && !parameters.equals("")) {
                String[] pt_array = parameters.split(",");
                JSONArray parasArray = new JSONArray();
                for (int i = 0; i < pt_array.length; i++) {
                    parasArray.add(pt_array[i]);
                }

                MeetuUserSettings settings = new MeetuUserSettings();
                settings.setUser_id(userid);

                if (parasArray.contains("sex")) {
                    settings.setSex(data.get("sex").toString());
                }
                if (parasArray.contains("city")) {
                    settings.setCity(data.get("city").toString());
                }
                if (parasArray.contains("age_down")) {
                    settings.setAge_down(data.getInteger("age_down"));
                }
                if (parasArray.contains("age_up")) {
                    settings.setAge_up(data.getInteger("age_up"));
                }
                if (parasArray.contains("personalized_tags")) {
                    settings.setPersonalized_tags(data.getString("personalized_tags"));
                }
                if (parasArray.contains("message")) {
                    settings.setMessage(data.getInteger("message"));
                }
                if (parasArray.contains("sound")) {
                    settings.setSound(data.getInteger("sound"));
                }
                if (parasArray.contains("vibration")) {
                    settings.setVibration(data.getInteger("vibration"));
                }
                settingsService.updateSettingsByUserId(settings);

                JSONObject json2 = new JSONObject();
                json2.put("token", newToken);

                json.put("data", json2);
                json.put("state", "200");
            } else {
                json.put("state", "300");
                json.put("error", "未传递需要更新的数据！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员！");
            logger.error(e.getMessage());
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 获取文件服务
     * 
     * @author lzming 2016-03-12
     * */
    @RequestMapping(value = "/getOSSSecurityToken", method = RequestMethod.POST)
    @ResponseBody
    public void getOSSSecurityToken(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            String newToken = (String) request.getAttribute("token");
            JSONObject access = RedisUtil.getOSSinfo();
            if (access != null) {
                access.put("token", newToken);
                json.put("data", access);
                json.put("state", "200");

            } else {
                json.put("state", "300");
                json.put("error", "获取失败，请联系管理员！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员！");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 更新用户位置
     * 
     * 
     * */
    @RequestMapping(value = "/updateLocation", method = RequestMethod.POST)
    @ResponseBody
    public void updateLocation(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String newToken = (String) request.getAttribute("token");
            String userid = (String) request.getAttribute("userid");

            Double longitude = data.getDouble("longitude");
            Double dimension = data.getDouble("dimension");

            User user = new User();
            user.setId(userid);
            user.setLongitude(longitude);
            user.setDimension(dimension);
            userService.updateLocation(user);

            json.put("state", "200");
            JSONObject json2 = new JSONObject();
            json2.put("token", newToken);
            json.put("data", json2);

        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员！");
            e.getMessage();
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
