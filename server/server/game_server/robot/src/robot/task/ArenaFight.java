package robot.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sun.javafx.geom.Area;

import robot.ConfigMgr;
import robot.Constant;
import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.pos.LogicPos;
import robot.team.Team;
import robot.team.TeamMgr;
import robot.team.TeamRoleState;
import mkio.Protocol;
import fire.pb.battle.CSendAction;
import fire.pb.battle.CSendRoundPlayEnd;
import fire.pb.battle.OperationType;
import fire.pb.battle.SSendBattleEnd;
import fire.pb.battle.SSendBattleStart;
import fire.pb.battle.SSendRoundPlayEnd;
import fire.pb.battle.SSendRoundScript;
import fire.pb.battle.SSendRoundStart;
import fire.pb.battle.pvp1.CPvP1ReadyFight;
import fire.pb.battle.pvp3.CPvP3ReadyFight;
import fire.pb.battle.pvp3.SPvP3ReadyFight;
import fire.pb.battle.pvp5.SPvP5ReadyFight;
import fire.pb.move.CCheckMove;
import fire.pb.move.CRoleMove;
import fire.pb.move.CRoleStop;
import fire.pb.move.SRoleEnterScene;
import fire.pb.npc.CGeneralSummonCommand;
import fire.pb.npc.SGeneralSummonCommand;
import fire.pb.npc.TransmitTypes;
import fire.pb.team.CAbsentReturnTeam;
import fire.pb.team.CAcceptToTeam;
import fire.pb.team.CCreateTeam;
import fire.pb.team.CRequestJoinTeam;
import fire.pb.team.SAddTeamApply;

/**
 * 竞技场机器人(包括1v1,3v3)
 * 
 * @author Bill_Ye 2016年1月14日 下午9:12:19
 */
public class ArenaFight extends Task_RoleBase {
	private static AtomicBoolean IS_ARENA_TEST_OPEN = new AtomicBoolean(false); // 竞技场测试是否开启
	private static AtomicBoolean IS_ARENA_TEST_TIME_SET = new AtomicBoolean(
			false); // 时间是否已经设置
	private static int ARENA_TYPE = 0; // 竞技场种类 0为单人竞技场 1为组队竞技场
	private static int GRID_SIZE = 16; // 格子大小

	private BlockingQueue<mkio.Protocol> protocols = new LinkedBlockingQueue<mkio.Protocol>(); // 消息队列

	public static final int ARENA_TYPE_SINGLE = 0; //单人
	public static final int ARENA_TYPE_TEAM_3 = 1; //3v3
	public static final int ARENA_TYPE_TEAM_5 = 2; //5v5

	private static final byte STATE_ARENA_IDLE = 0; // 空闲
	private static final byte STATE_ARENA_JOIN = 1; // 参加
	private static final byte STATE_ARENA_MOVE = 2; // 移动
	private static final byte STATE_ARENA_COMP = 3; // 匹配
	private static final byte STATE_ARENA_FIGHT = 4; // 战斗
	private static final byte STATE_ARENA_TEAM = 5; // 组队
	private static final byte STATE_ARENA_REST = 6; // 重置状态

	private static final int ARENA_OPEN_MINUTES = 5;
	private byte roleState = STATE_ARENA_IDLE;
	private int roundCount = 0;
	private int battleCount = 0;

	private static long lastResetArenaTime = -1; // 最后一次重置竞技场的时间
	private static long lastRequestTime = -1; // 最后一次有人申请组队的时间
	private boolean isrequesting = false; // 是否在申请组队
	private long requesttime = 0; // 申请组队时间
	public static long REQUEST_PERIOD = 30000; // 申请间隔时间

	private LogicPos targetpos = null;

	public ArenaFight(LoginRole role, int arenaType) {
		super(role);
		setArenaType(arenaType);
		initArena();
	}

	/**
	 * 竞技场类型设置 如:1v1 3v3
	 * 
	 * @param arenaType
	 */
	private void setArenaType(int arenaType) {
		ARENA_TYPE = arenaType;
	}

	/**
	 * 竞技场调试模式开启
	 */
	private void initArena() {
//		// 调整等级
//		if (role.getLevel() < 52) {
//		
//			role.sendCommand("//addlevel " + (52 - role.getLevel()) + " equip");
//		}
		// 调整等级 属性
		int curLv = role.getLevel();
		if (curLv < 40) {
			int level = (int) (Math.random() * 60 + 40);
			if (role.getLevel() < level) {
				role.sendCommand("//addlevel " + (level - role.getLevel()));
				role.sendCommand("//addjclienthb");
			}
		}

		// 初始化竞技场参数
		mkdb.Executor.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				setArenaTest();
			}
		}, 30, TimeUnit.SECONDS);

	}

	/**
	 * 初始化竞技场参数
	 */
	private synchronized void setArenaTest() {
		long curTime = System.currentTimeMillis();
		if ((curTime - lastResetArenaTime) < (1000 * 60 * ARENA_OPEN_MINUTES)) {
			return;
		}

		if (!IS_ARENA_TEST_OPEN.get()) {
			role.sendCommand("//pvptest 1");
			IS_ARENA_TEST_OPEN.set(true);
			mkdb.Trace.fatal("开启竞技场测试模式");
		}

		if (!IS_ARENA_TEST_TIME_SET.get()) {
			switch (ARENA_TYPE) {
			case ARENA_TYPE_SINGLE: // 1v1
				role.sendCommand("//startact 240001 " + ARENA_OPEN_MINUTES);
				break;
			case ARENA_TYPE_TEAM_3: // 3v3
				role.sendCommand("//startact 241001 " + ARENA_OPEN_MINUTES);
				break;
			case ARENA_TYPE_TEAM_5:
				role.sendCommand("//startact 242001 " + ARENA_OPEN_MINUTES);
				break;
			}
			IS_ARENA_TEST_TIME_SET.set(true);
			mkdb.Trace.fatal("开启竞技场活动 " + ARENA_OPEN_MINUTES + " 分钟");
			lastResetArenaTime = curTime;
		}
	}

	@Override
	public void run() {
		// 发送心跳包
		Global.DoGlobalTask(this);

		// 所有机器人加入锁屏测试
		Global.SendLockScreenMsg(this);

		// 处理协议
		dealProtocols();
		switch (roleState) {
		case STATE_ARENA_JOIN:
			roleMove();
			break;
		case STATE_ARENA_MOVE:
			checkMove();
			break;
		case STATE_ARENA_COMP:
			readyFight();
			break;
		case STATE_ARENA_TEAM:
			absent2return();
			checkTeam();
			break;
		case STATE_ARENA_REST:
			setArenaTest();
			break;
		}
	}

	/**
	 * 竞技场组队
	 */
	private synchronized void checkTeam() {
		if (role.getTeam() != null) {
			if (!role.getTeam().isLeader(role.roleId)) {
				roleState = STATE_ARENA_IDLE;
				mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
						+ role.loginui.getUserName() + "\tINFO=已经加入队伍！");
				return;
			} else {
				if ((ARENA_TYPE == ARENA_TYPE_TEAM_3 && role.getTeam().getMembersNum() >= 2) || (ARENA_TYPE == ARENA_TYPE_TEAM_5 && role.getTeam().getMembersNum() >= 4)) {
					roleState = STATE_ARENA_JOIN;
					mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
							+ role.loginui.getUserName() + "\tINFO=组队成功！");
					return;
				}

				if (lastRequestTime > 0
						&& (System.currentTimeMillis() - lastRequestTime > 3 * 60 * 1000)) {
					roleState = STATE_ARENA_JOIN;
					mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
							+ role.loginui.getUserName() + "\tINFO=组队成功！");
					return;
				}
				processApply();
			}
		} else {
			if (isrequesting) {
				long now = System.currentTimeMillis();
				if ((now - requesttime) > REQUEST_PERIOD) {
					isrequesting = false;
					requesttime = 0;
				} else
					return; // 正在申请不要申请
			}

			if (!role.canMove())
				return;// 不能行走的时候先不去组队

			if (Math.random() < 0.3)
				return;// 30%概率开始组队处理

			int teamNum = TeamMgr.getInstance().getTeams().size();
			if (teamNum < getNeedTeamNum()) {
				if (Math.random() < 0.2) {
					// 建立队伍
					role.sendProtocol(new CCreateTeam());
				} else {
					// 求组队
					requestJoinTeam();
				}
			} else {
				// 不再建立队伍，只去求组队
				requestJoinTeam();
			}
		}
	}

	/**
	 * 队长处理组队请求
	 */
	private void processApply() {
		if (role.getTeam() == null)
			return;
		if (!role.getTeam().isLeader(role.roleId)) {
			return;
		}
		SAddTeamApply p = role.getTeam().pollApply();
		if (p == null)
			return;
		if (!role.canMove())
			return;
		
		switch(ARENA_TYPE) {
		case ARENA_TYPE_TEAM_3:
			if (role.getTeam().getMembersNum() >= 2) {
				role.getTeam().clearApplies();
				return;
			}
			break;
		case ARENA_TYPE_TEAM_5:
			if (role.getTeam().getMembersNum() >= 4) {
				role.getTeam().clearApplies();
				return;
			}
			break;
		}
		
		fire.pb.team.TeamApplyBasic apply = p.applylist.get(0);
		LoginRole applier = LoginRoleMgr.getInstance().getLoginRole(
				apply.roleid);
		if (applier == null)
			return;
		role.gmgoto(applier.getMapId(), applier.getGridX(), applier.getGridX());
		role.sendProtocol(new CAcceptToTeam(apply.roleid, 1));

	}

	/**
	 * 需要多少队伍
	 * 
	 * @return
	 */
	private int getNeedTeamNum() {
		int roleNum = LoginRoleMgr.getInstance().getRoles().size();
		return roleNum / 3 + (roleNum % 3 == 0 ? 0 : 1);
	}

	/**
	 * 求组队
	 */
	private void requestJoinTeam() {
		Set<Entry<Long, Team>> coll = TeamMgr.getInstance().getTeams()
				.entrySet();
		List<Entry<Long, Team>> list = new ArrayList<Entry<Long, Team>>(coll);
		Collections.shuffle(list);
		for (Entry<Long, Team> team : list) {
			if (team.getValue() == null) {
				continue;
			}

			long leaderId = team.getValue().leaderRoleId;
			if (!isInSameLevel(leaderId)) {
				continue;
			}

			if ((ARENA_TYPE == ARENA_TYPE_TEAM_3 && team.getValue().getMembersNum() >= 2) || (ARENA_TYPE == ARENA_TYPE_TEAM_5 && team.getValue().getMembersNum() >= 4))
				continue;

			role.sendProtocol(new CRequestJoinTeam(leaderId));
			isrequesting = true;
			requesttime = System.currentTimeMillis();
			lastRequestTime = requesttime;
			break;
		}
	}

	/**
	 * 是不是在同一个层次的竞技场
	 * 
	 * @param leaderRoleId
	 * @return
	 */
	private boolean isInSameLevel(long leaderRoleId) {
		LoginRole role2 = LoginRoleMgr.getInstance().getLoginRole(leaderRoleId);
		if (role2 == null) {
			return false;
		}
		return getPvPTeamLvl(role.getLevel()) == getPvPTeamLvl(role2.getLevel());
	}

	/**
	 * 获得竞技场层次 3V3
	 * 
	 * @param level
	 * @return
	 */
	private static int getPvPTeamLvl(int level) {
		switch (ARENA_TYPE) {
		case ARENA_TYPE_SINGLE:
			return -1;
		case ARENA_TYPE_TEAM_3:
			if (level >= 45 && level < 70) {
				return 1;
			} else if (level >= 70 && level < 90) {
				return 2;
			} else if (level >= 90 && level < 1000) {
				return 3;
			}
			return -1;
		case ARENA_TYPE_TEAM_5:
			if (level >= 50 && level < 70) {
				return 1;
			} else if (level >= 70 && level < 90) {
				return 2;
			} else if (level >= 90 && level < 1000) {
				return 3;
			}
			return -1;
		default:
			return -1;
		}
		
	}

	/**
	 * 竞技场排队
	 */
	private void readyFight() {
		switch (ARENA_TYPE) {
		case ARENA_TYPE_SINGLE:
			CPvP1ReadyFight crfs = new CPvP1ReadyFight((byte) 1);
			role.sendProtocol(crfs);
			roleState = STATE_ARENA_IDLE;
			mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
					+ role.loginui.getUserName() + "\tINFO=正在排队！");
			break;
		case ARENA_TYPE_TEAM_3:
			if (!role.getTeam().isLeader(role.roleId))
				return;
			CPvP3ReadyFight crf = new CPvP3ReadyFight((byte) 1);
			role.sendProtocol(crf);
			roleState = STATE_ARENA_IDLE;
			break;
		case ARENA_TYPE_TEAM_5:
			roleState = STATE_ARENA_IDLE;
			break;
		}
	}

	/**
	 * 停止移动
	 */
	private void roleStop() {
		CRoleStop crs = new CRoleStop();
		crs.pos = new LogicPos(role.getPosX(), role.getPosY()).toProtocolPos();
		role.sendProtocol(crs);
	}

	/**
	 * 寻路
	 */
	private void checkMove() {
		if (targetpos != null) {
			LogicPos nowpos = new LogicPos(role.getPosX(), role.getPosY());
			LogicPos nextcheckpos = getNextCheckPos(nowpos, targetpos);
			role.setPos(nextcheckpos.getX(), nextcheckpos.getY());
			CCheckMove checkmove = new CCheckMove();
			checkmove.sceneid = role.getSceneID();
			checkmove.curpos = nextcheckpos.toProtocolPos();
			role.sendProtocol(checkmove);
			if (nextcheckpos.equals(targetpos)) {
				roleStop();
				targetpos = null;
				roleState = STATE_ARENA_COMP;
			}
		}
	}

	/**
	 * 移动
	 */
	private void roleMove() {
		if (targetpos == null) {
			LogicPos startPos = new LogicPos(role.getPosX(), role.getPosY());
			targetpos = getRandomPos();
			CRoleMove crm = new CRoleMove(startPos.toProtocolPos(),
					targetpos.toProtocolPos(), role.getSceneID());
			role.sendProtocol(crm);
		}
		roleState = STATE_ARENA_MOVE;
	}

	/**
	 * 处理消息
	 */
	private void dealProtocols() {
		synchronized (protocols) {
			Protocol p = protocols.poll();
			if (p == null) {
				return;
			}
			if (p instanceof SSendBattleStart) {
				roundCount = 0;
				roleState = STATE_ARENA_FIGHT;
				if (role.getTeam() == null
						|| role.getTeam().getMembersNum() == 1
						|| role.getTeam().isLeader(role.roleId)) {
					mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
							+ role.loginui.getUserName() + "\tINFO=战斗开始！");
				}
			} else if (p instanceof SSendRoundStart) {
				roleState = STATE_ARENA_FIGHT;
				++roundCount;
				// 简单模拟客户端玩家操作,3秒后反馈给服务端
				mkdb.Executor.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						int skillid = ConfigMgr.getInstance()
								.randSkillByPlayerType(role.school);
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
						CSendAction sndpet = new CSendAction();
						sndpet.isrole = 0;
						sndpet.action.aim = 0;
						sndpet.action.operationtype = OperationType.ACTION_ATTACK;
						role.sendProtocol(sndpet);
					}
				}, 5, TimeUnit.SECONDS);
			} else if (p instanceof SSendRoundScript) {
				roleState = STATE_ARENA_FIGHT;
				// 简单模拟客户端战斗动画播放,5秒后反馈给服务端
				mkdb.Executor.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						role.sendProtocol(new CSendRoundPlayEnd());
					}
				}, 5, TimeUnit.SECONDS);
			} else if (p instanceof SSendRoundPlayEnd) {
				roleState = STATE_ARENA_FIGHT;
			} else if (p instanceof SSendBattleEnd) {
				++battleCount;
				roleState = STATE_ARENA_COMP;

				if (role.getTeam() == null
						|| role.getTeam().getMembersNum() == 1
						|| role.getTeam().isLeader(role.roleId)) {
					mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
							+ role.loginui.getUserName()
							+ "\tINFO=战斗结束,本次战斗经历了" + roundCount + "回合,"
							+ (roundCount > 10 ? "太辛苦了!!" : "太快了~") + "\t共进行了"
							+ battleCount + "场次!");
				}
			} else if (p instanceof SRoleEnterScene) {
				SRoleEnterScene sres = (SRoleEnterScene) p;
				if (sres.sceneid >= 1656 && sres.sceneid <= 1661) {
					if (ARENA_TYPE == ARENA_TYPE_SINGLE) {
						roleState = STATE_ARENA_JOIN;
						mkdb.Trace
								.info("ROLEID=" + role.roleId + "\tUSERID="
										+ role.loginui.getUserName()
										+ "\tINFO=已经进入竞技场");
					} else {
						roleState = STATE_ARENA_TEAM;
						mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
								+ role.loginui.getUserName()
								+ "\tINFO=组队竞技场机器人,开始组队");
					}
				} else {
					// long curScenceId = role.getSceneID();
					// if (curScenceId >= 1656 && curScenceId <= 1661) {
					roleState = STATE_ARENA_REST;
					IS_ARENA_TEST_TIME_SET.set(false);
					mkdb.Trace.fatal("准备重新开启竞技场");
					// }
				}
				role.setScene(sres.sceneid);
				role.setPos(sres.destpos.x, sres.destpos.y);
			} else if (p instanceof SGeneralSummonCommand) {
				IS_ARENA_TEST_OPEN.set(true);
				IS_ARENA_TEST_TIME_SET.set(true);
				CGeneralSummonCommand cgsc = null;
				switch (ARENA_TYPE) {
				case ARENA_TYPE_SINGLE:
					cgsc = new CGeneralSummonCommand(TransmitTypes.singlepvp,
							(long) 0, 1);
					break;
				case ARENA_TYPE_TEAM_3:
					cgsc = new CGeneralSummonCommand(TransmitTypes.pvp3,
							(long) 0, 1);
					break;
				case ARENA_TYPE_TEAM_5:
					cgsc = new CGeneralSummonCommand(TransmitTypes.pvp5,
							(long) 0, 1);
					break;
				}
				role.sendProtocol(cgsc);
				mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
						+ role.loginui.getUserName() + "\tINFO=收到竞技场拉人通知,接受请求");
			} else if (p instanceof SAddTeamApply) {
				if (role.getTeam() == null)
					protocols.clear();
				if (!role.getTeam().isLeader(role.roleId))
					protocols.clear();
				if ((ARENA_TYPE == ARENA_TYPE_TEAM_3 && role.getTeam().getMembersNum() >= 2) || (ARENA_TYPE == ARENA_TYPE_TEAM_5 && role.getTeam().getMembersNum() >= 4))
					protocols.clear();
				role.getTeam().pushApply((SAddTeamApply) p);
			} else if (p instanceof SPvP3ReadyFight) {
				if (role.getTeam() == null
						|| role.getTeam().getMembersNum() == 1
						|| role.getTeam().isLeader(role.roleId)) {
					mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
							+ role.loginui.getUserName() + "\tINFO=正在排队！");
				}
			} else if (p instanceof SPvP5ReadyFight) {
				if (role.getTeam() == null
						|| role.getTeam().getMembersNum() == 1
						|| role.getTeam().isLeader(role.roleId)) {
					mkdb.Trace.info("ROLEID=" + role.roleId + "\tUSERID="
							+ role.loginui.getUserName() + "\tINFO=正在排队！");
				}
			}
		}
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public void processProtocol(Protocol p) {
		// System.out.println(p.getClass().getSimpleName());
		synchronized (protocols) {
			protocols.offer(p);
		}
	}

	/**
	 * 获取一个随机坐标作为目的地
	 * 
	 * @return
	 */
	private LogicPos getRandomPos() {
		int x = (int) (Math.random() * 35 + 30) * GRID_SIZE;
		int y = (int) (Math.random() * 35 + 20) * GRID_SIZE;
		return new LogicPos(x, y);
	}

	/**
	 * 寻路坐标生成
	 * 
	 * @param nowpos
	 * @param targetpos
	 * @return
	 */
	private LogicPos getNextCheckPos(LogicPos nowpos, LogicPos targetpos) {
		int deltax = targetpos.getX() - nowpos.getX();
		int deltay = targetpos.getY() - nowpos.getY();
		int targetx = targetpos.getX();
		int targety = targetpos.getY();
		if (Math.abs(deltax) > Constant.MOVE_SPEED)
			targetx = nowpos.getX()
					+ ((deltax > 0) ? Constant.MOVE_SPEED
							: -Constant.MOVE_SPEED);
		if (Math.abs(deltay) > Constant.MOVE_SPEED)
			targety = nowpos.getY()
					+ ((deltay > 0) ? Constant.MOVE_SPEED
							: -Constant.MOVE_SPEED);
		return new LogicPos(targetx, targety);
	}

	private void absent2return() {
		if (role.getTeam() == null)
			return;
		if (role.getTeamState() != TeamRoleState.TEAM_ABSENT)
			return;
		if (!role.canMove())
			return;
		LoginRole leader = LoginRoleMgr.getInstance().getLoginRole(
				role.getTeam().leaderRoleId);
		if (leader == null)
			return;
		role.gmgoto(leader.getMapId(), leader.getGridX(), leader.getGridX());
		role.sendProtocol(new CAbsentReturnTeam((byte) 0));
	}
}
