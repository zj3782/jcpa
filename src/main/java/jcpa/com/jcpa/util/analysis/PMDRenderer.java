package com.jcpa.util.analysis;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import com.jcpa.util.ToolUtil;

import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.renderers.AbstractIncrementingRenderer;

/**
 * PMD代码分析的Renderer类
 * */
public class PMDRenderer extends AbstractIncrementingRenderer {
	
	private CodeReports reports=null;
	private String RootPath="";//跟目录
	
    public PMDRenderer(CodeReports reports) {
		super("PMDAnalysis","PMDAnalysis Description", null);
		this.reports=reports;
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
		String tmp="";
		while (violations.hasNext()) {
		    RuleViolation rv = violations.next();
		    String descri="break the rule <a href='"+rv.getRule().getExternalInfoUrl()+
    		"' target='_blank'>"+rv.getRule().getName()+"</a>:\r\n";
		    descri+=rv.getDescription()+"\r\nat ";
		    if(rv.getPackageName()!="")descri+=rv.getPackageName()+".";
		    descri+=rv.getClassName()+" [line "+rv.getBeginLine()+",column"+rv.getBeginColumn()+"]";

		    CodeReport report=new CodeReport();
		    report.setPackageName(rv.getPackageName());
		    report.setClassName(rv.getClassName());
		    report.setMethodName(rv.getMethodName());
		    report.setRuleName(rv.getRule().getName());
		    report.setDescription(descri);
		    report.setRulePriority(rv.getRule().getPriority().getPriority());
		    tmp="";
		    if(rv.getRule().getExamples().size()>0){
		    	tmp=rv.getRule().getExamples().get(0);
		    }
		    report.setExample(tmp);
		    reports.add(report);
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end() throws IOException {
    	// errors
		for (Report.ProcessingError pe : errors) {
			CodeReportError re=new CodeReportError();
			re.setFile(ToolUtil.rmvRootPath(pe.getFile(),RootPath));
			re.setMsg(pe.getMsg());
			reports.addError(re);
		}
	
		// suppressed violations
		if (showSuppressedViolations) {
//			for (Report.SuppressedViolation s : suppressed) {
//				line="{";
//				line+="\"file\":\""+s.getRuleViolation().getFilename()+"\",";
//				line+="\"type\":\""+(s.suppressedByNOPMD() ? "nopmd" : "annotation")+"\",";
//				line+="\"msg\":\""+s.getRuleViolation().getDescription()+"\",";
//				line+="\"usermsg\":\""+(s.getUserMessage() == null ? "" : s.getUserMessage())+"\"";
//				line+="}";
//				report.addSuppressed(line);
//			}
		}
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
    }
	/**
	 * @return the rootPath
	 */
	public String getRootPath() {
		return RootPath;
	}
	/**
	 * @param rootPath the rootPath to set
	 */
	public void setRootPath(String rootPath) {
		RootPath = rootPath;
	}
}
