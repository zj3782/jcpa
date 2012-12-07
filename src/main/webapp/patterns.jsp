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
<style>
#patternAddEdit .line span{height: 30px;line-height: 30px;}
</style>
<div id="patternAddEdit" class="" style="display:none;">
	<div  class="line" id="patternNameBlock">
		<span class="span-2"><span class="cRed">(*)</span>Name:</span><input type="text" id="patternName" class="span-15"/>
	</div>
	<div  class="line">
		<div class="span-6" id="patternTypeBlock">
			<span class="span-2"><span class="cRed">(*)</span>Type:</span>
			<select id="patternType" class="span-3" onchange="changePatternType(this.value);" onkeyup="this.blur();this.focus();">
				<option value="xpath" selected="selected">xpath</option>
				<option value="java">java</option>
			</select>
		</div>
		<div class="span-10" id="patternClassBlock" style="display:none;">
			<span class="span-2"><span class="cRed">(*)</span>Class:</span>
			<select id="patternClass" class="span-7">
				<option value="">loading...</option>
			</select>
		</div>
	</div>
	<div  class="line"  id="patternExpressionBlock">
		<span class="span-2"><span class="cRed">(*)</span>Expression:</span><textarea id="patternExpression" class="span-15 h100"></textarea>
	</div>
	<div  class="line" id="patternAuxiliaryBlock">
		<span class="span-2">Auxiliary:</span><textarea id="patternAuxiliary" class="span-15 h40"></textarea>
	</div>
	<div  class="line" id="patternWarningBlock">
		<span class="span-2"><span class="cRed">(*)</span>Warning:</span><textarea id="patternWarning" class="span-15 h60"></textarea>
	</div>
	<div  class="line">
		<div class="left"  id="patternCategoryBlock">
			<span class="span-2"><span class="cRed">(*)</span>Category:</span>
			<select id="patternCategory" class="span-4">
				<option value="ExcessiveSynchronization">ExcessiveSynchronization</option>
				<option value="RedundantStatement">RedundantStatement</option>
				<option value="RedundantInvocation">RedundantInvocation</option>
				<option value="SuboptimalStatement">SuboptimalStatement</option>
				<option value="UnsafeDataAccess">UnsafeDataAccess</option>
				<option value="Other" selected="selected">Other</option>
			</select>
		</div>
		
		<div class="left"  id="patternScopeBlock">
			<span class="span-2"><span class="cRed">(*)</span>Scope:</span>
			<select id="patternScope" class="span-4">
				<option value="inner-class" selected>inner-class</option>
				<option value="inter-class">inter-class</option>
			</select>
		</div>
		
		<div class="left"  id="patternPriorityBlock">
			<span class="span-2"><span class="cRed">(*)</span>Priority:</span>
			<select id="patternPriority" class="span-2">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3" selected>3</option>
				<option value="4">4</option>
				<option value="5">5</option>
			</select>
		</div>
	</div>
	<div  class="line" id="patternExampleBlock">
		<span class="span-2"><span class="cRed">(*)</span>Example:</span><textarea id="patternExample" class="span-15 h80"></textarea>
	</div>
</div>
<!-- 查看pattern -->
<div id="patternDetail" class="" style="display:none;">
	<div  class="line" id="patternNameBlockDiv">
		<span class="span-2">Name:</span><div id="patternNameDiv" class="span-15 oHidden cGreen"></div>
	</div>
	<div  class="line">
		<div class="span-6" id="patternTypeBlockDiv">
			<span class="span-2">Type:</span>
			<div id="patternTypeDiv" class="span-3 oHidden cGreen"></div>
		</div>
		<div class="span-10" id="patternClassBlockDiv">
			<span class="span-2">Class:</span>
			<div id="patternClassDiv" class="span-7 oHidden cGreen"></div>
		</div>
	</div>
	<div  class="line" id="patternExpressionBlockDiv">
		<span class="span-2">Expression:</span><div id="patternExpressionDiv" class="span-15  h100 oAuto cGreen"></div>
	</div>
	<div  class="line" id="patternAuxiliaryBlockDiv">
		<span class="span-2">Auxiliary:</span><div id="patternAuxiliaryDiv" class="span-15  h40 oAuto cGreen"></div>
	</div>
	<div  class="line" id="patternWarningBlockDiv">
		<span class="span-2">Warning:</span><div id="patternWarningDiv" class="span-15 h60  oAuto cGreen"></div>
	</div>
	<div  class="line">
		<div class="left" id="patternCategoryBlockDiv">
			<span class="span-2">Category:</span>
			<div id="patternCategoryDiv" class="span-5 oHidden cGreen"></div>
		</div>
		<div class="left" id="patternScopeBlockDiv">
			<span class="span-2">Scope:</span>
			<div id="patternScopeDiv" class="span-4 oHidden cGreen"></div>
		</div>
		<div class="left" id="patternPriorityBlockDiv">
			<span class="span-2">Priority:</span>
			<div id="patternPriorityDiv" class="span-2 oHidden cGreen"></div>
		</div>
	</div>
	<div  class="line" id="patternExampleBlockDiv">
		<span class="span-2">Example:</span><div id="patternExampleDiv" class="span-15  h80 oAuto cGreen"></div>
	</div>
</div>
<!-- 管理ruleset -->
<style>#rulesets #rulesetsTB td{border-collapse: collapse; border:1px dotted #A8D8EB;background:#F5FFEF;padding:5px;}</style>
<div id="rulesets" style="display:none;" class="h300 w400 oAuto">
	<div>
		<a href="javascript:;" onclick="AddRuleSet();">Generate</a>
		<a href="javascript:;" onclick="UpRuleSet();">Upload</a>
	</div>
	<table id="rulesetsTB" class="w100p" cellspacing="0" cellpadding="5"><tbody></tbody></table>
</div>
<!-- 添加ruleset -->
<div id="addruleset" style="display:none;">
<div class="line">
	<span class="span-2"><label>FileName:</label></span>
	<span class="span-9"><input type="text" id="AddFileName" class="w320"/></span>
</div>
<div class="line">
	<span class="span-2"><label>Description:</label></span>
	<span class="span-9"><textarea id="AddDescription" class="w320 h100"></textarea></span>
</div>
<div class="line">
	<span class="span-2"><label>Patterns:</label></span>
	<span class="span-9">
		<label><input type="radio" id="AddPatternAll" checked="checked" name="addPatterns" onclick="$('#AddPattCond').hide();">All patterns</label>
		<label><input type="radio" id="AddPatternSelect"  name="addPatterns" onclick="$('#AddPattCond').hide();">Selected patterns</label>
		<label><input type="radio" id="AddPatternCond"  name="addPatterns" onclick="$('#AddPattCond').show();">Custom patterns</label>
		<br>
		<textarea id="AddPattCond" class="w320 h60" style="display:none;" 
		title="Here to fill in the SQL where statements, for example:ID &gt 10 and scope = 'function'"></textarea>
	</span>
</div>
</div>
<!-- 上传ruleset -->
<div id="upruleset" style="display:none;">
	<form name="form" action="" method="POST" enctype="multipart/form-data">
		<div class="line">
			<input id="fileToUpload" type="file" name="fileToUpload" class="w240"/>
			<input id="buttonUpload" type="button" onclick="ajaxUpRuleSet();" value="Upload" class="w80"/>
		</div>
		<div class="line">
			<span class="span-2"><label>Description:</label></span>
			<span class="span-7"><textarea id="upDescription" class="w240 h50"></textarea></span>
		</div>
	</form>
</div>
<%@ include file="footer.jsp"%>