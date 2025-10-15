package snail.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class Tool {
	/**
	 * 运行实现。
	 * 
	 * @param options
	 * @throws Exception
	 */
	public abstract void run(Options options) throws Exception;

	/**
	 * 构造工具选项。
	 * @param options
	 */
	public abstract void build(Options options);

	private static final boolean localAttachmentSupported;

	private static final boolean checkLocalAttachment() {
		try {
			Class.forName("com.sun.tools.attach.VirtualMachine");
			Class.forName("sun.management.ConnectorAddressLink");
			return true;
		} catch (NoClassDefFoundError x) {
		} catch (ClassNotFoundException x) {
		}
		return false;
	}

	private static final boolean addJarsToClasspath(File jdkHome) {
		File toolsJar = new File(new File(jdkHome, "lib"), "tools.jar");
		File jconsoleJar = new File(new File(jdkHome, "lib"), "jconsole.jar");
		if ( toolsJar.exists() && jconsoleJar.exists() ) {
			Classpath.addFile(toolsJar);
			Classpath.addFile(jconsoleJar);
			return true;
		}
		return false;
	}

	static {
		boolean supported = checkLocalAttachment();
		if ( !supported && Tool.isJdk() ) {
			// 尝试自动加入需要的jar。

			// JDKHOME 为安装目录。 tools.jar jconsole.jar 在 JDKHOME/lib 下。
			// java.home = <JDKHOME>/jre。可能以前的版本会直接指向 <JDKHOME>，未经确认。
			File javaHome = new File(System.getProperty("java.home"));
			if ( addJarsToClasspath(javaHome.getParentFile()) || addJarsToClasspath(javaHome) )
				supported = checkLocalAttachment();
		}
		localAttachmentSupported = supported;
		}

	/**
	 * attach 到本地进程是否可用。
	 */
	public static boolean isLocalAttachAvailable() {
		return localAttachmentSupported;
	}

	public static Tool newTool(String toolName)
		throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		@SuppressWarnings("unchecked")
		Class<Tool> toolCls = (Class<Tool>)Class.forName("snail.tools." + toUpper1(toolName));
		return toolCls.newInstance();
	}

	/**
	 * 把字符串首字母转换成大写的。
	 * @param name
	 * @return
	 */
	public static String toUpper1(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * 是否 JDK，非标准的检测方法。
	 * 
	 * @return true if current jvm is jdk, false for else.
	 */
	public static boolean isJdk() {
		String javaHome = System.getProperty("java.home");
		// Determine the JRE path by checking the existence of
		// <HOME>/jre/lib and <HOME>/lib.
		String jrePath = javaHome + File.separator + "jre";
		if (!new File(jrePath, "lib").exists()) {
			// java.home usually points to the JRE path
			jrePath = javaHome;
		}
		// <HOME>/jre exists which implies it's a JDK
		return jrePath.endsWith(File.separator + "jre");
	}

	/**
	 * 寻找JDK安装目录。读取环境变量 JAVA_HOME.
	 * @return JDKHOME if exist null for else.
	 */
	public static String findJdkHome() {
		String javaHome = System.getenv("JAVA_HOME");
		if (null == javaHome)
			return null;
		if (new File(javaHome, "jre" + File.separator + "lib" + File.separator + "rt.jar").exists())
			return javaHome;
		return null;
	}

	static class Redirect extends Thread {
		final InputStream is;
		final OutputStream os;

		Redirect(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
			this.setDaemon(true);
		}

		public void run() {
			byte [] b = new byte[1024];
			try {
				int len = 0;
				while ((len = is.read(b)) > 0)
					os.write(b, 0, len);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * never return
	 * 
	 * @param mainClass
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void exec(String mainClass, String args[]) {
		try {
			if ( Tool.isJdk() ) {
				System.out.println("exec : is already run in JDK!");
				System.exit(1);
			}

			String jdkHome = Tool.findJdkHome();
			if ( null == jdkHome ) {
				System.out.println("exec : JDKHOME not found!");
				System.exit(2);
			}

			List<String> cmdlist = new ArrayList<String>();
			cmdlist.add(new File(jdkHome, "bin/java").toString());
			cmdlist.add("-cp");
			cmdlist.add(System.getProperty("java.class.path"));
			cmdlist.add(mainClass);
			for (String arg : args)
				cmdlist.add(arg);

			Process p = Runtime.getRuntime().exec(cmdlist.toArray(new String[cmdlist.size()]));
			new Redirect(p.getErrorStream(), System.err).start();
			new Redirect(p.getInputStream(), System.out).start();
			try {
				p.waitFor();
			} catch ( InterruptedException x ) {
				p.destroy();
				x.printStackTrace();
				System.exit(3);
			}
		} catch (Throwable x) {
			x.printStackTrace();
			System.exit(4);
		}
		System.exit(0);
	}

	public static char[] spaceN(int n, String s) {
		n = n - (null == s? 0 : s.length());
		if (n < 1) n = 1;
		return spaceN(n);
	}

	public static char[] spaceN(int n) {
		char spaces[] = new char[n];
		java.util.Arrays.fill(spaces, ' ');
		return spaces;
	}

	public static String format(int n, int i) {
		String s = String.valueOf(i);
		return s + String.valueOf(spaceN(n, s));
	}
}
