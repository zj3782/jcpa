package com.jcpa.util;

import java.util.HashMap;
import java.util.Map;

public class AuxUtil {
	/**
	 * Integrate the expression and the auxiliary to get completed expression
	 * */
	public static String ExpIntegrate(String exp,String auxStr){
		if(exp==null || exp.equals(""))return "";
		if(auxStr==null || auxStr.equals(""))return exp;
		String auxStrReplaceAll=auxStr;
		if(auxStr.startsWith("##"))auxStr=auxStr.substring(2);
		
		String itemEqStr="",auxsEqStr="",itemEndStr="",auxsEndStr="",itemConStr="",auxsConStr="",itemRegStr="",auxsRegStr="";
		String auxs[]=auxStr.split("##");
		for(int j=0;j<auxs.length;j++){
			String items[]=auxs[j].split(",");
			itemEqStr="";itemEndStr="";itemConStr="";itemRegStr="";
			for(String item:items){
				item=item.trim();
				itemEqStr+="@Image=\""+item+"\" or ";
				itemConStr+="contains(@Image,\""+item+"\") or ";
				itemEndStr+="ends-with(@Image,\""+item+"\") or ";
				itemRegStr+="'"+item+"',";
			}

			if(itemEqStr.length()>4)itemEqStr=itemEqStr.substring(0,itemEqStr.length()-4);
			if(itemConStr.length()>4)itemConStr=itemConStr.substring(0,itemConStr.length()-4);
			if(itemEndStr.length()>4)itemEndStr=itemEndStr.substring(0,itemEndStr.length()-4);
			if(itemRegStr.length()>1)itemRegStr=itemRegStr.substring(0,itemRegStr.length()-1);
			exp=exp.replaceAll("##AUX_EQ_"+j+"##",EscapeRegSpecial(itemEqStr));
			exp=exp.replaceAll("##AUX_END_"+j+"##",EscapeRegSpecial(itemEndStr));
			exp=exp.replaceAll("##AUX_CON_"+j+"##",EscapeRegSpecial(itemConStr));
			exp=exp.replaceAll("##AUX_REP_"+j+"##",EscapeRegSpecial(auxs[j]));//replace
			exp=exp.replaceAll("##AUX_REG_"+j+"##",EscapeRegSpecial("pmd:matches(@Image,"+itemRegStr+")"));
			auxsEqStr+=itemEqStr+" or ";
			auxsEndStr+=itemEndStr+" or ";
			auxsConStr+=itemConStr+" or ";
			auxsRegStr+=itemRegStr+",";
		}
		if(auxsEqStr.length()>4)auxsEqStr=auxsEqStr.substring(0,auxsEqStr.length()-4);
		if(auxsEndStr.length()>4)auxsEndStr=auxsEndStr.substring(0,auxsEndStr.length()-4);
		if(auxsConStr.length()>4)auxsConStr=auxsConStr.substring(0,auxsConStr.length()-4);
		if(auxsRegStr.length()>1)auxsRegStr=auxsRegStr.substring(0,auxsRegStr.length()-1);
		exp=exp.replaceAll("##AUX_EQ##",EscapeRegSpecial(auxsEqStr));
		exp=exp.replaceAll("##AUX_END##",EscapeRegSpecial(auxsEndStr));
		exp=exp.replaceAll("##AUX_CON##",EscapeRegSpecial(auxsConStr));
		exp=exp.replaceAll("##AUX_REP##",EscapeRegSpecial(auxStrReplaceAll));
		exp=exp.replaceAll("##AUX_REG##",EscapeRegSpecial("pmd:matches(@Image,"+auxsRegStr+")"));
		return exp;
	} 
	/**
	 * replace special char in regular expression
	 * */
	public static String EscapeRegSpecial(String in){
		if(in==null || in.equals(""))return in;
		StringBuffer quotedString = new StringBuffer();
	    for (int i=0;  i<in.length();  ++i) {
	      String translated = SpecialToEscape.get(in.charAt(i));
	      quotedString.append( translated!=null ? translated : in.charAt(i) );
	    }
	    return quotedString.toString();
	}
	private static Map<Character,String> SpecialToEscape = new HashMap<Character,String>();
    static {
    	SpecialToEscape.put('$',"\\$");
    }
}
