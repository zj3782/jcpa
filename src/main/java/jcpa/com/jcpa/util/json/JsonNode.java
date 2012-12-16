package com.jcpa.util.json;

/**
 * json node base class（array，object，leaf）
 * */
public class JsonNode{
	protected String _NODE_CLASS_TYPE="base";
	protected String name="";/**node name*/

	/**
	 * construct
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
