package com.jcpa.util.analysis;

import java.util.LinkedList;
import java.util.List;

import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;
import com.jcpa.util.json.JsonObjectNode;

/**
 * 分析产生的Report（放在session中）
 * */
public class CodeReports {
	private int reportCount=0;
	private List<CodeReport> reports = new LinkedList<CodeReport>();
	private List<CodeReportError> errors = new LinkedList<CodeReportError>();
	
	/**分析进行到的步骤*/
	private int step;
	public static final int STEP_START=0;//开始
	public static final int STEP_LOGINING=1;//svn登录中
	public static final int STEP_LOGINOK=2;//svn登录成功
	public static final int STEP_CHECKOUTING=3;//svn检出中
	public static final int STEP_CHECKOUTOK=4;//svn检出成功
	public static final int STEP_PMDING=5;//pmd处理过程中
	public static final int STEP_PMDOK=6;//pmd处理成功
	public static final int STEP_SUCCESSEND=7;//成功结束
	public static final int STEP_FAILEND=8;//失败结束
	
	
	/**
	 * 构造方法
	 * */
	public CodeReports(){}
	/**
	 * 添加report
	 * */
	public void add(CodeReport report){
		synchronized(reports){
			report.setId(reportCount);
			reportCount++;
			CodeReport r=null;
			int i=0,size=reports.size();
			for(i=0;i<size;i++){
				r=reports.get(i);
				if(r.getRulePriority() > report.getRulePriority()){
					reports.add(i,report);
					break;
				}else if(r.getRulePriority() == report.getRulePriority()){
					if(r.isEquals(report)){//相同的错误（同种错误、同行同列）
						break;
					}
				}
			}
			if(i==size){
				reports.add(report);
			}
		}
	}
	/**
	 * 删除report
	 * */
	public boolean remove(int id){
		boolean ret=false;
		synchronized(reports){
			CodeReport r=null;
			int i=0,size=reports.size();
			for(i=0;i<size;i++){
				r=reports.get(i);
				if(r.getId()==id){
					reports.remove(i);
					reportCount--;
					ret=true;
					break;
				}
			}
		}
		return ret;
	}
	/**
	 * 添加error
	 * */
	public void addError(CodeReportError error){
		synchronized(errors){
			errors.add(error);
		}
	}
	
	public JsonArrayNode getReportJsonArray(int page,int onePageCount,String name){
		JsonArrayNode arr = new JsonArrayNode(name);
		int start=onePageCount*(page-1),size=reports.size();
		CodeReport r;
		for(int i=0;i<onePageCount;i++){
			if(start+i>=size)break;
			r=reports.get(start+i);
			JsonObjectNode item=new JsonObjectNode("");
			item.addChild(new JsonLeafNode("id", String.valueOf(r.getId())));
			item.addChild(r.toJsonNode("cell"));
			arr.addItem(item);
		}
		return arr;
	}
	
	public JsonArrayNode getErrorJsonArray(int page,int onePageCount,String name){
		JsonArrayNode arr = new JsonArrayNode(name);
		int start=onePageCount*(page-1),size=errors.size();
		CodeReportError r;
		for(int i=0;i<onePageCount;i++){
			if(start+i>=size)break;
			r=errors.get(start+i);
			JsonObjectNode item=new JsonObjectNode("");
			item.addChild(new JsonLeafNode("id", "0"));
			item.addChild(r.toJsonNode("cell"));
			arr.addItem(item);
		}
		return arr;
	}
	/**
	 * 返回report个数
	 * */
	public int reportCount(){
		return reports.size();
	}
	/**
	 * 返回error个数
	 * */
	public int errorCount(){
		return errors.size();
	}
	
	public List<CodeReport> getReports() {
		return reports;
	}
	public List<CodeReportError> getErrors() {
		return errors;
	}
	/**
	 * 取得step
	 * */
	public int getStep() {
		return step;
	}
	/**
	 * 设置step
	 * */
	public void setStep(int step) {
		this.step = step;
	}
}