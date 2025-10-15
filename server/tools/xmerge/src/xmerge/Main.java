package xmerge;

public class Main {
	static String xmergexml = null;
	static String srcdb = null;
	static String destdb = null;
	static String foreigndb = null;
	static boolean checkOnly = false;

	private static void usage() {
		System.out.println("Usage: java -cp mylogic.jar -jar xmerge.jar -conf xmerge.xml  [options] ");
		System.out.println("-check[optional]    ֻ��Ⲣ��¼src_db_dir��dest_db_dir�д����ظ���key��Ϣ");
		System.out.println("-srcdb		src_db_dir    Դ���ݿ�·�����Ϸ������в��ᱻ�޸�");
		System.out.println("-destdb		dest_db_dir    Ŀ�����ݿ�·�����Ϸ������в��ᱻ�޸�");
		System.out.println("-dest_foreigndb[optional]	  dest_foreign_dir��Ŀ�����ݿ������֤�����ɵ����������Ϣ���ݿ��·�����Ϸ������в��ᱻ�޸�[��ѡ��]");
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
