package com.jcpa.pattern.javaclass;

import java.util.List;

import com.jcpa.util.AuxUtil;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTArguments;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryExpression;


public class CombinativeFunctionJavaRule extends JcpaAbstractJavaRule {
	private String methodName="";
	private String aux="";
	
	@Override
	public Object visit(ASTCompilationUnit node, Object data) {	
		try {
			aux=(String) getProperty(getPropertyDescriptor("aux"));
			if (aux == null || aux.equals(""))return data;
			//in method
			List<ASTMethodDeclaration> methods=node.findChildNodesWithXPath("//MethodDeclaration");
			for(ASTMethodDeclaration m:methods){
				MyMethodVisit(m,data);
			}
			//inloop
			String xpath1 = "//ForStatement/Statement/descendant::*/PrimaryExpression/PrimaryPrefix";
			xpath1 += "[following-sibling::*[position()=1 and self::PrimarySuffix]/Arguments[@ArgumentCount>\"0\"]]";
			xpath1 += "/Name[##AUX_REG##]";
			xpath1 += "|";
			xpath1 += "//WhileStatement/Statement/descendant::*/PrimaryExpression/PrimaryPrefix";
			xpath1 += "[following-sibling::*[position()=1 and self::PrimarySuffix]/Arguments[@ArgumentCount>\"0\"]]";
			xpath1 += "/Name[##AUX_REG##]";
			xpath1 += "|";
			xpath1 += "//DoStatement/Statement/descendant::*/PrimaryExpression/PrimaryPrefix";
			xpath1 += "[following-sibling::*[position()=1 and self::PrimarySuffix]/Arguments[@ArgumentCount>\"0\"]]";
			xpath1 += "/Name[##AUX_REG##]";
			xpath1 = AuxUtil.ExpIntegrate(xpath1, aux);
			//System.out.println(xpath1);
			List<Node> lst = node.findChildNodesWithXPath(xpath1);
			for(Node n:lst){
				addViolation(data, n);
			}
		} catch (Exception e) {
			System.out.println("Exception:"+e.getMessage());
		}
		return data;
	}
	
	public void MyMethodVisit(ASTMethodDeclaration node, Object data) {
		try {
			if(aux==null || aux.equals(""))return;
			methodName=node.getMethodName();
			String xpath="//MethodDeclaration[MethodDeclarator/@Image=\""+methodName+"\"]/descendant::*/PrimaryExpression[PrimarySuffix/Arguments][PrimaryPrefix/Name[##AUX_REG##]]";
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
			//e.printStackTrace();
			System.out.println("Exception:"+e.getMessage());
		}
	}
	/**
	 * is method invoke  equal
	 * 		1.method name
	 * 		2.argument count
	 * 		3.argument type
	 * */
	public  boolean isInvokeEqual(ASTPrimaryExpression na, ASTPrimaryExpression nb) throws Exception{
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
		//to make question simple,we don't judge if every parameter's type,if args count equal,return true
		return true;
/*		List<ASTArgumentList> arglistA=argA.findChildNodesWithXPath("./ArgumentList");
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
		return true;*/
	}
	/**
	 * judge if two parameter type equal
	 * */
/*	public  boolean isExpTypeEqual(ASTExpression ea,ASTExpression eb) throws Exception{
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
	*/
	/**
	 * judge parameter equal
	 * */
/*	public  boolean isParameterTypeEqual(ASTLiteral ta,ASTLiteral tb){
		return (ta.isIntLiteral() && tb.isIntLiteral()) || (ta.isCharLiteral() && tb.isCharLiteral()) || (ta.isFloatLiteral() && tb.isFloatLiteral()) || (ta.isStringLiteral() && tb.isStringLiteral());
	}
	public  boolean isParameterTypeEqual(ASTLiteral ta,ASTName nb){
		//TODO
		String type=getNameType(nb);
		return (ta.isIntLiteral() && type.equals("int")) || (ta.isCharLiteral() && type.equals("char")) || (ta.isFloatLiteral() && type.equals("float")) || (ta.isStringLiteral() && type.equals("String"));
	}
	public  boolean isParameterTypeEqual(ASTName na,ASTName nb){
		//TODO
//		return getNameType(nb).equals(getNameType(nb));
		return false;
	}
	public  boolean isParameterTypeEqual(ASTLiteral ta,ASTExpression expB){
		//TODO
		return false;
	}
	public  boolean isParameterTypeEqual(ASTName na,ASTExpression expB){
		//TODO
		return false;
	}
	public  boolean isParameterTypeEqual(ASTExpression expA,ASTExpression expB){
		//TODO
		return false;
	}
*/	
	/**
	 * get reference parameter type
	 * */
/*	public String getNameType(ASTName n){
		return "";
	}
*/
}
