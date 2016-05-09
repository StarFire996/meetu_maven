package com.meetu.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import com.meetu.domain.User;
import com.meetu.photos.dao.SysMenusDao;
import com.meetu.photos.service.SysMenusService;
import com.meetu.service.UserService;
import com.meetu.util.Common;
import com.meetu.util.RedisUtil;
import com.meetu.util.StsService;
/**
 * 用户管理控制器
 * @author lzm
 * @date 2016-03-31
 */
@Controller
@RequestMapping(value = "console/userInfo")
public class AvatarReviewController extends BaseController {

	Logger log = Logger.getLogger(AvatarReviewController.class);
	@Autowired
	private UserService userService;
	@Autowired
	private SysMenusService menuService;
	@Autowired
	private SysMenusDao menuDao;
	
	@Override
	@RequestMapping(value = "/avatarReview")
	public String index(HttpServletResponse paramHttpServletResponse, ModelMap paramModelMap) {
		return this.getIndexView("meetu/userInfo/avatarReview", paramModelMap);
	}
	
	@RequestMapping(value = "/index")
	public String userIndex(HttpServletResponse paramHttpServletResponse, ModelMap paramModelMap) {
		return this.getIndexView("meetu/userInfo/index", paramModelMap);
	}

	@RequestMapping(value = "/reportIndex")
	public String reportIndex(HttpServletResponse paramHttpServletResponse, ModelMap paramModelMap){
		return this.getIndexView("meetu/userInfo/report", paramModelMap);
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
	
	@RequestMapping(value = "/userTradeShow")
	public String userinfoshow(HttpServletResponse response,ModelMap map) {
		try {
			String userid = this.getPara("userid");
			this.setAttr("userid", userid);
			
			List<Map<String, Object>> slist;
			slist = menuService.findMsg("SELECT SUM(TOTALFEE)/100 FEE FROM MEETU_TRADING_RECORD WHERE RESULT=? AND USER_ID=?", new Object[]{1, userid});
			if(slist!=null && slist.size()>0){
				this.setAttr("fee", slist.get(0).get("FEE"));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getIndexView("meetu/userInfo/trade", map);
	}
	 @RequestMapping(value = "/userfriendshow")
    public String userfriendshow(HttpServletResponse response,ModelMap map){
		try {
	    	String userid = this.getPara("userid");
	    	this.setAttr("userid", userid);
		}catch(Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return this.getIndexView("meetu/userInfo/friends", map);      
    }
	 @RequestMapping(value = "/reportQuery", method = RequestMethod.POST)
		public void reportQuery(@RequestParam Integer page, @RequestParam Integer rows,
				HttpServletRequest request, HttpServletResponse response, ModelMap map) {

			// 获取排序字段，前台定义默认字段
			String sidx = this.getPara("sidx");
			// 获取排序方式，前台定义，默认为asc
			String sord = this.getPara("sord");
			
			String shjd = this.getPara("shjd");

			try{
				StringBuffer where = new StringBuffer(" WHERE 1=1 ");
				List<Object> param = new ArrayList<Object>();

				if (StringUtils.isNotEmpty(shjd)) {
					String sub = Common.dealWithParam(shjd);
					where.append(" AND t.ISCHECKED=? ");
					param.add(sub);
				}
				
				String sqlStr = "SELECT ID,(SELECT NICKNAME FROM SYS_USER WHERE ID=t.FROM_USER_ID) FROM_USER,(SELECT NICKNAME FROM SYS_USER WHERE ID=t.TO_USER_ID) TO_USER,DATE,REASON,ISCHECKED,CHECK_DATE ";
				Page<Record> list = menuService.paginate(page,rows, 
						sqlStr, 
						" FROM MEETU_REPORTLIST t  "+where.toString()+" ORDER BY "+sidx+" "+sord, 
						param.toArray());
				List<Record> plist = list.getList();
				list = new Page<Record>(plist, page, rows, list.getTotalPage(),
						list.getTotalRow());
				JSONObject json =  this.toJqueryGrid(list);
				
				//net.sf.json.JSONObject json = toJqGrid(list);
				this.renderJson(response,json.toString());
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				this.ajaxDoneError(response, null);
			}
		}
	 
	@RequestMapping(value = "/tradeQuery", method = RequestMethod.POST)
	public void tradeQuery(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		// 获取排序字段，前台定义默认字段
		String sidx = this.getPara("sidx");
		// 获取排序方式，前台定义，默认为asc
		String sord = this.getPara("sord");
		
		String userid = this.getPara("userid");

		try{
			StringBuffer where = new StringBuffer(" WHERE 1=1 ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(userid)) {
				String sub = Common.dealWithParam(userid);
				where.append(" AND f.USER_ID=? ");
				param.add(sub);
			}
			
			String sqlStr = "SELECT *  ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr, 
					"FROM (SELECT t.ID,t.BILL_NO,u.NICKNAME,t.CHANNEL,t.TOTALFEE,t.RESULT,t.DATE,t.USER_ID FROM MEETU_TRADING_RECORD t LEFT JOIN SYS_USER u ON t.USER_ID = u.ID) f "+where.toString()+" ORDER BY "+sidx+" "+sord, 
					param.toArray());
			List<Record> plist = list.getList();
			list = new Page<Record>(plist, page, rows, list.getTotalPage(),
					list.getTotalRow());
			JSONObject json =  this.toJqueryGrid(list);
			
			//net.sf.json.JSONObject json = toJqGrid(list);
			this.renderJson(response,json.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			this.ajaxDoneError(response, null);
		}
	}
	@RequestMapping(value = "/friendsQuery", method = RequestMethod.POST)
	public void friendsQuery(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {
	
		// 获取排序字段，前台定义默认字段
		String sidx = this.getPara("sidx");
		// 获取排序方式，前台定义，默认为asc
		String sord = this.getPara("sord");
		
		String userid = this.getPara("userid");
		try{
			StringBuffer where = new StringBuffer(" WHERE 1=1 ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(userid)) {
				String sub = Common.dealWithParam(userid);
				where.append(" AND t.USER_ID1 = ? ");
				param.add(sub);
			}
			String sqlStr = "SELECT u.CODE,u.NICKNAME,t.DATE ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr, 
					" FROM MEETU_FRIENDS_REL t LEFT JOIN SYS_USER u ON u.ID=t.USER_ID2 "+where.toString()+" ORDER BY "+sidx+" "+sord, 
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
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public void query(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		// 获取排序字段，前台定义默认字段
		String sidx = this.getPara("sidx");
		// 获取排序方式，前台定义，默认为asc
		String sord = this.getPara("sord");
		
		String nickName = this.getPara("nickName");
		String phone = this.getPara("phone");
		try{
			StringBuffer where = new StringBuffer(" WHERE 1=1 and USER_TYPE='mobile' ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(nickName)) {
				String sub = Common.dealWithParam(nickName);
				where.append(" AND NICKNAME LIKE ? ");
				param.add("%" + sub + "%");
			}
			if(StringUtils.isNotEmpty(phone)){
				String sub2 = Common.dealWithParam(phone);
				where.append(" AND PHONE LIKE ? ");
				param.add("%" + sub2 + "%");
			}
			String sqlStr = "SELECT ID,NICKNAME,PHONE,CODE,SEX,STARSIGN,BIRTH_DATE,VIRTUAL_CURRENCY,(SELECT CITY_TOWN FROM SYS_CITYS WHERE ID=t.CITY limit 1)CITY,ISGRADUATED,CAREER,(SELECT SCHOOL_NAME FROM SYS_SCHOOLS WHERE SCHOOL_ID=t.SCHOOL)SCHOOL,ICON_IS_VALIDATE,REGISTER_DATE,ACTIVITY_TIME,"
					+ "(SELECT COUNT(*) FROM MEETU_FRIENDS_REL WHERE USER_ID1=t.ID) FRIENDS ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr, 
					"FROM SYS_USER t "+where.toString()+" ORDER BY "+sidx+" "+sord, 
					param.toArray());
			List<Record> plist = list.getList();
//			//学校，所在城市
//			String school = RedisUtil.getString("school");
//			if(school!=null){
//				JSONObject sch = JSONObject.parseObject(school);
//				for(int i = 0; i < plist.size(); i++){
//					Record red = plist.get(i);
//					String icon_url = red.getStr("ICON_URL");
//					red.set("ICON_URL",StsService.generateUrl(icon_url));
//				}
//			}
			
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
	
	
	
	@RequestMapping(value = "/avatarReviewQuery", method = RequestMethod.POST)
	public void avatarReviewQuery(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// TODO Auto-generated method stub
		
		// 获取排序字段，前台定义默认字段
		String sidx = this.getPara("sidx");
		// 获取排序方式，前台定义，默认为asc
		String sord = this.getPara("sord");
		
		String nickName = this.getPara("getNickName");
		String shjd = this.getPara("shjd");
		try{
			
			StringBuffer where = new StringBuffer(" WHERE 1=1 and USER_TYPE='mobile' ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(nickName)) {
				String sub = Common.dealWithParam(nickName);
				where.append(" AND NICKNAME LIKE ? ");
				param.add("%" + sub + "%");
			}
			if(StringUtils.isNotEmpty(shjd)){
				String sub2 = Common.dealWithParam(shjd);
				where.append(" AND ICON_IS_VALIDATE=? ");
				param.add(sub2);
			}
			
			String sqlStr = "SELECT ID,NICKNAME,PHONE,CODE,ICON_IS_VALIDATE,REGISTER_DATE,ICON_URL ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr, 
					"FROM SYS_USER t "+where.toString()+" ORDER BY "+sidx+" "+sord, 
					param.toArray());
			List<Record> plist = list.getList();
			//生成头像路径
			for(int i = 0; i < plist.size(); i++){
				Record red = plist.get(i);
				String icon_url = red.getStr("ICON_URL");
				red.set("ICON_URL",StsService.generateUrl(icon_url));
			}
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
	@RequestMapping( params="method=verifyIcon",method = RequestMethod.POST)
	@ResponseBody
	public void verifyIcon(HttpServletRequest request,HttpServletResponse response){
		try {
			String id = this.getParaJsonStr("id");
			String status = this.getParaJsonStr("status");
			if(status.equals("2")){
				menuDao.updateSQL("update sys_user set icon_is_validate=? where id=?", new Object[]{"2", id});
			}else{//审核不通过
				User user = userService.selectUserById(id);//用户信息
				if(user.getOriginal_icon_url()==null||user.getOriginal_icon_url().equals("")){
					//第一次上传头像
					menuDao.updateSQL("update sys_user set icon_is_validate=? where id=?", new Object[]{"4", id});
				}else{
					menuDao.updateSQL("update sys_user set icon_is_validate=? where id=?", new Object[]{"6", id});
				}
				
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			this.ajaxDoneError(response, null);
		}
		this.ajaxDoneSuccess(response, null);
	}

}
