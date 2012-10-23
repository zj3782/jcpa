package com.jcpa.util.json;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Array节点类
 * */
public class JsonArrayNode extends JsonNode{
	private List<JsonNode> items = new LinkedList<JsonNode>();
	/**
	 * 构造方法
	 * */
	public JsonArrayNode(String name){
		super(name);
		_NODE_CLASS_TYPE=_NODE_CLASS_TYPE+".array";
	}
	/**
	 * 添加节点元素
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