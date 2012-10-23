package com.jcpa.util.analysis;

import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;

public class CodeReport {
	private String packageName="";
	private String className="";
	private String methodName="";
	private String ruleName="";
	private String description="";
	private String example;
	private int rulePriority;
	
	public JsonArrayNode toJsonNode(){
		JsonArrayNode j = new JsonArrayNode("");
		j.addItem(new JsonLeafNode("",packageName));
		j.addItem(new JsonLeafNode("",className));
		j.addItem(new JsonLeafNode("",methodName));
		j.addItem(new JsonLeafNode("",ruleName));
		j.addItem(new JsonLeafNode("",description));
		j.addItem(new JsonLeafNode("",example));
		j.addItem(new JsonLeafNode("",String.valueOf(rulePriority)));
		return j;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the example
	 */
	public String getExample() {
		return example;
	}
	/**
	 * @param example the example to set
	 */
	public void setExample(String example) {
		this.example = example;
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
	
}
