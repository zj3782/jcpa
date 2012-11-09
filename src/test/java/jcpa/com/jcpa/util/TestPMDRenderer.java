package com.jcpa.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.renderers.AbstractIncrementingRenderer;

/**
 * PMD代码分析的Renderer类
 * */
public class TestPMDRenderer extends AbstractIncrementingRenderer {
	private List<String> _rules = new ArrayList<String>();
	private List<Integer> _count = new ArrayList<Integer>();
	
    public TestPMDRenderer() {
		super("TestPMDAnalysis","TestPMDAnalysis Description", null);
		writer=new OutputStreamWriter(System.out);
	}
    public String defaultFileExtension() { return "pmd"; }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderFileViolations(Iterator<RuleViolation> violations) throws IOException {
    	RuleViolation rv;
    	int index,count;
    	String ruleName;
    	while (violations.hasNext()) {
    		rv=violations.next();
    		ruleName=rv.getRule().getName();
    		index=_rules.indexOf(ruleName);
    		if(-1==index){
    			_rules.add(ruleName);
    			_count.add(1);
    		}else{
    			count=_count.get(index);
    			_count.set(index,count+1);
    		}
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end() throws IOException {
    	// errors
		for (Report.ProcessingError pe : errors) {
			writer.write("[Error]"+pe.getMsg()+"\r\n");
		}
		writer.write("++++++++reports++++++++\r\n");
		int size=_rules.size();
		for(int i=0;i<size;i++){
			writer.write("\t"+_rules.get(i)+"\t\t"+_count.get(i)+"\r\n");
		}
		writer.write("++++++++++++++++++++\r\n");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
    }

    
}
