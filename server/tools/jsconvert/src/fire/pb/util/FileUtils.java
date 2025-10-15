package fire.pb.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import fire.pb.main.Main;

public class FileUtils {
	/**
	 * 拷贝单个文件
	 * 
	 * @param srcFile
	 * @param dstDir
	 */
	public static void copyFile(String srcFile, String dstDir) {
		try {
			FileInputStream fis = new FileInputStream(srcFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			
			File file = new File(dstDir);
			String strFileFolder = file.getParent();
			File fileFolder = new File(strFileFolder);
			if(!fileFolder.exists()){
				fileFolder.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(dstDir);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			int len = 0;
			while ((len = bis.read()) != -1) {
				bos.write(len);
			}
			
			bis.close();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copyFile(File srcFile, File dstDir) {
		try {
			FileInputStream fis = new FileInputStream(srcFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			
			FileOutputStream fos = new FileOutputStream(dstDir);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			int len = 0;
			while ((len = bis.read()) != -1) {
				bos.write(len);
			}
			
			bis.close();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void copyGsFileToProject(String dstFilePath) {
		copyFile(dstFilePath.replace("tools\\jsconvert", "server\\game_server\\gs"), dstFilePath);
	}
	
	public static String getRealFilePath(String packClassName){
		String ret = packClassName.replace(".", "\\");
		return Main.getCurAbsolutePath() + "src\\" + ret + ".java";
	}
	
	public static String getPackNamePath() {
		return null;
	}
}
