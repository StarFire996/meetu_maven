/**
 * 首页操作
 */
Power.index = {
	version:"1.0",
	uid:"",
	filePath: ""
};
/**
 * 在页面中任何嵌套层次的窗口中获取顶层窗口
 * @return 是否支持HTML5
 */
Power.index.isSupportHtml5 =function(){
	var canvas = document.createElement('canvas');
	return (canvas.getContext && canvas.getContext('2d'));
	//try {
	//	return ('localStorage' in window && window['localStorage'] != null);
	//}catch(e) {
	//	return false;
	//}
};
/**
 * 初始化
 * @param root
 * @returns {Power.initApplication}
 */
Power.initApplication = function(root,uid){debugger
	this.rootPath = root+"/";
	this.jsPath = root+"/static/js/";
	this.cssPath=root+'/static/css/';
	this.imgPath=root+'/static/img/';
	window.onresize = window.onload = function(){
		var height = $(window).height()-70;
		$("#main").css("height",height); 
		$("#left").css("height",height); 
		//当前登录用户ID
		Power.index.uid = uid;
	};
	//[单点访问|非集成环境]退出网页加载事件
	//if(header!="noheader"){
	//	$(window).unload(function(){
	//		Power.index.logout();
	//	});
	//}
	/**
	 * 设置未来(全局)的AJAX请求默认选项
	 * 主要设置了AJAX请求遇到Session过期的情况
	 */
	$.ajaxSetup({
		type: 'GET',
		complete: function(xhr,status) {
			var sessionStatus = xhr.getResponseHeader('sessionstatus');
			//alert(sessionStatus);
			if(sessionStatus == 'timeout') {
				var yes = confirm('由于您长时间没有操作, session已过期, 请重新登录.');  
		        if (yes) {  
		      		var wintop = Power.getTopWinow();
					wintop.location.href = Power.rootPath+'login';         
				}  
			}
		}
	});
	//屏蔽鼠标右键
	//document.oncontextmenu=function(){return false;};
};
/**
 * 初始化
 * @param root
 * @returns {Power.initApplication}
 */
Power.initIframe = function(root){
	this.rootPath = root+"/";
	this.jsPath = root+"/static/js/";
	this.cssPath=root+'/static/css/';
	this.imgPath=root+'/static/img/';
};
/**
 * 加载页面
 */
Power.index.loadPage  = function(url){
	 //加载网盘内容
   /* $("#main-content").load(url, undefined, function() {
    	//var height = $(window).height()-130;
    	$(".page-content").css("height",$(this).attr("height"));
    });*/
    $("#main-content").loadModule(url,undefined,function() {
    	//var height = $(window).height()-130;
    	$(".page-content").css("height",$(this).attr("height"));
    });
};
/**
 * DIV加载HTML代码片段页面
 * @param id Div ID
 * @param url DIV要装载的html(div代码片段)页面路径
 */
Power.index.loadDivPage  = function(id,url){
	 //根据DIV ID加载HTML DIV代码片段
    /*$("#"+id).load(url, undefined, function() {
    	 //加载代码片段完毕后，执行该方法completeHander()
    	 // if(null!=completeHander&&typeof(completeHander)=="function"){
    	 if($.isFunction(window.completeHander)){
    		 completeHander();
    	 }
    });*/

	$("#"+id).loadModule(url,undefined,function() {
    	if($.isFunction(window.completeHander)){
    		 completeHander();
    	 }
    });
};
/**
 * DIV加载HTML TREE代码片段页面
 * @param id Div ID
 * @param url DIV要装载的html(div代码片段)页面路径
 */
Power.index.loadTree  = function(id,url){
	 //根据DIV ID加载HTML DIV代码片段
    $("#"+id).load(url, undefined, function() {
    	 //加载代码片段完毕后，执行该方法initTree()
    	 if($.isFunction(window.initTree)){
    		 initTree();
    	 }
    });
};
/**
 * 加载系统应用
 */
Power.index.loadApp  = function(){
	var url = "views/system/app";
	 //加载应用内容
   $("#main-content").load(url, undefined, function() {
   	
   });
};
/**
 * 加载我的网盘
 */
Power.index.loadDisk  = function(){
	var url =  "views/system/disk";
	 //加载网盘内容
    $("#main-content").load(url, undefined, function() {
    	
    });
};
/**
 * 加载系统菜单
 */
Power.index.loadMenu=function(pid,pPath){
	$("#sidebar").load(Power.rootPath+"menu?pid="+pid,function() {
		if(pPath!=null){
			Power.index.loadPage(Power.rootPath+pPath);//一级菜单默认加载菜单
		}
		$("#sidebar a[href!='#']").bind('click', function(e) {
	         var alink = $(e.target); //点击的菜单链接<a> 
	         var parentli = alink.parent(); //包装链接的li
	         $("#sidebar div").each(function(i) {
	             //if ($(this) != parentli) {
	                 $(this).removeClass('active');
	             //}
	         });
	         parentli.addClass('active');
	         if (parentli.parent().hasClass('submenu')) {
	             parentli.parent().parent().addClass('active');
	         }
	         //加载连接内容
	         //var dummy = new Date().getTime();
	         var url =Power.rootPath+$(this).attr('href');//+"?v="+dummy; //$(this).attr('href');
	         if(url.indexOf("/http://")!=-1){
	        	 url = url.substr(1);
	         }
	         var path = $(this).attr('name');
	     	 var icon = $(this).attr("icon");
        	 var menu = $(this).attr("tag");
        	 if(url.indexOf("?iframe")==-1){
        		 //#1用DIV加载
        		 $("#main-content-iframe").hide();
        		 $("#main-content").show();
		         $("#main-content").loadModule(url, undefined, function() {
		        	 //设置导航路径
			         if(path!=null&&path.indexOf("#")!=-1){
			        	 var ref = "javascript:Power.index.loadPage('"+Power.rootPath+"mng/system/portal/index')";
			        	 var home = '<li><i class="icon-home"></i><a href="'+ref+'">首页</a><span class="divider"><i class="icon-angle-right"></i></span></li>';
			        	 var paths = path.split("#");
			        	 var pathStr = "";
			        	 for(var i=0;i<paths.length;i++){
			        		 if(i<paths.length-1){
			        			 pathStr +='<li><a href="#">'+paths[i]+'</a><span class="divider"><i class="icon-angle-right"></i></span></li>';
			        		 }else{
			        			 pathStr +='<li class="active">'+paths[i]+'</li>';
			        		 }
			        	 }
			        	//alert(home+pathStr);
			        	$(".breadcrumb").html(home+pathStr);
			        	//设置菜单logo
			        	$(".ui-jqgrid-title").html("<i class='"+icon+" icon-large white'></i> "+menu);
			         }
		         });
        	 }else{
        		 //#2用IFRAME加载
        		 $("#main-content").hide();
        		 $("#main-content-iframe").show();
        		 //var iframe =  $("#main-content-iframe");
        		 var iframe = document.getElementById("main-content-iframe");
        		 iframe.src= url ;
        		 if (iframe.attachEvent){
        		        iframe.attachEvent("onload", function(){
        		        	Power.index.setMenuPath(path,icon,menu);
        		         });
        		 }else{
        		        iframe.onload = function(){
        		        	Power.index.setMenuPath(path,icon,menu);
        		        };
        		}
        		 //$("#main-content-iframe",parent.document.body).attr("src",url);
        	 }
	         return false;
	     });
		$("#sidebar a[href='#']").bind('click', function(e) {
			 var alink = $(this);
			 var parentli = alink.parent(); //包装链接的li
			 //先把所有的active关闭掉
	         $("#sidebar li.active").each(function(i) {
	             if ($(this) != parentli) {
	                 $(this).removeClass('active');
	             }
	         });
	         //parentli.addClass('active');
	         //alert(parentli.attr('class'));
	         if(parentli.hasClass("open")){
	        	 parentli.removeClass('open');
	        	 parentli.removeClass('active');
	         }else{
	        	 parentli.addClass('open');
	        	 parentli.addClass('active');
	         }
		});
	});
};
/**
 * 设置菜单路径
 *  <li><i class="fa fa-location-arrow"></i> 当前位置：</li>
 *	<li><a href="#">主页</a></li>
 * <li><a href="#">目标管理</a></li>
 * <li class="active">用户关注指标</li>
 * 
 */
Power.index.setMenuPath = function(path,icon,menu){
		//设置导航路径
         if(path!=null&&path.indexOf("#")!=-1){
        	 var ref = "javascript:Power.index.loadPage('"+Power.rootPath+"mng/system/portal/index')";
        	 var home = '<li><i class="fa fa-location-arrow"></i> 当前位置：</li>'+'<li><a href="'+ref+'">首页</a></li>';
        	 var paths = path.split("#");
        	 var pathStr = "";
        	 for(var i=0;i<paths.length;i++){
        		 if(i<paths.length-1){
        			 pathStr +='<li><a href="#">'+paths[i]+'</a></li>';
        		 }else{
        			 pathStr +='<li class="active">'+paths[i]+'</li>';
        		 }
        	 }
        	//alert(home+pathStr);
        	$("#main-content-iframe").contents().find(".breadcrumb").html(home+pathStr);
        	//$(".breadcrumb").html(home+pathStr);
        	//设置菜单logo
        	$("#main-content-iframe").contents().find(".ui-jqgrid-title").html("<i class='"+icon+" icon-large white'></i> "+menu);
        	//$(".ui-jqgrid-title").html("<i class='"+icon+" icon-large white'></i> "+menu);
         }
};
/**
 * 退出登录记时时间设置
 */
Power.index.logout = function(){
	var cmd = new Power.command("main/logout","post");
	//设置当前用户ID
	cmd.setParameter("uid", Power.index.uid);
	//alert(Power.index.uid);
	var ret = cmd.execute("");
};
/**
 * 系统设置
 */
Power.index.systemConfig=function(){
	 var addButtons = [{
     	 id:'system-style-change',
         name: '确定',
         callback: function () {
         	if(submit()){
         		//提交成功，加载个人意见列表页面
         		//$(body).addClass();
         		return true;
         	}
             return false;
         },
         focus: true
     }, {
         name: '取消'
     }];
	Power.dialog({
		width:350,
		title:"系统风格设置",
		url : "views/system/user/style",
		button:addButtons,
		init:function(){
			initWindow();
		}
	});
};
/**
 * 加载日程安排
 */
Power.index.loadSchedule=function(){
	  $("#main-content").load("/views/system/schedule/index");
};
/**
 * 加载意见反馈
 */
Power.index.loadFeedback=function(){
	 var addButtons = [{
     	 id:'system-index-feedback',
         name: '确定',
         callback: function () {
         	if(submit()){
         		//提交成功，加载个人意见列表页面
         		Power.index.loadPage("views/system/issue/index");
         		return true;
         	}
             return false;
         },
         focus: true
     }, {
         name: '取消'
     }];
	Power.dialog({
		width:450,
		title:"意见反馈",
		url : "views/system/issue/add",
		button:addButtons
	});
};
/**
 * [系统公共组件]修改密码
 */
Power.index.changePassword=function(){
	 var addButtons = [{
     	 id:'system-user-changpwd',
         name: '确定',
         callback: function () {
         	if(submit()){
         		return true;
         	}
             return false;
         },
         focus: true
     }, {
         name: '取消'
     }];
	Power.dialog({
		width:550,
		title:"密码修改",
		url : "mng/system/user/password",
		button:addButtons
	});
};
/**
 * 个人资料
 */
Power.index.loadInfo = function(){
	//加载用户信息
	$("#main-content").load('mng/system/user/info');
};

/**
 *  字典库下拉菜单绑定
 */
Power.index.setOption  = function(sltId,typeCode,defaultValue,flag,isAll){
	
	$("#"+sltId).empty();
	
	var cmd = new Power.command("mng/system/dict","post");
	cmd.setParameter("typeCode",typeCode);
	var ret = cmd.execute("addoption");
	if (ret.length > 0){
		if(true==flag){
			for (var i = 0;i < ret.length;i++){
				if(ret[i].CODE == defaultValue){
					$("#"+sltId).html(ret[i].NAME);
					break;
				}
			}
		}else{
			
			//下拉菜单增加全部选择处理
			if (isAll){
				
				$("<option value='' selected='true'>全部</option>").appendTo("#"+sltId);
				
			}
			
			for (var i = 0;i < ret.length;i++){
				
				if(ret[i].CODE == defaultValue && defaultValue != null && defaultValue != ""){
					
					$("<option value='"+ret[i].CODE+"' selected='true'>"+ret[i].NAME+"</option>").appendTo("#"+sltId);
					
				}else{
					
					$("<option value='"+ret[i].CODE+"'>"+ret[i].NAME+"</option>").appendTo("#"+sltId);
				
				}
			
			}
		}
	
	}
 
};
/**
 *  字典库下拉菜单绑定(父级)
 */
Power.index.setChildOption  = function(sltId,type,parent,defaultValue){
	var cmd = new Power.command("mng/system/dict","post");
	cmd.setParameter("type",type);
	cmd.setParameter("code",parent);
	var ret = cmd.execute("getchild");
	if (ret.length > 0){
		$("#"+sltId).html("");
		for (var i = 0;i < ret.length;i++){
			if(ret[i].KIND == defaultValue && defaultValue != null && defaultValue != ""){
				$("<option value='"+ret[i].CODE+"' selected='true'>"+ret[i].NAME+"</option>").appendTo("#"+sltId);
			}else{
				$("<option value='"+ret[i].CODE+"'>"+ret[i].NAME+"</option>").appendTo("#"+sltId);
			}
		}
		$("#"+sltId).show();
	}else{
		$("#"+sltId).hide();
	}
};

/**
 *  字典库下拉菜单绑定(父级)
 */
Power.index.setFatherOption  = function(sltId,defaultValue){
	var cmd = new Power.command("mng/system/dict","post");
	var ret = cmd.execute("getoption");
	if (ret.length > 0){
		for (var i = 0;i < ret.length;i++){
			if(ret[i].KIND == defaultValue && defaultValue != null && defaultValue != ""){
				$("<option value='"+ret[i].KIND+"' selected='true'>"+ret[i].TYPE+"</option>").appendTo("#"+sltId);
			}else{
				$("<option value='"+ret[i].KIND+"'>"+ret[i].TYPE+"</option>").appendTo("#"+sltId);
			}
		}
	}
};

/**
 * 事项目录、改变记录状态
 */
Power.index.changeCount = function(grid,id){
	var count = $(grid).jqGrid('getGridParam','records');
	if(count>0){
		$("."+id).html(count);
		$("."+id).removeClass("badge badge-danger");
		$("."+id).addClass("badge badge-success");
	}else{
		$("."+id).html(count);
		$("."+id).removeClass("badge badge-success");
		$("."+id).addClass("badge badge-danger");
	}
};
