<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	boolean bJump=true;
	if(bJump){
		response.sendRedirect("analysis.jsp");
		return;
	}
%>
<%
	pageTitle="Java Code Performance Analysis";
%>
<%@ include file="header.jsp" %>
	<hr class="space h200">
	<div class="aCenter">
	  <%if(user.equals("guest")){%>
	  		<div id="login" class="info">
	  			<h3>You are in a guest session,if you want to change your session,click <a href='login.jsp?from=index.jsp'>here</a></h3>
	  		</div>
	  		<hr class="space">
	  		<div id="action" class="line">
	  			<div class="span-11 success h100" style="line-height:100px;">
	  				<a href="benchmark.jsp">Performance benchmark</a>
	  			</div>
			  	<div class="span-11 success right h100" style="line-height:100px;">
			  		<a href="analysis.jsp">Code Analysis</a>
			  	</div>
	  		</div>
	  <%}else{%>
	  		<div id="user" class="info">
	  			<h3>Your session is <span class="cGreen"><%=user%></span>.
	  			If you want to logout,click <a href='login.do?method=out&from=index.jsp'>here</a></h3>
	  		</div>
	  		<hr class="space">
	  		<div id="action" class="line">
			  	<div class="span-7 success h100" style="line-height:100px;">
	  				<a href="patterns.jsp">Pattern List</a>
	  			</div>
			  	<div class="span-8 success h100" style="line-height:100px;">
	  				<a href="benchmark.jsp">Performance benchmark</a>
	  			</div>
			  	<div class="span-7 success right h100" style="line-height:100px;">
			  		<a href="analysis.jsp">Code Analysis</a>
			  	</div>
	  		</div>
	  <%}%>
	</div>
	<hr class="space h200">
<%@ include file="footer.jsp" %>
