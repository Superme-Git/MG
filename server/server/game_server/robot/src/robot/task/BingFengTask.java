package robot.task;

import java.util.LinkedList;
import java.util.List;

import mkio.Protocol;
import robot.LoginRole;
import robot.bingfeng.BingFengUtils;
import fire.pb.instancezone.bingfeng.BingFengWangZuoConfig;
import fire.pb.instancezone.bingfeng.CBattletoBingFeng;
import fire.pb.instancezone.bingfeng.CEnterBingFengLand;
import fire.pb.instancezone.bingfeng.SBingFengLandInfo;
import fire.pb.instancezone.bingfeng.SEnterBingFengLand;
import fire.pb.npc.CNpcService;

/**
 * 冰封王座的处理
 * 
 * @author yt
 */
public class BingFengTask extends Task_RoleBase {

	// 考古任务的npc和坐标
	private int mapid = 1615;

	private int x = 70;

	private int y = 127;

	public static long lastTime = -1;

	boolean isFirst = true;

	long needToReLoginTime = System.currentTimeMillis();;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	boolean isProcessProtocols = false;

	boolean isReturnData = false;

	long runTime = System.currentTimeMillis();;

	public BingFengTask(LoginRole role) {
		super(role);
		init();
	}

	/**
	 * 初始化数据满足进入任务的需求
	 */
	private void init() {
		// 调整等级
		if (role.getLevel() < 99) {
			role.sendCommand("//addlevel " + (99 - role.getLevel()) + " equip");
			role.sendCommand("//strong");
		}
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		// 协议处理
		if (!protocols.isEmpty()) {
			Protocol p = protocols.remove(0);// 每次只处理一个请求
			if (p instanceof SEnterBingFengLand) {
				SEnterBingFengLand sb = (SEnterBingFengLand) p;
				int landid = sb.landid;
				int stage = sb.stage;
				BingFengWangZuoConfig sBingFengCfgData = BingFengUtils.getInstance().getData(landid, stage);
				if (sBingFengCfgData == null)
					return;
				// mkdb.Trace.error("**********冰封sBingFengCfgData.FocusNpc:"
				// + sBingFengCfgData.getBattleNpc()
				// + "----sBingFengCfgData.serviceid"
				// + sBingFengCfgData.getServiceid() + "----"
				// + role.roleId);
				// 切换到接受冰封的npc地方
				// role.sendCommand("//goto " + dsx + " " + dsy + " " +
				// dstmapid);
				// 接受下一管卡
				// CNpcService cr = new CNpcService(
				// sBingFengCfgData.getBattleNpc(),
				// sBingFengCfgData.getServiceid());
				// role.sendProtocol(cr);
				CBattletoBingFeng cbf = new CBattletoBingFeng(role.roleId, sBingFengCfgData.getBattleNpc());
				role.sendProtocol(cbf);
				isProcessProtocols = true;
			}
			// 点击冰封npc中的冰封任务会获取冰封详细信息
			if ((p instanceof SBingFengLandInfo) && !isReturnData) {
				isReturnData = true;
				SBingFengLandInfo sl = (SBingFengLandInfo) p;
				int landid = sl.landid;
				int stage = sl.stage;
				// mkdb.Trace.error("**********第一次冰封landid:" + landid
				// + "----sl.stage" + sl.stage + "----" + role.roleId);
				BingFengWangZuoConfig sBingFengCfgData = BingFengUtils.getInstance().getData(landid, stage);
				if (sBingFengCfgData == null) {
					return;
				}
				// 进入冰封
				CEnterBingFengLand cf = new CEnterBingFengLand(landid, stage);
				role.sendProtocol(cf);
				// mkdb.Trace.error("**********第一次冰封sBingFengCfgData.FocusNpc:"
				// + sBingFengCfgData.getBattleNpc()
				// + "----sBingFengCfgData.serviceid"
				// + sBingFengCfgData.getServiceid() + "----"
				// + role.roleId);
				// 切换到接受冰封的npc地方
				role.gmgoto(sBingFengCfgData.getMapid(), sBingFengCfgData.getPosx(), sBingFengCfgData.getPosy());
				// // 接受下一管卡
				// CNpcService cr = new CNpcService(
				// sBingFengCfgData.getBattleNpc(),
				// sBingFengCfgData.getServiceid());
				// role.sendProtocol(cr);
				CBattletoBingFeng cbf = new CBattletoBingFeng(role.roleId, sBingFengCfgData.getBattleNpc());
				role.sendProtocol(cbf);
			}
		}
		if (isProcessProtocols) {
			isProcessProtocols = false;
			return;
		}
		// 如果一定时间没有消息接受就断线
		long offLineTime = System.currentTimeMillis() - needToReLoginTime;
		if ((offLineTime > (10 * 60 * 1000))) {
			// mkdb.Trace.error("**********冰封任务完成玩家需要下线:" + "---role.roleId"
			// + role.roleId);
			needToReLoginTime = System.currentTimeMillis();
			role.loginui.onDisconnected();
			return;
		}
		// 是不是首次
		if (isFirst) {
			isFirst = false;
			// mkdb.Trace.error("**********冰封这是首次吗:" + isFirst +
			// "---role.roleId"
			// + role.roleId);
			// 切换到接受（冰封）的npc地方
			role.gmgoto(mapid, x, y);
			// 接受任务
			CNpcService cr = new CNpcService(130, 355);
			role.sendProtocol(cr);
			return;
		}
		if (!isReturnData && System.currentTimeMillis() - runTime > (5 * 1000)) {
			runTime = System.currentTimeMillis();
			// 切换到接受（冰封）的npc地方
			role.gmgoto(mapid, x, y);
			// 接受任务
			CNpcService cr = new CNpcService(130, 355);
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
		if (p instanceof SEnterBingFengLand || p instanceof SBingFengLandInfo) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
	}
}
