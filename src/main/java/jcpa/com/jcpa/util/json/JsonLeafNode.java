package com.jcpa.util.json;

import com.jcpa.util.ToolUtil;

/**
 * Leaf Node
 * */
public class JsonLeafNode extends JsonNode{
	private String text;
	//if auto judge if text is a num,if not,text will be treated as string
	public boolean bAutoJudgeNum=true;
	
	/**
	 * construct
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
