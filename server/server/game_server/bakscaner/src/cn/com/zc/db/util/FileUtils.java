package cn.com.zc.db.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 
 */
public class FileUtils {
	/**
	 * 打开文件 
	 
	 */
	private static File openFile(String url,boolean clearOld){
		File active = new File(url);
		try {
			if (clearOld) {
				if(active.exists()){
					active.delete();
				}
				active.createNewFile();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return active;
	}
	
	
	public static void toFile(String path,String fileName,StringBuilder builder){
		toFile(path, fileName, builder.toString());
	}
	
	/**
	 * 生成文件
	 
	 */
	public static void toFile(String path,String fileName,String content){
		toFile(path, fileName, Arrays.asList(content));
	}
	
	/**
	 * 生成文件
	 * 
	 
	 */
	public static void toFile(String path,String fileName,List<String> contents){
		if (contents==null||contents.isEmpty()) {
			return;
		}
		File active = openFile(path+fileName,true);
		BufferedWriter writer = null ;
		try {
			writer = new BufferedWriter(new FileWriter(active));
			for (String content : contents) {
				writer.write(content.toString().toCharArray());
			}
			writer.flush();
			writer.close();
			mkdb.Trace.info("回写文件目录： " + path + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		}
	}
}
