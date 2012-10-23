<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%pageTitle="Login";%>
<%
String from=request.getParameter("from");
String initUser=(String)request.getAttribute("user");
String error=(String)request.getAttribute("error");
if(from==null)from="";
if(initUser==null)initUser="";
%>
<%@ include file="header.jsp" %>
<script type="text/javascript">
function LoginSubmit(){
	if(ID("user").value==""){
		alert("Please Input Username!");
		return false;
	}
	if(ID("password").value==""){
		alert("Please Input Password!");
		return false;
	}
	ID("loginForm").submit();
	return true;
}
</script>
<hr class="space h100">
<%if(error!=null && !error.equals("")){%>
<div id="error" class="error">
	<%=error%>
</div>
<%}%>
<div id="login" class="info aCenter">
	<form action="login.do?method=in" method="post" id="loginForm">
		<input type="hidden" value="<%=from%>" name="from" id="from"/>
		<label>UserName:<input type="text" name="user" id="user" value="<%=initUser%>"></label><br>
		<label>PassWord:<input type="password" name="password" id="password"></label><br>
		<input type="button" value="Login" onclick="LoginSubmit();"/><input type="reset" value="Reset"/>
	</form>
</div>
<hr class="space h100">
<%@ include file="footer.jsp" %>