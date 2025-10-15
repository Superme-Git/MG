package robot.task;

import java.util.LinkedList;
import java.util.List;

import mkio.Protocol;
import robot.ConfigMgr;
import robot.LoginRole;
import robot.Misc;
import robot.clan.ClanMgr;
import robot.redpacket.RedPacketMgr;
import fire.pb.fushi.redpack.CGetRedPack;
import fire.pb.fushi.redpack.CSendRedPack;
import fire.pb.fushi.redpack.CSendRedPackView;
import fire.pb.fushi.redpack.SNoticeRedPack;

/**
 * 红包操作
 * 
 * @author yt
 */
public class RedPacketTask extends Task_RoleBase {

	public long lastTime = 0l;

	boolean isProcessProtocols = false;

	// private final MoveBattle moveBattle;
	boolean isCanMove = false;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	String redpackid = "";

	int modeltype = 1;

	public RedPacketTask(LoginRole role) {
		super(role);
		// moveBattle = new MoveBattle(role, false);
		init();
	}

	public RedPacketTask(LoginRole role, boolean isChangeMap) {
		super(role);
		// moveBattle = new MoveBattle(role, isChangeMap);
		init();
	}

	public RedPacketTask(LoginRole role, boolean isChangeMap, Boolean isSay) {
		super(role);
		// moveBattle = new MoveBattle(role, isChangeMap, isSay);
		init();
	}

	/**
	 * 初始化数据满足
	 */
	private void init() {
		// 调整等级
		if (role.getLevel() < 50) {
			role.sendCommand("//addlevel " + (50 - role.getLevel()) + " equip");
			role.sendCommand("//addgold " + 1000000000);
			role.sendCommand("//addqian 13 10000000");
		}
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		// 能移动的时候在调用
		// moveBattle.run();
		// 协议处理
		// if (!protocols.isEmpty()) {
		// // 解析协议
		// jieXiProtocols();
		// }
		if (isProcessProtocols) {
			isProcessProtocols = false;
			return;
		}
		// // 间隔一定的时间处理
		// if (System.currentTimeMillis() - lastTime < 3000) {
		// // mkdb.Trace.error("**********lastTime" + lastTime + "role.roleId"
		// // + role.roleId);
		// return;
		// }
		// lastTime = System.currentTimeMillis();
		// 设置是否发送
		// RedPacketMgr.getInstance().setIsSendPacket();
		// 角色处理
		// roleProcess();
		sendRedPacketRoleProcess();
	}

	/**
	 * 发送红包
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月27日 上午12:53:33
	 * @version 1.0
	 */
	private void sendRedPacketRoleProcess() {
		if (System.currentTimeMillis() - lastTime < 3000) {
			return;
		}
		lastTime = System.currentTimeMillis();
		// 加一个小概率发送红包
		double random = Math.random();
		if (random < 0.4) {
			CSendRedPack cSendRedPack = null;
			if (ConfigMgr.getInstance().payserverType == 1) {
				int num = Misc.getRandomBetween(2000, 200000);
				cSendRedPack = new CSendRedPack(1, num, 100, role.rolename);
			} else {
				int num = Misc.getRandomBetween(20, 2000);
				cSendRedPack = new CSendRedPack(1, num, 100, role.rolename);
			}
			role.sendProtocol(cSendRedPack);
			// 内网加符石
			role.sendCommand("//addfushi 1 2000");
			role.sendCommand("//addgold " + 10000);
			role.sendCommand("//addqian 13 10");
		}
	}

	/**
	 * 角色处理
	 */
	private void roleProcess() {
		// if (System.currentTimeMillis() - lastTime < 3000) {
		// // mkdb.Trace.error("**********lastTime" + lastTime + "role.roleId"
		// // + role.roleId);
		// return;
		// }
		// lastTime = System.currentTimeMillis();
		// if (redpackid.length() > 1) {
		// CGetRedPack cGetRedPack = new CGetRedPack(modeltype, redpackid);
		// role.sendProtocol(cGetRedPack);
		// isProcessProtocols = true;
		// for (int i = 0; i < 60; i++) {
		// role.sendProtocol(cGetRedPack);
		// }
		// }
		CSendRedPackView cSendRedPackView = new CSendRedPackView(1, "");
		mkdb.Trace.info("请求红包界面" + role.roleId + "---协议号：" + cSendRedPackView.getType());
		for (int i = 0; i < 1; i++) {
			role.sendProtocol(cSendRedPackView);
		}
		// if (!RedPacketMgr.getInstance().isSendPacket) {
		// mkdb.Trace.info("发紅包" + role.roleId);
		// // 设置状态
		// RedPacketMgr.getInstance().setNumAndSendPacketStatus();
		// CSendRedPack cSendRedPack = new CSendRedPack(1, 2000, 100, "1111");
		// role.sendProtocol(cSendRedPack);
		// // 内网加符石
		// role.sendCommand("//addfushi 1 2000");
		// role.sendCommand("//addgold " + 10000);
		// }
		// int random = ClanMgr.getInstance().getRandom(14);
		// if (random >= 0 && random <= 4) {
		// CSendRedPackView cSendRedPackView = new CSendRedPackView(1, "");
		// role.sendProtocol(cSendRedPackView);
		// } else if (random > 4 && random <= 9) {
		// int modeltype = 0;
		// random = ClanMgr.getInstance().getRandom(2);
		// if (random == 0) {
		// modeltype = 1;
		// }
		// CSendRedPackRoleRecordView cSendRedPackRoleRecordView = new CSendRedPackRoleRecordView(modeltype, "0");
		// role.sendProtocol(cSendRedPackRoleRecordView);
		// } else if (random > 9 && random <= 10) {
		// CSendRedPackHisView cSendRedPackHisView = new CSendRedPackHisView(1, "0");
		// role.sendProtocol(cSendRedPackHisView);
		// } else if (random > 10 && random <= 11) {
		// } else if (random > 11 && random <= 12) {
		// } else {
		// }
	}

	/**
	 * 解析协议
	 */
	private void jieXiProtocols() {
		// mkdb.Trace.error("**********进入协议解析:role.roleId" + role.roleId);
		Protocol p = protocols.remove(0);// 每次只处理一个请求
		if (p instanceof SNoticeRedPack) {
			SNoticeRedPack sNoticeRedPack = (SNoticeRedPack) p;
			fire.pb.fushi.redpack.RedPackRoleTip redpackroletip = sNoticeRedPack.redpackroletip;
			mkdb.Trace.info("抢紅包" + role.roleId + "-------" + redpackroletip.redpackid);
			redpackid = redpackroletip.redpackid;
			modeltype = redpackroletip.modeltype;
			CGetRedPack cGetRedPack = new CGetRedPack(redpackroletip.modeltype, redpackroletip.redpackid);
			role.sendProtocol(cGetRedPack);
			isProcessProtocols = true;
			for (int i = 0; i < 60; i++) {
				role.sendProtocol(cGetRedPack);
			}
		}
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	/*
	 * 处理接受到的消息 (non-Javadoc)
	 * @see robot.task.Task_RoleBase#processProtocol(mkio.Protocol)
	 */
	@Override
	public void processProtocol(Protocol p) {
		if (p instanceof SNoticeRedPack) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
		// moveBattle.processProtocol(p);
	}
}
