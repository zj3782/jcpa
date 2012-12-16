package com.jcpa.util.json;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ObjectNode
 * */
public class JsonObjectNode extends JsonNode{
	List<JsonNode> childs=Collections.synchronizedList(new LinkedList<JsonNode>());
	/**
	 * construct
	 * */
	public JsonObjectNode(String name){
		super(name);
		_NODE_CLASS_TYPE=_NODE_CLASS_TYPE+".object";
	}
	/**
	 * add child（array,object,leaf）
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
