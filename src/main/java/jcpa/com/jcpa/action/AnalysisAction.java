package com.jcpa.action;


import java.io.File;
import java.util.List;

import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import com.jcpa.util.PMDUtil;
import com.jcpa.util.SVNUtil;
import com.jcpa.util.ToolUtil;
import com.jcpa.util.analysis.CodeReport;
import com.jcpa.util.analysis.CodeReportError;
import com.jcpa.util.analysis.CodeReports;
import com.jcpa.util.analysis.PMDRenderer;
import com.jcpa.util.json.Json;
import com.jcpa.util.json.JsonLeafNode;
import com.jcpa.util.json.JsonObjectNode;

////////////////////////////////////////////////////////////////////////////////////////////////////////

public class AnalysisAction extends Action{
	private String SourcePath="";//source code path
	protected void _prepare() throws Exception{
		
	}
	protected void _cleanup() throws Exception{
		
	}
	
	/**
	 * scan code and gernate report
	 * */
	public void work() throws Exception{		
		CodeReports report=(CodeReports)session.getAttribute("AnalysisReport");
		if(report!=null)session.removeAttribute("AnalysisReport");
		report=new CodeReports();
		session.setAttribute("AnalysisReport",report);
		report.setStep(CodeReports.STEP_START);
		
		String url=request.getParameter("url");
		String user=request.getParameter("user");
		String pwd=request.getParameter("pwd");
		if(url!=null && !url.equals(""))url=url.replaceAll("\\\\","/");
		session.setAttribute("codeUser",user);
		session.setAttribute("codeUrl",url);
		//judge is svn
		boolean isSvn=false;
		if(url.startsWith("http://") || url.startsWith("https://") ||
			url.startsWith("svn") || url.startsWith("file://")){
			isSvn=true;
		}
		
		System.out.println("["+ToolUtil.getTimeString()+"]Strat to analysis code.Url:"+url);
		
		try {
			if(isSvn){
				SourcePath=(String)application.getAttribute("JcpaSource")+session.getId();
				//svn login
				System.out.println("["+ToolUtil.getTimeString()+"]svn logining...");
				report.setStep(CodeReports.STEP_LOGINING);
				SVNUpdateClient client=SVNUtil.getClient(user,pwd);
				report.setStep(CodeReports.STEP_LOGINOK);
				//svn check out
				System.out.println("["+ToolUtil.getTimeString()+"]svn checking out...");
				report.setStep(CodeReports.STEP_CHECKOUTING);
				SVNUtil.checkout(client,url,SourcePath);
				report.setStep(CodeReports.STEP_CHECKOUTOK);
			}else{
				SourcePath=url;
			}
			//pmd analysis
			System.out.println("["+ToolUtil.getTimeString()+"]pmd analysing...");
			String RuleSets=(String)application.getAttribute("Ruleset")+request.getParameter("rule");
			report.setStep(CodeReports.STEP_PMDING);
			PMDRenderer renderer=new PMDRenderer(report);
			renderer.setRootPath(SourcePath);
			PMDUtil.report(SourcePath, RuleSets, renderer);
			report.setStep(CodeReports.STEP_PMDOK);
			
			report.setStep(CodeReports.STEP_SUCCESSEND);
			System.out.println("["+ToolUtil.getTimeString()+"]analyse over.");
			success("success");
		} catch (Exception e) {
			report.setStep(CodeReports.STEP_FAILEND);
			error("Code Analysis Error:"+e.getMessage());
			System.out.println("["+ToolUtil.getTimeString()+"]anayse exception");
		}finally{
			if(isSvn){
				ToolUtil.deleteDir(new File(SourcePath));//delete svn check out dir
			}
		}
	}
	/**
	 * current analysis step
	 * */
	public void step() throws Exception{
		CodeReports report=(CodeReports)session.getAttribute("AnalysisReport");
		if(report==null){
			error("Session is empty");
		}else{
			Json j=new Json(1);
			JsonObjectNode data=j.createData();
			data.addChild(new JsonLeafNode("step",String.valueOf(report.getStep())));
			echo(j.toString());
		}
	}
	/**
	 * reportlist
	 * */
	public void reportlist() throws Exception{
		CodeReports report=(CodeReports)session.getAttribute("AnalysisReport");
		if(report==null){
			error("Session is empty");
		}else{
			int ONE_PAGE_COUNT=ToolUtil.strToPositiveInt(request.getParameter("rp"),12);;//one page count
			int page=ToolUtil.strToPositiveInt(request.getParameter("page"),1);//sum pages
			
			JsonObjectNode j=new JsonObjectNode("");
			j.addChild(new JsonLeafNode("page",String.valueOf(page)));
			j.addChild(new JsonLeafNode("total",String.valueOf(report.reportCount())));
			j.addChild(report.getReportJsonArray(page,ONE_PAGE_COUNT,"rows"));
			echo(j.toString());
		}
	}
	/**
	 * errorlist
	 * */
	public void errorlist() throws Exception{
		CodeReports report=(CodeReports)session.getAttribute("AnalysisReport");
		if(report==null){
			error("Session is empty");
		}else{
			int ONE_PAGE_COUNT=ToolUtil.strToPositiveInt(request.getParameter("rp"),12);;
			int page=ToolUtil.strToPositiveInt(request.getParameter("page"),1);
			
			JsonObjectNode j=new JsonObjectNode("");
			j.addChild(new JsonLeafNode("page",String.valueOf(page)));
			j.addChild(new JsonLeafNode("total",String.valueOf(report.errorCount())));
			j.addChild(report.getErrorJsonArray(page,ONE_PAGE_COUNT,"rows"));
			echo(j.toString());
		}
	}
	/**
	 * delete(ingroe) report
	 * */
	public void RemoveReport() throws Exception{
		String SIds=request.getParameter("ids");
		CodeReports report=(CodeReports)session.getAttribute("AnalysisReport");
		if(report==null){
			error("Session is empty");
		}else if(SIds==null || SIds.equals("")){
			error("args is empty");
		}else{
			int delCount=0;
			String[] SArrIds=SIds.split(",");
			for(String SId:SArrIds){
				int id=ToolUtil.strToInt(SId,-1);
				if(id>=0 && report.remove(id)){
					delCount++;
				}
			}
			success(delCount+" reports delete success");
		}
	}
	/**
	 * clear report in session
	 * */
	public void ClearReport() throws Exception{
		_ClearReport();
	}
	private void _ClearReport(){
		session.removeAttribute("AnalysisProgress");
		session.removeAttribute("AnalysisReport");
		session.removeAttribute("codeUser");
		session.removeAttribute("codeUrl");
	}
	/**
	 * download report
	 */
	public void DownReport() throws Exception{
		CodeReports report=(CodeReports)session.getAttribute("AnalysisReport");
		if(report!=null){
			String txt="<html><head><title>Code Analysis Report</title>";
			txt+="<script type=\"text/javascript\">";
				txt+="window.onload=function(){";
					txt+="var tb=document.getElementById('reportTB');";
					txt+="for(var i=0;i<tb.rows.length;i++){";
						txt+="var td=tb.rows[i].cells[5];";
						txt+="try{td.innerHTML=decodeURIComponent(td.innerHTML);}catch(e){}";
					txt+="}";
				txt+="}";
			txt+="</script>";
			txt+="</head><body><div>";
			txt+="<h3>SourceUrl:&nbsp;&nbsp;&nbsp;<span style='color:green'>"+(String)session.getAttribute("codeUrl")+"</span></h3>";
			txt+="<h3>UserName:&nbsp;&nbsp;&nbsp;<span style='color:green'>"+(String)session.getAttribute("codeUser")+"</span></h3>";
			txt+="<table border='1' align='center' cellspacing='0' cellpadding='3' id='reportTB'>";
			txt+="<tr><th></th><th>Package</th><th>Class</th><th>Method</th><th>Location</th><th>Code</th><th>Rule</th><th>Priority</th></tr>";
			
			int index=1;
			List<CodeReport> RList = report.getReports();
			for(CodeReport r:RList){
				txt+="<tr><td>"+index+"</td><td>"+r.getPackageName()+"</td><td>"+r.getClassName()+"</td>";
				txt+="<td>"+r.getMethodName()+"</td>";
				txt+="<td>Line:["+r.getLine()+"]Column:["+r.getColumn()+"]</td>";
				txt+="<td>"+ToolUtil.strHtmlFmt(r.getCode()).replaceAll("\r\n","<br>")+"</td>";
				txt+="<td>"+r.getRuleName()+"</td>";
				txt+="<td>"+r.getRulePriority()+"</td></tr>";
				index++;
			}
			txt+="</table>";
	
			List<CodeReportError> EList = report.getErrors();
			txt+="<table border='1' align='center' cellspacing='0' cellpadding='3' id='errorTB'>";
			txt+="<tr><th>File</th><th>ErrorMsg</th></tr>";
			for(CodeReportError e:EList){
				txt+="<tr><td>"+e.getFile()+"</td><td>"+e.getMsg()+"</td></tr>";
			}
			txt+="</table>";
			
			txt+="</div></body></html>";
			// http header
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition","attachment;"+ "filename=\"report.html\"");
			//print to screen
			out.write(txt);
			out.flush();
		}
	}
}
