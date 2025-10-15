package robot.task;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import mkio.Protocol;
import robot.ConfigMgr;
import robot.Constant;
import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.Misc;
import robot.clan.ClanMgr;
import robot.pos.LogicPos;

import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.battle.CSendAction;
import fire.pb.battle.CSendRoundPlayEnd;
import fire.pb.battle.OperationType;
import fire.pb.battle.SSendBattleEnd;
import fire.pb.battle.SSendBattleStart;
import fire.pb.battle.SSendRoundPlayEnd;
import fire.pb.battle.SSendRoundScript;
import fire.pb.battle.SSendRoundStart;
import fire.pb.clan.SRefreshPosition;
import fire.pb.clan.fight.CBattleFieldRankList;
import fire.pb.clan.fight.CBattleFieldScore;
import fire.pb.clan.fight.CRequestRoleIsEnemy;
import fire.pb.clan.fight.CStartClanFightBattle;
import fire.pb.clan.fight.SClanFightOver;
import fire.pb.clan.fight.SRequestRoleIsEnemy;
import fire.pb.move.CCheckMove;
import fire.pb.move.CRoleMove;
import fire.pb.move.CRoleStop;
import fire.pb.move.RoleBasic;
import fire.pb.move.RoleBasicOctets;
import fire.pb.move.SAddUserScreen;
import fire.pb.npc.CNpcService;
import fire.pb.title.COnTitle;

/**
 * 公会战场
 * 
 * @author yt
 */
public class ClanFightTask extends Task_RoleBase {

	int mapid = 1615;

	int x = 235;

	int y = 96;

	public long lastTime = 0l;

	boolean isProcessProtocols = false;

	boolean isNotLeaveClan = false;

	boolean isRuneRequest = false;

	boolean isEnterClanCopy = false;

	private final MoveBattle moveBattle;

	boolean isCanMove = false;

	boolean isEnterCopy = false;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	private Set<Long> enemyList = new LinkedHashSet<Long>();

	private List<Long> frendList = new LinkedList<Long>();

	private java.util.LinkedList<Long> newAddRoleList = new LinkedList<Long>();

	boolean isBattle = false;

	private LogicPos targetpos = null;

	boolean isReach = false;

	boolean isActivityEnd = false;

	boolean isEntryMap = false;

	long battleTargetRoleId = 0l;

	int battleTargetNum = 0;

	static private Vector<LogicPos> randomPos1616 = new Vector<LogicPos>();

	private TeamTask randomTeamTask = null;

	public ClanFightTask(LoginRole role, boolean isNotLeaveClan, boolean isSay, boolean isEnterClanCopy) {
		super(role);
		moveBattle = new MoveBattle(role, false, isSay, false);
		this.isNotLeaveClan = isNotLeaveClan;
		this.isEnterClanCopy = isEnterClanCopy;
		randomTeamTask = new TeamTask(role);
		init();
	}

	/**
	 * 初始化数据满足进入公会或者创建公会的需求
	 */
	private void init() {
		// 调整等级
		if (role.getLevel() < 50) {
			role.sendCommand("//addlevel " + (50 - role.getLevel()) + " equip");
			role.sendCommand("//addgold " + 1000000);
			role.sendCommand("//addhyd " + 1000000);
			role.sendCommand("//addmoney " + 1000000000);
			int strong = 100 + ClanMgr._instance.getRandom(5000);
			role.sendCommand("//strong " + strong);
		}
		randomPos1616.add(new LogicPos(64 * 16, 89 * 16));
		randomPos1616.add(new LogicPos(57 * 16, 70 * 16));
		randomPos1616.add(new LogicPos(72 * 16, 82 * 16));
		randomPos1616.add(new LogicPos(83 * 16, 102 * 16));
		randomPos1616.add(new LogicPos(51 * 16, 96 * 16));
		// 切换到接受npc地方
		role.gmgoto(mapid, x, y);
	}

	@Override
	public void run() {
		if (isActivityEnd) {
			return;
		}
		// 发送心跳
		Global.DoGlobalTask(this);
		if (isReach)
			randomTeamTask.run();
		// 能移动的时候在调用
		if (isCanMove && isReach) {
			moveBattle.run();
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
		if (now - lastTime < ClanMgr.getInstance().timeDelay) {
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
		// 容错处理
		if (!isEntryMap || role.getSceneID() == mapid) {
			// 切换到接受npc地方
			role.gmgoto(mapid, x, y);
			// 进入公会战场
			enterClanFightMap();
			isEntryMap = true;
			ClanMgr.getInstance().timeDelay = 5000;
		} else {
			if (!isReach) {
				roleMove();
				ClanMgr.getInstance().timeDelay = 1000;
			}
		}
		if (isReach) {
			// 之前有进入帮战后不显示称号的
			double random1 = Math.random();
			if (random1 < 0.5) {
				// 设置称号
				COnTitle cOnTitle = new COnTitle(277);
				role.sendProtocol(cOnTitle);
			}
			// 开启战斗
			int state = startClanFightBattle();
			if (state == 2) {
				isCanMove = true;
				moveBattle.run();
				ClanMgr.getInstance().timeDelay = 1000;
			} else if (state == 5) {
				isCanMove = false;
				ClanMgr.getInstance().timeDelay = 1000;
			} else {
				isCanMove = false;
				ClanMgr.getInstance().timeDelay = 10000;
			}
			// 随机调用战场数据
			double random = Math.random();
			if (random < 0.4) {
				CBattleFieldScore cBattleFieldScore = new CBattleFieldScore();
				role.sendProtocol(cBattleFieldScore);
			} else if (random < 0.8) {
				CBattleFieldRankList cBattleFieldRankList = new CBattleFieldRankList();
				role.sendProtocol(cBattleFieldRankList);
			}
		}
	}

	/**
	 * 解析协议
	 */
	private void jieXiProtocols() {
		// mkdb.Trace.error("**********进入协议解析:role.roleId" + role.roleId);
		Protocol p = protocols.remove(0);// 每次只处理一个请求
		if (p instanceof SSendBattleStart) {
			isBattle = true;
			isCanMove = false;
			role.sendCommand("//full");
			isProcessProtocols = true;
			battleTargetNum = 0;
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
			}, 5, TimeUnit.SECONDS);
			isProcessProtocols = true;
			battleTargetNum = 0;
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
			battleTargetNum = 0;
		} else if (p instanceof SAddUserScreen) {
			// 添加角色
			SAddUserScreen sAddUserScreen = (SAddUserScreen) p;
			getNewAddRole(sAddUserScreen.rolelist);
			getRoleIsEnemy();
			// isProcessProtocols = true;
		} else if (p instanceof SSendRoundPlayEnd) {
			isCanMove = false;
			isProcessProtocols = true;
			battleTargetNum = 0;
			// mkdb.Trace.info("role[" + role.roleId
			// + "]recv：SSendRoundPlayEnd====确认本回合战斗动画结束！");
		} else if (p instanceof SSendBattleEnd) {
			ClanMgr._instance.removeBattleRole(role.roleId, battleTargetRoleId);
			battleTargetRoleId = 0l;
			// 战斗结束
			isBattle = false;
			isCanMove = true;
			isProcessProtocols = true;
			ClanMgr.getInstance().timeDelay = 1000;
			battleTargetNum = 0;
		} else if (p instanceof SRequestRoleIsEnemy) {
			// 解析敌对关系
			jieXiRoleisenemy((SRequestRoleIsEnemy) p);
			// isProcessProtocols = true;
		} else if (p instanceof SClanFightOver) {
			isActivityEnd = true;
			role.stopRoleTask();
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
		if (randomTeamTask != null)
			randomTeamTask.processProtocol(p);
		// 邀请玩家进入公会
		if (p instanceof SRefreshPosition || p instanceof SAddUserScreen || p instanceof SRequestRoleIsEnemy
				|| p instanceof SSendBattleEnd || p instanceof SSendRoundScript || p instanceof SSendRoundStart
				|| p instanceof SSendBattleStart || p instanceof SSendRoundPlayEnd || p instanceof SClanFightOver) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
		moveBattle.processProtocol(p);
	}

	/**
	 * 进入公会战场
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月5日 下午8:53:24
	 * @version 1.0
	 */
	public void enterClanFightMap() {
		// 点击npc进入战场
		CNpcService cr = new CNpcService(139, 910030);
		role.sendProtocol(cr);
		mkdb.Trace.info("role[" + role.roleId + "]进入帮战地图");
		// 设置称号
		COnTitle cOnTitle = new COnTitle(277);
		role.sendProtocol(cOnTitle);
	}

	/**
	 * 开始战斗
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午2:45:57
	 * @version 1.0
	 */
	public int startClanFightBattle() {
		if (isBattle) {
			return 1;
		}
		if (battleTargetRoleId == 0l) {
			// 如果有队伍，判断是否是队长
			if (role.getTeam() != null) {
				if (!role.getTeam().isLeader(role.roleId)) {
					return 4;
				}
			}
			long targetid = getBattleRole();
			if (targetid == 0)
				return 2;
			battleTargetRoleId = targetid;
			if (ClanMgr._instance.addBattleRole(targetid, role.roleId)) {
				CStartClanFightBattle cStartClanFightBattle = new CStartClanFightBattle(targetid);
				role.sendProtocol(cStartClanFightBattle);
			}
		} else {
			battleTargetNum++;
			// 可能在没有达到战斗时间的时候已经匹配单个人，等开展的时候可能就变成队伍了，在这里需要重置一下
			if (battleTargetNum == 2) {
				ClanMgr._instance.removeBattleRole(role.roleId, battleTargetRoleId);
				battleTargetRoleId = 0l;
				battleTargetNum = 0;
				isBattle = false;
				return 5;
			} else {
				CStartClanFightBattle cStartClanFightBattle = new CStartClanFightBattle(battleTargetRoleId);
				role.sendProtocol(cStartClanFightBattle);
			}
		}
		return 3;
	}

	/**
	 * 得到一个可战斗的玩家id
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午2:46:07
	 * @version 1.0
	 * @return
	 */
	private long getBattleRole() {
		Long targetId = ClanMgr._instance.battleRoleMap.get(role.roleId);
		if (targetId != null)
			return targetId;
		for (Long roleId : enemyList) {
			targetId = roleId;
			LoginRole loginRole = LoginRoleMgr.getInstance().getRoles().get(targetId);
			// 如果有队伍，找队长
			if (loginRole != null && loginRole.getTeam() != null) {
				targetId = loginRole.getTeam().leaderRoleId;
				if (targetId == null)
					continue;
			}
			if (ClanMgr._instance.battleRoleMap.containsKey(targetId)) {
				continue;
			}
			return targetId;
		}
		return 0;
	}

	/**
	 * 得到指定角色的敌对状态
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午2:42:36
	 * @version 1.0
	 */
	public void getRoleIsEnemy() {
		if (newAddRoleList.size() > 0) {
			CRequestRoleIsEnemy requestRoleIsEnemy = new CRequestRoleIsEnemy(newAddRoleList);
			role.sendProtocol(requestRoleIsEnemy);
			newAddRoleList.clear();
		}
	}

	/**
	 * 通过切格得到的新角色
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午2:43:04
	 * @version 1.0
	 * @param roleList
	 */
	public void getNewAddRole(java.util.LinkedList<fire.pb.move.RoleBasic> rolelist) {
		for (RoleBasic roleBasic : rolelist) {
			RoleBasicOctets octets = new RoleBasicOctets();
			Long roleId = 0l;
			try {
				octets.unmarshal(new OctetsStream(roleBasic.rolebasicoctets));
				roleId = octets.roleid;
			} catch (MarshalException e) {
				e.printStackTrace();
			}
			if (enemyList.contains(roleId)) {
				continue;
			}
			if (frendList.contains(roleId)) {
				continue;
			}
			newAddRoleList.add(roleId);
		}
	}

	/**
	 * 解析敌对关系
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午5:04:29
	 * @version 1.0
	 * @param sRequestRoleIsEnemy
	 */
	public void jieXiRoleisenemy(SRequestRoleIsEnemy sRequestRoleIsEnemy) {
		java.util.HashMap<Long, Integer> roleMap = sRequestRoleIsEnemy.rolelist;
		for (Entry<Long, Integer> entry : roleMap.entrySet()) {
			Long roleId = entry.getKey();
			int state = entry.getValue();
			if (state == 1) {
				// frendList.add(roleId);
			} else if (state == 2) {
				enemyList.add(roleId);
			}
		}
	}

	// 移动到指定的位置
	private void roleMove() {
		if (isReach)
			return;
		if (targetpos == null) {
			LogicPos startpos = new LogicPos(role.getPosX(), role.getPosY());
			targetpos = getRandomPos();
			CRoleMove rolemove = new CRoleMove();
			rolemove.sceneid = role.getSceneID();
			rolemove.srcpos = startpos.toProtocolPos();
			rolemove.destpos = targetpos.toProtocolPos();
			role.sendProtocol(rolemove);
		} else {
			LogicPos nowpos = new LogicPos(role.getPosX(), role.getPosY());
			LogicPos nextcheckpos = getNextCheckPos(nowpos, targetpos);
			role.setPos(nextcheckpos.getX(), nextcheckpos.getY());
			CCheckMove checkmove = new CCheckMove();
			checkmove.sceneid = role.getSceneID();
			checkmove.curpos = nextcheckpos.toProtocolPos();
			role.sendProtocol(checkmove);
			if (nextcheckpos.equals(targetpos)) {
				roleStop();
				isReach = true;
			}
		}
	}

	/**
	 * 随机一个位置
	 * 
	 * @author yangtao
	 * @dateTime 2016年8月10日 下午5:36:49
	 * @version 1.0
	 * @return
	 */
	static private LogicPos getRandomPos() {
		int index = Misc.getRandomBetween(0, randomPos1616.size() - 1);
		return randomPos1616.get(index);
	}

	private void roleStop() {
		CRoleStop rolestop = new CRoleStop();
		rolestop.pos = new LogicPos(role.getPosX(), role.getPosY()).toProtocolPos();
		role.sendProtocol(rolestop);
	}

	private LogicPos getNextCheckPos(LogicPos nowpos, LogicPos targetpos) {
		int deltax = targetpos.getX() - nowpos.getX();
		int deltay = targetpos.getY() - nowpos.getY();
		int targetx = targetpos.getX();
		int targety = targetpos.getY();
		if (Math.abs(deltax) > Constant.MOVE_SPEED)
			targetx = nowpos.getX() + ((deltax > 0) ? Constant.MOVE_SPEED : -Constant.MOVE_SPEED);
		if (Math.abs(deltay) > Constant.MOVE_SPEED)
			targety = nowpos.getY() + ((deltay > 0) ? Constant.MOVE_SPEED : -Constant.MOVE_SPEED);
		return new LogicPos(targetx, targety);
	}
}
