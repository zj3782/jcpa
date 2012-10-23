package com.jcpa.util.json;

import java.util.HashMap;
import java.util.Map;


/**
 * json类
 * */
public class Json {
	 private int status=1;//操作是否成功
	 private String info="";//提示信息
	 private JsonObjectNode data=null;//附加信息
	 /**
	  * 构造方法
	  * */
	 public Json(){
		 this.status=1;
		 this.info="";
	 }
	 public Json(int status){
		 this.status=status;
		 this.info="";
	 }
	 public Json(int status,String info){
		 this.status=status;
		 this.info=info;
	 }
	
	 /**
	  * 
	  * */
	 public JsonObjectNode createData(){
		 if(data==null){
			 data = new JsonObjectNode("data");
		 }
		 return data;
	 }
	 /**
	  * 生成json字符串
	  * */
	 public String toString(){
		 String str="{";
		 str+="\"status\":"+status;
		 str+=",\"info\":\""+JsonFmt(info)+"\"";
		 if(data!=null){
			 if(data.getName()!="data")data.setName("data");
			 str+=","+data.toString();
		 }
		 str+="}";
		 return str;
	 }

	/**
	 * @return the satus
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param satus the satus to set
	 */
	public void setStatus(int satus) {
		this.status = satus;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the data
	 */
	public JsonObjectNode getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(JsonObjectNode data) {
		this.data = data;
	}
	
	/**
	 * 便捷输出json信息
	 * */
	public static String success(String info){
		Json j=new Json(1,info);
		return j.toString();
	}
	public static String error(String info){
		Json j=new Json(0,info);
		return j.toString();
	}
	/**
	 * 格式化字符串以符合json输出
	 * */
	public static String JsonFmt(String in){
		if(in==null || in.equals(""))return in;
		StringBuffer quotedString = new StringBuffer();
	    for (int i=0;  i<in.length();  ++i) {
	      String translated = JsonCharsToQuote.get(in.charAt(i));
	      quotedString.append( translated!=null ? translated : in.charAt(i) );
	    }
	    return quotedString.toString();
	}
	private static Map<Character,String> JsonCharsToQuote = new HashMap<Character,String>();
    static {
    	JsonCharsToQuote.put('[',"%5b");
    	JsonCharsToQuote.put(']',"%5d");
    	JsonCharsToQuote.put('{',"%7b");
    	JsonCharsToQuote.put('}',"%7d");
    	JsonCharsToQuote.put('"',"%22");
    	JsonCharsToQuote.put('\\',"%5c");
    	JsonCharsToQuote.put('\b',"\\b");
    	JsonCharsToQuote.put('\f',"\\f");
    	JsonCharsToQuote.put('\r',"\\r");
    	JsonCharsToQuote.put('\n',"\\n");
    	JsonCharsToQuote.put('\t',"\\t");
    }
}
