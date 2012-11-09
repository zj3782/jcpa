<%@ page language="java" import="java.util.*,com.jcpa.util.analysis.CodeReports,com.jcpa.util.json.JsonObjectNode,com.jcpa.util.ToolUtil" pageEncoding="UTF-8"%>
<%pageTitle="Code Analysis";%>
<%
	CodeReports report=(CodeReports)session.getAttribute("AnalysisReport");
%>

<%@ include file="header.jsp" %>
<link rel="stylesheet" href="plugin/flexigrid/css/flexigrid.css" type="text/css">
<script type="text/javascript" src="plugin/flexigrid/js/jquery.flexigrid.js"></script>
<script type="text/javascript" src="plugin/jquery.contextmenu.js"></script>
<script type="text/javascript" src="js/analysis.js"></script>

<hr class="space h20"/>
<h2 class="aCenter">Code Analysis</h2>
<div id="analysis">
	<div class="box f12 clearfix" id="codeTitle" style="display:none;">
		<span class="span-19">
			<span class="f14 fB">Url:&nbsp;&nbsp;</span><span id="codeUrl" class="cGreen"></span>&nbsp;&nbsp;&nbsp;
			<span class="f14 fB">User:&nbsp;&nbsp;</span><span id="codeUser" class="cGreen"></span>
		</span>
		<span class="right">
			<a href="javascript:;" onclick="ReAnalysis();" title="Back">Back</a>
			<a href="analysis.do?method=DownReport" target='_blank' title="Download Reports">DownReport</a>
		</span>
	</div>
	<%if(report==null){%>
	<div id="login" class="clearfix box">
		<div class="span-19">
			<div class="line">
				<span class="left w360">
					<label for="url">SourceUrl:</label><input type="text" id="url" class="w240"/>
				</span>
				<span class="left w360">
					<label for="rule">RuleSetFile:</label><select id="rule" class="w240">
					<% 
						List<String> files=ToolUtil.getFileFromPath((String)application.getAttribute("Ruleset"),"xml");
						Iterator<String> it=files.iterator();String op;
						while(it.hasNext()){
							op=it.next();
							out.println("<option value='"+op+"'>"+op+"</option>");
						}
					%>
					</select>
				</span>
			</div>
			<div class="line">
				<span class="left w360">
					<label for="user">UserName:</label><input type="text" id="user" class="w240"/>
				</span>
				<span class="left w360">
					<label for="pwd">PassWord:</label><input type="password" id="pwd" class="w240"/>
				</span>
			</div>
		</div>
		<div class="span-3">
			<button class="round cWhite h70 w80 f14 fB bGreen bdNone cHand" id="submit" onclick="Analysis()">Analysis</button>
		</div>
	</div>
	<%}else{%>
		<script type="text/javascript">
			$(document).ready(function(){
				StartIntervalGetStep();
				$("#codeTitle").show();
				$("#codeUrl").html('<%=(String)session.getAttribute("codeUrl")%>');
				$("#codeUser").html('<%=(String)session.getAttribute("codeUser")%>');
				//显示选择分析结果还是从新分析
				$("#analysis").hide();
				$("#chooseAnalysis").show();
			});
		</script>
	<%}%>
	<hr class="space">
	<!-- report -->
	<div id="reportArea" <%if(report==null){out.println("style='display:none;'");}%>>
		<div class="info">
			<h3 class="aCenter" >Reports</h3>
			<div id="report" class="oAuto">
				<table id="reportTB"></table>
			</div>
		</div>
		<div class="error">
			<h3 class="aCenter" >Errors</h3>
			<div id="error" class="oAuto">
				<table id="errorTB"></table>
			</div>
		</div>
	</div>
	<!-- 查看单个report -->
	<div id="viewReport" class="box" style="display:none;">
		<div  class="line">
			<span class="span-2">Package:</span><div id="viewPackage" class="span-15 oHidden"></div>
		</div>
		<div  class="line">
			<span class="span-2">Class:</span><div id="viewClass" class="span-15 oAuto"></div>
		</div>
		<div  class="line">
			<span class="span-2">Method:</span><div id="viewMethod" class="span-15 oAuto"></div>
		</div>
		<div  class="line">
			<span class="span-2">Location:</span><div id="viewLocation" class="span-15"></div>
		</div>
		<div  class="line">
			<span class="span-2">Code:</span><div id="viewCode" class="span-15 oAuto"></div>
		</div>
		<div  class="line">
			<span class="span-2">Rule:</span><div id="viewRule" class="span-15 oAuto"></div>
		</div>
		<div  class="line">
			<span class="span-2">Priority:</span><div id="viewPriority" class="span-15 oAuto"></div>
		</div>
	</div>
</div>
<%if(report!=null){%>
<!-- 选择 -->
<div id="chooseAnalysis" style="display:none;">
	<hr class="space h60">
	<div class="span-11 aCenter left f14 alert" title="Report of <%=(String)session.getAttribute("codeUrl")%>">
		<a class="cHand" herf="javascript:;" onclick='$("#analysis").show();$("#chooseAnalysis").hide();'>Show last report</a>
	</div>
	<div class="span-11 aCenter right f14 info" title="Clear last report and start a new analysi">
		<a class="cHand" herf="javascript:;" onclick='ReAnalysis(false);this.innerHTML="Preparing...";''>New analysis</a>
	</div>
	<hr class="space h80">
</div>
<%}%>
<hr class="space h20"/>
<%@ include file="footer.jsp" %>
