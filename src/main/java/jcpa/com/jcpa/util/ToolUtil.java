package com.jcpa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtil {
	/**
	 * 正则表达式获取str中符合pattern格式的子串（返回第一个匹配的）
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
	 * 将l按照每份oneCount个进行区分，返回能区分的份数
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
	 * 将字符串str的首字母变为大写字母返回
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
	 * 将字符串str装换为整数,如果str无法转换，以0替换
	 * */
	public static int strToInt(String str){
		return strToInt(str,0);
	}
	/**
	 * 将字符串str装换为整数,如果str无法转换，以Default值替换
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
	 * 将字符串str装换为正整数(大于等于1),如果str无法转换，以Default值替换
	 * */
	public static int strToPositiveInt(String str,int Default){
		int ret=strToInt(str,Default);
		if(ret<=0)ret=1;
		return ret;
	}
	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir 将要删除的文件目录
	 * @return int 返回删除的文件和目录的数目
	 */
	public static int deleteDir(File dir){
		int deleteCount=0;
		File ChildFile;
		String[] children = dir.list();
		if(children!=null){
			//递归删除目录中的子目录和文件
			for (int i=0; i<children.length; i++) {
				ChildFile=new File(dir, children[i]);
				if(ChildFile.isDirectory()){
					deleteCount+=deleteDir(ChildFile);
				}else{
					if(ChildFile.delete())deleteCount++;
				}
			}
		}
		// 目录此时为空，可以删除
		if(dir.delete())deleteCount++;
		return deleteCount;
	}
    /**
     * 获取当前工程路径
     * */
    public static String getProjPath(Object obj){
    	return obj.getClass().getResource("/").getPath();
    }
    /**
     * 获取指定目录下指定格式的文件列表
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
     * 判断字符串是否为数字
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
     * 去除根路径
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
     * 将字符串格式化，方便在页面显示
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
    }
    /**
     * 将字符串变量进行处理以便能够直接在js中当作变量处理
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
     * 四舍五入
     * */
    public static double getRound(double dSource){
        double iRound;
        //BigDecimal的构造函数参数类型是double
        BigDecimal deSource = new BigDecimal(dSource);
        //deSource.setScale(0,BigDecimal.ROUND_HALF_UP) 返回值类型 BigDecimal
        //intValue() 方法将BigDecimal转化为int
        iRound= deSource.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return iRound;
    }
    
//    public static void main(String args[]){
//    	System.out.print(getRound(1.999999));
//    }
    /**
     * 获取文件内容
     * @throws Exception 
     * */
    public static String getFileConetent(String fn,String charset) throws Exception{
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
     * 判断目录是否存在
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
}
