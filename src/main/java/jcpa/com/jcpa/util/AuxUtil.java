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
		exp=AuxNormalReplace(exp,auxStr);
		exp=AuxClassMethodReplace(exp,auxStr);
		return exp;
	}
	/**
	 * normal replace:equal/end-with/contains/pmd:matches
	 * */
	public static String AuxNormalReplace(String exp,String auxStr){
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
			exp=exp.replaceAll("##AUX_EQ_"+j+"##",EscapeForReplaceAll(itemEqStr));//equal
			exp=exp.replaceAll("##AUX_END_"+j+"##",EscapeForReplaceAll(itemEndStr));//end-with
			exp=exp.replaceAll("##AUX_CON_"+j+"##",EscapeForReplaceAll(itemConStr));//contains
			exp=exp.replaceAll("##AUX_REP_"+j+"##",EscapeForReplaceAll(auxs[j]));//replace
			exp=exp.replaceAll("##AUX_REG_"+j+"##",EscapeForReplaceAll("pmd:matches(@Image,"+itemRegStr+")"));//regular
			auxsEqStr+=itemEqStr+" or ";
			auxsEndStr+=itemEndStr+" or ";
			auxsConStr+=itemConStr+" or ";
			auxsRegStr+=itemRegStr+",";
		}
		if(auxsEqStr.length()>4)auxsEqStr=auxsEqStr.substring(0,auxsEqStr.length()-4);
		if(auxsEndStr.length()>4)auxsEndStr=auxsEndStr.substring(0,auxsEndStr.length()-4);
		if(auxsConStr.length()>4)auxsConStr=auxsConStr.substring(0,auxsConStr.length()-4);
		if(auxsRegStr.length()>1)auxsRegStr=auxsRegStr.substring(0,auxsRegStr.length()-1);
		exp=exp.replaceAll("##AUX_EQ##",EscapeForReplaceAll(auxsEqStr));
		exp=exp.replaceAll("##AUX_END##",EscapeForReplaceAll(auxsEndStr));
		exp=exp.replaceAll("##AUX_CON##",EscapeForReplaceAll(auxsConStr));
		exp=exp.replaceAll("##AUX_REP##",EscapeForReplaceAll(auxStrReplaceAll));
		exp=exp.replaceAll("##AUX_REG##",EscapeForReplaceAll("pmd:matches(@Image,"+auxsRegStr+")"));
		return exp;
	} 
	/**
	 * replace Specific class instance's method invoke
	 * */
	public static String AuxClassMethodReplace(String exp,String auxStr){
		if(exp==null || exp.equals(""))return "";
		if(auxStr==null || auxStr.equals(""))return exp;
		if(auxStr.startsWith("##"))auxStr=auxStr.substring(2);
		String auxs[]=auxStr.split("##");
		String repXpathItemReg="",repXpathStrReg="",repXpathItem="",repXpathStr="";
		for(int j=0;j<auxs.length;j++){
			repXpathItemReg="";
			repXpathItem="";
			String items[]=auxs[j].split(",");
			for(String item:items){
				//split class->method
				String cm[]=item.split("->");
				if(cm==null){
					continue;
				}
				if(cm.length<2){//just one part,only judge method,don,t care about class instance
					repXpathItemReg+="(descendant-or-self::PrimaryExpression/PrimaryPrefix/Name[pmd:matches(@Image,'"+item+"')])";
					repXpathItemReg+=" or ";
					repXpathItem+="(descendant-or-self::PrimaryExpression/PrimaryPrefix/Name[@Image='"+item+"'])";
					repXpathItem+=" or ";
				}else{//two part
					String c=cm[0],m=cm[1];
					
					repXpathItemReg+="(descendant-or-self::PrimaryExpression/PrimaryPrefix/Name[pmd:matches(@Image,'\\\\."+m+"')]";
					repXpathItemReg+="and";
					repXpathItemReg+="(";
					repXpathItemReg+="ancestor::*/MethodDeclaration/descendant::*/LocalVariableDeclaration[Type/descendant::*/ClassOrInterfaceType[pmd:matches(@Image,'"+c+"')]]/VariableDeclarator/VariableDeclaratorId/@Image";
					repXpathItemReg+="=substring-before(descendant-or-self::PrimaryExpression/PrimaryPrefix/Name/@Image,'.')";
					repXpathItemReg+=" or ";
					repXpathItemReg+="ancestor::*/MethodDeclaration/descendant::*/FormalParameters/FormalParameter[Type/descendant::*/ClassOrInterfaceType[pmd:matches(@Image,'"+c+"')]]/VariableDeclaratorId/@Image";
					repXpathItemReg+="=substring-before(descendant-or-self::PrimaryExpression/PrimaryPrefix/Name/@Image,'.')";
					repXpathItemReg+=" or ";
					repXpathItemReg+="ancestor::*/ClassOrInterfaceBodyDeclaration/FieldDeclaration[Type/descendant::*/ClassOrInterfaceType[pmd:matches(@Image,'"+c+"')]]/VariableDeclarator/VariableDeclaratorId/@Image";
					repXpathItemReg+="=substring-before(descendant-or-self::PrimaryExpression/PrimaryPrefix/Name/@Image,'.')";
					repXpathItemReg+="))";
					repXpathItemReg+=" or ";
					
					repXpathItem+="(descendant-or-self::PrimaryExpression/PrimaryPrefix/Name[ends-with(@Image,'."+m+"')]";
					repXpathItem+="and";
					repXpathItem+="(";
					repXpathItem+="ancestor::*/MethodDeclaration/descendant::*/LocalVariableDeclaration[Type/descendant::*/ClassOrInterfaceType[@Image='"+c+"']]/VariableDeclarator/VariableDeclaratorId/@Image";
					repXpathItem+="=substring-before(descendant-or-self::PrimaryExpression/PrimaryPrefix/Name/@Image,'.')";
					repXpathItem+=" or ";
					repXpathItem+="ancestor::*/MethodDeclaration/descendant::*/FormalParameters/FormalParameter[Type/descendant::*/ClassOrInterfaceType[@Image='"+c+"']]/VariableDeclaratorId/@Image";
					repXpathItem+="=substring-before(descendant-or-self::PrimaryExpression/PrimaryPrefix/Name/@Image,'.')";
					repXpathItem+=" or ";
					repXpathItem+="ancestor::*/ClassOrInterfaceBodyDeclaration/FieldDeclaration[Type/descendant::*/ClassOrInterfaceType[@Image='"+c+"']]/VariableDeclarator/VariableDeclaratorId/@Image";
					repXpathItem+="=substring-before(descendant-or-self::PrimaryExpression/PrimaryPrefix/Name/@Image,'.')";
					repXpathItem+="))";
					repXpathItem+=" or ";
				}
			}
			if(repXpathItemReg.length()>4)repXpathItemReg=repXpathItemReg.substring(0,repXpathItemReg.length()-4);
			if(repXpathItem.length()>4)repXpathItem=repXpathItem.substring(0,repXpathItem.length()-4);
			exp=exp.replaceAll("##AUX_CMI_REG_"+j+"##",EscapeForReplaceAll(repXpathItemReg));
			exp=exp.replaceAll("##AUX_CMI_"+j+"##",EscapeForReplaceAll(repXpathItem));
			repXpathStrReg+=repXpathItemReg+" or ";
			repXpathStr+=repXpathItem+" or ";
		}
		if(repXpathStrReg.length()>4)repXpathStrReg=repXpathStrReg.substring(0,repXpathStrReg.length()-4);
		if(repXpathStr.length()>4)repXpathStr=repXpathStr.substring(0,repXpathStr.length()-4);
		exp=exp.replaceAll("##AUX_CMI_REG##",EscapeForReplaceAll(repXpathStrReg));
		exp=exp.replaceAll("##AUX_CMI##",EscapeForReplaceAll(repXpathStr));
		return exp;
	}
	/**
	 * replace special char in regular expression
	 * */
	public static String EscapeForReplaceAll(String in){
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
