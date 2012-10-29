<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	showTxt(cat);
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
<h1 class="aCenter cBlue">The User Manual Of JCPA</h1>
<div id="manual line">
	<div id="cat" class="left oAuto alert">
		<h1 cat="sys" txt="sys" class="cat-h"><a href="#sys">System Introduction</a></h1>
		<div class="cat-group" cat="sys">
			<h2 cat="sysInfo" txt="sysInfo" class="cat-h"><a href="#sysInfo">The Brief Introduction</a></h2>
			<h2 cat="sysDetail" txt="sysDetail" class="cat-h"><a href="#sysDetail">System Detailed Explanation</a></h2>
			<div class="cat-group" cat="sysDetail">
				<h3 cat="sysDetailAnalysis" txt="sysDetailAnalysis" class="cat-h"><a href="#sysDetailAnalysis">Code Analysis</a></h3>
				<h3 cat="sysDetailBenchmark" txt="sysDetailBenchmark" class="cat-h"><a href="#sysDetailBenchmark">Performance Benchmark</a></h3>
				<h3 cat="sysDetailPattern" txt="sysDetailPattern" class="cat-h"><a href="#sysDetailPattern">Pattern List</a></h3>
				<h3 cat="sysDetailLogin" txt="sysDetailLogin" class="cat-h"><a href="#sysDetailLogin">User and Access Control</a></h3>
			</div>
		</div>
		<h1 cat="patterns" txt="patterns" class="cat-h"><a href="#patterns">A Detailed Pattern Introduction</a></h1>
		<div class="cat-group" cat="patterns">
			<h2 cat="patternsPMD" txt="patternsPMD" class="cat-h"><a href="#patternsPMD">PMD</a></h2>
			<div class="cat-group" cat="patternsPMD">
				<h3 cat="patternsPMDBrief" txt="patternsPMDBrief" class="cat-h"><a href="#patternsPMDBrief">Brief Introduction</a></h3>
				<h3 cat="patternsPMDPlugin" txt="patternsPMDPlugin" class="cat-h"><a href="#patternsPMDPlugin">Eclipse Plugin</a></h3>
			</div>
			<h2 cat="patternsXPATH" txt="patternsXPATH" class="cat-h"><a href="#patternsXPATH">XPATH</a></h2>
			<h2 cat="patternsSys" txt="patternsSys" class="cat-h"><a href="#patternsSys">Pattern Of System Commonly Used</a></h2>
		</div>
		<h1 cat="problems" txt="problems" class="cat-h"><a href="#problems">Common Problems And Solutions</a></h1>
	</div>
	<div id="txt" class="right oAuto info">
	</div>
	<div id="hiddenTxt" style="display:none;">
		<div id="sys">
			<a name="sys"></a>
			<h1>System Introduction</h1>
			<a name="sysInfo"></a>
			<div id="sysInfo">
				<h2>Brief Introduction of the System</h2>
				<p>Java Code Performance Analysis (JCPA) is a tool to scan  java source code and provide performance recommendations.</p>
				<p>This system includes the following pages:</p>
				<p>1. Code Analysis page,mainly used to analyze codes to generate reports</p>
				<p>2.Performance Benchmark page,mainly used to display quantitative differences of different usages</p>
				<p>3.Pattern List	page	,mainly used to display,modify,add patterns and management,generate,download,upload ruleSetFiles </p>
			</div>
			<div id="sysDetail">
				<h2>System Detailed Explanation</h2>
				<div id="sysDetailAnalysis">
					<h3>Code Analysis</h3>
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
					<p>There is an error list below the report list. If the list is not empty which shows that there are files that can’t be resolved in the analyzing codes, please check whether the file contains syntax errors.</p>
					<img src="image/CodeAnalysis4.png" />
				</div>
				<div id="sysDetailBenchmark">
					<h3>Performance Benchmark</h3>
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
					<h3>Pattern List</h3>
					<p>page address:<a href="patterns.jsp" target="_blank">/patterns.jsp</a></p>
					<img src="image/patterns0.png" />
					<p>Only login users can access this page. The guest users don’t have permission to access this page, who can check the introduction of the user login and access control to log in.</p>
					<p>(1) add pattern</p>
					<p>Click the Add button on the top left of the list.</p>
					<img src="image/patterns1.png" />
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
					<h3>User and Access Control</h3>
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
		</div>
		<div id="patterns">
			<h1>A Detailed Pattern Introduction</h1>
			<p>The code analysis of the system is based on the PMD, so the pattern format must be in accordance with the format of PMD, which is described by using the way of XPATH.</p>
			<div id="patternsPMD">
				<h2>PMD</h2>
				<div id="patternsPMDBrief">
					<h3>Brief Introduction</h3>
					<p>PMD scans Java source code and looks for potential problems like:</p>
					<p>1.Possible bugs - empty try/catch/finally/switch statements</p>
					<p>2.Dead code - unused local variables, parameters and private methods</p>
					<p>3.Suboptimal code - wasteful String/StringBuffer usage</p>
					<p>4.Overcomplicated expressions - unnecessary if statements, for loops that could be while loops</p>
					<p>5.Duplicate code - copied/pasted code means copied/pasted bugs</p>
				</div>
				<div id="patternsPMDPlugin">
					<h3>Eclipse Plugin</h3>
				</div>
			</div>
			<div id="patternsXPATH">
				<h2>XPATH</h2>
				<p>XPath is a language for searching information through the elements and attributes for navigation in XML documents.</p>
			</div>
			<div id="patternsSys">
				<h2>Pattern Of System Commonly Used</h2>
				<div class="patterns">
					<div class="pattern">
					<pre>1、Name: ExcessiveDetectionWithLengthOrSize
Category: Legacy Usage
Scope: inner-class
Priority: 3
Warning: It's unnecessary to judge variable's length or size before it was used in For Statement.
Example:
(1)public class ClassA{
  	void methodA() {
  		ClassA[] array = {};
  		if (array.length &gt; 0) {
  			for (ClassA obj: array) {
  				//doSometing
  			}
  		}
  	}
  }
 
 
(2)public class ClassA{
   void methodA() {
		if (isNeedDeleteRememberMeCookie &amp;&amp; newCookies.size() &gt; 0) {
 			for (Cookie cookie : newCookies) {
 				response.addCookie(cookie);
 			}
 		}
  }
 }</pre>
					</div>
					<div class="pattern">
					<pre>2、Name: ExcessiveIfStatementWithNull
Category: Redundant Operations
Scope: inner-class
Priority: 3
Warning: It's unnecessary to judge whether the variable was set null value after it was allocated.
Example:
public class ClassA{
　　void methodA() {
	　　ClassA inst= new ClassA();
	　　if (inst == null) {
	　　//doSomething
	　　}
　　}
}</pre>
					</div>
					<div class="pattern">
					<pre>3、Name: ExplicitlyReclaimLocalVariable
Category: Memory Leak
Scope: inner-class
Priority: 4
Warning: It's unnecessary to set the local variable null value to reclaim the memory.
Example:
public class ClassA{
	void methodA() {
		ClassA inst = new ClassA();
		// method body
		inst = null;
	}
}</pre>
					</div>
					<div class="pattern">
					<pre>4、Name: RepetiveMethodInvocationAsArgument
Category: Duplicate Invocations
Scope: inner-class
Priority: 2
Warning: Repetive method invocations are arguments of other methods.
Example:
public class ClassA{
  	void methodA() {
  		ClassA inst = new ClassA();
  		one(inst.sum());
  		two(inst.sum());
  		three(inst.sum());
  	}
}</pre>
					</div>
					<div class="pattern">
					<pre>5、Name: RepetiveMethodInvocationInDifferentMethod
Category: Duplicate Invocations
Scope: inner-class
Priority: 1
Warning: Repetive method invocations are used in different method.
Example:
public class ClassA{
  	void methodA() {
     		HandlerFactory hf = HandlerFactory.getInstance().getHandler("A");
  	}
  	void methodB() {
  		HandlerFactory hf = HandlerFactory.getInstance().getHandler("A");
  	}
}</pre>
					</div>
					<div class="pattern">
					<pre>6、Name: RepetiveBitOperation
Category: Redundant Operations
Scope: inner-class
Priority: 3
Warning: Repetive bit operation can be merged.
Example:
public class ClassA{
  	void methodA() {
  		if (!A && !B && !C && !D) {
  			//doSomething
  		}
  	}
}</pre>
					</div>
					<div class="pattern">
					<pre>7、Name: BrokenThreadStateWithMutiThreads
Category: Corrupt Thread State
Scope: inner-class
Priority: 1
Warning: It'll be broken when there are multiple threads.
Example:
public class KeyHolder{
  	private Set&lt;key&gt; keys= new HashSet&lt;key&gt;();
          private static int size = 500;
          public void put(String key) {
               int less = keys.size() - size;
               for (Key key: keys) {
                    if (less-- &lt;= 0) break;
                    keys.remove(key);
                    
               }
               keys.add(key);
          }
  }</pre>
					</div>
					<div class="pattern">
					<pre>8、Name: LegacyUsageBecomeNewForStatement
Category: Legacy Usage
Scope: inner-class
Priority: 2
Warning: Legacy usage can be replaced with new ForStatement.
Example:
(1)public class ClassA{
  	void methodA() {
  		List queue = new ArrayList();
  		int size = queue.size();
  		for (int i=0; i&lt; size; i++) {
  			ClassA inst = (ClassA) queue.get(i);
  		}
  	}
  }
 
 
 (2)class A{
 public JSONObject toJSON() throws JSONException {
 Iterator&lt;string&gt; candidatesIterator = this.getCandidates().keySet().iterator();
     	JSONObject jsonCandidates = new JSONObject();
     	while(candidatesIterator.hasNext()){
     		String key = candidatesIterator.next();
     		jsonCandidates.put(key, this.getCandidates().get(key).toJSON());
     	}
    }
 }</pre>
					</div>
					<div class="pattern">
					<pre>9、Name: NoTargetTypeInArrayConversion
Category: Other
Scope: inner-class
Priority: 1
Warning: Array conversion does not specify target type.
Example:
public class ClassA{
  	public void doSomething() {
  		Integer[] a = (Integer [])c.toArray();
  	}
  }</pre>
					</div>
					<div class="pattern">
					<pre>10、Name: ExcessivedActionInSynchronizedBlock
Category: Excessive Synchronization
Scope: inner-class
Priority: 1
Warning: Some actions are excessived in synchronized block.
Example:
public class ClassA{
  	private Map&lt;key,value&gt; mapper = new HashMap&lt;key, value=""&gt;();
  	public void put(Key k, Value v) {
  		synchronized(mapper) {
  			mapper.put(k,v);
  			processItem(k, v);
                  }
          }
  }</pre>
					</div>
					<div class="pattern">
					<pre>11、Name: ExcessivedRWLock
Category: Excessive Synchronization
Scope: inner-class
Priority: 2
Warning: It's excessived to use ReentrantReadWriteLock for simple action. Use synchronized instead.
Example:
class RWDictionary {
      private final Map&lt;string, data=""&gt; m = new HashMap&lt;string, data=""&gt;();
      private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
      private final Lock r = rwl.readLock();
      private final Lock w = rwl.writeLock();
  
      public Data get(String key) {
          r.lock();
          try { return m.get(key); }
          finally { r.unlock(); }
      }
      public String[] allKeys() {
          r.lock();
          try { return m.keySet().toArray(); }
          finally { r.unlock(); }
      }
      public Data put(String key, Data value) {
          w.lock();
          try { return m.put(key, value); }
          finally { w.unlock(); }
      }
      public void clear() {
          w.lock();
          try { m.clear(); }
          finally { w.unlock(); }
      }
   }</pre>
					</div>
					<div class="pattern">
					<pre>12、Name: SynchronizedSetIsExcessive
Category: 2
Scope: inner-class
Priority: Excessive Synchronization
Warning: SynchronizedSet is excessive, you can use ConcurrentHashMap instead.
Example:
public class ClassA{
	void methodA() {
　　	Set safeSet= Collections.synchronizedSet(new HashSet());
		Set safeSet2=Collections.newSetFromMap(new ConcurrentHashMap&lt;String, Boolean&gt;());
　　}
}</pre>
					</div>
					<div class="pattern">
					<pre>13、Name: LongRoundTripsInDifferentMethod
Category: Frequent IOs
Scope: inner-class
Priority: 1
Warning: There are long round trips in different method.
Example:
public class ClassA{
  	public void methodA() {
  		serviceA.call(url1);
  		serviceA.call(url2);
  		methodB();
  	}
  	public void methodB() {
  		serviceA.call(url3);
  		serviceA.call(url4);
  	}
  }</pre>
					</div>
					<div class="pattern">
					<pre>14、Name: FrequentIOsUseForStatement
Category: Frequent IOs
Scope: inner-class
Priority: 1
Warning: To access database frequently for reading and writing data.
Example:
public class ClassA{
  	public void methodA() {
  		for (String key: keys) {
  			ClassA curRow = DBManager.getRowByID(key);
  			processRow(curRow);
  		}
  	}
  }</pre>
					</div>
					<div class="pattern">
					<pre>15、Name: InfiniteLoop
Category: Other
Scope: inner-class
Priority: 1
Warning: The time complexity of the infinite loop is too high.
Example:
public class ClassA{
  	public void doSomething() {
  		int size = 100;
  		for (int i=0; i &lt; size; i++) {
  			for (int j=0; j &lt; size; j++){
  				//doSomething
  			}
  		}
  	}
  }</pre>
					</div>
					<div class="pattern">
					<pre>16、Name: RepetiveMethodInvocationAsStatement
Category: Duplicate Invocations
Scope: inner-class
Priority: 2
Warning: Repetive method invocations are just statements or Assignment statements.
Example:
public class ClassA{
 	public void put() {
 		if(IdentityConstants.TRAIN_CONTEXT_TYPE.equalsIgnoreCase(assistantContext.getContextType())) {
 			 ServiceSessionManager.getInstance().deleteServiceSession(AuthenticationContext.NAME);
 		 } else {
 			 ServiceSessionManager.getInstance().setServiceSession(assistantContext);
 			 SPIContextManager.getSPIContext().registerServiceSessionParam(AssistantContext.NAME, assistantContext.getAttributes().get(IdentityConstants.TOKEN));
 		 }	
         }
 }</pre>
					</div>
					<div class="pattern">
					<pre>17、Name: NeverUsedMethod
Category: Other
Scope: inner-class
Priority: 1
Warning: The method is never used.
Example:
NeverUsedMethod</pre>
					</div>
				</div>
			</div>
		</div>
		<div id="problems">
			<h1>Common Problems And Solutions</h1>
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
		</div>
	</div>
</div>
<hr class="space">
<%@ include file="footer.jsp" %>
