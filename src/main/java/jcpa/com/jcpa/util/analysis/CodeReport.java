package com.jcpa.util.analysis;

import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;

public class CodeReport {
	private int id=0;
	private String packageName="";
	private String className="";
	private String methodName="";
	private String ruleName="";
	private int rulePriority;
	private int column;
	private int line;
	private String code="";
	private String extInfoUrl="";
	
	public JsonArrayNode toJsonNode(String name){
		JsonArrayNode j = new JsonArrayNode(name);
		j.addItem(new JsonLeafNode("",packageName));
		j.addItem(new JsonLeafNode("",className));
		j.addItem(new JsonLeafNode("",methodName));
		j.addItem(new JsonLeafNode("","Line:["+line+"]column:["+column+"]"));
		j.addItem(new JsonLeafNode("",code));
		j.addItem(new JsonLeafNode("",ruleName));
		j.addItem(new JsonLeafNode("",String.valueOf(rulePriority)));
		j.addItem(new JsonLeafNode("",extInfoUrl));
		return j;
	}
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}
	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	/**
	 * @return the rulePriority
	 */
	public int getRulePriority() {
		return rulePriority;
	}
	/**
	 * @param rulePriority the rulePriority to set
	 */
	public void setRulePriority(int rulePriority) {
		this.rulePriority = rulePriority;
	}
	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}
	/**
	 * @param line the line to set
	 */
	public void setLine(int line) {
		this.line = line;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}


	/**
	 * @return the extInfoUrl
	 */
	public String getExtInfoUrl() {
		return extInfoUrl;
	}


	/**
	 * @param extInfoUrl the extInfoUrl to set
	 */
	public void setExtInfoUrl(String extInfoUrl) {
		this.extInfoUrl = extInfoUrl;
	}
	
}
