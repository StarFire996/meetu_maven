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
    
    <title>活动设置</title>
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
    <form action="${pageContext.request.contextPath }/activity/setActivity"
		method="post">
		活动内容: <input name="activity" type="text"><br> 
		活动时间: <input name="update_date" type="text">(格式:2022-02-12_16:50:00)<br> 
		<input type="submit" value="提交">
	</form>
	
	<div> 活动数据格式<br>
            注意: <br>
	1. actys 中如果要增加一个活动,需要复制大括号,一个大括号就是一个活动,活动与活动之间用","连接,最后一个活动后面不用加<br>
	2. url前面都得有 http://<br>
	3. cover里放的是上传到阿里云的图片<br>
	</div>
	<div>
{<br>
	"status":1, <font color="red">活动是否开启,0 关闭,1 开启</font><br>
	"updateAt":1462961974,<font color="red">活动时间</font><br>
	"dialog":{ <font color="red">弹窗内容</font><br>
		"name":"xxxxxx" <font color="red">名称</font><br>
		"cover":"xxxxxxxxx", <font color="red">封面图片链接</font><br>
		"url":"http://xxxxxxxxx" <font color="red">封面详情链接</font><br>
	},<br>
	"actys":[<font color="red">具体活动</font><br>
		{	<br>
			"name":"xxxx", <font color="red">名称</font><br>
			"cover":"xxxxxxxxx", <font color="red">图片链接</font><br>
			"url":"http://xxxxxxxxx" <font color="red">详情链接</font><br>
		},.....<br>
	]<br>
}  <br>
   </div>
	
  </body>
</html>
