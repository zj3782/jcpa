<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%pageTitle="Pattern List"; %>
<%@ include file="header.jsp"%>
<%
	if(user.equals("guest")){
		response.sendRedirect("login.jsp?from=patterns.jsp");
		return;
	}
	String sPage=request.getParameter("p");
	if(sPage==null || sPage.equals(""))sPage="1";
%>
<link rel="stylesheet" href="plugin/flexigrid/css/flexigrid.css" type="text/css">
<script type="text/javascript" src="plugin/flexigrid/js/jquery.flexigrid.js"></script>
<script type="text/javascript" src="plugin/jquery.contextmenu.js"></script>
<script type="text/javascript" src="plugin/ajaxfileupload.js"></script>
<script type="text/javascript" src="js/patterns.js"></script>
<script type="text/javascript">
initPage=<%=sPage%>;
</script>

<div class="pattern">
	<hr class="space" />
	<h2 class="aCenter">Pattern List</h2>
	<div id="patterns">
		<table id="patternTB">
		</table>
	</div>
</div>
<!-- 添加或者修改pattern -->
<div id="patternAddEdit" class="box" style="display:none;">
	<div  class="line">
		<span class="span-2">(*)Name:</span><input type="text" id="patternName" class="span-15"/>
	</div>
	<div  class="line">
		<span class="span-2">(*)Expression:</span><textarea id="patternExpression" class="span-15 h100"></textarea>
	</div>
	<div  class="line">
		<span class="span-2">(*)Warning:</span><textarea id="patternWarning" class="span-15 h80"></textarea>
	</div>
	<div  class="line">
		<span class="span-2">(*)Category:</span>
		<select id="patternCategory" class="span-15">
			<option value="Frequent IOs">Frequent IOs</option>
			<option value="Memory Leak">Memory Leak</option>
			<option value="Corrupt Thread State">Corrupt Thread State</option>
			<option value="Excessive Synchronization">Excessive Synchronization</option>
			<option value="Legacy Usage">Legacy Usage</option>
			<option value="Duplicate Invocations">Duplicate Invocations</option>
			<option value="Redundant Operations">Redundant Operations</option>
			<option value="Other" selected="selected">Other</option>
		</select>
	</div>
	<div  class="line">
		<span class="span-2">(*)Scope:</span>
		<select id="patternScope" class="span-15">
			<option value="inner-class" selected>inner-class</option>
			<option value="inter-class">inter-class</option>
		</select>
	</div>
	<div  class="line">
		<span class="span-2">(*)Example:</span><textarea id="patternExample" class="span-15 h100"></textarea>
	</div>
	<div  class="line">
		<span class="span-2">(*)Priority:</span>
		<select id="patternPriority" class="span-15">
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3" selected>3</option>
			<option value="4">4</option>
			<option value="5">5</option>
		</select>
	</div>
</div>
<!-- 查看pattern -->
<div id="patternDetail" class="box" style="display:none;">
	<div  class="line">
		<span class="span-2">Name:</span><div id="patternNameDiv" class="span-15 oHidden"></div>
	</div>
	<div  class="line">
		<span class="span-2">Expression:</span><div id="patternExpressionDiv" class="span-15 h200 oAuto"></div>
	</div>
	<div  class="line">
		<span class="span-2">Warning:</span><div id="patternWarningDiv" class="span-15 h80 oAuto"></div>
	</div>
	<div  class="line">
		<span class="span-2">Category:</span><div id="patternCategoryDiv" class="span-15 oHidden"></div>
	</div>
	<div  class="line">
		<span class="span-2">Scope:</span><div id="patternScopeDiv" class="span-15 oHidden"></div>
	</div>
	<div  class="line">
		<span class="span-2">Example:</span><div id="patternExampleDiv" class="span-15 h100 oAuto"></div>
	</div>
	<div  class="line">
		<span class="span-2">Priority:</span><div id="patternPriorityDiv" class="span-15 oHidden"></div>
	</div>
</div>
<!-- 管理ruleset -->
<div id="rulesets" style="display:none;" class="h300 w400 oAuto">
	<style>#rulesetsTB td{border-collapse: collapse; border:1px dotted #A8D8EB;background:#F5FFEF;padding:5px;}</style>
	<div>
		<a href="javascript:;" onclick="AddRuleSet();">Generate</a>
		<a href="javascript:;" onclick="UpRuleSet();">Upload</a>
	</div>
	<table id="rulesetsTB" class="w100p" cellspacing="0" cellpadding="5"><tbody></tbody></table>
</div>
<!-- 添加ruleset -->
<div id="addruleset" style="display:none;">
<div class="line">
	<label>FileName:<input type="text" id="AddFileName" class="w200"/></label>
</div>
<div class="line">
	<label>Patterns:</label>
	<label><input type="checkbox" id="AddPatternAll" checked="checked" onclick="$('#AddPattCond').toggle();">All Patterns</label><br>	
	<textarea id="AddPattCond" class="w100p h60" style="display:none;" 
	title="Here to fill in the SQL where statements, for example:ID &gt 10 and scope = 'function'"></textarea>
</div>
</div>
<!-- 上传ruleset -->
<div id="upruleset" style="display:none;">
	<form name="form" action="" method="POST" enctype="multipart/form-data">
		<input id="fileToUpload" type="file" name="fileToUpload"/>
		<input id="buttonUpload" type="button" onclick="ajaxUpRuleSet();" value="Upload"/>
	</form>
</div>
<%@ include file="footer.jsp"%>