package robot;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import mkdb.Executor;
import mkio.ILoginIns;
import mkio.JAuthc;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import robot.task.Task_RoleThread;

public class Main implements Runnable {

	private static CloseableHttpAsyncClient httpclient;

	// 统计在线人数的次数
	public static long total = 0;

	public static CloseableHttpAsyncClient getHttpClient() {
		if (httpclient == null) {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
			httpclient = HttpAsyncClients.custom().setDefaultRequestConfig(requestConfig).build();
		}
		return httpclient;
	}

	static private void parseArg(String arg) {
		final String[] ws = arg.split("=");
		if (2 != ws.length)
			return;
		if (0 == ws[0].compareToIgnoreCase("trace.level")) {
			mkdb.Trace.set(mkdb.Trace.valueOf(ws[1]));
		} else if (0 == ws[0].compareToIgnoreCase("trace.file")) {
			File logfile = new File(ws[1]);
			mkdb.Trace.openNew(logfile, ":file", -1, -1);
		}
	}

	static private void usage() {
		System.out.println("启动参数及含义：服务器LinkIP " + "服务器Link端口" + "(例：10000或者10000-10004) " + "起始账号(只能填数组，例如：1，实际是user1) "
				+ "登录账号个数 账号前缀(2~3个字母，例如：sb)");
	}

	static public void main(String args[]) throws Exception {
		if (args.length < 4)
			usage();
		mkdb.Trace.set(mkdb.Trace.INFO);
		for (int i = 4; i < args.length; i++)
			parseArg(args[i]);
		mkdb.Trace.openIf();
		serverip = args[0];
		serverport = args[1];
		int startindex = Integer.parseInt(args[2]);
		int count = Integer.parseInt(args[3]);
		setCount(count);
		String prename = args[4];
		if (args.length >= 6) {
			// 带有下划线的代表测试所有的存储过程
			if (args[5].contains("_")) {
				String[] data = args[5].split("_");
				SetTaskType(Integer.parseInt(data[0]));
				setProtocolsType(Integer.parseInt(data[1]));
				if (data[1].equals("1"))
					isUniqName = true;
			} else {
				SetTaskType(Integer.parseInt(args[5]));
			}
		}
		if (args.length >= 7) {
			delaytime = Integer.parseInt(args[6]);
		}
		Task_RoleThread.getInstance().start();
		Stat.getInstance();
		if (Main.isUniqName) {
			ConfigMgr.getInstance().initMkdb();
		}
		Executor.start(0, 10, 1, 20, 2000);
		JAuthc.start("robot.xio.xml", ".");
		try {
			ConfigMgr.getInstance().init();
			getHttpClient();
			httpclient.start();
			run(serverip, serverport, startindex, count, prename);
			while (true) {
				Thread.sleep(30 * 60 * 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// jmxserver.stop();
			Task_RoleThread.getInstance().shutdown();
			JAuthc.close();
			Executor.stop();
			mkdb.Trace.close();
		}
	}

	static Main m = null;

	final private ArrayList<LoginUI> loginuis = new ArrayList<LoginUI>();

	private int currentindex = 0;

	static private int delaytime = 30;

	static private final int PRE_USER_COUNT = 50;

	static private final int PRE_PERIO_VALUE = 3;

	static private int tasktype = -1;

	static private int count = 100;

	static public boolean isUniqName = false;

	static public String serverip;

	static public String serverport;

	// 协议类型
	static private int protocolsType = 0;

	public static int getProtocolsType() {
		return protocolsType;
	}

	public static void setProtocolsType(int protocolsType) {
		Main.protocolsType = protocolsType;
	}

	public static int getCount() {
		return count;
	}

	public static void setCount(int count) {
		Main.count = count;
	}

	static public void SetTaskType(int tasktype) {
		Main.tasktype = tasktype;
	}

	static public int GetTaskType() {
		return tasktype;
	}

	static private interface GetPort {

		int getPort();
	}

	static private class SingleGetPort implements GetPort {

		private int port;

		public SingleGetPort(String port) {
			this.port = Integer.parseInt(port);
		}

		@Override
		public int getPort() {
			return port;
		}
	}

	static private class RandGetPort implements GetPort {

		private int ports;

		private int porte;

		public RandGetPort(String start, String end) {
			ports = Integer.parseInt(start);
			porte = Integer.parseInt(end);
		}

		@Override
		public int getPort() {
			return RandValue.randRanger(ports, porte);
		}
	}

	static private GetPort makeGetPort(String port) {
		String v[] = port.split("-");
		switch (v.length) {
		case 1:
			return new SingleGetPort(v[0]);
		case 2:
			return new RandGetPort(v[0], v[1]);
		default:
			throw new RuntimeException("bad port arg : " + port);
		}
	}

	static private void run(String host, String port, int start, int count, String fixname) {
		GetPort getport = makeGetPort(port);
		m = new Main();
		m.loginuis.ensureCapacity(count);
		ILoginIns.Param param = new ILoginIns.Param();
		param.host = host;
		param.password = "123456";
		param.iskickuser = true;
		param.logintype = 1;
		for (int i = 0; i < count; i++) {
			int index = start + i;
			param.username = fixname + index;
			param.port = Integer.toString(getport.getPort());
			LoginUI ui = new LoginUI(index);
			ui.setLoginInstance(JAuthc.getLoginManager().newLogin(param, ui));
			m.loginuis.add(ui);
		}
		mkdb.Executor.getInstance().schedule(m, 1, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		int hascount = loginuis.size();
		int runcount = currentindex + PRE_USER_COUNT;
		if (runcount > hascount)
			runcount = hascount;
		for (int i = currentindex; i < runcount; i++) {
			loginuis.get(i).start();
		}
		currentindex = runcount;
		mkdb.Trace.info("CurLoginIndex:" + currentindex);
		if (currentindex < hascount)
			mkdb.Executor.getInstance().schedule(this, PRE_PERIO_VALUE, TimeUnit.SECONDS);
		if (currentindex >= hascount) {
			mkdb.Trace.info("Finish Login.Robots Num:" + loginuis.size());
			int delayTime = delaytime;
			mkdb.Executor.getInstance().scheduleWithFixedDelay(new Runnable() {

				@Override
				public void run() {
					total++;
					int roles_num = LoginRoleMgr.getInstance().getRoles().size();
					mkdb.Trace.error("====================当前在线人数:" + roles_num + "==================================");
					if (ConfigMgr.getInstance().isStartHY)
						// 间隔下线上线
						OnlineAndOfflineUtils.allOnlineAndOffline(roles_num, serverip, serverport, currentindex);
				}
			}, delayTime + 10, 10, TimeUnit.SECONDS);
		}
	}
}
