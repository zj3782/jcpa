package com.jcpa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtil {
	/**
	 * regex get the first matched string in str
	 * */
	public static String regexGet(String str,String pattern){
		try {
			Matcher m = Pattern.compile(pattern).matcher(str);
			if(m.find())return m.group(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * get split count
	 * */
	public static long getSplitCount(long l,long oneCount)throws Exception
	{
		if(l==0)return 0;
		if(l<=0 || oneCount<=0)throw new Exception("Arg Format Error.Must Be Positive Integer");
		long splitCount=l / oneCount;
		if(l % oneCount != 0)splitCount++;
		return splitCount;
	}
	/**
	 * make str header char upper
	 * */
	public static String strHeadUpper(String str){
		if(str.length()>0){
			char head=str.charAt(0);
			if(head>='a' && head<='z'){
				head-=32;
				str=head+str.substring(1);
			}
		}
		return str;
	}
	/**
	 * str to int,if can't return 0
	 * */
	public static int strToInt(String str){
		return strToInt(str,0);
	}
	/**
	 * str to int,if can't return Default
	 * */
	public static int strToInt(String str,int Default){
		int ret=Default;
		if(str!=null && !str.equals("")){
			try{
				ret=Integer.parseInt(str);
			}catch(Exception e){ret=Default;}
		}
		return ret;
	}
	/**
	 * str to int,if the result less than 1,return 1
	 * if str can not convert to int,return Default
	 * */
	public static int strToPositiveInt(String str,int Default){
		int ret=strToInt(str,Default);
		if(ret<=0)ret=1;
		return ret;
	}
	/**
	 * Recursive delete foldeer
	 * @param dir
	 * @return int delete count
	 */
	public static int deleteDir(File dir){
		int deleteCount=0;
		File ChildFile;
		String[] children = dir.list();
		if(children!=null){
			for (String chid:children) {
				ChildFile=new File(dir, chid);
				if(ChildFile.isDirectory()){
					deleteCount+=deleteDir(ChildFile);
				}else{
					if(ChildFile.delete())deleteCount++;
				}
			}
		}
		// dir is empty now
		if(dir.delete())deleteCount++;
		return deleteCount;
	}

    /**
     * get file with certain endfix in folder
     * */
    public static List<String> getFileFromPath(String Path,String Fix){
    	List<String> files = new LinkedList<String>();
    	if(Fix!=null && !Fix.equals("") && Fix.charAt(0)!='.')Fix='.'+Fix;
    	try {
			File dir = new File(Path);
			String[] children = dir.list();
			for(String f:children){
				if(Fix==null || Fix.equals("") || f.endsWith(Fix)){
					files.add(f);
				}
			}
		} catch (Exception e) {}
		return files;
    }
    /**
     * is str a num
     * */
    public static boolean isNum(String str){
    	if(str==null || str.equals(""))return false;//空字符串
    	boolean bHasPoint=false;//是否包含小数点
    	for(int i=str.length();--i>=0;){ 
			int chr=str.charAt(i); 
			if(chr=='.'){
				if(bHasPoint)return false;//多个小数点
				bHasPoint=true;
			}else if(chr<48 || chr>57){//不是小数点也不是数字
				return false;
			}
		} 
		return true; 
    }
    /**
     * remove root path
     * */
    public static String rmvRootPath(String FullPath,String RootPath){
    	
    	if(RootPath==null || RootPath.equals("") || 
    		FullPath==null || FullPath.equals("")){
    		return FullPath;
    	}
    	int l1=FullPath.length(),l2=RootPath.length(),i;
    	if(l1<l2)return FullPath;
    	for(i=0;i<l2;i++){
    		if(FullPath.charAt(i)!=RootPath.charAt(i))return FullPath;
    	}
    	return FullPath.substring(i);
    }
    /**
     * format str by html
     * */
    public static String strHtmlFmt(String in){
    	if(in==null || in.equals(""))return in;
		StringBuffer quotedString = new StringBuffer();
	    for (int i=0;  i<in.length();  ++i) {
	      String translated = htmChars.get(in.charAt(i));
	      quotedString.append( translated!=null ? translated : in.charAt(i) );
	    }
	    return quotedString.toString();
    }
    private static Map<Character,String> htmChars = new HashMap<Character,String>();
    static {
    	htmChars.put('<',"&lt;");
    	htmChars.put('>',"&gt;");
    	htmChars.put('\r',"");
    	htmChars.put('\n',"<br>");
    	htmChars.put('\t',"&nbsp;&nbsp;&nbsp;&nbsp;");
    	htmChars.put(' ',"&nbsp;");
    }
    /**
     * format str by js var
     * */
    public static String strJsvarFmt(String in){
    	if(in==null || in.equals(""))return in;
		StringBuffer quotedString = new StringBuffer();
	    for (int i=0;  i<in.length();  ++i) {
	      String translated = Jsvars.get(in.charAt(i));
	      quotedString.append( translated!=null ? translated : in.charAt(i) );
	    }
	    return quotedString.toString();
    }
    private static Map<Character,String> Jsvars = new HashMap<Character,String>();
    static {
    	Jsvars.put('\r',"\\r");
    	Jsvars.put('\n',"\\n");
    	Jsvars.put('"',"\\\"");
    	Jsvars.put('\'',"\\'");
    }
    /**
     * remove \r and \n
     * */
    public static String rmvRN(String in){
    	if(in==null || in.equals(""))return in;
		StringBuffer quotedString = new StringBuffer();
		char c;
	    for (int i=0;  i<in.length();  ++i) {
	      c=in.charAt(i);
	      if(c!='\r' && c!='\n')quotedString.append(c);
	    }
	    return quotedString.toString();
    }
    /**
     * num round
     * */
    public static double getRound(double dSource){
        double iRound;
        BigDecimal deSource = new BigDecimal(dSource);
        iRound= deSource.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return iRound;
    }

    /**
     * get file content
     * @throws Exception 
     * */
    public static String getFileConetent(String fn,String charset) throws IOException{
    	String txt="";
		BufferedReader bufr = new BufferedReader(new InputStreamReader (new FileInputStream (new File (fn)),charset));
		String r=bufr.readLine();
    	while(r!=null){
    		txt+=r;
    		r=bufr.readLine();
    	}
    	bufr.close();
    	return txt;
    }
    /**
     * judge is folder exist,if not and bCreate is true,create it
     * */
    public static boolean ifFolderExist(String path,boolean bCreate){
    	try{
    		File folder = new File(path);
    		if(folder.exists()){
    			return true;
    		}
    		if(bCreate){
    			return folder.mkdirs();
    		}
    	}catch(Exception e){}
    	return false;
    }
    /**
     * judge if file exist,if not and bCreate is true,it will be created and content is initText.
     * */
    public static boolean ifFileExist(String path,boolean bCreate,String initText,String charset){
    	try {
			File f = new File(path);
			if (f.exists()){
				return true;
			}
			if (bCreate){
				if(!f.createNewFile())return false;
				if(initText!=null && !initText.equals("")){
					OutputStream out = new FileOutputStream(f);
					OutputStreamWriter fw = new OutputStreamWriter(out,charset);
					fw.write(initText);
					fw.flush();
					fw.close();
				}
				return true;
			}
		} catch (Exception e) {}
		return false;
    }
    /**
     * get time string
     * */
    public static String getTimeString() {
    	  String TimeString = "";
    	  Calendar c = Calendar.getInstance(Locale.CHINA);

    	  int yyyy = c.get(Calendar.YEAR);
    	  int mm = c.get(Calendar.MONTH) + 1;
    	  int dd = c.get(Calendar.DAY_OF_MONTH);
    	  int hh = c.get(Calendar.HOUR_OF_DAY);
    	  int MM = c.get(Calendar.MINUTE);
    	  int SS = c.get(Calendar.SECOND);

    	  String sMonth = mm < 10 ? "0" + mm : "" + mm;
    	  String sDay = dd < 10 ? "0" + dd : "" + dd;
    	  String sHour = hh < 10 ? "0" + hh : "" + hh;
    	  String sMinute = MM < 10 ? "0" + MM : "" + MM;
    	  String sSecond = SS < 10 ? "0" + SS : "" + SS;

    	  TimeString = yyyy + "." + sMonth + "." + sDay + " " + sHour + ":"+ sMinute + ":" + sSecond;

    	  return TimeString;
    }
}
