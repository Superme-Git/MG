package snail.tools;

public class Main {
	public static void printUsageAndExit() {
		System.out.println("java -jar tools.jar [main-options] command [command-options]");
		System.out.println("    -snail dir     : directory to find monkeyking.jar and jio.jar.");
		System.out.println("                     default : same directory of tools.jar");
		System.out.println("    help [command] : for help on a specific command");
		System.out.println("    ps             : show java application");
		System.out.println("    domains        : show JMX domains");
		System.out.println("    mbeans         : show mbeans by domain");
		System.out.println("    attrs          : show attrs for a mbean");
		System.out.println("    kill           : kill a mkdb application");
		System.out.println("    killall        : killall mkdb application");
		System.out.println("    deadlocked     : detect deadlocked");
		System.out.println("    mkcap           : show mkdb meta data");
		System.out.println("    top            : top for mkdb application");
		//System.out.println("    merge          : merge mkdb table");
		//System.out.println("    walk           : walk mkdb table");
		//System.out.println("    browse         : browse mkdb table");
		System.exit(1);
	}

	public static void main(final String args[]) throws Exception {
		if (args.length < 1)
			printUsageAndExit();

		// parse main options
		int commandOptIndex = -1;
		String snailDir = null;
		String command = null;
		for (int i = 0; i < args.length; ++i) {
			if (false == args[i].startsWith("-")) {
				command = args[i];
				commandOptIndex = i + 1;
				break;
			}
			if (args[i].equals("-snail")) {
				if (++i >= args.length)
					printUsageAndExit();
				snailDir = args[i];
			} else
				printUsageAndExit();
		}

		if (null == command)
			printUsageAndExit();

		// load mkdb.jar and jio.jar
		if (null != snailDir) {
			Classpath.addFile(new java.io.File(snailDir, "monkeyking.jar"));
			Classpath.addFile(new java.io.File(snailDir, "jio.jar"));
		} else {
			Classpath.addFile(Main.class, "monkeyking.jar");
			Classpath.addFile(Main.class, "jio.jar");
		}

		String toolName = null;
		boolean isHelp = false;
		if (command.equals("help")) {
			isHelp = true;
			if (args.length != commandOptIndex + 1)
				printUsageAndExit();
			toolName = args[commandOptIndex];
		} else
			toolName = command;

		Tool tool = null;
		try {
			tool = Tool.newTool(toolName);
		} catch (NoClassDefFoundError x) {
			System.out.println(x + " -- try Tool.exec ...");
			Tool.exec("snail.tools.Main", args);
		} catch (Exception x) {
			System.out.println(x);
			printUsageAndExit();
		}
		Options options = new Options();
		tool.build(options);

		if (isHelp) {
			System.out.println("java -jar tools.jar " + toolName + " [options]");
			System.out.println(options);
			return;
		}

		try {
			options.parse(args, commandOptIndex); // 第一个 arg 总是command，已经被处理了。
		} catch (Exception x) {
			System.out.println(x.getMessage());
			System.out.println("java -jar calccap.jar " + toolName + " [options]");
			System.out.println(options);
			return;
		}

		try {
			tool.run(options);
		} catch (NoClassDefFoundError x) {
			// 尝试重新运行。 ugly. 仅仅为了工具好用点，不管兼容性和易维护性了。
			System.out.println(x + " -- try Tool.exec ...");
			Tool.exec("snail.tools.Main", args);
		} catch (LocalAttachNotAvailable x) {
			// 尝试重新运行。 ugly. 仅仅为了工具好用点，不管兼容性和易维护性了。
			System.out.println(x + " -- try Tool.exec ...");
			Tool.exec("snail.tools.Main", args);
		}
	}
}
