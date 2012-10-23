package com.jcpa.beans;

import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;
import com.jcpa.util.json.JsonObjectNode;

public class Pattern {
	protected int id;
	protected String name;
	protected String expression;
	protected String warning;
	protected String category;
	protected String example;
	protected String scope;
	protected int priority;
	
	public int getId() {
		return id;
	}
	public void setId(int id) throws Exception {
		if(id<=0){
			throw new Exception("Arg Error[id]");
		}
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) throws Exception{
		if(name==null || name.equals("")){
			throw new Exception("Arg Error[name]");
		}
		this.name = name;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) throws Exception{
		if(expression==null || expression.equals("")){
			throw new Exception("Arg Error[expression]");
		}
		this.expression = expression;
	}
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) throws Exception{
		if(warning==null || warning.equals("")){
			throw new Exception("Arg Error[warning]");
		}
		this.warning = warning;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) throws Exception{
		if(category==null || category.equals("")){
			throw new Exception("Arg Error[category]");
		}
		this.category = category;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) throws Exception{
		this.example = example;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) throws Exception{
		if(scope==null || scope.equals("")){
			throw new Exception("Arg Error[scope]");
		}
		this.scope = scope;
	}	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) throws Exception{
		if(priority<0){
			throw new Exception("Arg Error[priority]");
		}
		this.priority = priority;
	}
	/**
	 * 
	 * */
	public JsonObjectNode getTBJsonNode(){
		JsonObjectNode j=new JsonObjectNode("");
		j.addChild(new JsonLeafNode("id",String.valueOf(id)));
		JsonArrayNode cell=new JsonArrayNode("cell");
		cell.addItem(new JsonLeafNode("",String.valueOf(id)));
		cell.addItem(new JsonLeafNode("",name));
		cell.addItem(new JsonLeafNode("",expression));
		cell.addItem(new JsonLeafNode("",warning));
		cell.addItem(new JsonLeafNode("",category));
		cell.addItem(new JsonLeafNode("",scope));
		cell.addItem(new JsonLeafNode("",example));
		cell.addItem(new JsonLeafNode("",String.valueOf(priority)));
		j.addChild(cell);
		return j;
	}
	/**
	 * 
	 * */
	public JsonObjectNode getObjectNode(String NodeName){
		JsonObjectNode j=new JsonObjectNode(NodeName);
		j.addChild(new JsonLeafNode("name",name));
		j.addChild(new JsonLeafNode("expression",expression));
		j.addChild(new JsonLeafNode("warning",warning));
		j.addChild(new JsonLeafNode("category",category));
		j.addChild(new JsonLeafNode("scope",scope));
		j.addChild(new JsonLeafNode("example",example));
		return j;
	}
}
