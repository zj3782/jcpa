package com.jcpa.pattern.javaclass;

import java.util.List;

import com.jcpa.util.AuxUtil;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
public class CallExpensiveFunctionJavaRule extends JcpaAbstractJavaRule {
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
			
			//System.out.println("Node:"+node.getBeginLine()+"-"+node.getEndLine()+"   ChildNodes:"+len+"   XPATH:"+xpath);

			for(int i = 0; i < len; i++) {
			    for(int j = i + 1; j < len; j++) {
			        Node a = (Node) lst.get(i);
			        Node b = (Node) lst.get(j);

			        if(isTreeEqual(a, b)) {
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
	
	public static boolean isTreeEqual(Node na, Node nb) throws Exception, ClassNotFoundException {
        if(!isNodeEqual(na, nb)) return false;

        List<? extends Node> lsta = na.findChildNodesWithXPath("./*");
        List<? extends Node> lstb = nb.findChildNodesWithXPath("./*");

        if(lsta.size() != lstb.size()) return false;

        int len = lsta.size();

        for(int i = 0; i < len; i++) {
            Node a = (Node)lsta.get(i);
            Node b = (Node)lstb.get(i);

            if(!isTreeEqual(a, b)) return false;
        }

        return true;
    }


    public static boolean isNodeEqual(Node na, Node nb) throws ClassNotFoundException {
        String clsa = na.toString();
        String clsb = nb.toString();

        if(!clsa.equals(clsb)) return false;


        String imga = na.getImage();
        String imgb = nb.getImage();


        if(imga != null && !imga.equals(imgb)) return false;

        if(imgb != null && !imgb.equals(imga)) return false;

        return true;
    }
}
