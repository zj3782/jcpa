<%@ page language="java" import="java.util.*,com.jcpa.dao.sql.interfaces.PatternDao,com.jcpa.dao.sql.PatternDaoImpl,com.jcpa.beans.Pattern,com.jcpa.util.ToolUtil" pageEncoding="UTF-8"%>
<%
	int id=ToolUtil.strToInt(request.getParameter("id"),-1);
	PatternDao dao=new PatternDaoImpl();
	Pattern p=null;
	if(id<=0 || (p=dao.get(id))==null){
		request.getRequestDispatcher("404.jsp").forward(request,response);
		return;
	}
	String name=ToolUtil.strHtmlFmt(p.getName());
%>
<%pageTitle="Pattern["+name+"]"; %>
<%@ include file="header.jsp" %>

<div id="pattern">
	<input type="hidden" id="patternID" value="<%=id%>" />
	<hr class="space"/>
	<h2 class="aCenter"><%=name%></h2>
	<!-- 查看pattern -->
	<div id="patternDetail" class="box">
		<div  class="line">
			<span class="span-2">Name:</span>
			<div id="patternNameDiv" class="span-20 oHidden"><%=name%></div>
		</div>
		<div  class="line">
			<span class="span-2">Expression:</span>
			<div id="patternExpressionDiv" class="span-20 oAuto"><%=ToolUtil.strHtmlFmt(p.getExpression()) %></div>
		</div>
		<div  class="line">
			<span class="span-2">Warning:</span>
			<div id="patternWarningDiv" class="span-20 oAuto"><%=ToolUtil.strHtmlFmt(p.getWarning()) %></div>
		</div>
		<div  class="line">
			<span class="span-2">Category:</span>
			<div id="patternCategoryDiv" class="span-20 oHidden"><%=ToolUtil.strHtmlFmt(p.getCategory()) %></div>
		</div>
		<div  class="line">
			<span class="span-2">Scope:</span>
			<div id="patternScopeDiv" class="span-20 oHidden"><%=ToolUtil.strHtmlFmt(p.getScope()) %></div>
		</div>
		<div  class="line">
			<span class="span-2">Example:</span>
			<div id="patternExampleDiv" class="span-20 oAuto"><%=ToolUtil.strHtmlFmt(p.getExample()) %></div>
		</div>
		<div  class="line">
			<span class="span-2">Priority:</span>
			<div id="patternPriorityDiv" class="span-20 oAuto"><%=p.getPriority() %></div>
		</div>
	</div>
</div>

<%@ include file="footer.jsp"%>