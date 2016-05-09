/**
 * 定义WebFinal
 */
/**
 * 定义WebFinal名称空间
 */
WebFinal = window.WebFinal || {
	name:"WebFinal",
	version:"1.0",
	iframe:true,
	rootPath:""
};
/**
 * 初始化
 * @param root
 * @returns {Power.initApplication}
 */
WebFinal.initApplication = function(root,uid){
	this.rootPath = root+"/";
	this.jsPath = root+"/static/js/";
	this.cssPath=root+'/static/css/';
	this.imgPath=root+'/static/img/';
	window.onresize = window.onload = function(){
		var height = $(window).height()-40;
		$("#main-content").css("height",height); 
		//$("#left").css("height",height); 
	};
	/**
	 * 设置未来(全局)的AJAX请求默认选项
	 * 主要设置了AJAX请求遇到Session过期的情况
	 */
	/**
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
	**/
	//屏蔽鼠标右键
	//document.oncontextmenu=function(){return false;};
};

/**
 * 加载AJAX页面
 */
WebFinal.loadPage  = function(url,data,callback){
	//清空页面dom元素
	$("#main-content").empty();
	//加载URL访问页面
    $("#main-content").load(url,data,function() {
    	//$(".page").css("height",$(this).attr("height"));
    	if(callback&&$.isFunction(callback)){
    		callback();
    	}
    });
};
/**
 * 加载IFRAME页面
 */
WebFinal.loadIframePage  = function(url,data,callback){
	//清空页面dom元素
	$("#main-content").empty();
	//加载IFrame对话框
	var srcUrl = WebFinal.rootPath+url+"?v="+new Date().getTime();
	var iframeHtml = "<iframe id='dzzw-webfinal-iframe' height='"+($("#main-content").height())+"' width='100%' src='"+"' allowtransparency='true' frameborder='0' scrolling='auto' seamless='seamless' marginheight='0' marginwidth='0'></iframe>";
	$("#main-content").html(iframeHtml);
	var iframe = document.getElementById("dzzw-webfinal-iframe");
	//var iframe = $("#dzzw-webfinal-iframe");
	iframe.src= srcUrl;
	if (iframe.attachEvent){
  	 	iframe.attachEvent("onload", function(){
  	 		WebFinal.setMenuPath(data.path,data.icon,data.menu);
        });
	 }else{
		 iframe.onload = function(){
			 WebFinal.setMenuPath(data.path,data.icon,data.menu);
	 	 };
	}
};
/**
 * DIV加载HTML代码片段页面
 * @param id Div ID
 * @param url DIV要装载的html(div代码片段)页面路径
 */
WebFinal.loadDivPage  = function(id,url,data,callback){
	 //根据DIV ID加载HTML DIV代码片段
    $("#"+id).load(url, data, function(){
    	if($.isFunction(callback)){
    		$(this).invoke(callback);
    	}
    });
};
/**
 * DIV加载HTML TREE代码片段页面
 * @param id Div ID
 * @param url DIV要装载的html(div代码片段)页面路径
 */
WebFinal.loadTree  = function(id,url){
	 //根据DIV ID加载HTML DIV代码片段
    $("#"+id).load(url, undefined, function() {
    	 //加载代码片段完毕后，执行该方法initTree()
    	 if($.isFunction(window.initTree)){
    		 initTree();
    	 }
    });
};
/**
 * 加载系统菜单
 */
WebFinal.loadMenu=function(pid,pPath){
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
		         $("#main-content").load(url, undefined, function() {
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
 */
WebFinal.setMenuPath = function(path,icon,menu){
		//设置导航路径
         if(path!=null&&path.indexOf("#")!=-1){
        	 var ref = "javascript:WebFinal.loadPage('"+WebFinal.rootPath+"main/index')";
        	 var home = '<li><i class="fa fa-flag" style="color:#0097a7;"></i>&nbsp;当前位置：</li>'+'<li><a href="'+ref+'">首页</a></li>';
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
        	 //设置菜单位置
        	$("#dzzw-webfinal-iframe").contents().find(".breadcrumb").html(home+pathStr);
         }
};
/**
 *  字典库下拉菜单绑定
 */
WebFinal.setOption  = function(sltId,typeCode,defaultValue,flag,isAll){
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
WebFinal.setChildOption  = function(sltId,type,parent,defaultValue){
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
WebFinal.setFatherOption  = function(sltId,defaultValue){
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