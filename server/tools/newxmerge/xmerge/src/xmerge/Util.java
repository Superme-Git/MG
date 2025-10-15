package xmerge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.TimeZone;

import mkdb.util.DatabaseMetaData;

import com.locojoy.base.Marshal.OctetsStream;


/**
 * helper
 * */
public final class Util {
	public static final long dayMills = 1000 * 60 * 60 * 24;
	public static final long rawTimeOffset = TimeZone.getDefault().getRawOffset();
	
	public static boolean inTheSameDay(long firstT,long secondT) {
		
//		final Calendar cal1 = Calendar.getInstance();
//		final Calendar cal2 = Calendar.getInstance();
//		cal1.setTimeInMillis(firstT);
//		cal2.setTimeInMillis(secondT);
//		if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
//				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
//				&& cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE))
//			return true;
		if (getCurrentDay(firstT) == getCurrentDay(secondT))
			return true;
		return false;
	}
	
	public static int getCurrentDay(long time) {

		return (int) ((time + rawTimeOffset) / dayMills);
	}
	
	
	/**
	 * 删除dir下的所有文件，目录结构仍然保留
	 * @param File dir   文件目录 
	 * */
	public static boolean clearDir(File dir) { 
		if (dir == null || !dir.exists() || !dir.isDirectory()) 
			return false; // 检查参数 
		for (File file : dir.listFiles()) { 
			if (file.isFile()) 
			{
				if(!file.delete()) // 删除所有文件
					return false;
			}
			else if (file.isDirectory()) 
			{
				if(!clearDir(file))
					return false; // 递规的方式删除文件夹
			}
		} 
		return true;
	} 
	
	/**
	 * 删除dir下的所有文件及目录
	 * @param File dir   文件目录 
	 * */
	public static boolean deleteDir(File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory()) 
			return false; // 检查参数 
		for (File file : dir.listFiles()) { 
			if (file.isFile()) {
				if(!file.delete()) // 删除所有文件
					return false;
			} else if (file.isDirectory()) {
				if(!deleteDir(file))
					return false; // 递规的方式删除文件夹
			}
		} 
		// 删除所有文件夹
		return dir.delete();
	} 
	
	/**
	 * 拷贝srcDir中的所有文件到destDir中
	 * @param String srcDir 源文件目录，需要确保存在且isDirectory
	 * @param String destDir 目标文件目录，随便给个路径就行
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * */
	public static void copyFolder(String srcDir, String destDir) throws FileNotFoundException, IOException {
		if(null == srcDir || srcDir.isEmpty())
			throw new Error("srcDir invalid... ...");
		if(null == destDir || destDir.isEmpty())
			throw new Error("destDir invalid... ...");
		
		File srcHome = new File(srcDir);
		File destHome = new File(destDir);
		if(!srcHome.exists() || !srcHome.isDirectory())
			throw new Error(srcDir + " is not exist or not a directory");
		if(!destHome.exists() || !destHome.isDirectory())
			destHome.mkdirs();
		//System.out.println(destHome.getAbsolutePath());
		for (File file : srcHome.listFiles()) { 
			if(file.isFile()){
				copyFile(file, destHome);
			}else if(file.isDirectory()){
				copyFolder(file.getCanonicalPath(), destHome.getCanonicalPath()+File.separator+file.getName());
			}
		}
	}
	
	/*
	 * 文件拷贝，用来执行表拷贝操作
	 */
	public static void copyFile(File src, File destDir) throws FileNotFoundException, IOException {
		InputStream is = new FileInputStream(src);
		try {
			OutputStream os = new FileOutputStream(new File(destDir, src.getName()));
			try {
				byte [] buffer = new byte[4096];
				int rc = 0;
				while ((rc = is.read(buffer)) > 0) {
					os.write(buffer, 0, rc);
				}
			} finally {
				os.close();
			}
		} finally {
			is.close();
		}
	}
	
	// 暂时替代uniqname.allocateNewKey()
	// BOOLEAN, SHORT, INT, LONG, STRING, FLOAT, BINARY, BEAN, ANY
	public static OctetsStream allocateNewKey(DatabaseMetaData.Type type) {
		switch (type.getTypeId()) {
		case BOOLEAN:
			return new OctetsStream().marshal(new Random().nextBoolean());
		case SHORT:
			return new OctetsStream().marshal(Short.valueOf((short)new Random().nextInt()));
		case INT:
			return new OctetsStream().marshal(new Random().nextInt());
		case LONG:
			return new OctetsStream().marshal(new Random().nextLong());
		case STRING:
			return new OctetsStream().marshal(String.valueOf(new Random().nextLong()), mkdb.Const.IO_CHARSET);
		case FLOAT:
			return new OctetsStream().marshal(new Random().nextFloat());
		case BEAN:
		case BINARY:
		case ANY:
		default:
			throw new IllegalArgumentException("unsupport type = " + type);
		}
	}
	
	/**
	 * simple log
	 * */
	public final static class Log{
		private final File home;
		private final String logName;
		private boolean console;
		private PrintStream ps;

		public Log(File home, String logName, boolean console) {
			this.home = home;
			this.console = console;
			this.logName = logName;
			try {
				if(!console){
					if (null != home) {
						File file = new File(home, logName);
						ps = new PrintStream(new FileOutputStream(file, true), false, "GBK");
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				ps = null;
			}
		}

		public String getLog(){
			if(!this.console)
				return home.getAbsolutePath()+"/"+logName;
			else
				return null;
		}
		
		public void println(String str) {
			if (null != ps) {
				ps.println(str);
			}
			if (this.console) {
				System.out.println(str);
			}
		}

		public void close() {
			if (null != ps) {
				ps.close();
				ps = null;
			}
			this.console = false;
		}
	}
}
