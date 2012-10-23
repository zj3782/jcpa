package com.jcpa.util.analysis;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;
import com.jcpa.util.json.JsonObjectNode;

/**
 * 分析产生的Report（放在session中）
 * */
public class CodeReports {
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
			reports.add(report);
		}
	}
	/**
	 * 添加error
	 * */
	public void addError(CodeReportError error){
		synchronized(errors){
			errors.add(error);
		}
	}
	/**
	 * 转化为json字符串，从rstart、estart处开始
	 * */
	public JsonObjectNode toJsonNode(int rstart,int estart){
		JsonObjectNode r=new JsonObjectNode("report");
		
		synchronized(reports){
			//report
			int rend=0;
			JsonArrayNode arrReport=new JsonArrayNode("report");
			Iterator<CodeReport> itr=reports.iterator();
			for(int i=0;i<rstart;i++){
				if(!itr.hasNext()){
					rstart=i;
					break;
				}else{
					itr.next();
					rend++;
				}
			}
			while(itr.hasNext()){
				rend++;
				arrReport.addItem(itr.next().toJsonNode());
			}
			r.addChild(new JsonLeafNode("rstart",String.valueOf(rstart)));	
			r.addChild(arrReport);
			r.addChild(new JsonLeafNode("rend",String.valueOf(rend)));
		}
		synchronized (errors) {
			//error
			int eend=0;
			JsonArrayNode arrError=new JsonArrayNode("error");
			Iterator<CodeReportError> ite=errors.iterator();
			for(int i=0;i<estart;i++){
				if(!ite.hasNext()){
					estart=i;
					break;
				}else{
					ite.next();
					eend++;
				}
			}
			while(ite.hasNext()){
				eend++;
				arrError.addItem(ite.next().toJsonNode());
			}
			r.addChild(new JsonLeafNode("estart",String.valueOf(estart)));
			r.addChild(arrError);
			r.addChild(new JsonLeafNode("eend",String.valueOf(eend)));
		}
		//step
		r.addChild(new JsonLeafNode("step",String.valueOf(step)));
		return r;
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
	/**
	 * 返回reports的迭代对象
	 * */
	public Iterator<CodeReport> reportIterator(){
		return reports.iterator();
	}/**
	 * 返回report errors的迭代对象
	 * */
	public Iterator<CodeReportError> reportErrorIterator(){
		return errors.iterator();
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