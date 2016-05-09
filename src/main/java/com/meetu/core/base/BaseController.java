package com.meetu.core.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

import com.meetu.core.db.Page;
import com.meetu.core.db.Record;

public abstract class BaseController {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	public HttpServletRequest request;
	@Resource
	public Properties systemConstant;
	
	
	@PostConstruct
	public void injectGate()
	{
//	    SYSTEM_CDN = systemConstant.getProperty("app.cdn");
	    logger = Logger.getLogger(getClass());
	}
	
	public void renderJson(HttpServletResponse paramHttpServletResponse, String paramString){
		
		o00000(paramHttpServletResponse, "application/json;charset=UTF-8", paramString);
	}
	
	
	
	private void o00000(HttpServletResponse paramHttpServletResponse, String paramString1, String paramString2)
	{
		paramHttpServletResponse.setContentType(paramString1);
	    paramHttpServletResponse.setHeader("Pragma", "No-cache");
	    paramHttpServletResponse.setHeader("Cache-Control", "no-cache");
	    paramHttpServletResponse.setDateHeader("Expires", 0L);
	    try
	    {
	      paramHttpServletResponse.getWriter().write(paramString2);
	    }
	    catch (IOException localIOException)
	    {
	      this.logger.error(localIOException.getMessage(), localIOException);
	    }
	}
	
	
	public String getIndexView(String paramString, ModelMap paramModelMap){
		
		String str1 = request.getParameter("iframe");
		if(StringUtils.isNotBlank(str1)){
			paramModelMap.put("iframe", str1);
		}
		String str2 = request.getContextPath();
		paramModelMap.put("path", str2);
		paramModelMap.put("root", request.getContextPath());
		paramModelMap.put("cdn", getCdnURL(request));
		return paramString;
	}
	public String getContextPath(HttpServletRequest paramHttpServletRequest)
	{
		String str1 = paramHttpServletRequest.getContextPath();
	    String str2 = new StringBuilder().append(paramHttpServletRequest.getScheme()).append("://").append(paramHttpServletRequest.getServerName()).append(":").append(paramHttpServletRequest.getServerPort()).append(str1).append("/").toString();
	    return str2;
	}
	
	public String getCdnURL(HttpServletRequest paramHttpServletRequest)
	{
		String str2 = getContextPath(paramHttpServletRequest);
		if (str2.endsWith("/"))
			str2 = str2.substring(0, str2.length() - 1);
	    return str2;
	}
	
	public void setAttr(String paramString, Object paramObject)
	{
		request.setAttribute(paramString, paramObject);
	}
	
	public abstract String index(HttpServletResponse paramHttpServletResponse, ModelMap paramModelMap);

	public abstract void save(HttpServletResponse paramHttpServletResponse);

	public abstract void del(HttpServletResponse paramHttpServletResponse);

	public abstract void update(HttpServletResponse paramHttpServletResponse);

	public abstract String add(HttpServletResponse paramHttpServletResponse);

	public abstract String show(HttpServletResponse paramHttpServletResponse);

	public abstract void query(@RequestParam Integer paramInteger1,
			@RequestParam Integer paramInteger2,
			HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse, ModelMap paramModelMap);
	
	public String getPara(String paramString)
	{
		String str = StringEscapeUtils.escapeSql(request.getParameter(paramString));
	    return str;
	}

	public net.sf.json.JSONObject toJqGrid(List<Map<String, Object>> paramPage)
	{
		return jdMethod_new(paramPage);
	}
	
	private net.sf.json.JSONObject jdMethod_new(List<Map<String, Object>> paramPage){
		JSONObject json = new JSONObject();
		
		return json;
	}

	public void ajaxDoneError(HttpServletResponse paramHttpServletResponse,
			String paramString) {
		com.alibaba.fastjson.JSONObject localJSONObject = new com.alibaba.fastjson.JSONObject();
		localJSONObject.put("state", Integer.valueOf(0));
		localJSONObject.put("statusCode", Integer.valueOf(300));
		if (StringUtils.isBlank(paramString))
			localJSONObject.put("msg", "\u64CD\u4F5C\u5931\u8D25!");
		else
			localJSONObject.put("msg", paramString);
		renderJson(paramHttpServletResponse, localJSONObject.toString());
	}
	
	public com.alibaba.fastjson.JSONObject toJqueryGrid(Page<Record> paramPage){
		
		return o00000(paramPage);
	}

	private com.alibaba.fastjson.JSONObject o00000(Page<Record> paramPage) {
		com.alibaba.fastjson.JSONObject localJSONObject1 = new com.alibaba.fastjson.JSONObject();
		localJSONObject1
				.put("page", Integer.valueOf(paramPage.getPageNumber()));
		localJSONObject1.put("records", Long.valueOf(paramPage.getTotalRow()));
		localJSONObject1
				.put("total", Integer.valueOf(paramPage.getTotalPage()));
		com.alibaba.fastjson.JSONArray localJSONArray = new com.alibaba.fastjson.JSONArray();
		List localList = paramPage.getList();
		Iterator localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			Record localRecord = (Record) localIterator.next();
			com.alibaba.fastjson.JSONObject localJSONObject2 = new com.alibaba.fastjson.JSONObject();
			localJSONObject2.put("cell", com.alibaba.fastjson.JSONObject
					.toJSON(localRecord.getColumns()));
			localJSONArray.add(localJSONObject2);
		}
		localJSONObject1.put("rows", localJSONArray);
		return localJSONObject1;
	}

	public String getParaJsonStr(String paramString) {
		net.sf.json.JSONObject localJSONObject = getPramJson();
		return localJSONObject.getString(paramString);
	}

	public net.sf.json.JSONObject getPramJson() {
		StringBuilder localStringBuilder = new StringBuilder();
		Enumeration localEnumeration = request.getParameterNames();
		if (localEnumeration.hasMoreElements())
			while (localEnumeration.hasMoreElements()) {
				String localObject = (String) localEnumeration.nextElement();
				if (!"method".equalsIgnoreCase((String) localObject)) {
					String[] arrayOfString = request
							.getParameterValues((String) localObject);
					if (arrayOfString.length == 1) {
						localStringBuilder.append((String) localObject)
								.append("").append(arrayOfString[0]);
					} else {
						localStringBuilder.append((String) localObject).append(
								"[]={");
						for (int i = 0; i < arrayOfString.length; i++) {
							if (i > 0)
								localStringBuilder.append(",");
							localStringBuilder.append(arrayOfString[i]);
						}
						localStringBuilder.append("}");
					}
				}
			}
		net.sf.json.JSONObject localObject = net.sf.json.JSONObject
				.fromObject(localStringBuilder.toString());
		return localObject;
	}

	public void ajaxDoneSuccess(HttpServletResponse paramHttpServletResponse,
			String paramString) {
		com.alibaba.fastjson.JSONObject localJSONObject = new com.alibaba.fastjson.JSONObject();
		localJSONObject.put("state", Integer.valueOf(1));
		localJSONObject.put("statusCode", Integer.valueOf(200));
		if (StringUtils.isBlank(paramString))
			localJSONObject.put("msg", "\u64CD\u4F5C\u6210\u529F!");
		else
			localJSONObject.put("msg", paramString);
		renderJson(paramHttpServletResponse, localJSONObject.toString());
	}
	public String formatChange(String strPre){
		String strReturn = "";
		try {
			strReturn = new String(strPre.getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strReturn;
	}
}
