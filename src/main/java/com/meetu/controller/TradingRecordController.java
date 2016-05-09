package com.meetu.controller;

import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONObject;
import com.meetu.core.base.BaseController;
import com.meetu.core.db.Page;
import com.meetu.core.db.Record;
import com.meetu.photos.dao.SysMenusDao;
import com.meetu.photos.service.SysMenusService;
import com.meetu.util.Common;
@Controller
@RequestMapping(value = "console/trade")
public class TradingRecordController extends BaseController{

	@Autowired
	private SysMenusService menuService;
	@Autowired
	private SysMenusDao menuDao;
	
	
	@Override
	@RequestMapping(value = "/index")
	public String index(HttpServletResponse paramHttpServletResponse,
			ModelMap paramModelMap) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> slist;
		try {
			slist = menuService.findMsg("SELECT SUM(TOTALFEE)/100 FEE FROM MEETU_TRADING_RECORD WHERE RESULT=?", new Object[]{1});
			if(slist!=null && slist.size()>0){
				this.setAttr("fee", slist.get(0).get("FEE"));
			}
			
			slist = menuService.findMsg("SELECT DISTINCT USER_ID FROM MEETU_TRADING_RECORD WHERE RESULT=?", new Object[]{1});
			if(slist!=null && slist.size()>0){
				this.setAttr("users", slist.size());
			}
		} catch (Exception e) {
			
		}
		
		
		return this.getIndexView("meetu/trade/tradingRecord", paramModelMap);
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
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public void query(@RequestParam Integer page, @RequestParam Integer rows,
			HttpServletRequest request, HttpServletResponse response, ModelMap map) {

		// 获取排序字段，前台定义默认字段
		String sidx = this.getPara("sidx");
		// 获取排序方式，前台定义，默认为asc
		String sord = this.getPara("sord");
		
		String result = this.getPara("result");//支付结果
		String rmb = this.getPara("rmb");//金额

		try{
			StringBuffer where = new StringBuffer(" WHERE 1=1 ");
			List<Object> param = new ArrayList<Object>();
			if (StringUtils.isNotEmpty(result)) {
				String sub = Common.dealWithParam(result);
				where.append(" AND f.RESULT=? ");
				param.add(Integer.parseInt(sub));
			}
			if(StringUtils.isNotEmpty(rmb)){
				String sub2 = Common.dealWithParam(rmb);
				where.append(" AND f.TOTALFEE=? ");
				param.add(Integer.parseInt(sub2));
			}
			String sqlStr = "SELECT *  ";
			Page<Record> list = menuService.paginate(page,rows, 
					sqlStr, 
					"FROM (SELECT t.ID,t.BILL_NO,u.NICKNAME,t.CHANNEL,t.TOTALFEE,t.RESULT,t.DATE FROM MEETU_TRADING_RECORD t LEFT JOIN SYS_USER u ON t.USER_ID = u.ID) f "+where.toString()+" ORDER BY "+sidx+" "+sord, 
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

}
