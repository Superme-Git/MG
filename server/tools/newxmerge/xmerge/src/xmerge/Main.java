package xmerge;

import java.io.File;

public class Main {
	static String xmergexml = null;
	static String srcdb = null;
	static String destdb = null;
	static String resultdb = null;
	static String foreigndb = null;
	static boolean checkOnly = false;
	private static String backupDir1 = "src1dbbackup";//dest���ݿ��ǲ��ᱻ�޸ĵ�,ֻҪ����srcdb������
	
	private static void usage() {
		System.out.println("Usage: java -cp mylogic.jar -jar xmerge.jar -conf xmerge.xml  [options] ");
		System.out.println("-check[optional]    ֻ��Ⲣ��¼src_db_dir��dest_db_dir�д����ظ���key��Ϣ");
		System.out.println("-srcdb		src_db_dir    Դ���ݿ�·�����Ϸ������в��ᱻ�޸�");
		System.out.println("-destdb		dest_db_dir    Ŀ�����ݿ�·�����Ϸ������в��ᱻ�޸�");
		System.out.println("-resultdb		result_db_dir    Ŀ�����ݿ�·�����Ϸ������в��ᱻ�޸�");
		System.out.println("-dest_foreigndb[optional]	  dest_foreign_dir��Ŀ�����ݿ������֤�����ɵ����������Ϣ���ݿ��·�����Ϸ������в��ᱻ�޸�[��ѡ��]");
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
		
		//�����������ݿⲢ�ñ��ݵ����ݿ���кϷ�,������ʹ�Ϸ�ʧ��,ԭ��������Ҳ������
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
		System.out.println("����ʱ��\t"+(time2/1000)+"\t��");
		
	}
}
