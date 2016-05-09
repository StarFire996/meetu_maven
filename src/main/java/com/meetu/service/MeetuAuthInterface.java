package com.meetu.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meetu.config.Constants;
import com.meetu.core.base.BaseController;
import com.meetu.device.domain.UserDevice;
import com.meetu.domain.User;
import com.meetu.tags.domain.MeetuUserSettings;
import com.meetu.tags.service.MeetuFriendsRelService;
import com.meetu.tags.service.MeetuInterestTagsService;
import com.meetu.tags.service.MeetuPersonalizedTagsService;
import com.meetu.tags.service.MeetuReferencesService;
import com.meetu.util.Common;
import com.meetu.util.RedisUtil;
import com.meetu.util.StsService;

@Controller
@RequestMapping(value = "app/auth")
public class MeetuAuthInterface extends BaseController {
    public static Logger log = Logger.getLogger(MeetuAuthInterface.class);

    @Autowired
    private MeetuAuthService mas;

    @Autowired
    private UserService userService;

    @Autowired
    private MeetuPersonalizedTagsService ptService;

    @Autowired
    private MeetuInterestTagsService itService;

    @Autowired
    private MeetuReferencesService referencesService;

    @Autowired
    private MeetuFriendsRelService friendsRelService;

    /**
     * 登录 2016-03-08
     * */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public void login(HttpServletRequest request, HttpServletResponse response) {

        JSONObject json = new JSONObject();
        try {

            if (this.getPara("data") != null && !this.getPara("data").equals("")) {
                JSONObject data = JSONObject.parseObject(this.getPara("data"));

                String phone = data.getString("phone");
                String password = data.getString("password");
                String deviceCode = data.getString("device_code");
                if (phone != null && !phone.equals("") && password != null && !password.equals("")
                        && deviceCode != null && !deviceCode.equals("")) {
                    User user = new User();
                    user.setPhone(phone);
                    user.setPassword(password);

                    // check(phone&password) return id
                    String check = userService.checkPhoneAndPwd(user);
                    if (null == check) {
                        json.put("state", "301");
                        json.put("error", "手机号不存在！");
                    } else if ("0".equals(check)) {
                        json.put("state", "302");
                        json.put("error", "密码错误！");
                    } else {

                        // String userid = userService.selectIdByPhone(data.getString("phone"));
                        // redis login
                        String _tk = Common.generateToken();
                        boolean redisRet = RedisUtil.userLogin(check, _tk, deviceCode);
                        if (redisRet) {
                            User _user = userService.selectUserById(check);

                            JSONObject json2 = new JSONObject();
                            json2.put("token", _tk);
                            json2.put("username", String.valueOf(_user.getCode()));
                            json2.put("password", Common.encryptByMD5(password));
                            json2.put("code", _user.getCode().toString());
                            json2.put("nickname", _user.getNickname());
                            json2.put("icon_url", StsService.generateCircleUrl(_user.getIcon_url()));
                            json2.put("virtual_currency", String.valueOf(_user.getVirtual_currency()));
                            
                            json.put("data", json2);
                            json.put("state", "200");
                        } else {
                            json.put("state", "300");
                            json.put("error", "Redis服务出现问题，请联系管理员！");
                        }
                    }
                } else {
                    json.put("state", "300");
                    json.put("error", "请检查数据！");
                }
            } else {
                json.put("state", "300");
                json.put("error", "请检查数据！");
            }

        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }
    

    /**
     * 注销 2016-03-08 2016-03-25 新增app传递参数user_code，清除Redis中百度云推送channelid，device_type
     * */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        JSONObject json = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            String _tk = data.getString("token");
            String user_code = data.getString("user_code");

            if (data != null && _tk != null && user_code != null) {
                String user_id = userService.selectIdByCode(Integer.parseInt(user_code));
                // redis delete key
                RedisUtil.logout(user_id, _tk);
                // Long redisRet = RedisUtil.delString(Constants.REDIS_TOKEN_PREFIX.concat(_tk));
                // if(redisRet == null){
                // json.put("state", "300");
                // json.put("error", "Redis服务出现问题，请联系管理员！");
                //
                // }else{
                /*
                 * SendMessageAPI sendMessageAPIImpl = new EasemobMessages(); String from =
                 * "example_user"; String target1 = "example_user_0"; String target2 =
                 * "example_user_1";
                 * 
                 * // 给用户发一条透传消息 Map<String, String> extension = new HashMap<String, String>();
                 * extension.put("executor", from); ObjectNode sendCmdMessageNode = (ObjectNode)
                 * sendMessageAPIImpl.sendMessage(MsgTargetType.USERS, new String[] {target1,
                 * target2}, new CommandMessageBody("Hello world!"), from, extension); if (null !=
                 * sendCmdMessageNode) { log.info("给用户发一条透传消息: " + sendCmdMessageNode.toString()); }
                 */

                json.put("state", "200");
                // }
            } else {
                json.put("state", "300");
                json.put("error", "请检查数据！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 注册 2016-03-05
     * */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public void register(HttpServletRequest request, HttpServletResponse response) {
        // URLDecoder.decode(request.getParameter("businessCode"),"utf-8");
        JSONObject json = new JSONObject();
        String _tk = Common.generateToken();
        try {
            if (this.getPara("data") != null && !this.getPara("data").equals("")) {

                JSONObject data = JSONObject.parseObject(this.getPara("data"));

                JSONObject dataCheck = registerCheck(data);
                if (dataCheck.getBoolean("state")) {// 数据校验
                    User user = new User();
                    UserDevice userDevice = new UserDevice();
                    MeetuUserSettings settings = new MeetuUserSettings();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthDate = sdf.parse(data.getString("birth_date"));
                    // System.out.println(new Date().toString()+"____register___data: " +
                    // data.toJSONString());
                    user.setId(Common.generateId());
                    user.setNickname(data.getString("nickname"));
                    user.setSex(data.getString("sex"));
                    user.setBirth_date(birthDate);
                    user.setStarsign(Common.getConstellation(birthDate));
                    user.setIsgraduated(data.get("isgraduated").toString());
                    user.setSchool(data.get("school") == null ? "" : data.get("school").toString());
                    user.setCity(data.getString("city"));
                    user.setCityf(data.get("cityf") == null ? "" : data.get("cityf").toString());
                    user.setCareer(data.get("career") == null ? "" : data.get("career").toString());
                    user.setPhone(data.getString("phone"));
                    user.setPassword(data.getString("password"));
                    user.setIcon_url(data.getString("icon_url"));
                    // user.setOriginal_icon_url(data.getString("original_icon_url"));
                    user.setRegister_date(new Date());
                    user.setPassword_hx(Common.encryptByMD5(data.getString("password")));
                    user.setUser_type("mobile");
                    user.setVirtual_currency(10);
                    user.setActivity_time(new Date());
                    user.setIcon_is_validate("1");

                    userDevice.setId(Common.generateId());
                    userDevice.setDevice_name(data.getString("device_name"));
                    userDevice.setDevice_code(data.getString("device_code"));
                    userDevice.setUser_id(user.getId());
                    userDevice.setStatus("1");

                    settings.setId(Common.generateId());
                    settings.setSex(data.getString("sex").equals("1") ? "2" : "1");
                    settings.setCity("1");
                    settings.setUser_id(user.getId());
                    settings.setUpdate_date(user.getRegister_date());
                    settings.setMessage(1);
                    settings.setSound(1);
                    settings.setVibration(1);

                    settings.setAge_down((Common.getAgeByBirthday(user.getBirth_date()) - 5));
                    settings.setAge_up(Common.getAgeByBirthday(user.getBirth_date()) + 5);
                    // insert mysql
                    mas.register(user, userDevice, settings);
                    Integer code = userService.selectCodeById(user.getId());

                    /**
                     * redis hset selectUserById
                     * */
                    boolean redisRet = RedisUtil.userLogin(user.getId(), _tk, userDevice.getDevice_code());
                    if (redisRet) {

                        // easemob
                        IMUserAPI userAPIImpl = new EasemobIMUsers();
                        ObjectNode createNewIMUserSingleNode1 = (ObjectNode) userAPIImpl
                                .createNewIMUserSingle(JsonNodeFactory.instance.objectNode()
                                        .put("username", String.valueOf(code))
                                        .put("password", user.getPassword_hx())
                                        .put("nickname", user.getNickname()));
                        if (null != createNewIMUserSingleNode1) {
                            if (!createNewIMUserSingleNode1.get("statusCode").toString().equals("200")) {
                                // fails
                                RedisUtil.delString(Constants.REDIS_TOKEN_PREFIX.concat(_tk));
                                mas.delRegisterInfo(user.getId(), userDevice.getId(), settings.getId());
                                json.put("state", "303");
                                json.put("error", "环信： " + createNewIMUserSingleNode1.get("error").toString()
                                        + "请联系管理员");
                            } else {

                                json.put("state", "200");
                                JSONObject json2 = new JSONObject();
                                json2.put("token", _tk);
                                json2.put("username", String.valueOf(code));
                                json2.put("password", user.getPassword_hx());
                                json2.put("code", code.toString());
                                json2.put("nickname", user.getNickname());
                                json2.put("icon_url", StsService.generateUrl(user.getIcon_url()));
                                json2.put("icon_thumbnailUrl",
                                        StsService.generateCircleUrl(user.getIcon_url()));

                                json.put("data", json2);
                            }
                        } else {// 环信api返回null
                            RedisUtil.delString(Constants.REDIS_TOKEN_PREFIX.concat(_tk));
                            mas.delRegisterInfo(user.getId(), userDevice.getId(), settings.getId());
                            json.put("state", "300");
                            json.put("error", "环信接口调用错误，请联系管理员！");
                        }
                    } else {
                        mas.delRegisterInfo(user.getId(), userDevice.getId(), settings.getId());
                        json.put("state", "300");
                        json.put("error", "Redis服务连接问题，请联系管理员！");
                    }

                } else {
                    json.put("state", "300");
                    json.put("error", dataCheck.getString("error"));
                }
            } else {
                json.put("state", "300");
                json.put("error", "data参数为空");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 修改密码
     * 
     * */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public void updatePassword(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            JSONObject data = JSONObject.parseObject(this.getPara("data"));
            // String newToken = (String) request.getAttribute("token");
            // String userid = (String) request.getAttribute("userid");

            String newPassword = data.getString("password");
            String phone = data.getString("phone");
            if (newPassword != null && !newPassword.equals("") && phone != null && !phone.equals("")) {

                mas.updatePassword(phone, newPassword);
                json.put("state", "200");

            } else {
                json.put("state", "300");
                json.put("error", "请检查参数！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        this.renderJson(response, json.toString());
    }

    /**
     * 修改密码之前，先验证旧密码(删除)
     * */
    @RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
    @ResponseBody
    public void checkPassword(HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     * 判断用户是否已登录
     * 
     * */
    @RequestMapping(value = "/checkIsLoggedIn", method = RequestMethod.POST)
    @ResponseBody
    public void checkIsLoggedIn(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

    }

    /**
     * 判断手机号是否已登录without Token
     * 
     * */
    @RequestMapping(value = "/checkPhoneIsRegistered", method = RequestMethod.POST)
    @ResponseBody
    public void checkPhoneIsRegistered(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {

            if (this.getPara("data") != null) {
                JSONObject data = JSONObject.parseObject(this.getPara("data"));
                String phone = data.getString("phone");
                if (phone != null && Common.checkMobileNumber(phone)) {

                    JSONObject json2 = new JSONObject();
                    String id = userService.selectIdByPhone(phone);
                    if (id == null) {
                        json2.put("result", "0");
                    } else {
                        json2.put("result", "1");
                    }
                    json.put("data", json2);
                    json.put("state", "200");

                } else {
                    json.put("state", "300");
                    json.put("error", "phone参数有误！");
                }
            } else {
                json.put("state", "300");
                json.put("error", "data参数有误！");
            }
        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
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

    public JSONObject registerCheck(JSONObject data) {
        JSONObject json = new JSONObject();

        if (data.getString("nickname") == null || data.getString("nickname").equals("")) {
            json.put("state", false);
            json.put("error", "未填写昵称！");
        } else if (data.getString("sex") == null || data.getString("sex").equals("")) {
            json.put("state", false);
            json.put("error", "未选择性别！");
        } else if (data.getString("birth_date") == null || data.getString("birth_date").equals("")) {
            json.put("state", false);
            json.put("error", "未填写生日！");
        } else if (data.get("isgraduated") == null || data.get("isgraduated").toString().equals("")) {
            json.put("state", false);
            json.put("error", "未选择是否毕业！");
        } else if (data.getString("city") == null || data.getString("city").equals("")) {
            json.put("state", false);
            json.put("error", "未填写所在城市！");
        } else if (data.getString("phone") == null || data.getString("phone").equals("")) {
            json.put("state", false);
            json.put("error", "未填写手机号！");
        } else if (data.getString("password") == null || data.getString("password").equals("")) {
            json.put("state", false);
            json.put("error", "未填写密码！");
        } else if (data.getString("icon_url") == null || data.getString("icon_url").equals("")) {
            json.put("state", false);
            json.put("error", "未上传头像！");
        } else if (data.getString("device_code") == null || data.getString("device_code").equals("")) {
            json.put("state", false);
            json.put("error", "未填写设备编码！");
        } else {
            json.put("state", true);
        }

        if (json.getBoolean("state")) {
            if (data.get("isgraduated").toString().equals("1")) {// 在读
                if (data.getString("school") == null || data.getString("school").equals("")) {
                    json.put("state", false);
                    json.put("error", "未选择所在学校！");
                }
            } else if (data.get("isgraduated").toString().equals("2")) {// 已毕业
                if (data.getString("career") == null || data.getString("career").equals("")) {
                    json.put("state", false);
                    json.put("error", "未选择职业！");
                }
            } else {
                json.put("state", false);
                json.put("error", "是否毕业信息错误！");
            }
        }
        return json;
    }

    /**
     * 获取文件服务 不需要Token
     * 
     * @author lzming 2016-03-12
     * */
    @RequestMapping(value = "/getOSSSecurityTokenWithoutT", method = RequestMethod.POST)
    @ResponseBody
    public void getOSSSecurityTokenWithoutT(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            JSONObject access = RedisUtil.getOSSinfo();
            if (access != null) {
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
     * 未登录
     * 
     * @author lzming 2016-03-12
     * */
    @RequestMapping(value = "/biubiuListUnlogin", method = RequestMethod.POST)
    @ResponseBody
    public void biubiuListUnlogin(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        try {
            Integer num = mas.getSettingByKey(Constants.biubiuListNumbers) == null ? Constants.biubiuListNumbers_default
                    : mas.getSettingByKey(Constants.biubiuListNumbers);
            Integer biuExpire = mas.getSettingByKey(Constants.biuExpire) == null ? Constants.biuExpire_default
                    : mas.getSettingByKey(Constants.biuExpire);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, -biuExpire);
            List<Map<String, Object>> list = referencesService
                    .selectReferencesListUnlogin(num, cal.getTime());

            JSONArray arr = new JSONArray();
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    JSONObject jb = new JSONObject();
                    jb.put("icon_thumbnailUrl",
                            list.get(i).get("icon_url") == null ? "" : StsService.generateCircleUrl(list
                                    .get(i).get("icon_url").toString()));
                    jb.put("user_code", list.get(i).get("code").toString());
                    jb.put("already_seen", "1");
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
                    jb.put("chat_id", list.get(i).get("chat_id") == null ? "" : list.get(i).get("chat_id")
                            .toString());
                    jb.put("reference_id", list.get(i).get("id").toString());
                    jb.put("time", list.get(i).get("date"));
                    arr.add(jb);
                }
            }
            JSONObject json2 = new JSONObject();
            json2.put("users", arr);
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
     * 向指定用户发测试biu
     * 
     * @param 2016-03-24
     * 
    @RequestMapping(value = "/sendBiubiu", method = RequestMethod.POST)
    @ResponseBody
    public void sendBiubiu(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        try {
            // mas.f();
            JSONObject data = JSONObject.parseObject(this.getPara("data"));

            String fromUserId = data.getString("from_user_id");// 发biu的人
            String toUserId = data.getString("to_user_id");// 发给谁
            User user = userService.selectUserById(fromUserId);// 用户信息

            List<Map<String, Object>> users = userService.selectBiuTest(toUserId);

            JSONObject message = new JSONObject();
            message.put("user_code", user.getCode().toString());
            message.put("nickname", user.getNickname());
            message.put("icon_thumbnailUrl", StsService.generateCircleUrl(user.getIcon_url()));
            message.put("chat_id", "11111111111111111");
            message.put("time", new Date());
            // 三种推送，"0"收到的biu，"1"通知用户你的biu被抢了，"2"通知用户推荐给你的biu已经被抢了
            message.put("messageType", "1");
            //
            mas.pushMsgToSingleDevice(toUserId, message);
            // 发送成功之后奖励biu币
            // Integer vc = mas.getSettingByKey(Constants.sendRewards)==null?
            // Constants.sendRewards_default:mas.getSettingByKey(Constants.sendRewards);
            // user.setVirtual_currency(vc);
            // userService.addVC(user);
            // json2.put("user", user);
            // json.put("data", json2);
            // json.put("state", "200");

            // List<Map<String, Object>> users =
            // menuDao.findSQL("select id,birth_date,starsign from sys_user where user_type='mobile'");
            // for(int i=0; i<users.size(); i++){
            // Map<String, Object> map = users.get(i);
            // if(map.get("STARSIGN")==null){
            // Date date =(Date) map.get("BIRTH_DATE");
            // String star = Common.getConstellation(date);
            // System.out.println(map.get("ID")+"___"+star);
            // menuDao.updateSQL("update sys_user set starsign=? where id=?", new Object[]{star,
            // map.get("ID")});
            // }
            // }

        } catch (Exception e) {
            json.put("state", "300");
            json.put("error", e.getMessage() + " 请联系管理员");
            e.printStackTrace();
        }
        // System.out.println(new Date().toString()+"____sendBiubiu___data: " +
        // json.toJSONString());
        this.renderJson(response, json.toString());
    }
*/
}
