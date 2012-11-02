X<%@ page language="java" import="java.util.*,com.jcpa.dao.sql.PatternDaoImpl,com.jcpa.dao.sql.interfaces.PatternDao,com.jcpa.beans.Pattern,com.jcpa.util.ToolUtil" pageEncoding="UTF-8"%>
<%pageTitle="Help"; %>
<%@ include file="header.jsp" %>
<style>
#cat {position:fixed;position:absolute\0;+position:absolute;}
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
	//设置界面元素宽度
	$("#content").removeClass("container");
	$("#cat").width(260);
	$("#txt").width(document.body.clientWidth-260-65);
	//内容部分最小高度
	$("#txt").css("min-height",$("#cat").height());
	//菜单挡住了内容，变换成水平菜单
	$("#menu").removeClass("vrmenu");
	$("#menu").addClass("hmenu");
	//初始化目录
	$("#cat .cat-h").each(function(){initCat(this);});
	//初始显示什么内容
	var url=document.location.href;
	var start=url.lastIndexOf('#');
	var cat="sys";
	if(start>0){
		cat=url.substr(start+1,url.length-start);
	}
	setTimeout(function(){showTxt(cat);},1000);
});
function initCat(cat){
	$(cat).click(function(){
		if($(this).hasClass('selected')){
			//折叠子菜单
			var scat=$(this).attr('cat');
			if(typeof(scat)=='string' && scat!=''){
				var subCat=$(".cat-group[cat='"+scat+"']");
				if(subCat.length)subCat.toggle();
				$(this).toggleClass('closed');
			}			
		}else{
			//更换显示内容
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
	<div id="cat" class="left oAuto alert">
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
			</div>
			<h2 cat="usagePlugin" txt="usagePlugin" class="cat-h"><a href="#usagePlugin">Eclipse plugin usage</a></h2>
		</div>
	</div>
	<div id="txt" class="right oAuto info">
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
					<p>If you click the ReAnalysis link on the right top of the page, you can return to the previous page to analyze code again.</p>
					<p>If you click the ExportResult link on the right top of the page to export results, the following dialog will pop up.</p>
					<img src="image/CodeAnalysis2.png" />
					<p>You can only need to press Ctrl + C to copy down the report generated by analyzing in the text box which are selected in default。You can also click the Download link on the upper left of the page to download a report in the formal of html.</p>
					<p>The middle of the page is the report list generated by analyzing 。</p>
					<p>Click package, class, rule or priority in the head of the form in order to sort the content in the form.</p>
					<p>There are kinds of colors in the report list which represent different serious extent of breaking the rule. The color is relative to the value of priority。If the value of priority is smaller, the color of the line is deeper and extent of breaking rule is more serious. </p>
					<p>If you click a row in the list on the right-click menu and select the View menu, you can view the details report of this row.</p>
					<p>You can click the underlined content to go to see the View Report page, as the same function as the View menu of the right-click menu.	</p>
					<img src="image/CodeAnalysis3.png" />
					<p>There is an error list below the report list. If the list is not empty which shows that there are files that can’t be resolved in the analyzing codes, please check whether the file contains syntax errors.
					If not,make sure your patterns expression is correct.
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
					<p>Click the Add button on the top left of the list.</p>
					<img src="image/patterns1.png" />
					<p><span style="color:red;">Attention:</span>If you meet the following situation--you want to find the line which call some function such as 'method1'、'method2'.</p>
					<p>You can write the expression like this:<span style="color:green;">//PrimarySuffix[@Image='method1' or @Image='method2']</span>.</p>
					<p>You can also write like this:<br>
						Expression: <span style="color:green;">//PrimarySuffix[##AUX##]</span><br>
						Auxiliary: <span style="color:green;">method1,method2</span><br>
						That means you can write "##AUX##" in the expression to occupy the position and auxiliary will take its position latter.
					</p>
					<p>What if there are more than one "##AUX##" in the expression ? </p>
					<p>You can write like this:<br>
						Expression: <span style="color:green;">//PrimarySuffix[##AUX##]..........[##AUX##]</span><br>
						Auxiliary: <span style="color:green;">method1,method2##class1,class2,class3##</span><br>
						You can use "##" to split in the auxiliary filed.
					</p>
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
					<p>You need to input the name of the rule set file which will be generated in the FileName text area. (For example, if you want to generate the test.xml, you only need to input the word of "test".)</p>
					<p>If you select All Patterns in the patterns line, all patterns in the database will be used to generate the rule set files. You can also generate these files by the conditions of the type of pattern if you don’t select All Patterns.</p>
					<img src="image/patterns6.png" />
					<p>Input Where conditions of the sql statement in the displayed text area. For example, if only want to use the pattern of Category equaling "Legacy Usage" to generate xml file, you can write like this: category = 'Legacy Usage'. If you also want to use the pattern of Scope equaling "inner-class", you can use these two conditions together like this: category = 'Legacy Usage' and scope = 'inner-class'</p>
					<p>You can also upload local rule set file.Click the upload button, the following dialog box will pop up.</p>
					<img src="image/patterns7.png" />
					<p>Select the file, then click upload button.</p>
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
									<div id="patternNameDiv" class="span-20 oHidden"><%=ToolUtil.strHtmlFmt(p.getName())%></div>
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
						<%}
					%>
				</div>
			</div>
		</div>
		<div id="problems">
			<h2>FAQ</h2>
			<div class="problem">
				<h3>System Compatibility</h3>
				<p>The system considers a variety of browser compatibility as much as possible to ensure universal function and strive for browser aesthetics at the same time. The main functions of the system are performing well in different browser , such as IE7/8/9, Chrome, Firefox, Opera, Safari. 
				We recommend using Chrome or Firefox.</p>
			</div>
			<div class="problem">
				<h3>Ruleset Can't Be Deleted</h3>
				<p>If code process is abort for some reasons when you are analyzing the code, the ruleset file can not be closed normally because of the occupation of java virtual machine all the time.
				The ruleset file can’t be deleted at this time. We can restart the tomcat to solve the problem.</p>
			</div>
			<div class="problem">
				<h3>Why Can't I See The Pattern List ?</h3>
				<p>Only Login User Can Access This Page.Guest has no this power.</p>
			</div>
			<div class="problem">
				<h3>I add or modify a pattern in one server. I go to Pattern List page in another server. Why can’t see my change?</h3>
				<p>Each server has it's own database based on file system.The database location is "%TOMCAT_HOME%\webapps\jcpa\WEB-INF\db".
					If you changed pattern(s) on server A and you want it make change on server B,you can copy this directory to server B.
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
					<p>Copy the compiled war package to the webapps directory of tomcat and start tomcat service, the website will release automatically. You can input the URL "http://%TOMCAT_URL%/jcpa" directly to access it.</p>
				</div>
			</div>
			<div id="usagePlugin">
				<h2>Eclipse plugin usage</h2>
			</div>
		</div>
	</div>
</div>
<hr class="space">
<%@ include file="footer.jsp" %>
