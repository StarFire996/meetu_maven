package com.meetu.push.service;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.meetu.config.Constants;
import com.meetu.service.UserService;
import com.meetu.tags.domain.MeetuUserSettings;
import com.meetu.tags.service.MeetuUserSettingsService;
import com.meetu.util.RedisUtil;

@Service("pushService")
@Transactional
public class PushService {
    public static Logger log = Logger.getLogger(PushService.class);

    public static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PushService.class);

    @Autowired
    private UserService userService;

	@Autowired
    private MeetuUserSettingsService settingsService;
	/**
	 * 解析传递过来的条件,将信息推送出去
	 * 
	 * @param conditions
	 *            传递过来的条件参数
	 * @param message
	 *            传递过来的消息
	 */
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

                     System.out.println(userid);
                     System.out.println("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime());
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
	
	
	

	public BaiduPushClient getPushClient(PushKeyPair pair) {

		BaiduPushClient pushClient = new BaiduPushClient(pair,
				BaiduPushConstants.CHANNEL_REST_URL);
		pushClient.setChannelLogHandler(new YunLogHandler() {
			@Override
			public void onHandle(YunLogEvent event) {
				//System.out.println(event.getMessage());
			}
		});
		return pushClient;
	}


}
