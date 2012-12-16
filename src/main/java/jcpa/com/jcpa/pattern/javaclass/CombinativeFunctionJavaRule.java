package com.jcpa.pattern.javaclass;

import java.util.List;

import com.jcpa.util.AuxUtil;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;

@SuppressWarnings("unchecked")
public class CombinativeFunctionJavaRule extends JcpaAbstractJavaRule {
	@Override
	public Object visit(ASTCompilationUnit node, Object data) {
		try {
			String aux=(String) getProperty(getPropertyDescriptor("aux"));
			if(aux==null || aux.equals(""))return data;
			String xpath1="//MethodDeclarator[##AUX_CON##][./descendant::*/ReferenceType[@Array = true() and @ArrayDepth = 1]]";
			String xpath2="//PrimaryExpression[PrimaryPrefix/Name[##AUX_CON##]][./descendant::*/Arguments]";
			xpath1=AuxUtil.ExpIntegrate(xpath1, aux);
			xpath2=AuxUtil.ExpIntegrate(xpath2, aux);
			
			List<Node> lstComb = (List<Node>) node.findChildNodesWithXPath(xpath1);
	        List<Node> lst = (List<Node>) node.findChildNodesWithXPath(xpath2);

	        for(Node ncomb : lstComb) {
	            String typeStr = getFormalArrayParameterElementType(ncomb);

	            for(Node n : lst) {
	                if(isActualParameterOfType(n, typeStr)) {
	                    addViolation(data, n);
	                }
	            }
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

    // suppose only one parameter
    public static String getFormalArrayParameterElementType(Node node) throws Exception {
        List<Node> lst = (List<Node>) node.findChildNodesWithXPath("./descendant::*/ReferenceType[@Array = true()]");

        int len = lst.size();

        if(len == 0) return null;

        return lst.get(0).jjtGetChild(0).getImage();
    }

    public static boolean isActualParameterOfType(Node node, String typeString) throws Exception {
        List<Node> lst = (List<Node>) node.findChildNodesWithXPath("./descendant::*/Arguments/ArgumentList/Expression[not(./descendant::*/AllocationExpression)]");

        if(lst.size() == 0) return false;

        for(Node n : lst) {
            List<Node> literals = (List<Node>) n.findChildNodesWithXPath("./descendant::*/Literal[@" + typeString + "Literal=true()]");

            if(literals.size() == 0) return false;
        }

        return true;
    }
}
