package com.jcpa.util.json;

import com.jcpa.util.ToolUtil;

/**
 * Leaf节点类
 * */
public class JsonLeafNode extends JsonNode{
	private String text;
	//是否自动判断text为整数，如果为false，text始终当作String进行处理
	public boolean bAutoJudgeNum=true;
	
	/**
	 * 构造方法
	 * */
	public JsonLeafNode(String name,String text){
		super(name);
		this.text=text;
		_NODE_CLASS_TYPE=_NODE_CLASS_TYPE+".leaf";
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * toString
	 * */
	public String toString(){
		String str="";
		if(name!=null && !name.equals("")){
			str+="\""+Json.JsonFmt(name)+"\":";
		}
		if(bAutoJudgeNum && ToolUtil.isNum(text)){
			str+=text;
		}else{
			str+="\""+Json.JsonFmt(text)+"\"";
		}
		return str;
	}
}
