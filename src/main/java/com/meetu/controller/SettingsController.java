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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meetu.core.base.BaseController;
import com.meetu.core.db.Page;
import com.meetu.core.db.Record;
import com.meetu.photos.dao.SysMenusDao;
import com.meetu.photos.domain.Citys;
import com.meetu.photos.domain.Schools;
import com.meetu.photos.domain.SysMenus;
import com.meetu.photos.service.SysMenusService;
import com.meetu.util.Common;
import com.meetu.util.RedisUtil;
import com.meetu.util.SQLiteJDBC;

@Controller
@RequestMapping(value = "console/settings")
public class SettingsController extends BaseController{

	@Autowired
	private SysMenusService menuService;
	@Autowired
	private SysMenusDao menuDao;
	
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
	public void query(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// TODO Auto-generated method stub
		
	}
	@RequestMapping(value="/menuIndex")
	public String menuIndex(HttpServletResponse paramHttpServletResponse,
			ModelMap paramModelMap) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> pMenus = new ArrayList<Map<String,Object>>();
		try {
			pMenus = menuService.findAll("SELECT ID,NAME FROM SYS_MENUS WHERE LEVEL='1' ORDER BY CODE");
		} catch (Exception e) {
			
		}
		this.setAttr("pMenus", pMenus);
		return this.getIndexView("meetu/settings/menus", paramModelMap);
	}
	@RequestMapping(value = "/menuQuery", method = RequestMethod.POST)
	public void menuQuery(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// TODO Auto-generated method stub
		// 获取排序字段，前台定义默认字段
		String sidx = this.getPara("sidx");
		// 获取排序方式，前台定义，默认为asc
		String sord = this.getPara("sord");
		
		String parentId = this.getPara("parentId");//一级菜单

		try{
			StringBuffer where = new StringBuffer(" WHERE 1=1 ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(parentId)) {
				String sub = Common.dealWithParam(parentId);
				where.append(" AND f.PARENT_ID=? ");
				param.add(sub);
			}

			String sqlStr = "SELECT *  ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr, 
					"FROM (SELECT a.ID,a.CODE,a.LEVEL,a.NAME,a.PATH,a.PARENT_ID,a.ICON,b.NAME PARENT_NAME FROM SYS_MENUS a "
					+ "LEFT JOIN SYS_MENUS b ON a.PARENT_ID=b.ID) f "+where.toString()+" ORDER BY "+sidx+" "+sord, 
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
	@RequestMapping(value = "/menuEdit")
	public String menuEdit(HttpServletResponse response) {
		String id = this.getPara("id");
		
		List<Map<String, Object>> slist = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> pMenus = new ArrayList<Map<String,Object>>();
		try {
			slist = menuService.findMsg("select * from sys_menus where id=?", new Object[]{id});
			if(slist!=null && slist.size()>0){
				this.setAttr("name", slist.get(0).get("NAME"));
				this.setAttr("level", slist.get(0).get("LEVEL"));
				this.setAttr("menuPath", slist.get(0).get("PATH")==null?"":slist.get(0).get("PATH"));
				this.setAttr("menuIcon", slist.get(0).get("ICON")==null?"":slist.get(0).get("ICON"));
				this.setAttr("parentId", slist.get(0).get("PARENT_ID")==null?"":slist.get(0).get("PARENT_ID"));
			}
			
			pMenus = menuService.findAll("SELECT ID,NAME FROM SYS_MENUS WHERE LEVEL='1' ORDER BY CODE");
			
		} catch (Exception e) {
			
		}
		
		this.setAttr("id", id);
		this.setAttr("pMenus", pMenus);
		return "meetu/settings/menusEdit";
	}
	@RequestMapping(params="method=editMenu",method = RequestMethod.POST)
	@ResponseBody
	public void saveMenu(HttpServletResponse response) throws Exception {
		String id = this.getParaJsonStr("id");
		String name = this.getParaJsonStr("name");
		String level = this.getParaJsonStr("level");
		String path = this.getParaJsonStr("path");
		String icon = this.getParaJsonStr("icon");
		String parentId = this.getParaJsonStr("parentId");
		menuDao.updateSQL("update sys_menus set name=?,level=?,path=?,icon=?,parent_id=? where id=? ", 
				new Object[]{name, level, path, icon, parentId, id});
		this.ajaxDoneSuccess(response, null);
	}
	@RequestMapping(value = "/menuAdd")
	public String menuAdd(HttpServletResponse response) {
		
		List<Map<String, Object>> pMenus = new ArrayList<Map<String,Object>>();
		try {
			pMenus = menuService.findAll("SELECT ID,NAME FROM SYS_MENUS WHERE LEVEL='1' ORDER BY CODE");
			
		} catch (Exception e) {
			
		}
		
		this.setAttr("pMenus", pMenus);
		return "meetu/settings/menusAdd";
	}
	
	@RequestMapping(params="method=addMenu",method=RequestMethod.POST)
	@ResponseBody
	public void addMenu(HttpServletResponse response) throws Exception {
		String name = this.getParaJsonStr("name");
		String level = this.getParaJsonStr("level");
		String path = this.getParaJsonStr("path");
		String icon = this.getParaJsonStr("icon");
		String parentId = this.getParaJsonStr("parentId");

		SysMenus menu = new SysMenus();
		menu.setId(Common.generateId());
		menu.setLevel(level);
		menu.setPath(path);
		menu.setIcon(icon);
		menu.setName(name);
		menu.setParent_id(parentId);
		menuService.save(menu);
		
		this.ajaxDoneSuccess(response, null);
	}
	//删除标签
	@RequestMapping(params="method=delete", method=RequestMethod.POST)
	@ResponseBody
	public void delete(HttpServletResponse response) throws Exception {
		String id = this.getParaJsonStr("id");
		menuDao.updateSQL("DELETE FROM SYS_MENUS WHERE ID=?", new Object[]{id});
		this.ajaxDoneSuccess(response, null);
	}
	
	@RequestMapping(value="/schoolsIndex")
	public String schoolIndex(HttpServletResponse paramHttpServletResponse,
			ModelMap paramModelMap) {
		return this.getIndexView("meetu/settings/schools", paramModelMap);
	}
	@RequestMapping(value="/citysIndex")
	public String citysIndex(HttpServletResponse paramHttpServletResponse,
			ModelMap paramModelMap) {
		return this.getIndexView("meetu/settings/citys", paramModelMap);
	}
	@RequestMapping(value = "/schoolsQuery", method = RequestMethod.POST)
	public void schoolsQuery(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// TODO Auto-generated method stub
		// 获取排序字段，前台定义默认字段
		String sidx = this.getPara("sidx");
		// 获取排序方式，前台定义，默认为asc
		String sord = this.getPara("sord");
		
		String schoolname = this.getPara("schoolname");

		try{
			StringBuffer where = new StringBuffer(" WHERE 1=1 ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(schoolname)) {
				String sub = Common.dealWithParam(schoolname);
				where.append(" AND SCHOOL_NAME LIKE ? ");
				param.add("%"+sub+"%");
			}

			String sqlStr = "SELECT SCHOOL_ID,SCHOOL_NAME,PROVINCE_ID  ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr, 
					"FROM SYS_SCHOOLS  "+where.toString()+" ORDER BY "+sidx+" "+sord, 
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
	@RequestMapping(value = "/citysQuery", method = RequestMethod.POST)
	public void citysQuery(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		// TODO Auto-generated method stub
		// 获取排序字段，前台定义默认字段
		String sidx = this.getPara("sidx");
		// 获取排序方式，前台定义，默认为asc
		String sord = this.getPara("sord");
		
		String cityname = this.getPara("cityname");

		try{
			StringBuffer where = new StringBuffer(" WHERE 1=1 ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(cityname)) {
				String sub = Common.dealWithParam(cityname);
				where.append(" AND CITY LIKE ? ");
				param.add("%"+sub+"%");
			}

			String sqlStr = "SELECT ID,PROVINCE,PROVINCE_NUM,CITY,CITY_NUM,TOWN,TOWN_NUM,CITY_TOWN,CITY_TOWN_NUM  ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr, 
					"FROM SYS_CITYS  "+where.toString()+" ORDER BY "+sidx+" "+sord, 
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
	
	/**
	 * 更新学校数据
	 * */
	@RequestMapping(params="method=updateSchools", method=RequestMethod.POST)
	@ResponseBody
	public void updateSchool(HttpServletResponse response) throws Exception{
		JSONArray ja = SQLiteJDBC.schools();
		if(ja!=null&&ja.size()>0){
			List<Schools> schoolslist = new ArrayList<Schools>();
			for(int i=0; i<ja.size(); i++){
				JSONObject jb = (JSONObject) ja.get(i);
				Schools school = new Schools();
				school.setSchool_id(jb.getString("school_id"));
				school.setSchool_name(jb.getString("school_name"));
				school.setProvince_id(jb.getString("province_id"));
				schoolslist.add(school);
			}
			menuService.updateSchools(schoolslist);
		}
		this.ajaxDoneSuccess(response, null);
	}
	
	
	@RequestMapping(params="method=updateCitys", method=RequestMethod.POST)
	@ResponseBody
	public void updateCitys(HttpServletResponse response) throws Exception{
		JSONArray ja = SQLiteJDBC.citys();
		if(ja!=null&&ja.size()>0){
			List<Citys> list = new ArrayList<Citys>();
			for(int i=0; i<ja.size(); i++){
				JSONObject jb = (JSONObject) ja.get(i);
				Citys city = new Citys();
				city.setId(jb.getString("id"));
				city.setProvince(jb.getString("province"));
				city.setProvince_num(jb.getString("province_num"));
				city.setCity(jb.getString("city"));
				city.setCity_num(jb.getString("city_num"));
				city.setTown(jb.getString("town"));
				city.setTown_num(jb.getString("town_num"));
				if(jb.getString("province_num").equals("110000")
						||jb.getString("province_num").equals("120000")
						||jb.getString("province_num").equals("310000")
						||jb.getString("province_num").equals("500000")){
					city.setCity_town(jb.getString("province")+" "+jb.getString("town"));
					city.setCity_town_num(jb.getString("province_num")+"_"+jb.getString("town_num"));
				}else{
					city.setCity_town(jb.getString("province")+" "+jb.getString("city"));
					city.setCity_town_num(jb.getString("province_num")+"_"+jb.getString("city_num"));
				}
				
				list.add(city);
			}
			menuService.updateCitys(list);
		}
		this.ajaxDoneSuccess(response, null);
	}

}
