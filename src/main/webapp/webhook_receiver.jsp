<%@page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@page import="net.sf.json.JSONObject" %>
<%@page import="java.io.BufferedReader" %>
<%@page import="java.io.UnsupportedEncodingException" %>
<%@page import="org.apache.commons.codec.digest.DigestUtils" %>
<%@page import="cn.beecloud.*" %>
<%@page import="org.apache.log4j.*" %>
<%@page import="com.meetu.photos.domain.MeetuTradingRecord" %>
<%@page import="org.springframework.beans.factory.annotation.Autowired" %>
<%@page import="com.meetu.photos.service.MeetuTradingRecordService" %>
<%@page import="com.meetu.service.MeetuAuthService" %>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@page import="org.springframework.web.context.WebApplicationContext"%>

<%
WebApplicationContext wac =  WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
MeetuTradingRecordService  tradingRecordService = (MeetuTradingRecordService)wac.getBean("tradingRecordService");
//System.out.println("tradingRecordService:"+tradingRecordService.toString());
MeetuAuthService authService = (MeetuAuthService)wac.getBean("authService");
//System.out.println("authService:"+authService.toString());
%>
<%!
	
    Logger log = Logger.getLogger(this.getClass());

    boolean verify(String sign, String text, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        log.info("mysign:" + mysign);

        long timeDifference = System.currentTimeMillis() - Long.valueOf(key);
        log.info("timeDifference:" + timeDifference);
        if (mysign.equals(sign) && timeDifference <= 300000) {
            return true;
        } else {
            return false;
        }
    }

    boolean verifySign(String sign, String timestamp) {
        log.info("sign:" + sign);
        log.info("timestamp:" + timestamp);

        return verify(sign, BCCache.getAppID() + BCCache.getAppSecret(),
                timestamp, "UTF-8");

    }

    byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
%>

<%
//
    BeeCloud.registerApp("3adc89a6-617f-4445-8f23-2b805df90fe4",null,"2f5add66-01cf-4024-9efe-e4c183f79205","a0529c9e-bd26-4d49-8b30-3dcafc344ea0");
    StringBuffer json = new StringBuffer();
    String line = null;

    try {
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    JSONObject jsonObj = JSONObject.fromObject(json.toString());
    String sign = jsonObj.getString("sign");
    String timestamp = jsonObj.getString("timestamp");

    boolean status = verifySign(sign, timestamp);
    if (status) { //验证成功
        out.println("success"); //请不要修改或删除
		String channel_type = jsonObj.getString("channel_type");
        JSONObject message_detail = jsonObj.getJSONObject("messageDetail"); 
    	String out_trade_no = message_detail.getString("out_trade_no");
		String total_fee = message_detail.getString("total_fee");
		Integer totalFee = 0;
		if("ALI".equals(channel_type)){//如果是支付宝
			Double db = Double.parseDouble(total_fee);
			totalFee = (int) (db*100);
		}else{
			totalFee = Integer.parseInt(total_fee);
		}
		
		MeetuTradingRecord record = tradingRecordService.selectAll(out_trade_no);
		if(record!=null && totalFee.equals(record.getTotalfee())){
			authService.updateVC(out_trade_no, record.getUser_id(), record.getTotalnum());
		}else{
			tradingRecordService.updateResult(out_trade_no, 2);
		}
		
    } else {
        out.println("fail");
    }
%>
