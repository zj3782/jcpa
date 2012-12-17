package com.jcpa.pattern.javaclass;

import java.util.List;

import com.jcpa.util.AuxUtil;

import net.sourceforge.pmd.lang.java.ast.ASTArgumentList;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTLiteral;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;

public class CombinativeFunctionJavaRule extends JcpaAbstractJavaRule {
	@Override
	public Object visit(ASTMethodDeclaration node, Object data) {
		try {
			String aux=(String) getProperty(getPropertyDescriptor("aux"));
			if(aux==null || aux.equals(""))return data;
			String xpath="//MethodDeclaration[MethodDeclarator/@Image=\""+node.getMethodName()+"\"]/descendant::*/PrimaryExpression[PrimarySuffix/Arguments][PrimaryPrefix/Name[##AUX_CON##]]";
			xpath=AuxUtil.ExpIntegrate(xpath, aux);
			//all method invoke node
			List<?> lst = node.findChildNodesWithXPath(xpath);
			int len = lst.size();
			
			for(int i = 0; i < len; i++) {
			    for(int j = i + 1; j < len; j++) {
			    	ASTPrimaryExpression a = (ASTPrimaryExpression) lst.get(i);
			    	ASTPrimaryExpression b = (ASTPrimaryExpression) lst.get(j);

			        if(isInvokeEqual(a, b)) {
			        	addViolation(data, a);
			        	break;
			        }
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	/**
	 * is method invoke  equal
	 * 		1.method name
	 * 		2.argument count
	 * 		3.argument type
	 * */
	public static boolean isInvokeEqual(ASTPrimaryExpression na, ASTPrimaryExpression nb) throws Exception{
		List<ASTName> prefixNamesA=na.findChildNodesWithXPath("./PrimaryPrefix/Name");
		List<ASTName> prefixNamesB=nb.findChildNodesWithXPath("./PrimaryPrefix/Name");
		//judge method name is equal
		if(isListEmpty(prefixNamesA) || isListEmpty(prefixNamesB) ||
				!prefixNamesA.get(0).getImage().equals(prefixNamesB.get(0).getImage()))
		{
			return false;
		}
		//judge arguments
		List<ASTArguments> argsA=na.findChildNodesWithXPath("./PrimarySuffix/Arguments");
		List<ASTArguments> argsB=nb.findChildNodesWithXPath("./PrimarySuffix/Arguments");
		if(isListEmpty(argsA) || isListEmpty(argsB)){
			return false;
		}
		ASTArguments argA=argsA.get(0);
		ASTArguments argB=argsB.get(0);
		int argCountA=argA.getArgumentCount();
		int argCountB=argB.getArgumentCount();
		if(argCountA!=argCountB){
			return false;//arguments count not equal
		}
		if(argCountA==0 && argCountB==0){
			return true;//no arguments
		}
		List<ASTArgumentList> arglistA=argA.findChildNodesWithXPath("./ArgumentList");
		List<ASTArgumentList> arglistB=argB.findChildNodesWithXPath("./ArgumentList");
		if(isListEmpty(arglistA) || isListEmpty(arglistB)){
			return false;
		}
		//compare argument one by one
		List<ASTExpression> expA=arglistA.get(0).findChildNodesWithXPath("./Expression");
		List<ASTExpression> expB=arglistB.get(0).findChildNodesWithXPath("./Expression");
		if(isListEmpty(expA) || isListEmpty(expB) || expA.size()!=expB.size()){
			return false;
		}
		int expCount=expA.size();
		for(int i=0;i<expCount;i++){
			if(!isExpTypeEqual(expA.get(i),expB.get(i)))return false;
		}
		return true;
	}
	/**
	 * judge if two parameter type equal
	 * */
	public static boolean isExpTypeEqual(ASTExpression ea,ASTExpression eb) throws Exception{
		//parameter has three types:		1.actual		2.reference		3.expression
		
		//actual parameter
		List<ASTLiteral> lsa=ea.findChildNodesWithXPath("./PrimaryExpression/PrimaryPrefix/Literal");
		List<ASTLiteral> lsb=eb.findChildNodesWithXPath("./PrimaryExpression/PrimaryPrefix/Literal");
		ASTLiteral la=null,lb=null;
		if(!isListEmpty(lsa))la=lsa.get(0);
		if(!isListEmpty(lsb))lb=lsb.get(0);
		//reference parameter
		List<ASTName> nsa=ea.findChildNodesWithXPath("./PrimaryExpression/PrimaryPrefix/Name");
		List<ASTName> nsb=eb.findChildNodesWithXPath("./PrimaryExpression/PrimaryPrefix/Name");
		ASTName na=null,nb=null;
		if(!isListEmpty(nsa))na=nsa.get(0);
		if(!isListEmpty(nsb))nb=nsb.get(0);
		
		if(la!=null){//a---actual
			if(lb!=null){
				//b---actual
				return isParameterTypeEqual(la,lb);
			}else if(nb!=null){
				//b---reference
				return isParameterTypeEqual(la,nb);
			}else{
				//b---expression
				return isParameterTypeEqual(la,eb);
			}
		}else if(na!=null){//a---reference
			if(lb!=null){
				//b---actual
				return isParameterTypeEqual(lb,na);
			}else if(nb!=null){
				//b---reference
				return isParameterTypeEqual(na,nb);
			}else{
				//b---expression
				return isParameterTypeEqual(na,eb);
			}
		}else{//a----expression
			if(lb!=null){
				//b---actual
				return isParameterTypeEqual(lb,ea);
			}else if(nb!=null){
				//b---reference
				return isParameterTypeEqual(nb,ea);
			}else{
				//b---expression
				return isParameterTypeEqual(ea,eb);
			}
		}
	}
	
	/**
	 * judge parameter equal
	 * */
	public static boolean isParameterTypeEqual(ASTLiteral ta,ASTLiteral tb){
		return (ta.isIntLiteral() && tb.isIntLiteral()) || (ta.isCharLiteral() && tb.isCharLiteral()) || (ta.isFloatLiteral() && tb.isFloatLiteral()) || (ta.isStringLiteral() && tb.isStringLiteral());
	}
	public static boolean isParameterTypeEqual(ASTLiteral ta,ASTName nb){
		//TODO
		return false;
	}
	public static boolean isParameterTypeEqual(ASTName na,ASTName nb){
		//TODO
		return false;
	}
	public static boolean isParameterTypeEqual(ASTLiteral ta,ASTExpression expB){
		//TODO
		return false;
	}
	public static boolean isParameterTypeEqual(ASTName na,ASTExpression expB){
		//TODO
		return false;
	}
	public static boolean isParameterTypeEqual(ASTExpression expA,ASTExpression expB){
		//TODO
		return false;
	}
	

	
	/**
	 * judge if list is empty
	 * */
	public static boolean isListEmpty(List<?> l){
		if(l==null || l.size()==0)return true;
		return false;
	}
}
