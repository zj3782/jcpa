package com.jcpa.util;

import java.io.File;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNUtil {
	private static SVNClientManager CM;
	
	/***
     * support servel agreements
     */
    public static void setupLibrary() {
       // http:// https://
       DAVRepositoryFactory.setup();
       //svn:/ / svn+xxx:/ /
       SVNRepositoryFactoryImpl.setup();
       //file://
       FSRepositoryFactory.setup();
    }
    
    /*
     * get SVNURL object by string
     * */
    public static SVNURL url(String url){
    	SVNURL repositoryURL = null;
	    try {
	    	repositoryURL = SVNURL.parseURIEncoded(url);
	    } catch (Exception e) {
	    	repositoryURL=null;
	    }
	    return repositoryURL;
    }
	/*
	 *get SVNUpdateClient by username and password
	 **/
	public static SVNUpdateClient getClient(String UserName,String PassWord){
		ISVNOptions options = SVNWCUtil.createDefaultOptions( true );
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(UserName, PassWord);
        CM = SVNClientManager.newInstance( options , authManager );
        return CM.getUpdateClient();
	}
	/*
	 * checkout
	 * */
	public static boolean checkout(String UserName,String PassWord,String url,String localDir)
			throws Exception
	{
		return checkout(getClient(UserName,PassWord),url,localDir);
	}
	@SuppressWarnings("deprecation")
	public static boolean checkout(SVNUpdateClient client,String url,String localDir)
		throws Exception
	{
		SVNUtil.setupLibrary();
		client.doCheckout(SVNUtil.url(url),new File(localDir), SVNRevision.HEAD, SVNRevision.HEAD, true);
		return true;
	}
}
