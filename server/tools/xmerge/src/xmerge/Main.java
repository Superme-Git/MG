package xmerge;

public class Main {
	static String xmergexml = null;
	static String srcdb = null;
	static String destdb = null;
	static String foreigndb = null;
	static boolean checkOnly = false;

	private static void usage() {
		System.out.println("Usage: java -cp mylogic.jar -jar xmerge.jar -conf xmerge.xml  [options] ");
		System.out.println("-check[optional]    只检测并记录src_db_dir和dest_db_dir中存在重复的key信息");
		System.out.println("-srcdb		src_db_dir    源数据库路径，合服过程中不会被修改");
		System.out.println("-destdb		dest_db_dir    目标数据库路径，合服过程中不会被修改");
		System.out.println("-dest_foreigndb[optional]	  dest_foreign_dir对目标数据库进行验证，生成的外键关联信息数据库的路径，合服过程中不会被修改[可选项]");
		Runtime.getRuntime().exit(1);
	}

	private static void initialize() {
		if (null != srcdb)
			XmergeConf.getInstance().setSrcDbHome(srcdb);
		if (null != destdb)
			XmergeConf.getInstance().setDestDbHome(destdb);
		if (null != foreigndb)
			XmergeConf.getInstance().setForeignInfoDbHome(foreigndb);
	}

	public static void main(String args[]) {
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
			else if (args[i].equals("-conf"))
				xmergexml = args[++i];
			else
				usage();
		}

		if (null == xmergexml)
			usage();
		Xmerge worker = new Xmerge(xmergexml);
		initialize();
		worker.run(checkOnly);
	}
}
