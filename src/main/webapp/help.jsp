<%@ page language="java" import="java.util.*,com.jcpa.dao.sql.PatternDaoImpl,com.jcpa.dao.sql.interfaces.PatternDao,com.jcpa.beans.Pattern,com.jcpa.util.ToolUtil" pageEncoding="UTF-8"%>
<%pageTitle="Help"; %>
<%@ include file="header.jsp" %>
<style>
#cat {/*position:fixed;position:absolute\0;+position:absolute;*/}
#cat h1{font-size:17px;}
#cat h2{font-size:15px;margin-left: 15px;}
#cat h3{font-size:13px;margin-left: 30px;}
#cat .cat-h{background:url(css/images/close.gif) no-repeat;padding-left:15px;cursor:pointer;}
#cat .cat-h.closed{background:url(css/images/open.gif) no-repeat;}
#cat .cat-h.selected a{color:green;font-weight:600;}
#cat .cat-h a{text-decoration: none;color: black;}

.pattern{border: dotted 2px green;margin-bottom: 15px;}
</style>

<script type="text/javascript">
$(document).ready(function(){
	//content height
	$("#content").removeClass("container");
	$("#cat").width(260);
	$("#txt").width(document.body.clientWidth-260-65);
	//txt min-height
	$("#txt").css("min-height",$("#cat").height());
	//make menu horizontal,or it will cover the content
	$("#menu").removeClass("vrmenu");
	$("#menu").removeClass("vlmenu");
	$("#menu").addClass("hmenu");
	//init category
	$("#cat .cat-h").each(function(){initCat(this);});
	//get init show from the url
	var url=document.location.href;
	var start=url.lastIndexOf('#');
	var cat="sys";
	if(start>0){
		cat=url.substr(start+1,url.length-start);
	}
	//delay a little time to show help txt
	setTimeout(function(){showTxt(cat);},1000);
});
function initCat(cat){
	$(cat).click(function(){
		if($(this).hasClass('selected')){
			//fold submenu
			var scat=$(this).attr('cat');
			if(typeof(scat)=='string' && scat!=''){
				var subCat=$(".cat-group[cat='"+scat+"']");
				if(subCat.length)subCat.toggle();
				$(this).toggleClass('closed');
			}			
		}else{
			showTxt(cat);	
		}
	});
}
function showTxt(cat){
	if(typeof(cat)=='string'){
		cat=$("#cat .cat-h[cat='"+cat+"']");
	}
	var stxt=$(cat).attr('txt');
	if(typeof(stxt)=='string' && stxt!=''){
		$("#cat .cat-h.selected").removeClass("selected");
		$(cat).addClass("selected");
		$("#txt").html($("#hiddenTxt #"+stxt).html());
	}
}
</script>

<hr class="space">
<div id="manual line">
	<div id="cat" class="left oAuto alert aLeft">
		<h1 cat="sys" txt="sys" class="cat-h"><a href="#sys">Overview</a></h1>
		<div class="cat-group" cat="sys">
			<h2 cat="sysInfo" txt="sysInfo" class="cat-h"><a href="#sysInfo">What's JCPA?</a></h2>
			<h2 cat="sysDetail" txt="sysDetail" class="cat-h"><a href="#sysDetail">Main Features</a></h2>
			<div class="cat-group" cat="sysDetail">
				<h3 cat="sysDetailAnalysis" txt="sysDetailAnalysis" class="cat-h"><a href="#sysDetailAnalysis">Scan Code</a></h3>
				<h3 cat="sysDetailBenchmark" txt="sysDetailBenchmark" class="cat-h"><a href="#sysDetailBenchmark">Run Benchmark Program</a></h3>
				<h3 cat="sysDetailPattern" txt="sysDetailPattern" class="cat-h"><a href="#sysDetailPattern">Maintain Patterns</a></h3>
			</div>
			<h2 cat="sysDetailLogin" txt="sysDetailLogin" class="cat-h"><a href="#sysDetailLogin">User Role</a></h2>
			<h2 cat="patterns" txt="patterns" class="cat-h"><a href="#patterns">PMD and JCPA</a></h2>
			<div class="cat-group" cat="patterns">
				<h3 cat="patternsPMD" txt="patternsPMD" class="cat-h"><a href="#patternsPMD">PMD</a></h3>
				<h3 cat="patternsXPATH" txt="patternsXPATH" class="cat-h"><a href="#patternsXPATH">XPATH</a></h3>
				<h3 cat="patternsSys" txt="patternsSys" class="cat-h"><a href="#patternsSys">Patterns</a></h3>
			</div>
			<h2 cat="problems" txt="problems" class="cat-h"><a href="#problems">FAQ</a></h2>			
		</div>
		<h1 cat="usage" txt="usage" class="cat-h"><a href="#usage">Usage</a></h1>
		<div class="cat-group" cat="usage">
			<h2 cat="usageInstall" txt="usageInstall" class="cat-h"><a href="#usageInstall">Installation</a></h2>
			<div class="cat-group" cat="usageInstall">
				<h3 cat="usageEnvironment" txt="usageEnvironment" class="cat-h"><a href="#usageEnvironment">Environment</a></h3>
				<h3 cat="usageCompile" txt="usageCompile" class="cat-h"><a href="#usageCompile">Compile</a></h3>
				<h3 cat="usageRelease" txt="usageRelease" class="cat-h"><a href="#usageRelease">Release</a></h3>
				<h3 cat="usageCustomize" txt="usageCustomize" class="cat-h"><a href="#usageCustomize">Customize</a></h3>
			</div>
			<h2 cat="usagePlugin" txt="usagePlugin" class="cat-h"><a href="#usagePlugin">Eclipse plugin usage</a></h2>
			<div class="cat-group" cat="usagePlugin">
				<h3 cat="usagePluginInstall" txt="usagePluginInstall" class="cat-h"><a href="#usagePluginInstall">Install pmd eclipse plugin</a></h3>
				<h3 cat="usagePluginRuleset" txt="usagePluginRuleset" class="cat-h"><a href="#usagePluginRuleset">Download ruleset file</a></h3>
				<h3 cat="usagePluginLoad" txt="usagePluginLoad" class="cat-h"><a href="#usagePluginLoad">Load ruleset in eclipse</a></h3>
				<h3 cat="usagePluginScan" txt="usagePluginScan" class="cat-h"><a href="#usagePluginScan">Scan code in eclipse</a></h3>
			</div>
		</div>
	</div>
	<div id="txt" class="right oAuto info aLeft">
	</div>
	<div id="hiddenTxt" style="display:none;">
		<div id="sys">
			<h1>Overview</h1>
			<div id="sysInfo">
				<h2>What's JCPA?</h2>
				<p>Java Code Performance Analysis (JCPA) is a tool to scan  java source code and provide performance recommendations.</p>
				<p>This system includes the following pages:</p>
				<p>1. Code Analysis page,mainly used to analyze codes to generate reports</p>
				<p>2.Performance Benchmark page,mainly used to display quantitative differences of different usages</p>
				<p>3.Pattern List	page	,mainly used to display,modify,add patterns and management,generate,download,upload ruleSetFiles </p>
			</div>
			<div id="sysDetail">
				<h2>Main Features</h2>
				<div id="sysDetailAnalysis">
					<h3>Scan Code</h3>
					<p>page address:<a href="analysis.jsp" target='_blank'>/analysis.jsp</a></p>
					<img src="image/CodeAnalysis0.png" />
					<p>(1) Input the address of the codes which would be analyzed in the SourceUrl text area containing the following conditions :</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;(a). You can input the SVN source address beginning of http、https、svn、file and so on.  And then you need to fill in the UserName text area and Password text area below. For example: https://192.168.132.111/svn/test</p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;(b). You can also input the absolute address of the background server file system. It’s no need to fill in the Username text area and Password text area below at this time.For example:C:\Workspace\Java\src. </p>
					<p>&nbsp;&nbsp;&nbsp;&nbsp;(c). You can also input the address of shared folders of the background server file system or other mounted machines. It’s no need to fill in the Username text area and Password text area below at this time.For example: //192.168.132.151/Code/Java/Src</p>
					<p>(2) Choose the rule set file which need to be used to analyze codes in the RuleSetFile list. </p>
					<p>(3) Click the Analysis button and start to analyze code. The report after finishing the analysis is as shown below. </p>
					<img src="image/CodeAnalysis1.png" />
					<p>If you click the DownReport link on the right top of the page,it will download a report file in the formal of html.</p>
					<p>The middle table of the page is the report list generated by analyzing .</p>
					<img src="image/CodeAnalysis2.png" />
					<p>Click package, class, rule or priority in the head of the form in order to sort the content in the form.</p>
					<p>There are kinds of colors in the report list which represent different serious extent of breaking the rule. The color is relative to the value of priority.If the value of priority is smaller, the color of the line is deeper and extent of breaking rule is more serious. </p>
					<p>You can click the underlined Location column to go to see the View Report page,in this page you can see where break the rule in the source file.	</p>
					<img src="image/CodeAnalysis3.png" />
					<p>There is an error list below the report list. If the list is not empty which shows that there are files that can’t be resolved in the analyzing codes, please check whether the file contains syntax errors.
					If not,it's probable that your patterns expression have something wrong,for example: the brackets don't match.
					</p>
					<img src="image/CodeAnalysis4.png" />
				</div>
				<div id="sysDetailBenchmark">
					<h3>Run Benchmark Program</h3>
					<p>page address:<a href="benchmark.jsp" target="_blank">/benchmark.jsp</a></p>
					<img src="image/benchmark0.png"/>
					<p>If you click a row in the list on the Right-click menu, you can choose to click Run or View menu.</p>
					<img src="image/benchmark1.png"/>
					<p>View page</p>
					<img src="image/benchmark2.png"/>
					<p>Run page</p>
					<img src="image/benchmark3.png"/>
					<p>Input the number of threads to run in the ThreadNum text area, the number of operations performed in each thread in the RepeatNum text area and the proportion of Read operation in the ReadRate text area. And then click Run button. If you want to stop the running program, you can click the stop button on the bottom right of the dialog box to stop the running program. If the program has finished running, the dialog box will display the results returned by the program.</p>
					<p>Run multiple cases at the same time:</p>
					<p>Select more than one case on the list and click the Run button on the left top of the list, the following dialog box will pop up. Run the same way as above mentioned.</p>
					<img src="image/benchmark4.png"/>
				</div>
				<div id="sysDetailPattern">
					<h3>Maintain Patterns</h3>
					<p>page address:<a href="patterns.jsp" target="_blank">/patterns.jsp</a></p>
					<img src="image/patterns0.png" />
					<p>Only login users can access this page. The guest users don’t have permission to access this page, who can check the introduction of the user login and access control to log in.</p>
					<p>(1) add pattern</p>
					<p>Click the Add button on the top left of the list.You can choose what type of patterns you want to add.</p>
					<p><B>a.xpath pattern</B></p>
					<img src="image/patterns1-0.png" />
					<p><span style="color:red;">Attention:</span></p>
					<p>1)If you want to find out which line calls function such as 'method1'、'method2'.</p>
					<p>You can write the expression like this:<span style="color:green;">//PrimarySuffix[@Image='method1' or @Image='method2']</span>.</p>
					<p>You can also write like this in the page:<br>
						Expression: <span style="color:green;">//PrimarySuffix[##AUX_EQ##]</span><br>
						Auxiliary: <span style="color:green;">method1,method2</span><br>
						That means you can write "##AUX_EQ##" in the expression to occupy the position and auxiliary will take its position latter.
					</p>
					<p>What if I need more than one "##AUX_EQ##" in the expression ? For example:</p>
					<p>I want to replace the first position with '@Image="method1" or @Image="method2"' 
						and replace the second position with '@Image="method3" or @Image="method4"'
						and replace the second position with '@Image="method1" or @Image="method2" or @Image="method3" or @Image="method4"':<br>
						Expression: <span style="color:green;">//PrimarySuffix[##AUX_EQ_0##]..........[##AUX_EQ_1##]......[##AUX_EQ##]</span><br>
						Auxiliary: <span style="color:green;">method1,method2##method3,method4##</span><br>
						We use "##" to split the auxiliary filed to many parts.<br>
						"##AUX_EQ_0##"means that position should be replaced by the first part of the auxiliary filed,<br>
						"##AUX_EQ_1##"means that position should be replaced by the second part of the auxiliary filed and so on.<br>
						"##AUX_EQ##" means this position should be replaced by all of the auxiliary filed.
					</p>
					<p>2)If you want to find out which line calls function whose name end with  specific string such as 'fix1'.For example:<br>
						I want to replace the first position with 'ends-with(@Image,"fix1") or ends-with(@Image,"fix2")' 
						and replace the second position with 'ends-with(@Image,"fix3") or ends-with(@Image,"fix4")' 
						and replace the second position with "ends-with(@Image,"fix1") or ends-with(@Image,"fix2") or ends-with(@Image,"fix3") or ends-with(@Image,"fix4")":<br>
						Expression: <span style="color:green;">//PrimarySuffix[##AUX_END_0##]..........[##AUX_END_1##]......[##AUX_END##]</span><br>
						Auxiliary: <span style="color:green;">fix1,fix2##fix3,fix4##</span><br>
						We use "##" to split the auxiliary filed to many parts.<br>
						"##AUX_END_0##"means that position should be replaced by the first part of the auxiliary filed,<br>
						"##AUX_END_1##"means that position should be replaced by the second part of the auxiliary filed and so on.<br>
						"##AUX_END##" means this position should be replaced by all of the auxiliary filed.
					</p>
					<p>3)If you want to find out which line calls function whose name contains  specific string such as 'fix1'.For example:<br>
						I want to replace the first position with 'contains(@Image,"fix1") or contains(@Image,"fix2")' 
						and replace the second position with 'contains(@Image,"fix3") or contains(@Image,"fix4")' 
						and replace the second position with "contains(@Image,"fix1") or contains(@Image,"fix2") or contains(@Image,"fix3") or contains(@Image,"fix4")":<br>
						Expression: <span style="color:green;">//PrimarySuffix[##AUX_CON_0##]..........[##AUX_CON_1##]......[##AUX_CON##]</span><br>
						Auxiliary: <span style="color:green;">fix1,fix2##fix3,fix4##</span><br>
						We use "##" to split the auxiliary filed to many parts.<br>
						"##AUX_CON_0##"means that position should be replaced by the first part of the auxiliary filed,<br>
						"##AUX_CON_1##"means that position should be replaced by the second part of the auxiliary filed and so on.<br>
						"##AUX_CON##" means this position should be replaced by all of the auxiliary filed.</p>
					<p>4)If you just want to replace the aux string in the expression,For example:<br>
						I want to find out whic method need more than two parameter or need none;
						Expression: <span style="color:green;">//PrimarySuffix/Argument[@ArgumentCount>##AUX_REP_0## or @ArgumentCount=##AUX_REP_1##]</span><br>
						Auxiliary: <span style="color:green;">2##0##</span><br>
						Then ##AUX_REP_0## will be replaced by 2 and ##AUX_REP_1## will be replaced by 0
					</p>
					<p>5)What if I want to find out which line calls function whose name start as 'DB' and end as 'er'.<br>
						Expression: <span style="color:green;">//PrimarySuffix[##AUX_REG_0##]</span><br>
						Auxiliary: <span style="color:green;">^DB(.)*er$##^in[A-Z].*##^in$,^out$##</span><br>
						Result: <span style="color:green;">//PrimarySuffix[pmd:matches(@Image,'^DB(.)*er$')]</span><br>
						That means that if you used ##AUX_REG## or ##AUX_REG_0## in the expression,you can write regular string in the auxiliary filed.
					</p>
					<p>6)I want to find out which line call function like this:"obj.func();".But obj must be instance of Class MyObj </p>
					Expression: <span style="color:green;">//BlockStatement[##AUX_CMI_0##]</span><br>
					Auxiliary: <span style="color:green;">MyObj->func##String->split,JsonObject->toString##</span><br>
					"MyObj->func" means that instance of MyObj calls method of func.<br>
					If you want to use regular string in the auxiliary filed,you can use ##AUX_CMI_REG_0## in the expression.<br>
					for example:(.)*Bean->get$,^DBManager$->select$,(.)*DB(.)*->(.)*<br/>
					But you can not write like this:"^DBManager$->^select$".You can't write ^ in the header of method.Reasons are followed:<br/>
					<span class="cGreen">DBManager db;<br>
					db.select();</span><br/>
					The function name are parsed to "db.select",not "select",if you write like this:^DBManager$->^select$,it means you want to list function name start with "select",but the function name is "db.select",so it will not be found.
					You can write like this:^DBManager$->select$.<br/>
					<font style="color:red">Note:You must put "##AUX_CMI_0##" under BlockStatement or PrimaryExpression.</font>That is because the ##AUX_CMI_0## will be replaced by some xpath code,and this code works under BlockStatement.
					<p><B>b.java pattern</B></p>
					<img src="image/patterns1-1.png" />
					<p>If you want to add a java pattern,you must choose which class to handle this pattern.If you want to see how to add a new class to hanlde this pattern,see "How to add a java pattern" in "Customize"</p>
					<p>(2) delete pattern</p>
					<p>If you want to delete a single pattern in the pattern line, you can use the right-click menu to delete.</p>
					<p>If you want to delete many patterns in the pattern line, you can click the Delete button on the top left of the list to delete the patterns selected.</p>
					<p>(3) View specific information of pattern</p>
					<p>Click the pattern on the right-click and choose view, so the following dialog box will pop up.</p>
					<img src="image/patterns2.png" />
					<p>If you click the New Window button, you can view the pattern in a new window. If you click on Edit button, you can edit the pattern information.</p>
					<p>(4) modify the pattern information</p>
					<p>You can modify the pattern information not only by right-click menu ,but also by Edit button in View Pattern window as (3) mentioned.</p>
					<img src="image/patterns3.png" />
					<p>(5) Ruleset Management</p>
					<p>Click the Rulesets button on the upper left of the list, the following dialog box will pop up.</p>
					<img src="image/patterns4.png" />
					<p>You can do operations to a single rule set file such as view, download and delete. </p>
					<p>You can also generate the rule set file by patterns. Click the Generate button, the following dialog box will pops up</p>
					<img src="image/patterns5.png" />
					<p>You need to input the name of the rule set file which will be generated in the FileName text area. </p>
					<p>If you select "All Patterns" , all patterns in the database will be used to generate the rule set files. </p>
					<p>If you select "Selected Patterns" , only selected patterns in the table will be used to generate the rule set files. </p>
					<p>You can also generate these files by the conditions of the type of pattern if you  select "Custom Patterns".</p>
					<img src="image/patterns6.png" />
					<p>Input Where conditions of the sql statement in the displayed text area. For example, if only want to use the pattern of Category equaling "Legacy Usage" to generate xml file, you can write like this: category = 'Legacy Usage'. If you also want to use the pattern of Scope equaling "inner-class", you can use these two conditions together like this: category = 'Legacy Usage' and scope = 'inner-class'</p>
					<p>You can also upload local rule set file.Click the upload button, the following dialog box will pop up.</p>
					<img src="image/patterns7.png" />
					<p>Select the file, then click upload button.</p>
					<p>You can also download the ruleset file.Click the download link.</p>
				</div>
				<div id="sysDetailLogin">
					<h2>User Role</h2>
					<p>The ordinary guest users don’t need to log on, who can only visit the Performance Benchmark page and Code Analysis page. Only users logged in can access the Pattern List page.</p>
					<p>There is a Login link on the right side of the head portion of each page.</p>
					<img src="image/login0.png" />
					<p>Click Login command to jump to the login page.</p>
					<img src="image/login1.png" />
					<p>There is a file contain user information in the background server, which can be modified to manage user information. The path of this file is WEB-INF/user.dat.</p>
					<p>One user need one line to describe the information and the format is as UserName \ tPassword \ r \ n.</p>
					<p>Example:</p>
					<p>Tom	admin123456</p>
　　				<p>John	admin1111</p>
				</div>
			</div>
			<div id="patterns">
			<h2>PMD and JCPA</h2>
			<p>The code analysis of the system is based on the PMD, so the pattern format must be in accordance with the format of PMD, which is described by using the way of XPATH.</p>
			<div id="patternsPMD">
				<h3>PMD</h3>
				<p>PMD scans Java source code and looks for potential problems like:</p>
				<p>1.Possible bugs - empty try/catch/finally/switch statements</p>
				<p>2.Dead code - unused local variables, parameters and private methods</p>
				<p>3.Suboptimal code - wasteful String/StringBuffer usage</p>
				<p>4.Overcomplicated expressions - unnecessary if statements, for loops that could be while loops</p>
				<p>5.Duplicate code - copied/pasted code means copied/pasted bugs</p>
			</div>
			<div id="patternsXPATH">
				<h3>XPATH</h3>
				<p>XPath is a language for searching information through the elements and attributes for navigation in XML documents.</p>
			</div>
			<div id="patternsSys">
				<h3>Patterns</h3>
				<div class="patterns">
					<%
						PatternDao dao = new PatternDaoImpl();
						List<Pattern> list = dao.list("","ID desc");
						Iterator<Pattern> it = list.iterator();
						while(it.hasNext()){
							Pattern p=it.next();%>
							<div class="pattern">
								<div  class="line">
									<span class="span-2">Name:</span>
									<div class="span-20 oHidden"><%=ToolUtil.strHtmlFmt(p.getName())%></div>
								</div>
								<div  class="line">
									<span class="span-2">Type:</span>
									<div class="span-20 oHidden"><%=ToolUtil.strHtmlFmt(p.getPatternType())%></div>
								</div>
								<%if(p.getPatternType()!=null && p.getPatternType().equals("java")) {%>
								<div  class="line">
									<span class="span-2">Class:</span>
									<div class="span-20 oAuto"><%=ToolUtil.strHtmlFmt(p.getJavaClass()) %></div>
								</div>
								<%}else{ %>
								<div  class="line">
									<span class="span-2">Expression:</span>
									<div class="span-20 oAuto"><%=ToolUtil.strHtmlFmt(p.getExpression()) %></div>
								</div>
								<%} %>
								<div  class="line">
									<span class="span-2">Warning:</span>
									<div class="span-20 oAuto"><%=ToolUtil.strHtmlFmt(p.getWarning()) %></div>
								</div>
								<div  class="line">
									<span class="span-2">Category:</span>
									<div class="span-20 oHidden"><%=ToolUtil.strHtmlFmt(p.getCategory()) %></div>
								</div>
								<div  class="line">
									<span class="span-2">Scope:</span>
									<div class="span-20 oHidden"><%=ToolUtil.strHtmlFmt(p.getScope()) %></div>
								</div>
								<div  class="line">
									<span class="span-2">Example:</span>
									<div class="span-20 oAuto"><%=ToolUtil.strHtmlFmt(p.getExample()) %></div>
								</div>
								<div  class="line">
									<span class="span-2">Priority:</span>
									<div class="span-20 oAuto"><%=p.getPriority() %></div>
								</div>
							</div>	
						<%}
					%>
				</div>
			</div>
		</div>
		<div id="problems">
			<h2>FAQ</h2>
			<div class="problem">
				<h3>System compatibility</h3>
				<p>The system considers a variety of browser compatibility as much as possible to ensure universal function and strive for browser aesthetics at the same time. The main functions of the system are performing well in different browser , such as IE7/8/9, Chrome, Firefox, Opera, Safari. 
				We recommend using Chrome or Firefox.</p>
			</div>
			<div class="problem">
				<h3>Ruleset can't be deleted</h3>
				<p>If code process is abort for some reasons when you are analyzing the code, the ruleset file can not be closed normally because of the occupation of java virtual machine all the time.
				The ruleset file can’t be deleted at this time. We can restart the tomcat to solve the problem.</p>
			</div>
			<div class="problem">
				<h3>Why can't I see the pattern list ?</h3>
				<p>Only login user can access this Page.Guest has no this power.</p>
			</div>
			<div class="problem">
				<h3>I add or modify a pattern in one server. I go to Pattern List page in another server. Why can’t see my change?</h3>
				<p>Each server has it's own database based on file system.The database location is "%TOMCAT_HOME%\webapps\jcpa\WEB-INF\db".
					If you changed pattern(s) on server A and you want it make change on server B,you can copy this directory to server B.
				</p>
			</div>
			<div class="problem">
				<h3>The page alerts that "Code Analysis Error:Couldn't find the class net.sourceforget.pmd.rules.XPathRule" when I scan code.</h3>
				<p>That is because the ruleset file you use is only for plugin usage.Go back and select another ruleset.</p>
			</div>
			<div class="problem">	
				<h3>When I scan code with the eclipse pmd plugin,it generate error and says that "An internal error occurred during ReviewCode Implementing class"</h3>
				<img src="image/reviewcode_error.png">
				<p>This error is caused by some source files,we haven't find out what kind of file can cause this problem,maybe it's a bug of pmd plugin.<br>
				In this situation,you can scan your code package by package,and you can find out which file causes this problem.
				</p>
			</div>
			<div class="problem">	
				<h3>Why I can import the ruleset file to eclipse plugin?</h3>
				<p>For now,<font style='color:red'>we only support for pmd plugin vesion 4.0</font>,if your plugin is version lower,just upgrade it.</p>
				<p>If your plugin version is 4.0,then check if the ruleset file is right,and if there are java patterns in the ruleset file,check if you had copy the "%WORKSPACE%\target\pmd-5.1.0-SNAPSHOT.jar" file to cover the "%ECLIPSE_HOME%\plugins\net.sourceforge.pmd.eclipse.plugin_4.0.0.201211080927\lib\pmd-5.1.0-SNAPSHOT.jar"(You must restart eclipse to take effect).</p>
			</div>
			<div class="problem">	
				<h3>I have import the ruleset file to eclipse pmd plugin successed,bu when I scan code,there are no violence,it seems that the patterns dont work.</h3>
				<p>Make sure you have enable pmd in the project properties dialog.</p>
			</div>
			<div class="problem">
				<h3>When I load ruleset file in eclipse,it alert errors--"Project ruleset file already exists......"</h3>
				<img src="image/load_error.png"/>
				<p>Method A:Close eclipse and enter the project folder,open ".pmd" file with notepad.Edit the "ruleSetFileruleSetFile" block with an existent ruleset file path.<br>
					   Method B:New a java project and enable pmd in the project properties.Then close eclipse,copy the ".pmd" file of the newly bulid project to your project.Then reconfig.
				</p>
			</div>
		</div>
		</div>
		<div id="usage">
			<h1>Usage</h1>
			<div id="usageInstall">
				<h2>Installation</h2>
				<div id="usageEnvironment">
					<h3>Environment</h3>
					<p>Jdk1.6.0_33、Maven 3.0.4、Tomcat 7.0</p>
　　				<p>1. install jdk and configure the system environment variables</p>
　　				<p>2. install Maven to configure the environment variables<p>
					<p>&nbsp;&nbsp;(a). visit http://maven.apache.org/download.html to download Maven program. And then unzip the downloaded file to the local folder, for example D:\Program\apache-maven-3.0.4.<p>
					<p>&nbsp;&nbsp;(b). Set the environment variables as following mentioned.
					 Add "MAVEN_HOME" to the name item and add its value is the path of unzipped local folder as "D:\Program\apache-maven-3.0.4".
					 Add ";%MAVEN_HOME%\bin" to the end of the PATH variable to modify its value. </p>
					<p>&nbsp;&nbsp;(c). Open cmd and input "mvn –v"  to check whether the installation is successful.</p>
 　　				<p>3.install Tomcat</p>
				</div>
				<div id="usageCompile">
					<h3>Compile</h3>
					<p>For example, checkout the source code to the directory that values "D:\WorkSpace\MyEclipse\jcpa Maven Webapp."</p>
					<p>1、Open cmd and go to the directory "D:\WorkSpace\MyEclipse\jcpa Maven Webapp".</p>
　　				<p>2、Execute "mvn clean" instruction to delete the previous target directory. (If there is no target directory in the source codes, you don’t need to do this step.)
					<p>3、Execute "mvn package" instruction to pack the program</p>　
					<p>4、You can see some jar packages is downloading by the maven program. 
					If the downloading operation can’t be completed because of the network or other reasons,
					please set maven repository or add the downloaded jar file to maven repository in accordance with the rules.
					( The default path is ". M2" directory in the current user directory.)</p>
　　				<p>5、Go to the target directory, you will see a war file which is packed through the above mentioned direction.</p>
				</div>
				<div id="usageRelease">
					<h3>Release</h3>
					<p>Copy the compiled war package to the webapps directory of tomcat and start tomcat service, the website will release automatically. </p>
					<p>You can input the URL "http://%TOMCAT_URL%/jcpa" directly to access it.</p>
				</div>
				<div id="usageCustomize">
					<h3>Customize</h3>
					<h4>Project encoding：utf8</h4>
					<p>Project files are all encode by utf8.So if you want to customize the code,be careful.</p>
					<h4>Project package</h4>
					<img src="image/customize_package.png">
					<h4>Main Menu</h4>
					<p>If you want to modify the menu, you need to open the file "%TOMCAT_HOME%/webapps/jpca/header.jsp.". <br/>
					Modify the classname of the div item which id equals menu to change the style and location of the main menu:<br/>
					1、Level menu<br>
					&lt;div id='menu' class="cssmenus hmenu"&gt;...&lt;/div&gt;<br>
					2、Vertical to the right<br>
					&lt;div id='menu' class="cssmenus vrmenu"&gt;...&lt;/div&gt;<br>
					3、Vertical to the left<br>
					&lt;div id='menu' class="cssmenus vlmenu"&gt;...&lt;/div&gt;<br>
					</p>
					<h4>Create new Benchmark Program</h4>
					<p>1、New a class extends com.jcpa.cases.Case.(the new class must be in the com.jcpa.cases package too.)</p>
					<p>2、Login and visit the benchmark page.On the top left of the program list will be a button named "Add".Click it.</p>
					<img src="image/customize_ben0.png"/>
					<p>3、The name field must be the class name of step 1.</p>
					<img src="image/customize_ben1.png"/>
					<h4>How to add a java pattern.</h4>
					<p>1、New a class extends com.jcpa.pattern.javaclass.JcpaAbstractJavaRule.(the new class must be in the com.jcpa.pattern.javaclass package too.)</p>
					<p>2、Visit the pattern list page and click add button.Choose 'java' in the type filed,and select the classname you just create in the class filed.</p>
					<p>If you just add a java pattern and grenate a ruleset file,and you want to import the ruleset file to the eclipse plugin,you must repackage the project and copy the "%WORKSPACE%\target\pmd-5.1.0-SNAPSHOT.jar" file to cover the "%ECLIPSE_HOME%\plugins\net.sourceforge.pmd.eclipse.plugin_4.0.0.201211080927\lib\pmd-5.1.0-SNAPSHOT.jar"(You must restart eclipse to take effect).</p>
				</div>				
			</div>
			<div id="usagePlugin">
				<h2>Eclipse plugin usage</h2>
				<div id="usagePluginInstall">
					<h3>Install pmd eclipse plugin</h3>
					<p>1、Select "Help"->"Install New Software" on the eclipse main menu.</p>
					<p>2、Click "Add" button on the Install dialog.</p>
					<p>3、Input a name on the name field.Input "http://sourceforge.net/projects/pmd/files/pmd-eclipse/update-site/" on the Location field.Click OK.<br/><font style="color:red">Note:We only support for plugin v4.0,be cared about the plugin version.</font></p>
					<p>4、You will see some items on the Install dialog.Select and Click next.</p>
					<p>5、While the plugin installing,it may popup a dialog warnning that plugin cant't be trusted,just click ok.</p>
					<p>6、After the plugin installation finish,restart eclipse.</p>
					<p>7、Select "Window"->"Preferences" on the eclipse main menu.</p>
					<p>8、You will see the "PMD" column on the left of Preferences dialog.</p>
				</div>
				<div id="usagePluginRuleset">
					<h3>Download ruleset file</h3>
					<p>1、For login user:<br>
						Go to the pattern list page.<br>
						Click the "Rulesets" button on the top of the patterns table,you will see the list of ruleset files.<br>
						Click the "Download" link after the file you want to download.<br>
					</p>
					<p>2、For guest user:<br/>
						Guest user have no power to access the pattern list page.Login user can download the file and send it to guset user.
					</p>
				</div>
				<div id="usagePluginLoad">
					<h3>Load ruleset in eclipse</h3>
					<p class="cRed">Attention:Before you import the ruleset file,
					you'd better close your eclipse and copy the "%WORKSPACE%\target\pmd-5.1.0-SNAPSHOT.jar" file to cover the "%ECLIPSE_HOME%\plugins\net.sourceforge.pmd.eclipse.plugin_4.0.0.201211080927\lib\pmd-5.1.0-SNAPSHOT.jar" after mvn package,
					then restart eclipse to take effect.Because if there are java patterns in the ruleset file,one java pattern must depend on a certain class.
					</p>
					<p>1、Global load:<br>
						(1)Select "Window"->"Preferences" on the eclipse main menu. <br>
						(2)Selcect "PMD"->"Rules Configuration" on the left of prefrences dialog.<br>
						(3)Click the "import rule set..." button on the right of the preferences dialog.<br>
						(4)Browse and select the ruleset file you want to load.Click OK<br>
						(5)Then in the rule list,you will see the rules have been added.You can select "group by ruleset" or sort the list by the "Rule set" column,
						and the rules whose rule set name are "jcpa pmd" are just imported.<br/>
						<img src="image/customize_load0.png"/><br>
						If you want to make those rules work,you must click to select them.<br/>
					</p>
					<p>2、Load only for one single project.</p>
					<p>
						(1)Click the right mouse button on the project.Click Properties on the popupmenu.<br>
						(2)Select "PMD" on the left of project Properties dialog.<br>
						(3)You can choose which rule you want to take effect or select the checkbox named "Use the ruleset configured in a project file" and location a ruleset file on the properties dialog.<br>
						<img src="image/customize_load1.png" /><br>
						<font class="cRed">Note:We find that if you don't load globally,only load only for one single project,sometimes it doesn't work.But if you load global first and then load the ruleset file for one project,it works well.It seems that is a bug for the plugin.</font>
					</p>
				</div>
				<div id="usagePluginScan">
					<h3>Scan code in eclipse</h3>
					<p>1、Switch to the "PMD" perspective.</p>
					<img src="image/customize_load2.png" />
					<p>2、Click the right mouse button on the source folder or source file(s).Click "PMD"->"Check code with pmd" on the popupmenu.</p>
					<img src="image/customize_load3.png" />
					<p>3、You will see the violations on the Violation Overview(If you can't see this area,Please this menu:Window->Show View->Other->Pmd->Violation Overview).<br/>
					<img src="image/customize_load4.png" /><br>
					You will see there are marks in the package and files,that means this files break rules.
					The Violations Outline will show the Violations current opened file break.
					</p>
				</div>
			</div>
		</div>
	</div>
</div>
<hr class="space">
<%@ include file="footer.jsp" %>
