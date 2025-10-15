package robot.task;

import java.util.LinkedList;
import java.util.List;

import mkio.Protocol;
import robot.LoginRole;
import fire.pb.battle.SSendBattleStart;
import fire.pb.circletask.SRefreshSpecialQuest;
import fire.pb.hook.CSetAutoBattle;
import fire.pb.npc.CNpcService;

/**
 * 藏宝图的处理
 * 
 * @author yt
 */
public class TreasureMapTask extends Task_RoleBase {

	// 考古任务的npc和坐标
	private int mapid = 1615;

	private int x = 35;

	private int y = 86;

	public static long lastTime = -1;

	boolean isFirst = true;

	long needToReLoginTime = System.currentTimeMillis();;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	public TreasureMapTask(LoginRole role) {
		super(role);
		init();
	}

	/**
	 * 初始化数据满足进入公会或者创建公会的需求
	 */
	private void init() {
		// 调整等级
		if (role.getLevel() < 50) {
			role.sendCommand("//addfushi 1 100000");
			role.sendCommand("//addlevel " + (50 - role.getLevel()) + " equip");
		}
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		boolean isProcessProtocols = false;
		// 协议处理
		if (!protocols.isEmpty()) {
			Protocol p = protocols.remove(0);// 每次只处理一个请求
			if (p instanceof SRefreshSpecialQuest) {
				// mkdb.Trace.error("**********处理下一次接受任务:");
				SRefreshSpecialQuest slc = (SRefreshSpecialQuest) p;
				int questid = slc.questid;
				if (questid != 1040000) {
					return;
				}
				long dstnpckey = slc.dstnpckey;
				int dstmapid = slc.dstmapid;
				int dsx = slc.dstx;
				int dsy = slc.dsty;
				// mkdb.Trace.error("**********处理下一次接受任务的mapid，x,y:" + dstmapid
				// + "--" + x + "----" + y+"---role.roleId"
				// + role.roleId);
				// 切换到接受藏宝图（考古）的npc地方
				role.sendCommand("//goto " + dsx + " " + dsy + " " + dstmapid);
				// 接受下一个任务
				CNpcService cr = new CNpcService(dstnpckey, 100011);
				role.sendProtocol(cr);
				// 设置自动战斗
				CSetAutoBattle cb = new CSetAutoBattle((byte) 1);
				role.sendProtocol(cb);
				isProcessProtocols = true;
			} else if (p instanceof SSendBattleStart) {
				role.sendCommand("//full");
			}
		}
		if (isProcessProtocols) {
			isProcessProtocols = false;
			return;
		}
		// 如果一定时间没有消息接受就断线
		long offLineTime = System.currentTimeMillis() - needToReLoginTime;
		if ((offLineTime > (10 * 60 * 1000))) {
			// mkdb.Trace.error("**********考古任务完成玩家需要下线:" + "---role.roleId"
			// + role.roleId);
			needToReLoginTime = System.currentTimeMillis();
			role.loginui.onDisconnected();
			return;
		}
		// 是不是首次
		if (isFirst) {
			isFirst = false;
			// mkdb.Trace.error("**********考古这是首次吗:" + isFirst + "---role.roleId"
			// + role.roleId);
			// 切换到接受藏宝图（考古）的npc地方
			role.sendCommand("//goto " + x + " " + y + " " + mapid);
			// 接受任务
			CNpcService cr = new CNpcService(124, 100001);
			role.sendProtocol(cr);
		}
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		role.loginui.reconnect();
		System.out.println("roleId=" + role.roleId + " 准备重新连接服务器");
	}

	/*
	 * 处理接受到的消息 (non-Javadoc)
	 * @see robot.task.Task_RoleBase#processProtocol(mkio.Protocol)
	 */
	@Override
	public void processProtocol(Protocol p) {
		needToReLoginTime = System.currentTimeMillis();
		// 发送刷新下一个npc怪
		if (p instanceof SRefreshSpecialQuest || p instanceof SSendBattleStart) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
	}
}
