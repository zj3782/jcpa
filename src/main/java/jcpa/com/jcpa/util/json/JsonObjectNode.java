package com.jcpa.util.json;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Object节点类
 * */
public class JsonObjectNode extends JsonNode{
	List<JsonNode> childs=new LinkedList<JsonNode>();
	/**
	 * 构造方法
	 * */
	public JsonObjectNode(String name){
		super(name);
		_NODE_CLASS_TYPE=_NODE_CLASS_TYPE+".object";
	}
	/**
	 * 添加子节点（array,object,leaf）
	 * */
	public void addChild(JsonNode child){
		childs.add(child);
	}
	/**
	 * toString
	 * */
	public String toString(){
		String str="";
		if(name!=null && !name.equals("")){
			str+="\""+Json.JsonFmt(name)+"\":";
		}
		str+="{";
		Iterator<JsonNode> it=childs.iterator();
		if(it.hasNext()){
			str+=it.next().toString();
		}
		while(it.hasNext()){
			str+=","+it.next().toString();
		}
		str+="}";
		return str;
	}
}
