package com.jcpa.action;


import java.io.File;
import java.util.Iterator;

import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import com.jcpa.util.PMDUtil;
import com.jcpa.util.SVNUtil;
import com.jcpa.util.ToolUtil;
import com.jcpa.util.analysis.CodeReport;
import com.jcpa.util.analysis.CodeReportError;
import com.jcpa.util.analysis.CodeReports;
import com.jcpa.util.analysis.PMDRenderer;
import com.jcpa.util.json.Json;
import com.jcpa.util.json.JsonObjectNode;

////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 * 代码分析action
 * */
public class AnalysisAction extends Action{
	private String SourcePath="";//源代码存放目录
	/**
	 * 执行动作之前的准备工作
	 */
	protected void _prepare() throws Exception{
		
	}
	/**
	 * 执行完动作之后的清理工作
	 */
	protected void _cleanup() throws Exception{
		
	}
	
	/**
	 * 
	 * */
	public void work() throws Exception{		
		CodeReports report=(CodeReports)session.getAttribute("AnalysisReport");
		if(report!=null)session.removeAttribute("AnalysisReport");
		report=new CodeReports();
		session.setAttribute("AnalysisReport",report);
		report.setStep(CodeReports.STEP_START);//开始
		
		String url=request.getParameter("url");
		String user=request.getParameter("user");
		String pwd=request.getParameter("pwd");
		session.setAttribute("SvnUser",user);
		session.setAttribute("SvnUrl",url);
		//判断是否是svn
		boolean isSvn=false;
		if(url.startsWith("http://") || url.startsWith("https://") ||
			url.startsWith("svn") || url.startsWith("file://")){
			isSvn=true;
		}
		try {
			if(isSvn){
				SourcePath=(String)application.getAttribute("JcpaSource")+session.getId();
				//svn登录
				report.setStep(CodeReports.STEP_LOGINING);
				SVNUpdateClient client=SVNUtil.getClient(user,pwd);
				report.setStep(CodeReports.STEP_LOGINOK);
				//svn获取源码
				report.setStep(CodeReports.STEP_CHECKOUTING);
				SVNUtil.checkout(client,url,SourcePath);
				report.setStep(CodeReports.STEP_CHECKOUTOK);
			}else{
				SourcePath=url;
			}
			//pmd分析
			String RuleSets=(String)application.getAttribute("Ruleset")+request.getParameter("rule");
			report.setStep(CodeReports.STEP_PMDING);
			PMDRenderer renderer=new PMDRenderer(report);
			renderer.setRootPath(SourcePath);
			PMDUtil.report(SourcePath, RuleSets, renderer);
			report.setStep(CodeReports.STEP_PMDOK);
			report.setStep(CodeReports.STEP_SUCCESSEND);
			success("success");
		} catch (Exception e) {
			report.setStep(CodeReports.STEP_FAILEND);
			error("Code Analysis Error:"+e.getMessage());
		}finally{
			if(isSvn){
				ToolUtil.deleteDir(new File(SourcePath));//删除源代码目录
			}
		}
	}
	/**
	 * 客户端每隔一段时间获取分析进程
	 * */
	public void report() throws Exception{
		CodeReports report=(CodeReports)session.getAttribute("AnalysisReport");
		if(report==null){
			error("Session is empty");
		}else{
			int rstart=ToolUtil.strToInt(request.getParameter("rstart"));//report start
			int estart=ToolUtil.strToInt(request.getParameter("estart"));//error start
			Json j=new Json(1);
			JsonObjectNode data=j.createData();
			data.addChild(report.toJsonNode(rstart,estart));
			echo(j.toString());
		}
	}
	/**
	 * 清除session中的report
	 * */
	public void ClearReport() throws Exception{
		_ClearReport();
	}
	private void _ClearReport(){
		session.removeAttribute("AnalysisProgress");
		session.removeAttribute("AnalysisReport");
		session.removeAttribute("SvnUser");
		session.removeAttribute("SvnUrl");
	}
	/**
	 * 下载report
	 */
	public void DownReport() throws Exception{
		CodeReports report=(CodeReports)session.getAttribute("AnalysisReport");
		if(report!=null){
			String txt="<html><head><title>Code Analysis Report</title></head><body><div>";
			txt+="<h3>SourceUrl:&nbsp;&nbsp;&nbsp;<span style='color:green'>"+(String)session.getAttribute("SvnUrl")+"</span></h3>";
			txt+="<h3>UserName:&nbsp;&nbsp;&nbsp;<span style='color:green'>"+(String)session.getAttribute("SvnUser")+"</span></h3>";
			txt+="<table border='1' align='center' cellspacing='0' cellpadding='3' width='100%'>";
			txt+="<tr><th></th><th>package</th><th>class</th><th>method</th><th>rule</th><th>description</th><th>example</th><th>priority</th></tr>";
			
			int index=1;
			Iterator<CodeReport> itr = report.reportIterator();
			while(itr.hasNext()){
				CodeReport r = itr.next();
				txt+="<tr><td>"+index+"</td><td>"+r.getPackageName()+"</td><td>"+r.getClassName()+"</td>";
				txt+="<td>"+r.getMethodName()+"</td><td>"+r.getRuleName()+"</td>";
				txt+="<td>"+ToolUtil.strHtmlFmt(r.getDescription())+"</td>";
				txt+="<td>"+ToolUtil.strHtmlFmt(r.getExample())+"</td>";
				txt+="<td>"+r.getRulePriority()+"</td></tr>";
				index++;
			}
			txt+="</table>";
	
			Iterator<CodeReportError> ite = report.reportErrorIterator();
			if(ite.hasNext()){
				txt+="<table border='1' align='center' cellspacing='0' cellpadding='3' width='100%'>";
				txt+="<tr><th>File</th><th>ErrorMsg</th></tr>";
				while(ite.hasNext()){
					CodeReportError e = ite.next();
					txt+="<tr><td>"+e.getFile()+"</td><td>"+e.getMsg()+"</td></tr>";
				}
				txt+="</table>";
			}
			txt+="</div></body></html>";
			// 设置HTTP头：
			response.reset();
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition","attachment;"+ "filename=\"report.html\"");
			//写入内容
			out.write(txt);
			out.flush();
		}
	}
}
