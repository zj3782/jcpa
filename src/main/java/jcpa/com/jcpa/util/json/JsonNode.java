package com.jcpa.util.json;

/**
 * json节点基类（array，object，leaf）
 * */
public class JsonNode{
	protected String _NODE_CLASS_TYPE="base";
	protected String name="";/**节点名称*/

	/**
	 * 构造方法
	 * */
	JsonNode(String name){
		this.name=name;
		_NODE_CLASS_TYPE="json";
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
