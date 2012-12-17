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
			for(String item:items){
				item=item.trim();
				itemEqStr+="@Image=\""+item+"\" or ";
				itemConStr+="contains(@Image,\""+item+"\") or ";
				itemEndStr+="ends-with(@Image,\""+item+"\") or ";
			}
			if(itemEqStr.length()>4)itemEqStr=itemEqStr.substring(0,itemEqStr.length()-4);
			if(itemConStr.length()>4)itemConStr=itemConStr.substring(0,itemConStr.length()-4);
			if(itemEndStr.length()>4)itemEndStr=itemEndStr.substring(0,itemEndStr.length()-4);
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
