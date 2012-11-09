package com.jcpa.util;

import junit.framework.TestCase;

public class PMDUtilTest extends TestCase {

	public void testReport() {
		TestPMDRenderer renderer = new TestPMDRenderer();
		String path=ToolUtil.getProjPath(this)+"net/example/";
//		String ruleset="";
//		PMDUtil.report(path, ruleset, renderer);
	}

}
