<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//System.out.println(basePath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>内容设置</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    <form action="${pageContext.request.contextPath }/activity/setContents" method="post">
    	内容: <input name="body" type="text"><br>
    	<input type="submit" value="提交">
    </form>
     <div> 内容数据格式<br>
            注意: <br>
	1. 如果要增加一个内容,需要复制大括号,一个大括号就是一个活动,活动与活动之间用","连接,最后一个活动后面不用加<br>
	2. url前面都得有 http://<br>
	3. cover里放的是上传到阿里云的图片<br>
	</div>
	<div>
	[<br>
    {<br>
		"title": "jiaming2", <font color="red">内容标题</font><br>
        "cover": "xxxxxxxx", <font color="red">内容封面</font><br>
        "url": "http://xxxxxxxxx" <font color="red">内容链接</font><br>
    },....<br>
	]<br>
	</div>
  </body>
</html>
