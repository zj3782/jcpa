package com.jcpa.beans;

import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;
import com.jcpa.util.json.JsonObjectNode;

public class Benchmark {
	private int id;
	private String name;
	private String descript;
	private String result;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	public JsonObjectNode getTBJsonNode(){
		JsonObjectNode j=new JsonObjectNode("");
		j.addChild(new JsonLeafNode("id",String.valueOf(id)));
		JsonArrayNode cell=new JsonArrayNode("cell");
		cell.addItem(new JsonLeafNode("",String.valueOf(id)));
		cell.addItem(new JsonLeafNode("",name));
		cell.addItem(new JsonLeafNode("",descript));
		cell.addItem(new JsonLeafNode("",result));
		j.addChild(cell);
		return j;
	}
}
