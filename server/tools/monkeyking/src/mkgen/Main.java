package mkgen;

import java.io.File;
import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Main {
	private static Mkdb mkdb;
	public static String outputEncoding = "GBK";
	private static boolean nowarn = false;
	private static String warn = "cfo";
	static boolean noverify = false;

	// �����ʾ�����
	static boolean explicitLockCheck = false;
	static Set<String> explicitLockIgnoreTables = new TreeSet<String>();

	// --add for tranform.
	private static boolean transform = false;
	private static String srcdb = null;
	private static String destdb = null;
	// --add for transform check.
	private static boolean transformCheck = false;

	private static void usage() {
		System.out.println("Usage: java -jar mkgen.jar [options] mkdb.xml");
		System.out.println("	-outputEncoding  encoding.");
		System.out.println("	-output          output dir.");
		System.out.println("	-nowarn          do not print warning message.");
		System.out.println("	-warn [cfo]      select warning message. c=capacity, f=foreign, o=owner.");
		System.out.println("	-noverify        do not generate xbean verify code.");
		System.out.println("	-transform       for Tranform db");
		System.out.println("	-srcdb           tranform src db Dir. ");
		System.out.println("	-destdb          tranform dest db Dir. ");
		System.out.println("	-transformCheck  for check tranform is need to transform or not");
		System.out.println("	-explicitLockCheck [tale1:table2]  ���ɼ����ʾ������Ĵ���, ");
		System.out.println("	                 ���У�[]��ð�ŷָ����Ǻ���explicitLockCheck�ı�");
		System.out
				.println("	                       ������{add(fail),get,delete,remove} ������{add(success),insert,select}");
		Runtime.getRuntime().exit(1);
	}

	public static void _xdb_verify_(PrintStream ps, String prefix) {
		if (!noverify)
			ps.println(prefix + "_xdb_verify_unsafe_();");
	}

	public static void main(String args[]) throws Exception {
		System.setProperty("line.separator", "\n");
		String xdbxml = null;
		String mkgenOutput = null;

		for (int i = 0; i < args.length; ++i) {
			if (args[i].equals("-outputEncoding"))
				outputEncoding = args[++i];
			else if (args[i].equals("-output"))
				mkgenOutput = args[++i];
			else if (args[i].equals("-nowarn"))
				nowarn = true;
			else if (args[i].equals("-warn")) {
				if ((i + 1 < args.length) && !args[i + 1].startsWith("-"))
					warn = args[++i].toLowerCase();
				else
					warn = ""; // select nothing
			} else if (args[i].equals("-noverify"))
				noverify = true;
			else if (args[i].equals("-transform"))
				transform = true;
			else if (args[i].equals("-transformCheck"))
				transformCheck = true;
			else if (args[i].equals("-srcdb"))
				srcdb = args[++i];
			else if (args[i].equals("-destdb"))
				destdb = args[++i];
			else if (args[i].equals("-explicitLockCheck")) {
				explicitLockCheck = true;
				if ((i + 1 < args.length) && args[i + 1].startsWith("[")) {
					++i;
					if (!args[i].startsWith("[") || !args[i].endsWith("]"))
						usage();
					String[] ignoreTables = args[i].substring(1, args[i].length() - 1).split(":");
					for (String ignoreTable : ignoreTables) {
						if (ignoreTable.equals(""))
							continue;
						explicitLockIgnoreTables.add(ignoreTable);
					}
				}
			} else if (xdbxml == null)
				xdbxml = args[i];
			else
				usage();
		}

		if (null == xdbxml)
			usage();

		if (transform == true) {
			if (srcdb == null) {
				System.out.println("-srcdb need to been set!");
				usage();
			}
			if (destdb == null) {
				System.out.println("-destdb need to been set!");
				usage();
			}
		}

		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xdbxml);
		Element root = doc.getDocumentElement();
		if (null != mkgenOutput)
			root.setAttribute("mkgenOutput", mkgenOutput);
		mkdb = new Mkdb(root);
		mkdb.compile();
		if (!transform && !transformCheck) {
			mkdb.make();
		} else {
			// mkdb.diffMetaData();
			mkdb.transformMake();
		}
		mkdb.clean();
	}

	static void warn(String msg, char type) {
		if (!nowarn && warn.indexOf(type) > -1)
			System.err.println("WARN " + msg);
	}

	static PrintStream openBeanFile(String classname) {
		File dir = mkdb.getXbeandir();
		return fopen(dir, classname);
	}

	static PrintStream openBean__File(String classname) {
		File dir = mkdb.getXbeandir__();
		return fopen(dir, classname);
	}

	static PrintStream openTableFile(String classname) {
		File dir = mkdb.getXtabledir();
		return fopen(dir, classname);
	}

	static PrintStream fopen(File dir, String classname) {
		try {
			File file = new File(dir, classname + ".java");
			// System.out.println(file);
			return new PrintStream(new CachedFileOutputStream(file), false, outputEncoding);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static String toUpper1(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public static String quote(String s) {
		return "\"" + s + "\"";
	}

	public static void verifyName(String name) {
		if (name.startsWith("_") && name.endsWith("_"))
			throw new RuntimeException("Invalid Name of '" + name + "'. the name like '_*_' is reserved");
	}

	static PrintStream openTransformMainFile(String classname) {
		File dir = mkdb.getXgenOutputdir();
		return fopen(dir, classname);
	}

	static String getTransformSrcDb() {
		return srcdb;
	}

	static String getTransformDestDb() {
		return destdb;
	}

	static void doTransformCheck(boolean isNeedTransform) {
		final int NeedTransform = 100;
		final int NotNeedTransform = 101;
		if (transformCheck) {
			if (isNeedTransform) {
				System.exit(NeedTransform);// ��ʱ����1������Ҫtransform
			} else {
				System.exit(NotNeedTransform);
			}
		}
	}

	// �ڹ���owner�Ĺ����м�鲢���档see Type.addOwnerTable Mkdb.compile
//	public static void checkAddTypeOwnerTable(Type type, Set<Table> owners, Table newOwner) {
//		/**
//		 * ��û���κ� owner��
//		 */
//		if (owners.isEmpty())
//			return;
//
//		/**
//		 * ���ݽṹ����ʱ�������ΰ���ͬһ�����ݽṹ.
//		 * �ٴμ���ͬһ��owner������ġ�
//		 */
//		if (owners.contains(newOwner))
//			return;
//
//		/**
//		 * ֻ�����Զ��Զ���ṹ�����Ի������͵ľ��棬��Ϊ��
//		 * 1 ����ֱ�ӱ� table ʹ�õĻ�������������ӵ���ߣ��������������磬int��long���ȡ�
//		 * 2 ������ table ֱ��ʹ�õĻ������ͣ��϶��������Զ���ṹ�У����ظ����档�磬set,list���ȡ�
//		 */
//		if (!(type instanceof CBean) && !(type instanceof XBean))
//			return;
//
//		warn("OWNER bean=" + Main.quote(type.getName()) + " current=" + owners
//				+ " add=" + Main.quote(newOwner.getName()));
//	}
}
