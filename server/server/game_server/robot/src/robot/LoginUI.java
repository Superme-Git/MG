package robot;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import mkdb.Mkdb;
import mkio.ILoginIns;
import mkio.ILoginUI;
import mkio.JAuthc;
import mkio.Manager;
import mkio.Mkio;
import mkio.Protocol;
import robot.Stat.Status;
import robot.task.ArenaFight;
import robot.task.BingFengTask;
import robot.task.BuyAndSell;
import robot.task.ChatItemTask;
import robot.task.CircleTask;
import robot.task.ClanCopyTask;
import robot.task.ClanFightTask;
import robot.task.ClanTask;
import robot.task.FriendZone;
import robot.task.Multiple;
import robot.task.NewMove;
import robot.task.OnlineOffline;
import robot.task.RedPacketTask;
import robot.task.Ride;
import robot.task.SetColor;
import robot.task.ShiLianTask;
import robot.task.SpotCard;
import robot.task.TeamFightTask;
import robot.task.TeamMove;
import robot.task.TeamTask;
import robot.task.TestTask;
import robot.task.TreasureMapTask;
import robot.task.ZhuXian;
import robot.task.ZhuoGuiTask;
import fire.pb.CNotifyDeviceInfo;
import fire.pb.CRoleList;
import fire.pb.UserInfoUnit;
import fire.pb.item.BagTypes;

public class LoginUI implements ILoginUI {

	public static ConcurrentHashMap<Integer, Integer> logintime = new ConcurrentHashMap<Integer, Integer>();// 存放机器人掉线次数，用来计算重新连接的时间

	private final int index;

	private ILoginIns instance = null;

	public Stat.Status status = Stat.Status.ST_NULL;

	int userid = -1;

	public long lastTime = System.currentTimeMillis();

	public boolean isCanRobot = false;

	int addFuShiCount = 0;

	public int getUserid() {
		return this.userid;
	}

	private final Object mutex = new Object();

	LoginRole loginrole = null;

	public static final int delconnectintime = 90;

	public synchronized LoginRole getLoginRole() {
		return this.loginrole;
	}

	public void setLoginRole(final LoginRole loginrole) {
		this.loginrole = loginrole;
	}

	public String getUserName() {
		return this.getLoginInstance().getParam().username;
	}

	public LoginUI(final int index) {
		this.index = index;
	}

	public final void setLoginInstance(final ILoginIns ins) {
		this.instance = ins;
	}

	public final ILoginIns getLoginInstance() {
		return this.instance;
	}

	public final int getIndex() {
		return this.index;
	}

	public final void start() {
		this.instance.start();
	}

	public final Object getMutex() {
		return this.mutex;
	}

	@Override
	public void onAuthHandSet(final int num) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onAuthMatrixCard(final int[] x, final int[] y) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void onAuthOk(final int userid) {
		if (mkdb.Trace.isDebugEnabled()) {
			mkdb.Trace.debug("user " + this.index + " onAuthOk userid = " + userid);
		}
		// 发送请求角色列表
		this.instance.send(new CRoleList());
		this.status = Stat.getInstance().userAuthcOK();
		this.userid = userid;
	}

	/**
	 * 设备数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月29日 下午4:37:09
	 * @version 1.0
	 */
	private void sendDeviceInfo() {
		java.util.ArrayList<fire.pb.UserInfoUnit> infoList = new java.util.ArrayList<fire.pb.UserInfoUnit>();
		UserInfoUnit userInfoUnit = new UserInfoUnit();
		userInfoUnit.key = "PlatType";
		userInfoUnit.value = "6";
		infoList.add(userInfoUnit);
		userInfoUnit = new UserInfoUnit();
		userInfoUnit.key = "DeviId";
		userInfoUnit.value = "e3052346";
		infoList.add(userInfoUnit);
		userInfoUnit = new UserInfoUnit();
		userInfoUnit.key = "ChId";
		userInfoUnit.value = "ChId";
		infoList.add(userInfoUnit);
		CNotifyDeviceInfo cNotifyDeviceInfo = new CNotifyDeviceInfo(infoList);
		this.instance.send(cNotifyDeviceInfo);
	}

	public void reconnect() {
		mkdb.Executor.getInstance().schedule(new ReconnectTask(this.instance.getParam(), this.index), 20, TimeUnit.SECONDS);
	}

	public void reconnect(int delay) {
		mkdb.Executor.getInstance().schedule(new ReconnectTask(this.instance.getParam(), this.index), delay, TimeUnit.SECONDS);
	}

	static private class ReconnectTask implements Runnable {

		private final ILoginIns.Param param;

		private final int index;

		public ReconnectTask(final ILoginIns.Param param, final int index) {
			this.param = param;
			this.index = index;
		}

		@Override
		public void run() {
			final LoginUI ui = new LoginUI(this.index);
			ui.setLoginInstance(JAuthc.getLoginManager().newLogin(this.param, ui));
			ui.start();
		}
	}

	@Override
	public void onAuthError(final Action a, final int e, final String detail) {
		synchronized (this) {
			if (mkdb.Trace.isErrorEnabled()) {
				mkdb.Trace.error("user " + this.index + " onAuthError Action = " + a + " e = " + e + " detail = " + detail);
			}
			if (this.status == Status.ST_NULL) {
				return;
			}
			this.onDisconnected();
			// // 网关踢人禁止再次登录
			// if (e == 20) {
			// mkdb.Trace.error("user " + this.index + "网关踢人禁止再次登录");
			// return;
			// }
			// 停止排队中的定时器
			QueueUtils.getInstance().stopScheduledFuture(this.instance);
			this.instance.close();
			// 这里修改一下掉线重连时间10秒为60秒 by hzl
			LoginUI.addDisConnectNum(this.index);
			// mkdb.Executor.getInstance().schedule(
			// new ReconnectTask(instance.getParam(), index),
			// getDisConnectTime(index), TimeUnit.SECONDS);
			mkdb.Executor.getInstance().schedule(new ReconnectTask(this.instance.getParam(), this.index), 30, TimeUnit.SECONDS);
			Stat.getInstance().userAuthcError();
		}
	}

	@Override
	public void onAnnounceForbidInfo(final char type, final int time, final int createtime, final String reason) {
		if (mkdb.Trace.isWarnEnabled()) {
			mkdb.Trace.warn("user " + this.index + " onAnnounceForbidInfo type = " + type + " time = " + time + " createtime = "
					+ createtime + " reason = " + reason);
		}
		Stat.getInstance().userAuthcForbid();
	}

	@Override
	public void onInstantAddCashRep(final int retcode, final int userid, final int reserved) {
	}

	@Override
	public void dispatch(final Manager manager, final Mkio connection, final Protocol p) {
		p.setContext(this.instance);
		Mkdb.executor().execute(p);
	}

	/**
	 * 添加掉线次数
	 *
	 * @param index
	 */
	public static void addDisConnectNum(final int index) {
		final Integer num = LoginUI.logintime.get(index);
		if (num == null) {
			LoginUI.logintime.put(index, 1);
		} else {
			LoginUI.logintime.put(index, num + 1);
		}
	}

	/**
	 * 获得断线重连时间
	 *
	 * @param index
	 * @return
	 */
	public static int getDisConnectTime(final int index) {
		final Integer num = LoginUI.logintime.get(index);
		if (num == null) {
			return LoginUI.delconnectintime;
		} else {
			int value = LoginUI.delconnectintime * num;
			if (value < LoginUI.delconnectintime) {
				value = LoginUI.delconnectintime;
			}
			return value;
		}
	}

	public final synchronized void onDisconnected() {
		if (this.loginrole != null) {
			// 停止排队中的定时器
			QueueUtils.getInstance().stopScheduledFuture(instance);
			this.loginrole.onDisconnected();
			this.loginrole = null;
		}
		this.status = Stat.getInstance().userDisconnect(this.status);
	}

	public final synchronized void onRoleEnterWorld(final ILoginIns ins, final fire.pb.RoleDetail detail) {
		// 发送设备数据
		sendDeviceInfo();
		// 停止排队中的定时器
		QueueUtils.getInstance().stopScheduledFuture(ins);
		this.status = Stat.getInstance().roleEnterWorld();
		this.loginrole = new LoginRole(detail.roleid, this.userid, detail.baginfo.get(BagTypes.BAG), detail.baginfo, detail.pets);
		this.loginrole.setLoginui(this);
		this.loginrole.setLevel(detail.level);
		this.loginrole.school = detail.school;// 设置职业
		this.loginrole.rolename = detail.rolename;// 设置名字
		LoginRoleMgr.getInstance().addLoginRole(this.loginrole);
		final StringBuilder sb = new StringBuilder();
		sb.append("roleid=").append(detail.roleid).append("\tusername=").append(this.getUserName()).append("\tuserid=")
				.append(this.userid).append("==========登陆成功!!!");
		mkdb.Trace.error(sb.toString());
		if (this.loginrole.getRoleTask() == null) {
			// -2 代表没有也不做，主要是针对只创建角色
			if (Main.GetTaskType() == -2) {
				return;
			}
			// 外网战斗机器人加符石
			if (Main.GetTaskType() == 27 || Main.GetTaskType() == 28) {
				// 外网加符石
				this.loginrole.sendCommand("//addfushi 1 300");
			} else {
				// 内网加符石
				this.loginrole.sendCommand("//addfushi 1 100000");
			}
			// if (!isCanRobot) {
			// if (addFuShiCount < 3) {
			// // 加符石
			// this.loginrole.sendCommand("//addfushi 1 100000");
			// }
			// addFuShiCount++;
			// return;
			// }
			// 若未设定任务类型 随机挑选一个 [摆摊测试占用率过高,暂时不放在随机任务里头]
			if (Main.GetTaskType() == -1) {
				// 最终区间为[1-11],Math.random()取不到1 yebin added
				Main.SetTaskType((int) ((Math.random() * 11) + 1));
			}
			if ((Main.GetTaskType() != 18) && (Main.GetTaskType() != 22)) {
				// mkdb.Trace.error("**********创建公会:");
				// new ClanTask(loginrole).createClan();
			}
			sb.setLength(0);
			sb.append("**********开始任务:" + Main.GetTaskType());
			switch (Main.GetTaskType()) {
			case 1: // 随机走动
				this.loginrole.startRoleTask(new NewMove(this.loginrole));
				sb.append("随机走动");
				break;
			case 2: // 主线任务
				this.loginrole.startRoleTask(new ZhuXian(this.loginrole));
				sb.append("主线任务");
				break;
			case 3: // 组队走动
				this.loginrole.startRoleTask(new TeamMove(this.loginrole));
				sb.append("组队走动");
				break;
			case 4: // 上下线测试
				this.loginrole.startRoleTask(new OnlineOffline(this.loginrole));
				sb.append("上下线测试");
				break;
			case 5: // 不组队打怪
				this.loginrole.startRoleTask(new Multiple(this.loginrole));
				sb.append("不组队打怪");
				break;
			case 6: // 染色测试
				this.loginrole.startRoleTask(new SetColor(this.loginrole));
				sb.append("染色测试");
				break;
			case 7: // 循环任务
				this.loginrole.startRoleTask(new CircleTask(this.loginrole));
				sb.append("循环任务");
				break;
			case 8: // 组队(不走路)
				this.loginrole.startRoleTask(new TeamTask(this.loginrole));
				sb.append("组队(不走路)");
				break;
			case 9: // 组队打怪
				this.loginrole.startRoleTask(new TeamFightTask(this.loginrole));
				sb.append("组队打怪");
				break;
			case 10: // 单人PVP
				this.loginrole.startRoleTask(new ArenaFight(this.loginrole, ArenaFight.ARENA_TYPE_SINGLE));
				sb.append("单人PVP");
				break;
			case 11: // PVP3
				this.loginrole.startRoleTask(new ArenaFight(this.loginrole, ArenaFight.ARENA_TYPE_TEAM_3));
				sb.append("PVP3");
				break;
			case 12: // 摆摊测试
				this.loginrole.startRoleTask(new BuyAndSell(this.loginrole));
				sb.append("摆摊测试");
				break;
			case 13: // 语音聊天测试
				this.loginrole.startRoleTask(new TeamMove(this.loginrole));
				sb.append("语音测试");
				break;
			case 14: // 语音文本聊天混合测试
				this.loginrole.startRoleTask(new TeamMove(this.loginrole));
				sb.append("语音文本聊天混合测试");
				break;
			case 15:
				this.loginrole.startRoleTask(new FriendZone(this.loginrole));
				sb.append("好友空间测试");
				break;
			case 16:
				this.loginrole.startRoleTask(new ArenaFight(this.loginrole, ArenaFight.ARENA_TYPE_TEAM_5));
				sb.append("PVP5");
				break;
			case 18:
				this.loginrole.startRoleTask(new ClanTask(this.loginrole));
				sb.append("公会可以离开公会");
				break;
			case 19:
				this.loginrole.startRoleTask(new TreasureMapTask(this.loginrole));
				sb.append("藏宝图任务");
				break;
			case 20:
				this.loginrole.startRoleTask(new BingFengTask(this.loginrole));
				sb.append("冰封王座");
				break;
			case 21:
				this.loginrole.startRoleTask(new ZhuoGuiTask(this.loginrole));
				sb.append("捉鬼日常副本");
				break;
			case 22:
				this.loginrole.startRoleTask(new ClanTask(this.loginrole, true, false));
				sb.append("公会成员不能离开公会");
				break;
			case 23:
				this.loginrole.startRoleTask(new SpotCard(this.loginrole, true, false));
				sb.append("符石");
				break;
			case 24:
				this.loginrole.startRoleTask(new Ride(this.loginrole, true, false));
				sb.append("坐骑");
				break;
			case 25: // 指定地图随机走动
				this.loginrole.startRoleTask(new NewMove(this.loginrole, true));
				sb.append("指定地图随机走动");
				break;
			case 26: // test
				this.loginrole.startRoleTask(new TestTask(this.loginrole));
				sb.append("test_" + Main.getProtocolsType());
				break;
			case 27: // 不组队打怪外网
				this.loginrole.startRoleTask(new Multiple(this.loginrole, true));
				sb.append("外网不组队打怪");
				break;
			case 28: // 组队打怪
				this.loginrole.startRoleTask(new TeamFightTask(this.loginrole, true));
				sb.append("外网组队打怪");
				break;
			case 29:
				this.loginrole.startRoleTask(new ClanTask(this.loginrole, true, false, true));
				sb.append("公会成员不能离开公会进入副本");
				break;
			case 30:
				this.loginrole.startRoleTask(new RedPacketTask(this.loginrole, true, false));
				sb.append("红包");
				break;
			case 31:
				this.loginrole.startRoleTask(new ChatItemTask(this.loginrole, false, false));
				sb.append("聊天道具");
				break;
			case 32:
				this.loginrole.startRoleTask(new ShiLianTask(this.loginrole));
				sb.append("冠军试炼");
				break;
			case 33:
				this.loginrole.startRoleTask(new ClanFightTask(this.loginrole, true, false, false));
				sb.append("公会战");
				break;
			case 34:
				this.loginrole.startRoleTask(new ClanTask(this.loginrole, true, false, false, false));
				sb.append("创建公会不进入任何公会地图");
				break;
				
			case 35:
				this.loginrole.startRoleTask(new ClanCopyTask(this.loginrole, true, false, false));
				sb.append("创建公会副本战斗");
				break;
			default:
				this.loginrole.startRoleTask(new NewMove(this.loginrole));
				sb.append("任务设置错误!!!默认随机走动!!!");
				break;
			}
			sb.append("**********");
			mkdb.Trace.info(sb.toString());
			// 初始化登录数据
			// GoodsUtils.initLogin(loginrole);
			// if(this.index<300){
			// loginrole.startRoleTask(new
			// RandomMove1Task(loginrole,this.index));
			// }else{
			// loginrole.startRoleTask(new
			// RandomFight2Task(loginrole,this.index));
			// }
		}
	}
}
