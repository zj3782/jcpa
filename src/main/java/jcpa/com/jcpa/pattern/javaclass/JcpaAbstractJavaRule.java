package com.jcpa.pattern.javaclass;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.lang.ast.Node;
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
}
