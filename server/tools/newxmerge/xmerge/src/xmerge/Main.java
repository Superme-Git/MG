package xmerge;

import java.io.File;

public class Main {
	static String xmergexml = null;
	static String srcdb = null;
	static String destdb = null;
	static String resultdb = null;
	static String foreigndb = null;
	static boolean checkOnly = false;
	private static String backupDir1 = "src1dbbackup";//dest数据库是不会被修改的,只要备份srcdb就行了
	
	private static void usage() {
		System.out.println("Usage: java -cp mylogic.jar -jar xmerge.jar -conf xmerge.xml  [options] ");
		System.out.println("-check[optional]    只检测并记录src_db_dir和dest_db_dir中存在重复的key信息");
		System.out.println("-srcdb		src_db_dir    源数据库路径，合服过程中不会被修改");
		System.out.println("-destdb		dest_db_dir    目标数据库路径，合服过程中不会被修改");
		System.out.println("-resultdb		result_db_dir    目标数据库路径，合服过程中不会被修改");
		System.out.println("-dest_foreigndb[optional]	  dest_foreign_dir对目标数据库进行验证，生成的外键关联信息数据库的路径，合服过程中不会被修改[可选项]");
		Runtime.getRuntime().exit(1);
	}

	private static void initialize() {
		if (null != srcdb)
			XmergeConf.getInstance().setSrcDbHome(srcdb);
		if (null != destdb)
			XmergeConf.getInstance().setDestDbHome(destdb);
		if (null != resultdb)
			XmergeConf.getInstance().setResultDbHome(resultdb);
		if (null != foreigndb)
			XmergeConf.getInstance().setForeignInfoDbHome(foreigndb);
	}

	public static void main(String args[]) throws Exception {
		
//		srcdb = "F:\\xmergetest\\src_db";
//		destdb = "F:\\xmergetest\\dest_db";
//		resultdb = "F:\\xmergetest\\result_db";
//		xmergexml = "xmerge.xml";
		
		long time=System.currentTimeMillis();
			
		System.setProperty("line.separator", "\n");
		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals("-check"))
				checkOnly = true;
			else if (args[i].equals("-dest_foreigndb"))
				foreigndb = args[++i];
			else if (args[i].equals("-srcdb"))
				srcdb = args[++i];
			else if (args[i].equals("-destdb"))
				destdb = args[++i];
			else if (args[i].equals("-resultdb"))
				resultdb = args[++i];
			else if (args[i].equals("-conf"))
				xmergexml = args[++i];
			else
				usage();
		}

		if (null == xmergexml)
			usage();
		Xmerge worker = new Xmerge(xmergexml);
		initialize();
		
		//备份两个数据库并用备份的数据库进行合服,这样即使合服失败,原来的数据也不会损坏
		File backup1 = new File(backupDir1);
		if (!backup1.exists())
			backup1.mkdir();
		if (!backup1.isDirectory()) 
			throw new RuntimeException(backupDir1+"is not a directory!");
		xmerge.Util.clearDir(backup1);
		xmerge.Util.copyFolder(XmergeConf.getInstance().getSrcDbHome(), backupDir1);
//		File backup2 = new File(backupDir2);
//		if (!backup2.exists())
//			backup2.mkdir();
//		if (!backup2.isDirectory()) 
//			throw new RuntimeException(backupDir2+"is not a directory!");
//		xmerge.Util.clearDir(backup2);
//		xmerge.Util.copyFolder(XmergeConf.getInstance().getDestDbHome(), backupDir2);
		
		XmergeConf.getInstance().setSrcDbHome(backupDir1);
		//XmergeConf.getInstance().setDestDbHome(backupDir2);
		
		worker.run(checkOnly);
		long time2=System.currentTimeMillis()-time;
		System.out.println("消耗时间\t"+(time2/1000)+"\t秒");
		
	}
}
