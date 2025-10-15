package robot.task;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import mkio.Protocol;
import robot.ConfigMgr;
import robot.LoginRole;
import robot.Misc;
import robot.clan.ClanMgr;
import robot.pos.LogicPos;
import fire.pb.battle.CSendAction;
import fire.pb.battle.CSendRoundPlayEnd;
import fire.pb.battle.OperationType;
import fire.pb.battle.SSendBattleEnd;
import fire.pb.battle.SSendBattleStart;
import fire.pb.battle.SSendRoundPlayEnd;
import fire.pb.battle.SSendRoundScript;
import fire.pb.battle.SSendRoundStart;
import fire.pb.clan.SRefreshPosition;
import fire.pb.move.NpcBasic;
import fire.pb.move.SAddUserScreen;
import fire.pb.move.SRemoveUserScreen;
import fire.pb.move.SRoleEnterScene;
import fire.pb.npc.CNpcService;

/**
 * 公会副本
 * 
 * @author yt
 */
public class ClanCopyTask extends Task_RoleBase {

	int mapid = 1615;

	int x = 235;

	int y = 96;

	public long lastTime = 0l;

	boolean isProcessProtocols = false;

	boolean isNotLeaveClan = false;

	boolean isRuneRequest = false;

	boolean isEnterClanCopy = false;

	private LogicPos targetpos = null;

	boolean isReach = false;

	private final MoveBattle moveBattle;

	boolean isCanMove = false;

	boolean isEnterCopy = false;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	public static Map<Long, NpcBasic> npcKey2NpcBasicMap = new ConcurrentHashMap<Long, NpcBasic>();

	public static Map<Integer, Integer> indexMap = new ConcurrentHashMap<Integer, Integer>();

	boolean isBattle = false;

	long npcKey = 0l;

	int serviceId = 0;

	NpcBasic npcBasicData = null;

	boolean isFirst = false;

	public Object battleNpcLocks = new Object();

	int gotoNum = 0;

	int listSize = 0;

	int currentIndex = -1;

	int checkmoveNum = 0;

	int timeDelay = 2000;

	public ClanCopyTask(LoginRole role, boolean isNotLeaveClan, boolean isSay, boolean isEnterClanCopy) {
		super(role);
		moveBattle = new MoveBattle(role, false, isSay, false);
		this.isNotLeaveClan = isNotLeaveClan;
		this.isEnterClanCopy = isEnterClanCopy;
		init();
	}

	/**
	 * 初始化数据满足进入公会或者创建公会的需求
	 */
	private void init() {
		// 调整等级
		if (role.getLevel() < 50) {
			role.sendCommand("//strong");
		}
		// 切换到接受npc地方
		role.gmgoto(mapid, x, y);
		timeDelay = 2000;
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);
		// 能移动的时候在调用
		if (isCanMove) {
			// moveBattle.run();
		}
		// 协议处理
		if (!protocols.isEmpty()) {
			// 解析协议
			jieXiProtocols();
		}
		if (isProcessProtocols) {
			isProcessProtocols = false;
			return;
		}
		Long now = System.currentTimeMillis();
		// 间隔一定的时间处理
		if (now - lastTime < timeDelay) {
			// mkdb.Trace.error("**********lastTime" + lastTime + "role.roleId"
			// + role.roleId);
			return;
		}
		lastTime = System.currentTimeMillis();
		// 角色处理
		roleProcess();
	}

	/**
	 * 角色处理
	 */
	private void roleProcess() {
		if (!isFirst) {
			isFirst = true;
			// 切换到接受npc地方
			role.gmgoto(mapid, x, y);
			timeDelay = 5000;
			return;
		}
		// 容错处理
		if (!isEnterCopy || role.getSceneID() == mapid) {
			// 进入公会战场
			enterClanCopyMap();
			isEnterCopy = true;
			timeDelay = 5000;
		}
		double data = Math.random();
		if (data < 0.2) {
			// 进入公会战场
			enterClanCopyMap();
		}
		int state = 0;
		if (!isBattle) {
			timeDelay = 5000;
			state = getBattleRole();
		}
		// 如果能移動，并且沒有找到攻击的对象就随机走动
		if (isCanMove && state == 0) {
			targetpos = getRandomPos();
			int mapID = ClanMgr.getBaseMapIdBySceneId(role.getSceneID());
			// 切换指定的位置
			role.gmgoto(mapID, targetpos.getX() / 16, targetpos.getY() / 16);
			timeDelay = 2000;
		}
	}

	/**
	 * 解析协议
	 */
	private void jieXiProtocols() {
		Protocol p = protocols.remove(0);// 每次只处理一个请求
		if (p instanceof SSendBattleStart) {
			isBattle = true;
			isCanMove = false;
			role.sendCommand("//full");
			isProcessProtocols = true;
			endBattle();
		} else if (p instanceof SSendRoundStart) {
			isBattle = true;
			isCanMove = false;
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
			}, 1, TimeUnit.SECONDS);
			isProcessProtocols = true;
			// 结束战斗
			endBattle();
		} else if (p instanceof SSendRoundScript) {
			isBattle = true;
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
			isCanMove = false;
			isProcessProtocols = true;
		} else if (p instanceof SAddUserScreen) {
			// mkdb.Trace.error("********role.getSceneID()**** " + role.getSceneID());
			int mapID = ClanMgr.getBaseMapIdBySceneId(role.getSceneID());
			if (mapID != mapid) {
				// 添加角色
				SAddUserScreen sAddUserScreen = (SAddUserScreen) p;
				java.util.LinkedList<fire.pb.move.NpcBasic> npclist = sAddUserScreen.npclist;
				jieXiAddNPC(npclist);
			}
			// isProcessProtocols = true;
		} else if (p instanceof SRemoveUserScreen) {
			// //移除场景中的npc
			// SRemoveUserScreen sRemoveUserScreen = (SRemoveUserScreen) p;
			// jieXiSubNPC(sRemoveUserScreen.npcids);
		} else if (p instanceof SSendRoundPlayEnd) {
			isCanMove = false;
			isProcessProtocols = true;
			endBattle();
		} else if (p instanceof SSendBattleEnd) {
			// 战斗结束
			isBattle = false;
			isCanMove = true;
			isProcessProtocols = true;
			timeDelay = 1000;
		} else if (p instanceof SRoleEnterScene) {
			role.setScene(((SRoleEnterScene) p).sceneid);
			npcKey2NpcBasicMap.clear();
		}
	}

	/**
	 * 結束战斗
	 * 
	 * @author yangtao
	 * @dateTime 2016年12月17日 上午11:07:52
	 * @version 1.0
	 */
	private void endBattle() {
		role.sendCommand("//battle end 1");
		ClanMgr._instance.addBattleNPC(npcKey, role.roleId);
		npcKey = 0l;
		// 战斗结束
		isBattle = false;
		isCanMove = true;
	}

	/**
	 * 添加npc
	 * 
	 * @author yangtao
	 * @dateTime 2016年12月16日 上午11:12:12
	 * @version 1.0
	 * @param npclist
	 */
	private void jieXiAddNPC(LinkedList<NpcBasic> npclist) {
		synchronized (battleNpcLocks) {
			for (NpcBasic npcBasic : npclist) {
				npcKey2NpcBasicMap.put(npcBasic.npckey, npcBasic);
			}
		}
	}

	// /**
	// * 移除npc
	// *
	// * @author yangtao
	// * @dateTime 2016年12月16日 上午11:12:12
	// * @version 1.0
	// * @param npclist
	// */
	// private void jieXiSubNPC(java.util.LinkedList<Long> npclist) {
	// synchronized (battleNpcLocks) {
	// for (Long npcKey : npclist) {
	// npcKey2NpcBasicMap.remove(npcKey);
	// // mkdb.Trace.error("*****移除npc*****npcKey" + npcKey + "role.roleId" + role.roleId);
	// }
	// }
	// }
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
		// 邀请玩家进入公会
		if (p instanceof SRefreshPosition || p instanceof SAddUserScreen || p instanceof SSendBattleEnd
				|| p instanceof SSendRoundScript || p instanceof SSendRoundStart || p instanceof SSendBattleStart
				|| p instanceof SSendRoundPlayEnd || p instanceof SRemoveUserScreen || p instanceof SRoleEnterScene) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
		moveBattle.processProtocol(p);
	}

	/**
	 * 进入公会副本
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月5日 下午8:54:48
	 * @version 1.0
	 */
	public void enterClanCopyMap() {
		mkdb.Trace.error("*********进入副本:role.roleId" + role.roleId);
		role.sendCommand("//goinst 111");
	}

	/**
	 * 得到一个可战斗的NPCid
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午2:46:07
	 * @version 1.0
	 * @return
	 */
	private int getBattleRole() {
		synchronized (battleNpcLocks) {
			// 如果已经找到对战的npc就不需要查找了
			if (npcKey != 0l) {
				// 点击npc交互战斗
				CNpcService cr = new CNpcService(npcKey, serviceId);
				role.sendProtocol(cr);
				gotoNum++;
				// 容错处理
				if (gotoNum == 1) {
					npcKey = 0l;
				}
				return 1;
			} else {
				gotoNum = 0;
				mkdb.Trace.error("*********npcKey2NpcBasicMap" + npcKey2NpcBasicMap.size());
				for (Entry<Long, NpcBasic> enrey : npcKey2NpcBasicMap.entrySet()) {
					long npckeyData = enrey.getKey();
					NpcBasic npcBasic = enrey.getValue();
					// 得到服务id
					Integer serviceIdData = ClanMgr.npcId2ServiceIdMap.get(npcBasic.id);
					if (serviceIdData == null) {
						// mkdb.Trace.error("*****数据出错了**npcBasic.id" + npcBasic.id + "---" + npcBasic.name);
						continue;
					}
					if (ClanMgr.battleNpcMap.containsKey(npckeyData)) {
						continue;
					}
					// 战斗中
					if ((npcBasic.scenestate & 1) == 1) {
						continue;
					}
					npcKey = npckeyData;
					npcBasicData = npcBasic;
					serviceId = serviceIdData;
					// 点击npc交互战斗
					CNpcService cr = new CNpcService(npcBasic.npckey, serviceId);
					role.sendProtocol(cr);
					return 1;
				}
			}
			return 0;
		}
	}

	/**
	 * 清出副本
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月5日 下午8:54:48
	 * @version 1.0
	 */
	public void destroyzoneClanCopyMap() {
		role.sendCommand("//destroyzone");
	}

	// // 移动到指定的位置
	// private void roleMove() {
	// if (isReach)
	// return;
	// if (targetpos == null) {
	// LogicPos startpos = new LogicPos(role.getPosX(), role.getPosY());
	// targetpos = getRandomPos();
	// CRoleMove rolemove = new CRoleMove();
	// rolemove.sceneid = role.getSceneID();
	// rolemove.srcpos = startpos.toProtocolPos();
	// rolemove.destpos = targetpos.toProtocolPos();
	// role.sendProtocol(rolemove);
	// checkmoveNum = 0;
	// } else {
	// LogicPos nowpos = new LogicPos(role.getPosX(), role.getPosY());
	// LogicPos nextcheckpos = getNextCheckPos(nowpos, targetpos);
	// role.setPos(nextcheckpos.getX(), nextcheckpos.getY());
	// CCheckMove checkmove = new CCheckMove();
	// checkmove.sceneid = role.getSceneID();
	// checkmove.curpos = nextcheckpos.toProtocolPos();
	// role.sendProtocol(checkmove);
	// if (nextcheckpos.equals(targetpos)) {
	// roleStop();
	// isReach = true;
	// targetpos = null;
	// }
	// checkmoveNum++;
	// if (checkmoveNum == 30) {
	// roleStop();
	// enterClanCopyMap();
	// targetpos = null;
	// }
	// }
	// }
	/**
	 * 随机一个位置(第一次确定索引后，之后就是递增，直到最大，置为0)
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午5:36:49
	 * @version 1.0
	 * @return
	 */
	private LogicPos getRandomPos() {
		// mkdb.Trace.error("-------------currentIndex" + currentIndex + "randomPos1616.size():" + role.roleId);
		if (currentIndex == -1) {
			int index = Misc.getRandomBetween(0, ClanMgr.listSize - 1);
			currentIndex = index;
		} else {
			currentIndex++;
			if (currentIndex >= ClanMgr.listSize) {
				currentIndex = 0;
			}
		}
		return ClanMgr.randomPos1616.get(currentIndex);
	}
	// private void roleStop() {
	// CRoleStop rolestop = new CRoleStop();
	// rolestop.pos = new LogicPos(role.getPosX(), role.getPosY()).toProtocolPos();
	// role.sendProtocol(rolestop);
	// }
	//
	// private LogicPos getNextCheckPos(LogicPos nowpos, LogicPos targetpos) {
	// int deltax = targetpos.getX() - nowpos.getX();
	// int deltay = targetpos.getY() - nowpos.getY();
	// int targetx = targetpos.getX();
	// int targety = targetpos.getY();
	// if (Math.abs(deltax) > Constant.MOVE_SPEED)
	// targetx = nowpos.getX() + ((deltax > 0) ? Constant.MOVE_SPEED : -Constant.MOVE_SPEED);
	// if (Math.abs(deltay) > Constant.MOVE_SPEED)
	// targety = nowpos.getY() + ((deltay > 0) ? Constant.MOVE_SPEED : -Constant.MOVE_SPEED);
	// return new LogicPos(targetx, targety);
	// }
}
