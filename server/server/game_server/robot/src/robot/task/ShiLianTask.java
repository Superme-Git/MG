package robot.task;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mkio.Protocol;
import robot.ConfigMgr;
import robot.LoginRole;
import robot.ride.PetUtils;
import fire.pb.battle.CSendAction;
import fire.pb.battle.CSendRoundPlayEnd;
import fire.pb.battle.OperationType;
import fire.pb.battle.SSendBattleStart;
import fire.pb.battle.SSendRoundScript;
import fire.pb.battle.SSendRoundStart;
import fire.pb.circletask.ActiveQuestData;
import fire.pb.circletask.SRefreshActiveQuest;
import fire.pb.hook.CSetAutoBattle;
import fire.pb.move.NpcBasic;
import fire.pb.move.SAddUserScreen;
import fire.pb.npc.CNpcService;
import fire.pb.npc.CVisitNpc;
import fire.pb.npc.SVisitNpc;

/**
 * 冠军试炼的处理
 * 
 * @author yt
 */
public class ShiLianTask extends Task_RoleBase {

	// 考古任务的npc和坐标
	private int mapid = 1615;

	private int x = 144;

	private int y = 127;

	boolean isFirst = true;

	long needToReLoginTime = System.currentTimeMillis();;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	boolean isProcessProtocols = false;

	boolean isReturnData = false;

	long runTime = System.currentTimeMillis();;

	private final TeamTask randomTeamTask;

	private java.util.LinkedList<fire.pb.move.NpcBasic> npcs = null;

	int npcId = -1;

	long dstnpcid = 0;

	long npckey = 0;

	public ShiLianTask(LoginRole role) {
		super(role);
		randomTeamTask = new TeamTask(role);
		init();
	}

	/**
	 * 初始化数据满足进入任务的需求
	 */
	private void init() {
		// 调整等级
		if (role.getLevel() < 50) {
			role.sendCommand("//addlevel " + (50 - role.getLevel()) + " equip");
			role.sendCommand("//strong 25000");
		}
		// 随机一个宠物
		int petid = PetUtils.getRandomPetId();
		role.sendCommand("//addpet " + petid + "50");
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		randomTeamTask.run();
		if (role.getTeam() == null)
			return;
		if (!role.getTeam().isLeader(role.roleId)) {
			// System.out.println(role.roleId);
			return;
		}
		if (!role.getTeam().isTeamFull()) {
			// System.out.println(role.roleId);
			return;
		}
		// 协议处理
		if (!protocols.isEmpty()) {
			Protocol p = protocols.remove(0);// 每次只处理一个请求
			if ((p instanceof SRefreshActiveQuest)) {
				isReturnData = true;
				SRefreshActiveQuest slc = (SRefreshActiveQuest) p;
				ActiveQuestData activeQuestData = slc.questdata;
				dstnpcid = activeQuestData.dstnpcid;
				// mkdb.Trace.error("**********ActiveQuestData:" + "---role.roleId" + role.roleId + "---dstnpcid:" + dstnpcid);
				// 切换到接受冠军试炼具体某个任务的地方
				role.sendCommand("//goto " + activeQuestData.dstx + " " + activeQuestData.dsty + " " + activeQuestData.dstmapid);
				// 设置自动战斗
				CSetAutoBattle cb = new CSetAutoBattle((byte) 1);
				role.sendProtocol(cb);
				isProcessProtocols = true;
			} else if (p instanceof SAddUserScreen) {
				// 以后维护获取动态npckey和serviceid用
				// 添加场景中的npc
				SAddUserScreen sAddUserScreen = (SAddUserScreen) p;
				if (npcs == null) {
					npcs = sAddUserScreen.npclist;
				} else {
					npcs.addAll(sAddUserScreen.npclist);
				}
				// mkdb.Trace.error("**********SAddUserScreen*******:" + "---role.roleId" + role.roleId + "----npcs:" + npcs);
				// 以后维护获取动态npckey和serviceid用
				NpcBasic npcBasic = getNpcBasic(dstnpcid);
				if (npcBasic != null) {
					if (npckey == npcBasic.npckey)
						return;
					// mkdb.Trace.error("**********SAddUserScreen:" + "---role.roleId" + role.roleId + "---npckey:" + npckey
					// + "---dstnpcid:" + dstnpcid);
					npckey = npcBasic.npckey;
					CVisitNpc cVisitNpc = new CVisitNpc(npcBasic.npckey);
					role.sendProtocol(cVisitNpc);
				}
				isProcessProtocols = true;
			} else if (p instanceof SVisitNpc) {
				// 以后维护获取动态npckey和serviceid用
				SVisitNpc sVisitNpc = (SVisitNpc) p;
				Long npckey = sVisitNpc.npckey;
				java.util.ArrayList<Integer> servicesList = sVisitNpc.services;
				if (servicesList != null && servicesList.size() > 0) {
					// mkdb.Trace.error("**********SVisitNpc:" + "---role.roleId" + role.roleId + "---npckey:" + npckey
					// + "--servicesList.get(0):" + servicesList.get(0));
					// 先不用
					CNpcService cr = new CNpcService(npckey, servicesList.get(0));
					role.sendProtocol(cr);
					// 设置自动战斗
					CSetAutoBattle cb = new CSetAutoBattle((byte) 1);
					role.sendProtocol(cb);
					isProcessProtocols = true;
				}
			} else if (p instanceof SSendBattleStart) {
				role.sendCommand("//full");
			} else if (p instanceof SSendRoundStart) {
				// mkdb.Trace.info("role[" + role.roleId +
				// "]recv:SSendRoundStart====回合开始！");
				// 简单模拟客户端玩家操作,3秒后反馈给服务端
				mkdb.Executor.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						int skillid = ConfigMgr.getInstance().randSkillByPlayerType(role.school);
						CSendAction snd = new CSendAction();
						snd.isrole = 1;
						snd.action.aim = 0;
						if (skillid > 0) {
							snd.action.operationtype = OperationType.ACTION_SKILL;
							snd.action.operationid = skillid;
						} else {
							snd.action.operationtype = OperationType.ACTION_ATTACK;
						}
						role.sendProtocol(snd);
						// mkdb.Trace.info("role[" + role.roleId +
						// "]send:role,CSendAction optype:" +
						// snd.action.operationtype
						// + " opid:" + snd.action.operationid);
						CSendAction sndpet = new CSendAction();
						sndpet.isrole = 0;
						sndpet.action.aim = 0;
						sndpet.action.operationtype = OperationType.ACTION_ATTACK;
						role.sendProtocol(sndpet);
						// mkdb.Trace.info("role[" + role.roleId +
						// "]send:pet,CSendAction optype:" +
						// sndpet.action.operationtype
						// + " opid:" + sndpet.action.operationid);
					}
				}, 5, TimeUnit.SECONDS);
				isProcessProtocols = true;
			} else if (p instanceof SSendRoundScript) {
				// mkdb.Trace.info("role[" + role.roleId +
				// "]recv:SSendRoundScript====开始播放本回合战斗动画！");
				// 简单模拟客户端战斗动画播放,5秒后反馈给服务端
				mkdb.Executor.getInstance().schedule(new Runnable() {

					@Override
					public void run() {
						role.sendProtocol(new CSendRoundPlayEnd());
						// mkdb.Trace.info("role[" + role.roleId +
						// "]send：CSendRoundPlayEnd====本回合战斗动画结束！");
					}
				}, 10, TimeUnit.SECONDS);
				isProcessProtocols = true;
			}
		}
		if (isProcessProtocols) {
			isProcessProtocols = false;
			return;
		}
		// 如果一定时间没有消息接受就断线
		long offLineTime = System.currentTimeMillis() - needToReLoginTime;
		if ((offLineTime > (5 * 60 * 1000))) {
			needToReLoginTime = System.currentTimeMillis();
			// mkdb.Trace.error("**********捉鬼任务完成当前轮数，该下一轮了:" + "---role.roleId"
			// + role.roleId);
			isReturnData = false;
			return;
		}
		// 是不是首次
		if (isFirst) {
			mkdb.Trace.error("**********first:" + "---role.roleId" + role.roleId);
			isFirst = false;
			dstnpcid = 161515;
			// 切换到接受npc地方
			role.gmgoto(mapid, x, y);
			isReturnData = true;
			return;
		}
		if (!isReturnData && System.currentTimeMillis() - runTime > (5 * 1000)) {
			mkdb.Trace.error("**********第二轮 first:" + "---role.roleId" + role.roleId);
			runTime = System.currentTimeMillis();
			dstnpcid = 161515;
			// 切换到接受npc地方
			role.gmgoto(mapid, x, y);
			isReturnData = true;
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
		randomTeamTask.processProtocol(p);
		needToReLoginTime = System.currentTimeMillis();
		// if (p instanceof SRefreshSpecialQuest||p instanceof SAddUserScreen||p instanceof SVisitNpc) {
		// 发送刷新下一个npc怪
		if (p instanceof SRefreshActiveQuest || p instanceof SSendBattleStart || p instanceof SAddUserScreen
				|| p instanceof SVisitNpc || p instanceof SSendRoundScript || p instanceof SSendRoundStart) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
	}

	/**
	 * 得到npc的信息数据
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月20日 下午4:14:23
	 * @version 1.0
	 * @param npcID
	 * @return
	 */
	public NpcBasic getNpcBasic(long npcID) {
		if (npcs != null) {
			for (fire.pb.move.NpcBasic npc : npcs) {
				if (npc.id == npcID) {
					return npc;
				}
			}
		}
		return null;
	}
}
