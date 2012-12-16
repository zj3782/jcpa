package com.jcpa.util;

public class AuxUtil {
	/**
	 * Integrate the expression and the auxiliary to get completed expression
	 * */
	public static String ExpIntegrate(String exp,String auxStr){
		if(exp==null || exp.equals(""))return "";
		if(auxStr==null || auxStr.equals(""))return exp;
		if(auxStr.startsWith("##"))auxStr=auxStr.substring(2);
		
		String itemEqStr="",auxsEqStr="",itemEndStr="",auxsEndStr="",itemConStr="",auxsConStr="";
		String auxs[]=auxStr.split("##");
		for(int j=0;j<auxs.length;j++){
			String items[]=auxs[j].split(",");
			itemEqStr="";itemEndStr="";itemConStr="";
			if(items.length>0){
				itemEqStr+="@Image=\""+items[0]+"\"";
				itemConStr+="contains(@Image,\""+items[0]+"\")";
				itemEndStr+="ends-with(@Image,\""+items[0]+"\")";
			}
			for(String item:items){
				itemEqStr+=" or @Image=\""+item+"\"";
				itemConStr+=" or contains(@Image,\""+item+"\")";
				itemEndStr+=" or ends-with(@Image,\""+item+"\")";
			}
			exp=exp.replaceAll("##AUX_EQ_"+j+"##",itemEqStr);
			exp=exp.replaceAll("##AUX_END_"+j+"##",itemEndStr);
			exp=exp.replaceAll("##AUX_CON_"+j+"##",itemConStr);
			auxsEqStr+=itemEqStr+" or ";
			auxsEndStr+=itemEndStr+" or ";
			auxsConStr+=itemConStr+" or ";
		}
		if(auxsEqStr.length()>4)auxsEqStr=auxsEqStr.substring(0,auxsEqStr.length()-4);
		if(auxsEndStr.length()>4)auxsEndStr=auxsEndStr.substring(0,auxsEndStr.length()-4);
		if(auxsConStr.length()>4)auxsConStr=auxsConStr.substring(0,auxsConStr.length()-4);
		exp=exp.replaceAll("##AUX_EQ##",auxsEqStr);
		exp=exp.replaceAll("##AUX_END##",auxsEndStr);
		exp=exp.replaceAll("##AUX_CON##",auxsConStr);
		return exp;
	} 
}
