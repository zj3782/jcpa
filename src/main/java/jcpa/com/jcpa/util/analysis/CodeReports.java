package com.jcpa.util.analysis;

import java.util.LinkedList;
import java.util.List;

import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;
import com.jcpa.util.json.JsonObjectNode;


public class CodeReports {
	private int reportCount=0;
	private List<CodeReport> reports = new LinkedList<CodeReport>();
	private List<CodeReportError> errors = new LinkedList<CodeReportError>();
	
	/**analysis step*/
	private int step;
	public static final int STEP_START=0;//start
	public static final int STEP_LOGINING=1;//svn logining
	public static final int STEP_LOGINOK=2;//svn login ok
	public static final int STEP_CHECKOUTING=3;//svn checking out
	public static final int STEP_CHECKOUTOK=4;//svn check out success
	public static final int STEP_PMDING=5;//pmd analysing
	public static final int STEP_PMDOK=6;//pmd analyse ok
	public static final int STEP_SUCCESSEND=7;//analyse ok
	public static final int STEP_FAILEND=8;//analyse fial
	

	
	public CodeReports(){}
	/**
	 * add report
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
					if(r.isEquals(report)){//some report(some rule,some line)
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
	 * delete report
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
	 * add error
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
	 * report count
	 * */
	public int reportCount(){
		return reports.size();
	}
	/**
	 * error count
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

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}
}