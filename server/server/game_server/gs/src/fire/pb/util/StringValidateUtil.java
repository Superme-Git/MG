package fire.pb.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import fire.log.Module;


public class StringValidateUtil {

	public static List<String> readValidateFile(String path) {
		List<String> list = new LinkedList<String>();
		File file = new File(path);

		if (!file.exists()) {
			Module.logger.error("文件路径： " + path + "找不到相关文件");
			return list;
		}
		BufferedReader reader=null;
		try{
			InputStreamReader  fr= new InputStreamReader(new FileInputStream(path),"utf-8");
			reader = new BufferedReader(fr);
			String str = "";
			while((str = reader.readLine() )!= null){
				if(str.length() > 0){
//					str = str.toLowerCase();
					str = str.trim();
					list.add(str);
				}
			}
		} catch(Exception e){
			Module.logger.error("", e);
		} finally {
			try {
				if(null != reader){
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public static String convertIllegalWord(String keyWord) {
		if (keyWord == null)
			return "";

		char[] ch = keyWord.toCharArray();
		StringBuffer sbBuffer = new StringBuffer();
		for (int i = 0; i < ch.length; i++) {
			sbBuffer.append('*');
		}

		return sbBuffer.toString();
	}

	public static String checkAndReplaceIllegalWord(long roleid,int type,String content) {
		if (content == null)
			return content;

		for (String keyWord : Module.getNormalValidList()) {
			if (content.indexOf(keyWord) > -1) {
				content = content.replaceAll(keyWord, convertIllegalWord(keyWord));
			}
		}

		return content;
	}
	

	public static boolean checkIllegalWord(String content) {
		if (content == null)
			return false;

		for (String keyWord : Module.getNormalValidList()) {
			if (content.indexOf(keyWord) > -1) {
				return false;
			}
		}

		return true;
	}
	
	public static boolean checkValidName(String name){
		if (name == null)
			return false;
	
//		for (String keyWord : Module.getNormalValidList()) {
//			if (name.indexOf(keyWord) > -1) {
//				Module.logger.error("1名字有有字库的非法字\t" + keyWord);
//				return false;
//			}
//		}
		
		for (String keyWord : Module.getNpcValidList()) {
			if (name.indexOf(keyWord) > -1) {
				Module.logger.error("2名字有有字库的非法字\t" + keyWord);
				return false;
			}
		}
	
		return true;
	}
	
	public static void writeValiateListToFile(List<String> list, String path) {
		if (list == null || list.size() == 0) {
			return;
		}
		BufferedWriter fw=null;
		try {
			FileOutputStream fs=new FileOutputStream(path);
			OutputStreamWriter om=new OutputStreamWriter(fs, "utf-8");
			fw=new BufferedWriter(om);
			boolean isFirst = true;
			for (String e : list) {
				e.trim();
				if(isFirst){
					isFirst = false;
				}else{
					fw.newLine();
				}
				fw.write(e.toCharArray());
			}
			fw.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != fw){
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	private static Pattern pattern = Pattern.compile("(http:|https:)//[^[A-Za-z0-9\\._\\?%&+\\-=/#]]*");
	
	public static void matchURLsFromString(String str, List<String> strphases, java.util.Set<Integer> urlindexes)
	{
		Matcher matcher = pattern.matcher(str);
		if(matcher.groupCount() == 0)
			return;
		int whilenum = 0;
		int lastend = 0;
		int urlindex = 0;
		while(matcher.find())
		{
			int start = matcher.start();
			if(start > lastend)
			{
				strphases.add(str.substring(lastend, start));
				urlindex++;
			}
			strphases.add(matcher.group());
			urlindexes.add(urlindex);
			lastend = matcher.end();
			urlindex++;
			if(++whilenum > 40) return;//防止while过多
		}
		
		if(lastend < str.length())
			strphases.add(str.substring(lastend, str.length()));
		return;
	}
	
	
	public static float charNum(String src){
		if(src==null || src.isEmpty()) return 0;
		int chineseCnt = 0;
		int otherCnt = 0;
		for (int i=0; i<src.length(); i++){
			//匹配汉字
			if (src.substring(i, i+1).matches("[\u4e00-\u9fa5]")){
				chineseCnt++;
			}else{
				otherCnt++;
			}
		}
		return chineseCnt + otherCnt/2.0f;
	}
	
	
	public static String convertStringToUrlLabelString(String str)
	{
		List<String> strphases = new ArrayList<String>();
		java.util.Set<Integer> urlindexes = new HashSet<Integer>();
		matchURLsFromString(str, strphases, urlindexes);
		if(strphases.isEmpty())
			return str;
		StringBuilder sb = new StringBuilder("<T t=\"\"></T>");
		for(int i = 0 ; i < strphases.size(); i++)
		{
			String phase = strphases.get(i);
			if(urlindexes.contains(i))
				sb.append("<Http t=\"").append(phase).append("\" address=\"").append(phase).append("\"></Http>");
			else
				sb.append("<T t=\"").append(phase).append("\"></T>");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(convertStringToUrlLabelString("请访问http://www.163.com或者http://www.sina.com浏览新闻"));
	}
}
