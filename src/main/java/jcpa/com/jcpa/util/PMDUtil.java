package com.jcpa.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.RulesetsFactoryUtils;
import net.sourceforge.pmd.benchmark.Benchmark;
import net.sourceforge.pmd.benchmark.Benchmarker;
import net.sourceforge.pmd.lang.Language;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.LanguageVersionDiscoverer;
import net.sourceforge.pmd.renderers.Renderer;
import net.sourceforge.pmd.util.IOUtil;
import net.sourceforge.pmd.util.datasource.DataSource;

public class PMDUtil{
	/**
	 * analysis source code,grenate report to a file
	 * @param	InputPaths	String	scorce code path
	 * @param	RuleSets	String	ruleset file path
	 * @param	ReportFile	String	report file path
	 * @param	ReportFormat	String	format of the (xml,csv,html,emacs,ideaj,summaryhtml,xslt,vbhtml,text,textpad,textcolor,yahtml)
	 * */
	public static boolean report(String InputPaths,String RuleSets,String ReportFile,String ReportFormat){
		PMDConfiguration configuration=new PMDConfiguration();
		configuration.setInputPaths(InputPaths);
		configuration.setRuleSets(RuleSets);
		configuration.setReportFile(ReportFile);
		configuration.setReportFormat(ReportFormat);
		
		// Load the RuleSets
		long startLoadRules = System.nanoTime();
		RuleSetFactory ruleSetFactory = RulesetsFactoryUtils.getRulesetFactory(configuration);
		RuleSets ruleSets = RulesetsFactoryUtils.getRuleSets(configuration.getRuleSets(), ruleSetFactory, startLoadRules);
		if (ruleSets == null)return false;
		
		Set<Language> languages = new HashSet<Language>();
		LanguageVersionDiscoverer discoverer = configuration.getLanguageVersionDiscoverer();
		for (Rule rule : ruleSets.getAllRules()) {
			Language language = rule.getLanguage();
			if (languages.contains(language))continue;
			LanguageVersion version = discoverer.getDefaultLanguageVersion(language);
			if (RuleSet.applies(rule, version)) {
				languages.add(language);
			}
		}
		
		List<DataSource> files = PMD.getApplicableFiles(configuration,languages);

		try {
			Renderer renderer = configuration.createRenderer();
			List<Renderer> renderers = new LinkedList<Renderer>();
			renderers.add(renderer);

			renderer.setWriter(IOUtil.createWriter(configuration.getReportFile()));
			renderer.start();
			
			RuleContext ctx = new RuleContext();

			PMD.processFiles(configuration, ruleSetFactory, files, ctx, renderers);

			renderer.end();
			renderer.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * analysi source code,and the renderer will handle the report
	 * @param	InputPaths	String	scorce code path
	 * @param	RuleSets	String	ruleset path
	 * @param	renderer	Renderer
	 * */
	public static boolean report(String InputPaths,String RuleSets,Renderer renderer){
		PMDConfiguration configuration=new PMDConfiguration();
		configuration.setInputPaths(InputPaths);
		configuration.setRuleSets(RuleSets);
		
		// Load the RuleSets
		long startLoadRules = System.nanoTime();
		RuleSetFactory ruleSetFactory = RulesetsFactoryUtils.getRulesetFactory(configuration);
		RuleSets ruleSets = RulesetsFactoryUtils.getRuleSets(configuration.getRuleSets(), ruleSetFactory, startLoadRules);
		if (ruleSets == null)return false;
		
		Set<Language> languages = new HashSet<Language>();
		LanguageVersionDiscoverer discoverer = configuration.getLanguageVersionDiscoverer();
		for (Rule rule : ruleSets.getAllRules()) {
			Language language = rule.getLanguage();
			if (languages.contains(language))continue;
			LanguageVersion version = discoverer.getDefaultLanguageVersion(language);
			if (RuleSet.applies(rule, version)) {
				languages.add(language);
			}
		}
		
		List<DataSource> files = PMD.getApplicableFiles(configuration,languages);

		long reportStart = System.nanoTime();
		try {
			List<Renderer> renderers = new LinkedList<Renderer>();
			renderers.add(renderer);

			renderer.start();
			
			Benchmarker.mark(Benchmark.Reporting, System.nanoTime() - reportStart, 0);
			
			RuleContext ctx = new RuleContext();

			PMD.processFiles(configuration, ruleSetFactory, files, ctx, renderers);

			reportStart = System.nanoTime();
			renderer.end();
			renderer.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			Benchmarker.mark(Benchmark.Reporting, System.nanoTime() - reportStart, 0);
		}
		return true;
	}
	/**
	 * analysi source code by cmd line,and the renderer will handle the report
	 * @param	InputPaths	String	scorce code path
	 * @param	RuleSets	String	ruleset path
	 * @param	renderer	Renderer
	 * */
	public static int reportByCmd(String InputPaths,String ReportFmt,String RuleSets){
		String[] args=new String[3];
		args[0]=InputPaths;args[1]=ReportFmt;args[2]=RuleSets;
		return PMD.run(args);
	}
	/**
     * get file content form beginline to endline
     * @throws Exception 
     * */
    public static String getSource(String fn,int errorBeginLine,int errorEndLine) throws IOException{
    	String snippet = "";
		String str = null;
		int startLine=errorBeginLine-3,endLine=errorEndLine+3,line = 1;
		if(startLine<1)startLine=1;
		
		BufferedReader reader = new BufferedReader(new FileReader(fn));
		str = reader.readLine();
		while ( str != null){			
			if (line >= startLine && line <= endLine){
				if(line>=errorBeginLine && line<=errorEndLine){
					snippet +=line+"&nbsp;&nbsp;%3cspan class='cRed'%3e"+str+"%3c/span%3e\r\n";
				}else{
					snippet +=line+"&nbsp;&nbsp;"+str+"\r\n";
				}
				if (line == endLine){
					break;
				}
			}
			str = reader.readLine();
			line++;
		}	
		reader.close();
		return snippet;
    }
}