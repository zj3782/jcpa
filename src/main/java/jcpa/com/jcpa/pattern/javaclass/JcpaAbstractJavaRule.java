package com.jcpa.pattern.javaclass;

import java.util.List;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTBreakStatement;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTIfStatement;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTStatement;
import net.sourceforge.pmd.lang.java.ast.ASTSwitchStatement;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.rule.*;
import net.sourceforge.pmd.lang.rule.properties.StringProperty;

public abstract class JcpaAbstractJavaRule extends AbstractJavaRule {
	public static final StringProperty AUX_DESCRIPTOR = new StringProperty("aux", "Auxiliary field", "", 1.0f);

	public JcpaAbstractJavaRule(){
		definePropertyDescriptor(AUX_DESCRIPTOR);
	}
	
	/**
	 * judge if list is empty
	 * */
	public static  boolean isListEmpty(List<?> l){
		if(l==null || l.size()==0)return true;
		return false;
	}
	
	/**
	 * judge if parent is child's parent
	 * */
	public static boolean isParent(Node parent,Node child){
		List<Node> ps=(List<Node>)child.getParentsOfType(parent.getClass());
		for(Node p:ps){
			if(p.equals(parent))return true;
		}
		return false;
	}
	/**
     * judge if two node in different if Branches
     * */
    public static boolean isDiffJudgeBranch(Node na,Node nb){
    	if(na.getBeginLine()>nb.getBeginLine()){
    		Node nc=na;na=nb;nb=nc;
    	}
    	List<ASTIfStatement> laIf=na.getParentsOfType(ASTIfStatement.class);
    	for(ASTIfStatement i:laIf){
    		ASTStatement s=i.getFirstChildOfType(ASTStatement.class);
    		if(isParent(s,nb)){//in same branch
    				return false;
    		}
    		if(isParent(i,s)){
    				return true;
    		}
    	}
    	return false;
    }
    /**
     * judge if two node in different case branches
     * */
    public static boolean isDiffCaseBranch(Node na,Node nb) throws Exception{
    	if(na.getBeginLine()>nb.getBeginLine()){
    		Node nc=na;na=nb;nb=nc;
    	}
    	List<ASTSwitchStatement> laS=na.getParentsOfType(ASTSwitchStatement.class);
    	for(ASTSwitchStatement s:laS){
    		if(isParent(s,nb)){//na is in same switch block with nb
    			//list all breakstatement and judge if there are some between na and nb
    			List<ASTBreakStatement> bs=s.findChildNodesWithXPath("./BlockStatement/Statement/BreakStatement");
    			for(ASTBreakStatement b:bs){
    				if(b.getBeginLine()>na.getBeginLine() && b.getBeginLine()<nb.getBeginLine()){
    					return true;//there is breakstatement between na and nb.
    				}
    			}
    		}
    	}
    	return false;
    }
    /**
     * get parameter type
     * */
    public static String getParameterType(String p,ASTMethodDeclaration mNode,ASTClassOrInterfaceDeclaration cNode) throws Exception{
    	if(p==null || p.equals(""))return "";
    	//find in method
    	if(mNode!=null){
    		//Definited in method
    		List<ASTType> ts=mNode.findChildNodesWithXPath("./descendant::*/LocalVariableDeclaration[VariableDeclarator/VariableDeclaratorId/@Image='"+p+"']/Type");
    		if(!isListEmpty(ts)){
    			return ts.get(0).getTypeImage();
    		}
    		//Definited as method parameter
    		ts=mNode.findChildNodesWithXPath("./descendant::*/FormalParameters/FormalParameter[VariableDeclaratorId/@Image='"+p+"']/Type");
    		if(!isListEmpty(ts)){
    			return ts.get(0).getTypeImage();
    		}
    	}
    	//if not find in method ,find in class
    	if(cNode!=null){
    		List<ASTType> ts=cNode.findChildNodesWithXPath("./descendant::*/ClassOrInterfaceBodyDeclaration/FieldDeclaration[VariableDeclarator/VariableDeclaratorId/@Image=\""+p+"\"]/Type");
    		if(!isListEmpty(ts)){
    			return ts.get(0).getTypeImage();
    		}
    	}
    	return "";
    }
}
