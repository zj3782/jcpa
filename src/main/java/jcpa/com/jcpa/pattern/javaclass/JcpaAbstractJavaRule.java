package com.jcpa.pattern.javaclass;

import net.sourceforge.pmd.lang.java.rule.*;
import net.sourceforge.pmd.lang.rule.properties.StringProperty;

public abstract class JcpaAbstractJavaRule extends AbstractJavaRule {
	public static final StringProperty AUX_DESCRIPTOR = new StringProperty("aux", "Auxiliary field", "", 1.0f);

	public JcpaAbstractJavaRule(){
		definePropertyDescriptor(AUX_DESCRIPTOR);
	}
}
