package com.meetu.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.easemob.server.api.IMUserAPI;
import com.easemob.server.api.SendMessageAPI;
import com.easemob.server.jersey.EasemobIMUsers;
import com.easemob.server.jersey.EasemobMessages;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meetu.config.Constants;
import com.meetu.device.domain.UserDevice;
import com.meetu.device.service.UserDeviceService;
import com.meetu.domain.User;
import com.meetu.photos.service.MeetuPhotosService;
import com.meetu.photos.service.MeetuTradingRecordService;
import com.meetu.tags.domain.MeetuChatList;
import com.meetu.tags.domain.MeetuFriendsRel;
import com.meetu.tags.domain.MeetuReferences;
import com.meetu.tags.domain.MeetuUserSettings;
import com.meetu.tags.domain.SysSettings;
import com.meetu.tags.service.MeetuChatListService;
import com.meetu.tags.service.MeetuChatTagsService;
import com.meetu.tags.service.MeetuFriendsRelService;
import com.meetu.tags.service.MeetuInterestTagsService;
import com.meetu.tags.service.MeetuPersonalizedTagsService;
import com.meetu.tags.service.MeetuReferencesService;
import com.meetu.tags.service.MeetuUserSettingsService;
import com.meetu.tags.service.SysSettingsService;
import com.meetu.util.ApiService;
import com.meetu.util.Common;
import com.meetu.util.LoggerUtils;
import com.meetu.util.RedisUtil;
import com.meetu.util.StsService;

@Service("authService")
@Transactional
public class MeetuAuthService {
    public static Logger log = Logger.getLogger(MeetuAuthService.class);

    public static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MeetuAuthService.class);

    @Autowired
    ApiService apiService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private MeetuChatTagsService cts;

    @Autowired
    private MeetuPersonalizedTagsService pts;

    @Autowired
    private MeetuInterestTagsService its;

    @Autowired
    private MeetuUserSettingsService settingsService;

    @Autowired
    private MeetuPhotosService photosService;

    @Autowired
    private MeetuChatListService chatlistService;

    @Autowired
    private MeetuReferencesService referencesService;

    @Autowired
    private SysSettingsService ssService;

    @Autowired
    private MeetuFriendsRelService relService;

    @Autowired
    private MeetuTradingRecordService tradingRecordService;

    private final String REDIS_TAGS_CHAT = "CHAT_TAGS";

    private final String REDIS_TAGS_INTEREST = "INTEREST_TAGS";

    private final String REDIS_TAGS_PERSONALIZED = "PERSONALIZED_TAGS";

    private static final JsonNodeFactory factory = new JsonNodeFactory(false);

    @Transactional
    public String login(User user) {
        String ret = null;
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Transactional
    public void register(User user, UserDevice userDevice, MeetuUserSettings settings) throws Exception {
        userService.insertOper(user);
        userDeviceService.insert(userDevice);
        settingsService.insertOper(settings);
    }

    public void delRegisterInfo(String userId, String userDeviceId, String settingsId) throws Exception {
        userService.deleteByPrimaryKey(userId);
        userDeviceService.deleteByPrimaryKey(userDeviceId);
        settingsService.deleteByPrimaryKey(settingsId);
    }

    /**
     * 获取标签数据 修改返回值形式
     * */
    public JSONArray getTags(String type, String sex) throws Exception {
        JSONArray ja = new JSONArray();
        List<Map<String, Object>> ret = null;
        String _str = null;
        if (type.equals("chat")) {
            _str = RedisUtil.getString(REDIS_TAGS_CHAT);
            if (_str != null) {
                return JSONArray.parseArray(_str);
            } else {
                ret = cts.selectAll();
                if (ret != null && ret.size() > 0) {
                    for (int i = 0; i < ret.size(); i++) {
                        JSONObject json = new JSONObject();
                        json.put("code", ret.get(i).get("id").toString());
                        json.put("name", ret.get(i).get("name").toString());
                        ja.add(json);
                    }
                    RedisUtil.setString(REDIS_TAGS_CHAT, ja.toJSONString());
                } else {
                    return null;
                }
            }
        } else if (type.equals("interest")) {
            _str = RedisUtil.getString(REDIS_TAGS_INTEREST);
            if (_str != null) {
                return JSONArray.parseArray(_str);
            } else {
                ret = its.selectAll();
                if (ret != null && ret.size() > 0) {
                    // get types
                    JSONArray type_array = new JSONArray();
                    for (int i = 0; i < ret.size(); i++) {
                        if (type_array.indexOf(ret.get(i).get("typecode").toString()) == -1) {
                            type_array.add(ret.get(i).get("typecode").toString());
                        }
                    }

                    for (int j = 0; j < type_array.size(); j++) {
                        JSONObject jb = new JSONObject();
                        jb.put("typecode", type_array.get(j));

                        JSONArray data_array = new JSONArray();
                        for (int k = 0; k < ret.size(); k++) {
                            if (type_array.get(j).equals(ret.get(k).get("typecode").toString())) {

                                JSONObject tag = new JSONObject();
                                tag.put("code", ret.get(k).get("id").toString());
                                tag.put("name", ret.get(k).get("name").toString());
                                if (jb.getString("typename") == null) {
                                    jb.put("typename", ret.get(k).get("typename").toString());
                                }
                                data_array.add(tag);
                            }
                        }
                        jb.put("data", data_array);

                        ja.add(jb);
                    }
                    RedisUtil.setString(REDIS_TAGS_INTEREST, ja.toJSONString());
                } else {
                    return null;
                }
            }

        } else {
            _str = RedisUtil.getString(REDIS_TAGS_PERSONALIZED.concat(sex));
            if (_str != null) {
                return JSONArray.parseArray(_str);
            } else {
                ret = pts.selectAll(sex);
                if (ret != null && ret.size() > 0) {
                    for (int i = 0; i < ret.size(); i++) {
                        JSONObject json = new JSONObject();
                        json.put("code", ret.get(i).get("id").toString());
                        json.put("name", ret.get(i).get("name").toString());
                        ja.add(json);
                    }
                    RedisUtil.setString(REDIS_TAGS_PERSONALIZED.concat(sex), ja.toJSONString());
                } else {
                    return null;
                }
            }
        }
        return ja;
    }

    /**
     * 兴趣标签，返回App端
     * 
     * @param tagslist name, code, typecode, typename
     * */
    public JSONArray handleInterestedTags(List<Map<String, Object>> tagslist) {
        if (tagslist == null) {
            return null;
        }
        JSONArray ret_array = new JSONArray();

        JSONArray type_array = new JSONArray();
        for (int i = 0; i < tagslist.size(); i++) {
            if (type_array.indexOf(tagslist.get(i).get("typecode").toString()) == -1) {
                type_array.add(tagslist.get(i).get("typecode").toString());
            }
        }

        for (int j = 0; j < type_array.size(); j++) {
            JSONObject jb = new JSONObject();
            jb.put("typecode", type_array.get(j));

            JSONArray data_array = new JSONArray();
            for (int k = 0; k < tagslist.size(); k++) {
                if (type_array.get(j).equals(tagslist.get(k).get("typecode").toString())) {

                    JSONObject tag = new JSONObject();
                    tag.put("code", tagslist.get(k).get("id").toString());
                    tag.put("name", tagslist.get(k).get("name").toString());
                    if (jb.getString("typename") == null) {
                        jb.put("typename", tagslist.get(k).get("typename").toString());
                    }
                    data_array.add(tag);
                }
            }
            jb.put("data", data_array);
            ret_array.add(jb);
        }

        return ret_array;
    }

    /**
     * 根据code获取用户照片墙信息
     * 
     * @return [{"photoName":"", "photoThumbnail":"", "photoOrigin":""}, ......]
     * @throws Exception
     * */
    public JSONArray handlePhotos(Integer code) throws Exception {

        List<Map<String, Object>> photolist = photosService.selectByUserCode(code);
        if (photolist == null) {
            return null;
        } else {
            JSONArray ret_array = new JSONArray();
            for (int i = 0; i < photolist.size(); i++) {
                JSONObject jb = new JSONObject();
                jb.put("photoCode", photolist.get(i).get("ID").toString());
                jb.put("photoName", photolist.get(i).get("PHOTO").toString());
                jb.put("photoOrigin", StsService.generateUrl(photolist.get(i).get("PHOTO").toString()));
                jb.put("photoThumbnail",
                        StsService.generateThumbnailUrl(photolist.get(i).get("PHOTO").toString()));
                ret_array.add(jb);
            }
            return ret_array;
        }
    }

    // 处理匹配用户
    @Async
    public void handleBiuList(String userid, String chat_tags)
            throws Exception {
    	
        String url ="http://101.200.150.226:8080/meetu_maven/app/push/pushMsgToDevices";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userid);
        map.put("chat_tags", chat_tags);
        
        apiService.doPost(url, map);
        System.out.println("____2");
        
    }

    /**
     * 发送单台设备透传消息
     * 
     * @throws PushServerException
     * @throws PushClientException
     * 
     *         public void pushMsgToSingleDevice(Integer deviceType, String channelId, String
     *         Message) throws PushClientException, PushServerException{ BaiduPushClient pushClient
     *         = null; if(deviceType == 3){ pushClient = getPushClient(Constants.androidPair);
     *         }else{ pushClient = getPushClient(Constants.iosPair); }
     * 
     *         PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().
     *         addChannelId(channelId). addMessageType(0). //设置消息类型,0表示透传消息,1表示通知,默认为0.
     *         addMessage(Message). addDeviceType(deviceType); //3 for android, 4 for ios
     * 
     *         PushMsgToSingleDeviceResponse response = pushClient.pushMsgToSingleDevice(request);
     * 
     *         System.out.println("BaiduPush >>> msgId: " + response.getMsgId() + ",sendTime: " +
     *         response.getSendTime()); }
     * 
    @Async
    public void pushMsgToSingleDevice(String userid, JSONObject message) {

        int messageType = 0;
        // 获取设备类型、该设备一一对应的channelId
        String channelId = RedisUtil.hget(Constants.redis_online,
                Constants.redis_online_channelId.concat(userid));
        String _dt = RedisUtil.hget(Constants.redis_online, Constants.redis_online_deviceType.concat(userid));
        if (channelId != null && _dt != null) {
            // 应用在前台还是后台，仅iOS
            String appStatus = userService.selectAppStatus(userid);
            // 获取用户设置
            MeetuUserSettings settings = settingsService.selectByUserId(userid);
            Integer mess = settings.getMessage() == null ? 1 : settings.getMessage();
            Integer sound = settings.getSound() == null ? 1 : settings.getSound();
            Integer deviceType = Integer.parseInt(_dt);

            BaiduPushClient pushClient = null;
            if (deviceType == 3) {
                pushClient = getPushClient(Constants.androidPair);
            } else {
                pushClient = getPushClient(Constants.iosPair);
                messageType = 1;
                // iOS 如果设置屏蔽，app在前台发消息在后台不发消息（0）
                // app在前台发消息在后台不发消息(1,2),与设置无关
                // APP_STATUS 0后台，1应用中

                JSONObject json = new JSONObject();
                if (message.get("messageType").equals("0")) {
                    json.put("alert", "您收到一个biu:" + message.get("nickname") + " "
                            + (message.get("sex").equals("1") ? "男" : "女") + " " + message.get("age") + "岁 ");
                    if (sound.equals(1)) {
                        json.put("sound", "biubiu.wav");
                    }
                    json.put("badge", 1);
                } else {
                    json.put("content-available", 1);
                    // json.put("alert", "您收到一个biu");
                }

                // 其值为数字，表示当通知到达设备时，应用的角标变为多少。如果没有使用这个字段，那么应用的角标将不会改变。设置为 0 时，会清除应用的角标。
                message.put("aps", json);

            }
            try {
                // System.out.println(userid+"_mess"+mess+"_messageType"+message.get("messageType")+"_appStatus"+appStatus+"_deviceType"+deviceType);
                // if(deviceType==3
                // ||(deviceType==4&&message.get("messageType").equals("0")&&mess.equals(1))
                // ||(deviceType==4&&!message.get("messageType").equals("0")&&appStatus!=null&&appStatus.equals("1"))){
                if (deviceType == 3
                        || (deviceType == 4 && message.get("messageType").equals("0") && mess.equals(1))
                        || (deviceType == 4 && !message.get("messageType").equals("0"))) {
                    PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest()
                            .addChannelId(channelId).addMsgExpires(1).addMessageType(messageType)
                            .addMessage(message.toString()).addDeviceType(deviceType).addDeployStatus(2);

                    PushMsgToSingleDeviceResponse response = pushClient.pushMsgToSingleDevice(request);

                    // System.out.println(userid);
                    // System.out.println("msgId: " + response.getMsgId() + ",sendTime: " +
                    // response.getSendTime());
//                    log.info("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime());
                }
            } catch (PushClientException e) {
                e.printStackTrace();
            } catch (PushServerException e) {
                System.out.println(String.format("userid: %s, requestId: %d, errorCode: %d, errorMsg: %s",
                        userid, e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
    }
     */
    /***
     * public JSONObject getAps(String userid, JSONObject message, MeetuUserSettings settings){
     * JSONObject json = new JSONObject(); if(settings != null){ Integer mess =
     * settings.getMessage(); Integer sound = settings.getSound(); if(mess!=null&&mess.equals(new
     * Integer(1))){ json.put("alert",
     * "您收到一个biu:"+message.get("nickname")+" "+message.get("sex")+" "+message.get("age")+" ");
     * if(sound.equals(new Integer(1))){ json.put("sound", "biubiu.wav"); } json.put("badge", 1);
     * return json; }else{ return null; } }else{ json.put("alert",
     * "您收到一个biu:"+message.get("nickname")+" "+message.get("sex")+" "+message.get("age")+" ");
     * json.put("sound", "biubiu.wav"); json.put("badge", 1); return json; } }
     **/
    public BaiduPushClient getPushClient(PushKeyPair pair) {

        BaiduPushClient pushClient = new BaiduPushClient(pair, BaiduPushConstants.CHANNEL_REST_URL);
        pushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });
        return pushClient;
    }

    /**
     * 计算匹配度
     * 
     * @param hitTags 命中的个性标签 interestedTags 共同的兴趣标签
     * @throws Exception
     * */
    public Integer calMatchScore(String[] hitTags, String[] interestedTags) throws Exception {
        // 预设匹配权重
        Integer param_init = getSettingByKey(Constants.param_init) == null ? Constants.param_init_default
                : getSettingByKey(Constants.param_init);
        // 个性标签最大权重
        Integer param_pers = getSettingByKey(Constants.param_pers) == null ? Constants.param_pers_default
                : getSettingByKey(Constants.param_pers);
        // 兴趣标签最大权重
        Integer param_inte = getSettingByKey(Constants.param_inte) == null ? Constants.param_inte_default
                : getSettingByKey(Constants.param_inte);
        // 个性标签命中数量超过param_pers_num个，按param_pers计算
        Integer param_pers_num = getSettingByKey(Constants.param_pers_num) == null ? Constants.param_pers_num_default
                : getSettingByKey(Constants.param_pers_num);
        // 兴趣爱好超过param_inte_num个，按param_inte计算
        Integer param_inte_num = getSettingByKey(Constants.param_inte_num) == null ? Constants.param_inte_num_default
                : getSettingByKey(Constants.param_inte_num);

        Integer score = param_init;
        if (hitTags.length >= param_pers_num) {
            score += param_pers;
        } else {
            score += param_pers * hitTags.length / param_pers_num;
        }
        if (interestedTags.length >= param_inte_num) {
            score += param_inte;
        } else {
            score += param_inte * interestedTags.length / param_inte_num;
        }
        return score;
    }

    // 计算共同标签
    public String[] calCommonTags(String fromTags, String toTags) {
        if (fromTags == null || toTags == null) {
            return null;
        }
        if (fromTags.equals("") && toTags.equals("")) {
            return (new String[] {});
        }
        String[] fromArray = fromTags.split(",");
        String[] toArray = toTags.split(",");
        String[] intersect = intersect(fromArray, toArray);
        return intersect;
    }

    // 求两个字符串数组的并集，利用set的元素唯一性
    public static String[] union(String[] arr1, String[] arr2) {
        Set<String> set = new HashSet<String>();
        for (String str : arr1) {
            set.add(str);
        }
        for (String str : arr2) {
            set.add(str);
        }
        String[] result = {};
        return set.toArray(result);
    }

    // 求两个数组的交集
    public static String[] intersect(String[] arr1, String[] arr2) {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        List<String> list = new ArrayList<String>();
        for (String str : arr1) {
            if (!map.containsKey(str)) {
                map.put(str, Boolean.FALSE);
            }
        }
        for (String str : arr2) {
            if (map.containsKey(str)) {
                map.put(str, Boolean.TRUE);
            }
        }

        for (Iterator<Entry<String, Boolean>> it = map.entrySet().iterator(); it.hasNext();) {
            Entry<String, Boolean> e = (Entry<String, Boolean>) it.next();
            if (e.getValue().equals(Boolean.TRUE)) {
                list.add(e.getKey());
            }
        }
        return list.toArray(new String[] {});
    }

    // 根据key获取系统配置表里的Integer数据
    public Integer getSettingByKey(String key) throws Exception {
        String data = RedisUtil.hget(Constants.paramsKey, key);
        // String data = null;
        if (data == null) {
            SysSettings sss = ssService.selectDataByKey(key);
            if (sss == null) {
                return null;
            } else {
                RedisUtil.hset(Constants.paramsKey, key, sss.getValue1().toString());
                return sss.getValue1();
            }
        }
        return Integer.parseInt(data);
    }

    /**
     * 抢biu功能实现
     * 
     * @throws Exception 废弃0407
     * @Transactional public JSONObject grabAddFriends(String userid, String chat_id, Integer vc)
     *                throws Exception {
     * 
     *                //更新chat_id对应信息 MeetuChatList chatlist = new MeetuChatList();
     *                chatlist.setId(chat_id); chatlist.setHxid(""); chatlist.setAgree_date(new
     *                Date()); chatlist.setTo_user_id(userid);
     *                chatlistService.updateGrabInfo(chatlist);
     * 
     *                //抢biu扣除biu币 User user2 = new User(); user2.setId(userid);
     *                user2.setVirtual_currency(-vc); userService.addVC(user2);
     * 
     *                JSONObject json = new JSONObject();
     * 
     *                // Integer code = userService.selectCodeById(userid);//抢biu用户code User user =
     *                userService.selectUserById(userid);//抢biu用户 // Integer founderCode =
     *                chatlistService.selectFounderCode(chat_id); // String founderId =
     *                chatlistService.selectFounderId(chat_id); MeetuChatList ChatList =
     *                chatlistService.selectChatListById(chat_id); Integer founderCode =
     *                chatlistService.selectFounderCode(chat_id); String founderId =
     *                ChatList.getFrom_user_id(); String chatTags = ChatList.getChat_tags();
     * 
     * 
     *                //环信，加好友 IMUserAPI userAPIImpl = new EasemobIMUsers(); SendMessageAPI
     *                sendMessage = new EasemobMessages(); ObjectNode addFriendSingleNode =
     *                userAPIImpl.addFriendSingle(founderCode.toString(),
     *                user.getCode().toString()); if (null != addFriendSingleNode &&
     *                addFriendSingleNode.get("statusCode").toString().equals("200")) { //双方各发送一条信息
     * 
     *                String from = founderCode.toString(); String targetTypeus = "users";
     *                ObjectNode ext = factory.objectNode(); ArrayNode targetusers =
     *                factory.arrayNode(); targetusers.add(user.getCode().toString()); ObjectNode
     *                txtmsg = factory.objectNode(); txtmsg.put("msg",
     *                chatTags==null?"现在我们可以开始聊天了!":chatTags); txtmsg.put("type","txt"); ObjectNode
     *                sendTxtMessageusernode = sendMessage.sendMessages(targetTypeus, targetusers,
     *                txtmsg, from, ext);
     * 
     *                ObjectNode ext2 = factory.objectNode(); ArrayNode targetusers2 =
     *                factory.arrayNode(); targetusers2.add(from); ObjectNode txtmsg2 =
     *                factory.objectNode(); txtmsg2.put("msg", "嗨，我抢到你的biubiu啦（话题标签："+chatTags+"）");
     *                txtmsg2.put("type","txt"); ObjectNode sendTxtMessageusernode2 =
     *                sendMessage.sendMessages(targetTypeus, targetusers2, txtmsg2,
     *                user.getCode().toString(), ext2);
     * 
     * 
     *                // //更新chat_id对应信息 // MeetuChatList chatlist = new MeetuChatList(); //
     *                chatlist.setId(chat_id); // chatlist.setHxid(""); //
     *                chatlist.setAgree_date(new Date()); // chatlist.setTo_user_id(userid); //
     *                chatlistService.updateGrabInfo(chatlist);
     * 
     *                //好友关系 addFriends(founderId, userid); addFriends(userid, founderId);
     * 
     *                grabAddFriends_f(user, chat_id, founderId, founderCode); json.put("", user);
     *                json.put("state", true); }else{ json.put("state", false); json.put("error",
     *                "环信：创建好友关系失败，请联系管理员");
     * 
     *                }
     * 
     *                return json; }
     **/
    /**
     * 抢biu功能实现
     * 
     * @throws Exception
     * 
     * */
    @Transactional
    public JSONObject grabAddFriendsV2(String userid, String chat_id, Integer vc) throws Exception {

        JSONObject json = new JSONObject();

        User user = userService.selectUserById(userid);// 抢biu用户
        MeetuChatList ChatList = chatlistService.selectChatListById(chat_id);
        Integer founderCode = chatlistService.selectFounderCode(chat_id);
        String founderId = ChatList.getFrom_user_id();
        String chatTags = ChatList.getChat_tags();

        // 环信，加好友
        IMUserAPI userAPIImpl = new EasemobIMUsers();
        SendMessageAPI sendMessage = new EasemobMessages();
        ObjectNode addFriendSingleNode = userAPIImpl.addFriendSingle(founderCode.toString(), user.getCode()
                .toString());
        if (null != addFriendSingleNode && addFriendSingleNode.get("statusCode").toString().equals("200")) {
            // 双方各发送一条信息
            /** **/
            String from = founderCode.toString();
            String targetTypeus = "users";
            ObjectNode ext = factory.objectNode();
            ArrayNode targetusers = factory.arrayNode();
            targetusers.add(user.getCode().toString());
            ObjectNode txtmsg = factory.objectNode();
            txtmsg.put("msg", chatTags == null ? "现在我们可以开始聊天了!" : chatTags);
            txtmsg.put("type", "txt");
            sendMessage.sendMessages(targetTypeus, targetusers, txtmsg, from, ext);

            ObjectNode ext2 = factory.objectNode();
            ArrayNode targetusers2 = factory.arrayNode();
            targetusers2.add(from);
            ObjectNode txtmsg2 = factory.objectNode();
            txtmsg2.put("msg", "嗨，我抢到你的biubiu啦（话题标签：" + chatTags + "）");
            txtmsg2.put("type", "txt");
            sendMessage.sendMessages(targetTypeus, targetusers2, txtmsg2, user.getCode().toString(), ext2);

            // 更新chat_id对应信息
            MeetuChatList chatlist = new MeetuChatList();
            chatlist.setId(chat_id);
            chatlist.setHxid("");
            chatlist.setAgree_date(new Date());
            chatlist.setTo_user_id(userid);
            chatlistService.updateGrabInfo(chatlist);

            // 抢biu扣除biu币
            User user2 = new User();
            user2.setId(userid);
            user2.setVirtual_currency(-vc);
            userService.addVC(user2);

            // 好友关系
            addFriends(founderId, userid);
            addFriends(userid, founderId);

            json.put("user", user);
            json.put("founderId", founderId);
            json.put("founderCode", founderCode);
            json.put("state", true);
        } else {
            json.put("state", false);
            json.put("error", "环信：创建好友关系失败，请联系管理员");

        }

        return json;
    }

    @Async
    public void grabAddFriends_f(User user, String chat_id, String founderId, Integer founderCode) {


    	String url ="http://101.200.150.226:8080/meetu_maven/app/push/getMsgToDevices";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", user.getId());
        map.put("chat_id", chat_id);
        map.put("founderId", founderId);
        map.put("founderCode", founderCode);
        
        apiService.doPost(url, map);
        System.out.println("____2");
    	
    }

    // 双方加好友
    public void addFriends(String id1, String id2) throws Exception {
        if (id1 == id2 || isFriends(id1, id2)) {

        } else {
            MeetuFriendsRel friendsRel = new MeetuFriendsRel();
            friendsRel.setId(Common.generateId());
            friendsRel.setDate(new Date());
            friendsRel.setUser_id1(id1);
            friendsRel.setUser_id2(id2);
            relService.insertOper(friendsRel);
        }
    }

    // 判断双方是否是好友关系
    public boolean isFriends(String id1, String id2) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user_id1", id1);
        map.put("user_id2", id2);
        if (relService.isFriends(map) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据上次发biu时间判断
     * 
     * select start_date from meetu_chatlist where from_user_id='ef5d8e39ee1b4951b71c7e21bc92b845'
     * and to_user_id is null and agree_date is null order by start_date desc limit 1
     * 
     * @return true 可以发biu
     * */
    public boolean checkSendBiu(String userid) {
        Date latestDate = chatlistService.checkSendBiu(userid);

        if (latestDate == null) {
            return true;
        } else {
            Long diff = Math.abs(new Date().getTime() - latestDate.getTime());
            Integer seconds = Math.round(diff / 1000);
            // System.out.println(new Date().toString()+"____sendBiu____checkSendBiu____"+seconds);
            if (seconds < 90) {
                return false;
            }
        }
        return true;
    }

    /**
     * 计算地球上任意两点(经纬度)距离
     * 
     * @param long1 第一点经度
     * @param lat1 第一点纬度
     * @param long2 第二点经度
     * @param lat2 第二点纬度
     * @return 返回距离 单位：米
     */
    public Long Distance(Double long1, Double lat1, Double long2, Double lat2) {
        if (long1 == null || lat1 == null || long2 == null || lat2 == null) {
            return (long) 0;
        }
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        Long dis = Math.round(d);
        return dis;
    }

    /**
     * 计算用户匹配关系，匹配度、距离、匹配时间
     * 
     * @param userid1 发biu的用户 userid2 抢biu的用户
     * @throws Exception
     * */
    public JSONObject handleMatchByUserID(String userid1, String userid2) throws Exception {
        JSONObject json = new JSONObject();
        MeetuUserSettings settings = settingsService.selectByUserId(userid1);// 发biu的用户设置
        User user1 = userService.selectUserById(userid1);// 发biu的用户信息
        User user2 = userService.selectUserById(userid2);// 抢biu

        String[] pTags = calCommonTags(
                settings.getPersonalized_tags() == null ? "" : settings.getPersonalized_tags(),
                user2.getPersonality_tags() == null ? "" : user2.getPersonality_tags());
        String[] iTags = calCommonTags(user1.getInterested_tags() == null ? "" : user1.getInterested_tags(),
                user2.getInterested_tags() == null ? "" : user2.getInterested_tags());
        Integer score = calMatchScore(pTags, iTags);

        json.put("matching_score", score);
        json.put(
                "distance",
                Distance(user1.getLongitude(), user1.getDimension(), user2.getLongitude(),
                        user2.getDimension()));
        Date date = chatlistService.selectChatDate(userid1, userid2);
        // json.put("time", date==null?new Date():date);
        json.put("time", timeInterval(new Date(), date));
        return json;

    }

    /**
     * 计算两个用户匹配关系
     * 
     * @throws Exception
     * */
    public JSONObject handleMatch(User user_from, String s_pTags, User user_to, String chat_id,
            String reference_id) throws Exception {

        JSONObject json = new JSONObject();

        String[] pTags = calCommonTags(s_pTags,
                user_to.getPersonality_tags() == null ? "" : user_to.getPersonality_tags());
        String[] iTags = calCommonTags(
                user_from.getInterested_tags() == null ? "" : user_from.getInterested_tags(),
                user_to.getInterested_tags() == null ? "" : user_to.getInterested_tags());
        Integer score = calMatchScore(pTags, iTags);

        json.put(
                "icon_thumbnailUrl",
                user_from.getIcon_url() == null ? "" : StsService.generateCircleUrl(user_from.getIcon_url()
                        .toString()));
        json.put(
                "icon_url",
                user_from.getIcon_url() == null ? "" : StsService.generateUrl(user_from.getIcon_url()
                        .toString()));
        json.put("user_code", user_from.getCode().toString());
        json.put("nickname", user_from.getNickname() == null ? "" : user_from.getNickname());
        json.put(
                "distance",
                Distance(user_from.getLongitude(), user_from.getDimension(), user_to.getLongitude(),
                        user_to.getDimension()));
        json.put("matching_score", score);
        json.put("timebefore", timeInterval(new Date(), referencesService.selectFoundDateById(reference_id)));
        json.put("description", user_from.getDescription() == null ? "" : user_from.getDescription()
                .toString());
        json.put("sex", user_from.getSex() == null ? "0" : user_from.getSex().toString());
        json.put("age", Common.getAgeByBirthday(user_from.getBirth_date()));
        json.put("birth_date", user_from.getBirth_date());
        json.put("starsign", user_from.getStarsign() == null ? "" : user_from.getStarsign().toString());
        json.put("school", user_from.getSchool() == null ? "" : user_from.getSchool().toString());
        json.put("company", user_from.getCompany() == null ? "" : user_from.getCompany().toString());
        json.put("carrer", user_from.getCareer() == null ? "" : user_from.getCareer().toString());
        json.put("isgraduated", user_from.getIsgraduated() == null ? "" : user_from.getIsgraduated()
                .toString());
        json.put("superman", user_from.getSuperman() == null ? 0 : user_from.getSuperman());
        json.put("hit_tags_num", pTags.length);
        if (pTags.length > 0) {
            List<Map<String, Object>> ret = pts.selectTags(pTags);
            if (ret != null) {
                JSONArray ja = new JSONArray();
                for (int k = 0; k < ret.size(); k++) {
                    JSONObject jb = new JSONObject();
                    jb.put("code", ret.get(k).get("id").toString());
                    jb.put("name", ret.get(k).get("name").toString());
                    ja.add(jb);
                }
                json.put("hit_tags", ja);
            } else {
                json.put("hit_tags", new JSONArray());
            }
        } else {
            json.put("hit_tags", new JSONArray());
        }
        json.put("interested_tags_num", iTags.length);
        if (iTags.length > 0) {
            List<Map<String, Object>> ret = its.selectTags(iTags);
            JSONArray ja = handleInterestedTags(ret);

            json.put("interested_tags", ja);
        } else {
            json.put("interested_tags", new JSONArray());
        }
        return json;
    }

    // 分钟
    public Integer timeInterval(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Long diff = Math.abs(date2.getTime() - date1.getTime());
            Integer min = Math.round(diff / (1000 * 60));

            return min;
        }
        return null;
    }

    /**
     * 不在关注
     * 
     * @param userid 调接口的人
     * @param muserid 不再关注谁
     * 
     * */
    public void nolongerMatch(String userid, String muserid) {
        // TODO Auto-generated method stub

    }

    public void updatePassword(String phone, String newPassword) throws Exception {
        // TODO Auto-generated method stub
        User user = new User();
        user.setPhone(phone);
        user.setPassword(newPassword);
        userService.updatePwdByPhone(user);

        Integer code = userService.selectCodeByPhone(phone);
        if (code != null) {
            IMUserAPI userAPIImpl = new EasemobIMUsers();
            String username = code.toString();
            ObjectNode json2 = JsonNodeFactory.instance.objectNode();
            json2.put("newpassword", Common.encryptByMD5(newPassword));
            ObjectNode modifyIMUserPasswordWithAdminTokenNode = userAPIImpl
                    .modifyIMUserPasswordWithAdminToken(username, json2);
            if (null != modifyIMUserPasswordWithAdminTokenNode) {
                log.info("重置IM用户密码 提供管理员token: " + modifyIMUserPasswordWithAdminTokenNode.toString());
            }
            ObjectNode imUserLoginNode2 = userAPIImpl
                    .imUserLogin(username, json2.get("newpassword").asText());
            if (null != imUserLoginNode2) {
                log.info("重置IM用户密码后,IM用户登录: " + imUserLoginNode2.toString());
            }
        } else {

        }
    }

    /**
     * @throws Exception
     * 
     * */
    @Transactional
    public void updateVC(String out_trade_no, String user_id, Integer totalnum) throws Exception {
        // TODO Auto-generated method stub
        // 抢biu扣除biu币
        User user = new User();
        user.setId(user_id);
        user.setVirtual_currency(totalnum);
        userService.addVC(user);

        tradingRecordService.updateResult(out_trade_no, 1);
    }

    public static void main(String args[]) {
        // JSONObject message = new JSONObject();
        // message.put("messageType", "2");
        // String appStatus = "1";
        // Integer deviceType = 4;
        // Integer mess = 1;
        // if(deviceType==3
        // ||(deviceType==4&&message.get("messageType").equals("0")&&mess.equals(1))
        // ||(deviceType==4&&!message.get("messageType").equals("0")&&appStatus!=null&&appStatus.equals("1"))){
        // System.out.println(1111);
        // }else{
        // System.out.println(222);
        // }
        // testAsyncMethod();
        // System.out.println("aaaaaaaa");
    }

    @Async
    public void f() {
        System.out.println("f()");
        testAsyncMethod();
    }

    @Async
    public void testAsyncMethod() {
        try {
            System.out.println("testAsyncMethod");
            // 让程序暂停100秒，相当于执行一个很耗时的任务
            Thread.sleep(10000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
