package robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import mkio.ILoginIns;
import mkio.JAuthc;
import robot.clan.ClanMgr;
import fire.pb.CReturnToLogin;

/**
 * 上下线处理
 * 
 * @author yangtao
 * @dateTime 2016年6月29日 下午9:57:37
 * @version 1.0
 */
public class OnlineAndOfflineUtils {

	static long offlinelastTime = System.currentTimeMillis();

	static long onlinelastTime = System.currentTimeMillis();

	// 秒
	static long jianGeTime = 30000;

	// 秒
	static long newUserLoginJianGeTime = jianGeTime + 10000;

	// 下线比例
	static int offlineRate = 2;

	// 真正下线数量
	static int offlineNum = 0;

	// 新用户登录比例
	static int onlineRate = 1;

	// 重连时间（秒）
	static int reconnectTime = 60;

	// 新用户登录数量
	static int onlineNum = offlineNum + onlineRate;

	// 当前索引值
	static int currentindex = 0;

	static boolean isStart = false;

	static Map<LoginRole, String> reConnectRoleMap = new HashMap<>();
	static {
		Properties everydayhyProp = ConfigMgr.getInstance().getPropConf("everydayhy");
		jianGeTime = FireProp.getIntValue(everydayhyProp, "jianGeTime") * 1000;
		newUserLoginJianGeTime = FireProp.getIntValue(everydayhyProp, "newUserLoginJianGeTime") * 1000;
		offlineRate = FireProp.getIntValue(everydayhyProp, "offlineRate");
		onlineRate = FireProp.getIntValue(everydayhyProp, "onlineRate");
		reconnectTime = FireProp.getIntValue(everydayhyProp, "reconnectTime");
	}

	/**
	 * 老用户上下线和新用戶上线
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月29日 下午10:35:42
	 * @version 1.0
	 * @param role_num
	 * @param serverip
	 * @param serverport
	 */
	public static void allOnlineAndOffline(int role_num, String serverip, String serverport, int index) {
		if (currentindex == 0) {
			currentindex = index;
			isStart = true;
		}
		onlineAndOfflineSchedule(role_num);
		newUserOnline(serverip, serverport);
	}

	/**
	 * 老用户上下线
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月29日 下午10:36:32
	 * @version 1.0
	 * @param roles_num
	 */
	private static void onlineAndOfflineSchedule(int roles_num) {
		if (System.currentTimeMillis() - offlinelastTime < jianGeTime) {
			return;
		}
		offlinelastTime = System.currentTimeMillis();
		mkdb.Executor.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				onlineAndOffline(roles_num);
			}
		}, 1, TimeUnit.SECONDS);
	}

	/**
	 * 老用户上下线
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月29日 下午10:36:43
	 * @version 1.0
	 * @param role_num
	 */
	public static void onlineAndOffline(int role_num) {
		offlineNum = 0;
		Map<Long, LoginRole> roleMap = LoginRoleMgr.getInstance().getRoles();
		int i = 0;
		for (Map.Entry<Long, LoginRole> entry : roleMap.entrySet()) {
			LoginRole role = entry.getValue();
			CReturnToLogin cReturnToLogin = new CReturnToLogin();
			role.sendProtocol(cReturnToLogin);
			// 一定几率上线
			double random = Math.random();
			boolean reconnect = false;
			if (random <= 0.9) {
//				System.out.println("role:" + role.userId);
				reConnectRoleMap.put(role, "");
				reconnect = true;
			}
			i++;
			if (!reconnect) {
				offlineNum++;
			}
			if (i >= role_num / offlineRate)
				break;
		}
		System.out.println("i:" + i + "--offlineNum:" + offlineNum);
		onlineNum = offlineNum + onlineRate;
	}

	/**
	 * 新用戶是否能上线
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月29日 下午10:36:50
	 * @version 1.0
	 * @return
	 */
	private static boolean isNewUserOnline() {
		if (System.currentTimeMillis() - onlinelastTime < newUserLoginJianGeTime) {
			return false;
		}
		onlinelastTime = System.currentTimeMillis();
		return true;
	}

	/**
	 * 新用户上线
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月29日 下午10:26:20
	 * @version 1.0
	 */
	private static void newUserOnline(String serverip, String serverport) {
		if (isNewUserOnline()) {
			String name = ClanMgr.getInstance().getRandomName(8);
			mkdb.Executor.getInstance().schedule(new Runnable() {

				@Override
				public void run() {
					System.out.println("newUserOnline:" + onlineNum);
					userOnline(serverip, serverport, currentindex + 1, onlineNum, name);
				}
			}, 1, TimeUnit.SECONDS);
		}
	}

	/**
	 * 新用户上线
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月29日 下午10:11:35
	 * @version 1.0
	 * @param start
	 * @param count
	 * @param fixname
	 */
	private static void userOnline(String serverip, String serverport, int start, int count, String fixname) {
		if (currentindex >= Integer.MAX_VALUE)
			currentindex = 1;
		ArrayList<LoginUI> newloginuis = new ArrayList<LoginUI>();
		GetPort getport = makeGetPort(serverport);
		newloginuis.ensureCapacity(count);
		ILoginIns.Param param = new ILoginIns.Param();
		param.host = serverip;
		param.password = "123456";
		param.iskickuser = true;
		param.logintype = 1;
		for (int i = 0; i < count; i++) {
			int index = start + i;
			param.username = fixname + index;
			param.port = Integer.toString(getport.getPort());
			LoginUI ui = new LoginUI(index);
			ui.setLoginInstance(JAuthc.getLoginManager().newLogin(param, ui));
			newloginuis.add(ui);
			System.out.println("fixname:" + fixname + index);
		}
		currentindex += count;
		for (int i = 0; i < count; i++) {
			newloginuis.get(i).start();
		}
		mkdb.Trace.info("new User Finish Login.Robots Num:" + newloginuis.size());
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

	/**
	 * 下线
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月30日 下午3:51:22
	 * @version 1.0
	 * @param role
	 */
	public static void disconnected(LoginRole role) {
		if (isStart)
			if (role != null && role.loginui != null) {
				// 下线
				role.loginui.onDisconnected();
				if (reConnectRoleMap.containsKey(role)) {
					role.loginui.reconnect(reconnectTime);
					reConnectRoleMap.remove(role);
				}
			}
	}
}
