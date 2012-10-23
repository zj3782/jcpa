package com.jcpa.util.analysis;

import com.jcpa.util.json.JsonArrayNode;
import com.jcpa.util.json.JsonLeafNode;

public class CodeReportError {
	private String file;
	private String msg;
	
	public JsonArrayNode toJsonNode(){
		JsonArrayNode j = new JsonArrayNode("");
		j.addItem(new JsonLeafNode("",file));
		j.addItem(new JsonLeafNode("",msg));
		return j;
	}
	
	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}
	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
