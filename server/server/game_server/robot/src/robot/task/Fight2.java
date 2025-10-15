package robot.task;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import robot.ConfigMgr;
import robot.Constant;
import robot.LoginRole;
import robot.Misc;
import robot.pos.LogicPos;
import mkio.Protocol;
import fire.pb.battle.CSendAction;
import fire.pb.battle.CSendRoundPlayEnd;
import fire.pb.battle.OperationType;
import fire.pb.battle.SSendBattleEnd;
import fire.pb.battle.SSendBattleStart;
import fire.pb.battle.SSendRoundPlayEnd;
import fire.pb.battle.SSendRoundScript;
import fire.pb.battle.SSendRoundStart;
import fire.pb.move.CCheckMove;
import fire.pb.move.CRoleMove;
import fire.pb.move.CRoleStop;
import fire.pb.move.SRoleEnterScene;

/**
 * @author dc@20150730
 */
public class Fight2 extends Task_RoleBase {
	private BlockingQueue<mkio.Protocol> protocols = new LinkedBlockingQueue<mkio.Protocol>();
	// private static final int OPERATION_PERIOD = 4000;
	// private long lastOperateTime = 0;
	@SuppressWarnings("unused")
	private int battleCount = 0;
	@SuppressWarnings("unused")
	private int roundCount = 0;
	private LogicPos targetpos = null;
	private byte state = 0; // 0空闲状态 1行走状态 2战斗状态 3发送战斗

	private int index = 0;

	static private Vector<LogicPos> randomPos = new Vector<LogicPos>();
	@SuppressWarnings("unused")
	static private Vector<LogicPos> randomPos2 = new Vector<LogicPos>();

	static private Vector<Integer> randomMaps = new Vector<>();

	public static final int MAPINDEX[] = new int[] { 1601, 1624, 1625 };

	public Fight2(LoginRole role, int index) {
		super(role);
		this.index = index;
		init();
	}

	private void init() {
		// 调整等级
		if (role.getLevel() <= 5 && index < 310) {
			role.sendCommand("//addlevel " + (5 - role.getLevel()) + " equip");
			// 设置伙伴用于战斗
			// role.sendCommand("//addjclienthb");
		} else if (role.getLevel() <= 15 && index < 320) {
			role.sendCommand("//addlevel " + (15 - role.getLevel()) + " equip");
		} else if (role.getLevel() <= 30) {
			role.sendCommand("//addlevel " + (30 - role.getLevel()) + " equip");
		}

		// 选择一些挂机的地图和坐标来测试机器人
		// 1601 1624 1625
		randomPos.add(new LogicPos(59 * 16, 42 * 16));
		randomPos.add(new LogicPos(43 * 16, 52 * 16));
		randomPos.add(new LogicPos(39 * 16, 61 * 16));
		randomPos.add(new LogicPos(40 * 16, 78 * 16));
		randomPos.add(new LogicPos(58 * 16, 82 * 16));
		randomPos.add(new LogicPos(70 * 16, 73 * 16));
		randomPos.add(new LogicPos(60 * 16, 42 * 16));

		randomMaps.add(1601);
		randomMaps.add(1624);
		randomMaps.add(1625);

	}

	static private LogicPos getRandomPos(long mapid) {
		int index = Misc.getRandomBetween(0, randomPos.size() - 1);
		return randomPos.get(index);
	}

	static private int getRandomMap() {
		int index = Misc.getRandomBetween(0, randomMaps.size() - 1);
		return randomMaps.get(index);
	}

	@Override
	public void start() {
		int randomMap = getRandomMap();
		if (index < 310) {
			randomMap = 1601;
		} else if (index < 320) {
			randomMap = 1624;
		} else {
			randomMap = 1625;
		}

		// randomMap = 1608;
		role.sendCommand("//gomap " + randomMap);
		// mkdb.Trace.info("sendCommand:goto 18 75 1617");
	}

	@Override
	public final void stop() {
		if (state == 1) {
			roleStop();
		}
		end();
	}

	@Override
	public void processProtocol(Protocol p) {
		// mkdb.Trace.info("processProtocol() =>" + p.str());
		synchronized (protocols) {
			protocols.offer(p);
		}
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);

		dealProtocols();
		if (state == 0) {
			end();
			// if (isBattleStart()) {
			// GMBattleStart(8500);
			// } else
			roleMove();
		} else if (state == 1) {
			// if (isBattleStart()) {
			// GMBattleStart(8500);
			// } else
			checkMove();
		} else if (state == 2) {
			return;
		}
		// else if (state == 3) {
		// return;
		// }
		else {
			end();
		}
	}

	// private void GMBattleStart(int battleId) {
	// state = 3;
	// //role.sendCommand("//full");
	// role.sendCommand("//battle start " + battleId);
	// mkdb.Executor.getInstance().schedule(new Runnable() {
	// @Override
	// public void run() {
	// if (state != 2) {
	// // mkdb.Trace.info("role[" + role.roleId +
	// "]BattleStart Failed!Server didn't response.");
	// end();
	// } else {
	// //mkdb.Trace.info("role[" + role.roleId + "]BattleStart Success!");
	// }
	// }
	// }, 5, TimeUnit.SECONDS);
	// }

	private void roleMove() {
		if (targetpos == null) {
			LogicPos startpos = new LogicPos(role.getPosX(), role.getPosY());
			targetpos = getRandomPos(role.getSceneID());
			/*
			 * if(startpos.equals(new Position(1946, 1513))){ targetpos = new
			 * Position(288, 1200); } else { targetpos = new Position(1946,
			 * 1513); }
			 */
			CRoleMove rolemove = new CRoleMove();
			rolemove.sceneid = role.getSceneID();
			rolemove.srcpos = startpos.toProtocolPos();
			rolemove.destpos = targetpos.toProtocolPos();
			// mkdb.Trace.info("state=" + state + "send CRoleMove:srcpos.x=" +
			// startpos.getX() + ",srcpos.y=" + startpos.getY() +
			// ",targetpos.x=" + targetpos.getX() + ",targetpos.y=" +
			// targetpos.getY());
			role.sendProtocol(rolemove);
			state = 1;
		}
	}

	private void checkMove() {
		if (targetpos != null) {
			LogicPos nowpos = new LogicPos(role.getPosX(), role.getPosY());
			LogicPos nextcheckpos = getNextCheckPos(nowpos, targetpos);
			role.setPos(nextcheckpos.getX(), nextcheckpos.getY());
			CCheckMove checkmove = new CCheckMove();
			checkmove.sceneid = role.getSceneID();
			checkmove.curpos = nextcheckpos.toProtocolPos();
			// mkdb.Trace.info("state=" + state + "send CCheckMove:curpos.x=" +
			// nextcheckpos.getX() + ",curpos.y=" + nextcheckpos.getY());
			role.sendProtocol(checkmove);
			if (nextcheckpos.equals(targetpos)) {
				roleStop();
				end();
			}
		}
	}

	private void roleStop() {
		CRoleStop rolestop = new CRoleStop();
		rolestop.pos = new LogicPos(role.getPosX(), role.getPosY())
				.toProtocolPos();
		// mkdb.Trace.info("state=" + state + "send CRoleStop:pos.x=" +
		// rolestop.pos.x + ",pos.y=" + rolestop.pos.y);
		role.sendProtocol(rolestop);
	}

	private void dealProtocols() {
		synchronized (protocols) {
			Protocol p = protocols.poll();
			if (p == null) {
				return;
			}
			if (p instanceof SSendBattleStart) {
				state = 2;
				role.sendCommand("//full");
				// mkdb.Trace.info("role[" + role.roleId +
				// "]recv:SSendBattleStart====战斗开始！");
			} else if (p instanceof SSendRoundStart) {
				state = 2;
				// mkdb.Trace.info("role[" + role.roleId +
				// "]recv:SSendRoundStart====回合开始！");
				// 简单模拟客户端玩家操作,3秒后反馈给服务端
				++roundCount;
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
				}, 4, TimeUnit.SECONDS);
			} else if (p instanceof SSendRoundScript) {
				state = 2;
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
				}, 6, TimeUnit.SECONDS);
			} else if (p instanceof SSendRoundPlayEnd) {
				state = 2;
				// mkdb.Trace.info("role[" + role.roleId +
				// "]recv：SSendRoundPlayEnd====确认本回合战斗动画结束！");
			} else if (p instanceof SSendBattleEnd) {
				++battleCount;
				state = 0;
				// mkdb.Trace.info("role[" + role.roleId +
				// "]recv：SSendBattleEnd====战斗结束！roundCount:" + roundCount +
				// ",battleCount:" + battleCount);
				roundCount = 0;
				// role.sendCommand("//battle start 8500");
			} else if (p instanceof SRoleEnterScene) {
				role.setScene(((SRoleEnterScene) p).sceneid);
				role.setPos(((SRoleEnterScene) p).destpos.x,
						((SRoleEnterScene) p).destpos.y);
				// mkdb.Trace.info("state=" + state + "deal SRoleEnterScene:" +
				// ((SRoleEnterScene)p).destpos.x + "," +
				// ((SRoleEnterScene)p).destpos.y);
				end();
			}
		}
	}

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

	private void end() {
		targetpos = null;
		state = 0;
	}
	/*
	 * public static final int randomdelta = 3 * 16; private Position
	 * randomTargetPos(Position startpos) { int deltax =
	 * RandValue.randRanger(-randomdelta, randomdelta); int deltay =
	 * RandValue.randRanger(-randomdelta, randomdelta); Position target = new
	 * Position( Math.min(Short.MAX_VALUE, Math.max(0, startpos.getX() +
	 * deltax)), Math.min(Short.MAX_VALUE, Math.max(0, startpos.getY() +
	 * deltay)) ); return target; }
	 */
	// private boolean isBattleStart(){
	// return Misc.getRandomBetween(1, 1000) >= 980;
	// }
}
