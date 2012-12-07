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
	<h2 class="aCenter">Performance Benchmark</h2>
	<div id="cases">
		<table id="casesTB">
		</table>
	</div>
</div>
<!-- Run -->
<div id="RunDiv" class="w640" style="display:none;">
	<div class="info">
		<label><span class="cRed">(*)</span>ThreadNum:<input type="text" id="ThreadNum" value="" class="w80"/></label>
		<label><span class="cRed">(*)</span>RepeatNum:<input type="text" id="RepeatNum" value="" class="w80"/></label>
		<label><span class="cRed">(*)</span>Read Rate:<input type="text" id="RWRate" value="" class="w80" title='0~100'/>%</label>
		<input type="button" id="RunCaseBtn" value="Run" onclick="RealRunCase()"/>
		<input type="hidden" id="RunCaseNames" value=""/>
	</div>
	<div id="RunCaseResult" class="info h300 oAuto"></div>
</div>
<!-- View -->
<div id="ViewDiv" class="box w640" style="display:none;">
	<h3 id="ViewCaseName"></h3>
	<div id="ViewCaseDescript" class="success h80 oAuto"></div>
	<div id="ViewCaseResult" class="info h200 oAuto"></div>
</div>
<!-- Add -->
<div id="AddDiv" class="box w400" style="display:none;">
	<div class="line">
		<span class="span-2">Name:</span>
		<span class="span-5">
			<input type="text" id="AddCaseName" class="w280"/>
		</span>
	</div>
	<div class="line">
		<span class="span-2">Description:</span>
		<span class="span-8">
			<textarea id="AddCaseDescript" class="w280 h200"></textarea>
		</span>
	</div>
	<div class="line">
		<p class="cRed">Before Adding,make sure you already have a class has the same name as the 'name' field in the com.jcpa.cases package.</p>
	</div>
</div>
<script type="text/javascript">
<%if(!user.equals("guest")){%>
btns.push({ name: 'Add', displayname: "Add", onpress: toolbarItem_onclick });
<%}%>
</script>
<%@ include file="footer.jsp"%> 
