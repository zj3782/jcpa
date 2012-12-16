package com.jcpa.util.json;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ArrayNode
 * */
public class JsonArrayNode extends JsonNode{
	private List<JsonNode> items =Collections.synchronizedList(new LinkedList<JsonNode>());
	/**
	 * construct
	 * */
	public JsonArrayNode(String name){
		super(name);
		_NODE_CLASS_TYPE=_NODE_CLASS_TYPE+".array";
	}
	/**
	 * add item
	 * */
	public void addItem(JsonNode item){
		items.add(item);
	}
	/**
	 * toString
	 * */
	public String toString(){
		String str="";
		if(name!=null && !name.equals("")){
			str+="\""+Json.JsonFmt(name)+"\":";
		}
		str+="[";
		Iterator<JsonNode> it=items.iterator();
		if(it.hasNext()){
			str+=it.next().toString();
		}
		while(it.hasNext()){
			str+=","+it.next().toString();
		}
		str+="]";
		return str;
	}
}