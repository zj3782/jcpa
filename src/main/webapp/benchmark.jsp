<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%pageTitle="Performance benchmark"; %>
<%
	String sPage=request.getParameter("p");
	if(sPage==null || sPage.equals(""))sPage="1";
%>

<%@ include file="header.jsp"%>
<link rel="stylesheet" href="plugin/flexigrid/css/flexigrid.css" type="text/css">
<script type="text/javascript" src="plugin/flexigrid/js/jquery.flexigrid.js"></script>
<script type="text/javascript" src="plugin/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/benchmark.js"></script>
<script type="text/javascript">
initPage=<%=sPage%>;
</script>
<div id="benchmark">
   	<hr class="space" />
	<h2 class="aCenter">Performance benchmark</h2>
	<div id="cases">
		<table id="casesTB">
		</table>
	</div>
</div>
<div id="RunDiv" class="box w640" style="display:none;">
	<div class="info">
		<label>(*)ThreadNum:<input type="text" id="ThreadNum" value="" class="w80"/></label>
		<label>(*)RepeatNum:<input type="text" id="RepeatNum" value="" class="w80"/></label>
		<label>(*)Read Rate:<input type="text" id="RWRate" value="" class="w80" title='0~100'/>%</label>
		<input type="button" id="RunCaseBtn" value="Run" onclick="RealRunCase()"/>
		<input type="hidden" id="RunCaseNames" value=""/>
	</div>
	<div id="RunCaseResult" class="info h400 oAuto">
	</div>
</div>
<div id="ViewDiv" class="box w640" style="display:none;">
	<h2 id="ViewCaseName"></h2>
	<div id="ViewCaseDescript" class="success oAuto"></div>
	<div id="ViewCaseResult" class="info oAuto"></div>
</div>
<%@ include file="footer.jsp"%> 
