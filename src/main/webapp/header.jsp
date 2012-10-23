<%@page import="java.net.URLEncoder"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%!String pageTitle="";%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String pageUrl=request.getRequestURL().toString();
pageUrl=URLEncoder.encode(pageUrl);
%>
<%
	String user=(String)session.getAttribute("user");
	if(user==null || user.equals(""))user="guest";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <!--base href="<%=basePath%>"-->
    <title><%=pageTitle%></title>
 
	<link rel="stylesheet" href="css/common.css" type="text/css" media="screen, projection">
	<!--[if IE]><link rel="stylesheet" href="css/ie.css" type="text/css" media="screen, projection"><![endif]-->
	
	<script type="text/javascript" src="plugin/artDialog/artDialog.js?skin=default"></script>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
  </head>
<body>
	<div id="header" class="bGreen cWhite">
		<div id='title' class="container h30">
			<div class="left"><img src="css/images/logo.png" class="h30"/></div>
			<div class="left f20 fB"> Java Code Performance Analysis </div>
			<div class="right f20">
				<%if(user.equals("guest")){%>
				<a class='cWhite' href='login.jsp?from=<%=pageUrl%>'>Login</a>
				<%}else{%>
				<span class='cRed'><%=user%></span>&nbsp;&nbsp;&nbsp;&nbsp;<a class='cWhite' href='login.do?method=out&from=<%=pageUrl%>'>Logout</a>
				<%}%>
			</div>
		</div>
		<div id='menu' class="cssmenus vrmenu">
			<%if(!user.equals("guest")){%>
			<div class='cssmenu'><a href="patterns.jsp" target="_blank" title='Pattern List'>Pattern List</a></div>
		   	<%}%>
		   	<div class='cssmenu'><a href="benchmark.jsp" target="_blank" title='Benchmark'>Benchmark</a></div>
		   	<div class='cssmenu'><a href="analysis.jsp" target="_blank" title='Code Analysis'>Code Analysis</a></div>
		</div>
	</div>
	<div id="content" class="container" >