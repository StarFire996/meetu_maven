package com.meetu.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.meetu.core.base.BaseController;
import com.meetu.core.db.Page;
import com.meetu.core.db.Record;
import com.meetu.photos.dao.SysMenusDao;
import com.meetu.photos.service.SysMenusService;
import com.meetu.service.UserService;
import com.meetu.tags.domain.MeetuChatTags;
import com.meetu.tags.domain.MeetuInterestTags;
import com.meetu.tags.domain.MeetuPersonalizedTags;
import com.meetu.tags.service.MeetuChatTagsService;
import com.meetu.tags.service.MeetuInterestTagsService;
import com.meetu.tags.service.MeetuPersonalizedTagsService;
import com.meetu.util.Common;
import com.meetu.util.RedisUtil;

@Controller
@RequestMapping(value = "console/tags")
public class TagsController extends BaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private SysMenusService menuService;
	@Autowired
	private SysMenusDao menuDao;
	@Autowired
	private MeetuChatTagsService chatTagsService;
	@Autowired
	private MeetuPersonalizedTagsService personalizedTagsService;
	@Autowired
	private MeetuInterestTagsService interestTagsService;
	
	@Override
	public String index(HttpServletResponse paramHttpServletResponse,
			ModelMap paramModelMap) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 个性标签
	 * @throws Exception 
	 **/
	@RequestMapping(value="/personalizedTagsIndex")
	public String personalizedTagsIndex(HttpServletResponse paramHttpServletResponse,
			ModelMap paramModelMap){
		return this.getIndexView("meetu/tags/personalizedTags", paramModelMap);
	}
	@RequestMapping(value="/personalizedTagsQuery")
	public void personalizedTagsQuery(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		String sidx = this.getPara("sidx");
		String sord = this.getPara("sord");
		
		String tagName = this.getPara("tagName");
		String sex = this.getPara("sex");
		try{
			StringBuffer where = new StringBuffer(" WHERE 1=1 ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(tagName)) {
				String sub = Common.dealWithParam(tagName);
				where.append(" AND NAME LIKE ? ");
				param.add("%" + sub + "%");
			}
			if(StringUtils.isNotEmpty(sex)){
				String sub2 = Common.dealWithParam(sex);
				where.append(" AND SEX = ? ");
				param.add(sub2);
			}
			
			String sqlStr = "SELECT ID,CODE,NAME,(CASE SEX WHEN '1' THEN '男' ELSE '女' END)SEX,CREATE_DATE ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr,
					"FROM MEETU_PERSONALIZED_TAGS t "+where.toString()+" ORDER BY "+sidx+" "+sord, 
					param.toArray());
			List<Record> plist = list.getList();
			list = new Page<Record>(plist, page, rows, list.getTotalPage(),
					list.getTotalRow());
			JSONObject json =  this.toJqueryGrid(list);
			
			this.renderJson(response,json.toString());
		} catch (Exception e) {
			logger.error(e.getMessage());
			this.ajaxDoneError(response, null);
		}
	}
	
	@RequestMapping(value = "/personalizedTagsAdd")
	public String personalizedTagsAdd(HttpServletResponse response) {
		return "meetu/tags/personalizedTagsAdd";
	}
	@RequestMapping(params="method=addPersonalizedTags",method = RequestMethod.POST)
	@ResponseBody
	public void addPersonalizedTags(HttpServletResponse response) throws Exception {
		String name = this.getParaJsonStr("name");
		String sex = this.getParaJsonStr("sex");
		
		MeetuPersonalizedTags pTags = new MeetuPersonalizedTags();
		pTags.setId(Common.generateId());
		pTags.setName(name);
		pTags.setSex(sex);
		pTags.setCreate_date(new Date());

		personalizedTagsService.insert(pTags);
		refreshTagsInRedis("PERSONALIZED_TAGS");
		this.ajaxDoneSuccess(response, null);
	}
	@RequestMapping(value = "/personalizedTagsEdit")
	public String personalizedTagsEdit(HttpServletResponse response) {
		String id = this.getPara("id");
		
		List<Map<String, Object>> slist;
		try {
			slist = menuService.findMsg("select * from meetu_personalized_tags where id=?", new Object[]{id});
			if(slist!=null && slist.size()>0){
				this.setAttr("name", slist.get(0).get("NAME"));
				this.setAttr("sex", slist.get(0).get("SEX"));
			}
		} catch (Exception e) {
			
		}
		
		this.setAttr("id", id);
		return "meetu/tags/personalizedTagsEdit";
	}
	@RequestMapping( params="method=editPersonalizedTags",method = RequestMethod.POST)
	@ResponseBody
	public void editPersonalizedTags(HttpServletResponse response) throws Exception {
		String id = this.getParaJsonStr("id");
		String name = this.getParaJsonStr("name");
		String sex = this.getParaJsonStr("sex");
		menuDao.updateSQL("update meetu_personalized_tags set name=?,sex=? where id=? ", new Object[]{name, sex, id});
		refreshTagsInRedis("PERSONALIZED_TAGS");
		this.ajaxDoneSuccess(response, null);
	}
	
	
	/**
	 * 兴趣标签
	 * @throws Exception 
	 * 
	 **/
	@RequestMapping(value = "/interestTagsIndex")
	public String interestTagsIndex(HttpServletResponse paramHttpServletResponse,
			ModelMap paramModelMap){
		// TODO Auto-generated method stub
		this.setAttr("typeList", getTagType());
		return this.getIndexView("meetu/tags/interestTags", paramModelMap);
	}
	
	private List<Map<String,Object>> getTagType(){
		List<Map<String,Object>> tmplist = new ArrayList<Map<String,Object>>();
		try {
			tmplist = menuService.findAll("SELECT ID,NAME FROM MEETU_TAGTYPES");
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return tmplist;
	}
	@RequestMapping(value = "/interestTagsQuery", method = RequestMethod.POST)
	public void interestTagsQuery(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		// 获取排序字段，前台定义默认字段
		String sidx = this.getPara("sidx");
		// 获取排序方式，前台定义，默认为asc
		String sord = this.getPara("sord");
		
		String tagName = this.getPara("tagName");
		String tagType = this.getPara("tagType");
		try{
			StringBuffer where = new StringBuffer(" WHERE 1=1 ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(tagName)) {
				String sub = Common.dealWithParam(tagName);
				where.append(" AND NAME LIKE ? ");
				param.add("%" + sub + "%");
			}
			if(StringUtils.isNotEmpty(tagType)){
				String sub2 = Common.dealWithParam(tagType);
				where.append(" AND TYPE = ? ");
				param.add(sub2);
			}
			
			String sqlStr = "SELECT ID,CODE,NAME,(SELECT NAME FROM MEETU_TAGTYPES WHERE ID=T.TYPE)TYPENAME,TYPE,CREATE_DATE ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr, 
					"FROM MEETU_INTEREST_TAGS t "+where.toString()+" ORDER BY "+sidx+" "+sord, 
					param.toArray());
			List<Record> plist = list.getList();
			list = new Page<Record>(plist, page, rows, list.getTotalPage(),
					list.getTotalRow());
			JSONObject json =  this.toJqueryGrid(list);
			
			this.renderJson(response,json.toString());
		} catch (Exception e) {
			logger.error(e.getMessage());
			this.ajaxDoneError(response, null);
		}
	}
	@RequestMapping(value = "/interestTagsEdit")
	public String interestTagsEdit(HttpServletResponse response) {
		String id = this.getPara("id");
		
		List<Map<String, Object>> slist;
		try {
			slist = menuService.findMsg("select * from meetu_interest_tags where id=?", new Object[]{id});
			if(slist!=null && slist.size()>0){
				this.setAttr("name", slist.get(0).get("NAME"));
				this.setAttr("type", slist.get(0).get("TYPE"));
			}
		} catch (Exception e) {
			
		}
		
		this.setAttr("id", id);
		this.setAttr("typeList", getTagType());
		return "meetu/tags/interestTagsEdit";
	}
	
	
	@RequestMapping( params="method=editInterestTags",method = RequestMethod.POST)
	@ResponseBody
	public void editInterestTags(HttpServletResponse response) throws Exception {
		String id = this.getParaJsonStr("id");
		String name = this.getParaJsonStr("name");
		String type = this.getParaJsonStr("type");
		menuDao.updateSQL("update meetu_interest_tags set name=?,type=? where id=? ", new Object[]{name, type, id});
		refreshTagsInRedis("INTEREST_TAGS");
		this.ajaxDoneSuccess(response, null);
	}
	@RequestMapping(value = "/interestTagsAdd")
	public String interestTagsAdd(HttpServletResponse response) {
		this.setAttr("typeList", getTagType());
		return "meetu/tags/interestTagsAdd";
	}
	@RequestMapping(params="method=addInterestTags",method = RequestMethod.POST)
	@ResponseBody
	public void addInterestTags(HttpServletResponse response) throws Exception {
		String name = this.getParaJsonStr("name");
		String type = this.getParaJsonStr("type");
		
		MeetuInterestTags interestTags = new MeetuInterestTags();
		interestTags.setId(Common.generateId());
		interestTags.setName(name);
		interestTags.setType(type);
		interestTags.setCreate_date(new Date());

		interestTagsService.insert(interestTags);
		refreshTagsInRedis("INTEREST_TAGS");
		this.ajaxDoneSuccess(response, null);
	}
	
	
	
	/**
	 * 话题标签
	 * 
	 **/
	@RequestMapping(value = "/chatTagsIndex")
	public String chatTagsIndex(HttpServletResponse paramHttpServletResponse,
			ModelMap paramModelMap) {
		// TODO Auto-generated method stub
		return this.getIndexView("meetu/tags/chatTags", paramModelMap);
	}
	@RequestMapping(value = "/chatTagsAdd")
	public String chatTagsAdd(HttpServletResponse response) {
		return "meetu/tags/chatTagsAdd";
	}
	@RequestMapping(value = "/chatTagsEdit")
	public String chatTagsEdit(HttpServletResponse response) {
		String id = this.getPara("id");
		
		List<Map<String, Object>> slist;
		try {
			slist = menuService.findMsg("select * from meetu_chat_tags where id=?", new Object[]{id});
			if(slist!=null && slist.size()>0){
				this.setAttr("name", slist.get(0).get("NAME"));
			}
		} catch (Exception e) {
			
		}
		this.setAttr("id", id);
		return "meetu/tags/chatTagsEdit";
	}
	@RequestMapping(params="method=addChatTags",method = RequestMethod.POST)
	@ResponseBody
	public void addChatTags(HttpServletResponse response) throws Exception {
		String name = this.getParaJsonStr("name");
		
		MeetuChatTags chatTags = new MeetuChatTags();
		chatTags.setId(Common.generateId());
		chatTags.setName(name);
		chatTags.setCreate_date(new Date());

		chatTagsService.insert(chatTags);
		refreshTagsInRedis("CHAT_TAGS");
		this.ajaxDoneSuccess(response, null);
	}
	@RequestMapping( params="method=editChatTags",method = RequestMethod.POST)
	@ResponseBody
	public void editChatTags(HttpServletResponse response) throws Exception {
		String id = this.getParaJsonStr("id");
		String name = this.getParaJsonStr("name");
		menuDao.updateSQL("update meetu_chat_tags set name=? where id=? ", new Object[]{name, id});
		refreshTagsInRedis("CHAT_TAGS");
		this.ajaxDoneSuccess(response, null);
	}
	//删除标签
	@RequestMapping(params="method=deleteTag", method=RequestMethod.POST)
	@ResponseBody
	private void deleteTag(HttpServletResponse response) throws Exception {
		String id = this.getParaJsonStr("id");
		String table = this.getParaJsonStr("table");
		menuDao.updateSQL("DELETE FROM "+table+" WHERE ID=?", new Object[]{id});
		if(table.equals("MEETU_PERSONALIZED_TAGS")){
			refreshTagsInRedis("PERSONALIZED_TAGS");
		}else if(table.equals("MEETU_INTEREST_TAGS")){
			refreshTagsInRedis("INTEREST_TAGS");
		}else if(table.equals("MEETU_CHAT_TAGS")){
			refreshTagsInRedis("CHAT_TAGS");
		}
		this.ajaxDoneSuccess(response, null);
	}
	//将redis中数据删除即可
	private void refreshTagsInRedis(String tag){
		RedisUtil.delString(tag);
	}
	
	@RequestMapping(value = "/chatTagsQuery", method = RequestMethod.POST)
	public void chatTagsQuery(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		// 获取排序字段，前台定义默认字段
		String sidx = this.getPara("sidx");
		// 获取排序方式，前台定义，默认为asc
		String sord = this.getPara("sord");
		
		String tagName = this.getPara("tagName");
		try{
			StringBuffer where = new StringBuffer(" WHERE 1=1 ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(tagName)) {
				String sub = Common.dealWithParam(tagName);
				where.append(" AND NAME LIKE ? ");
				param.add("%" + sub + "%");
			}
			
			String sqlStr = "SELECT ID,CODE,NAME,CREATE_DATE ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr, 
					"FROM MEETU_CHAT_TAGS t "+where.toString()+" ORDER BY "+sidx+" "+sord, 
					param.toArray());
			List<Record> plist = list.getList();
			list = new Page<Record>(plist, page, rows, list.getTotalPage(),
					list.getTotalRow());
			JSONObject json =  this.toJqueryGrid(list);
			
			//net.sf.json.JSONObject json = toJqGrid(list);
			this.renderJson(response,json.toString());
			
			
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			this.ajaxDoneError(response, null);
		}
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
