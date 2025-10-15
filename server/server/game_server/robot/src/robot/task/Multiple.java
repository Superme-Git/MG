package robot.task;

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

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import robot.ConfigMgr;
import robot.Constant;
import robot.LoginRole;
import robot.Misc;
import robot.pos.LogicPos;
import robot.ride.PetUtils;
import mkio.Protocol;

public class Multiple extends Task_RoleBase {

	private BlockingQueue<mkio.Protocol> protocols = new LinkedBlockingQueue<mkio.Protocol>();

	// private static final int OPERATION_PERIOD = 4000;
	// private long lastOperateTime = 0;
	@SuppressWarnings("unused")
	private int battleCount = 0;

	@SuppressWarnings("unused")
	private int roundCount = 0;

	private LogicPos targetpos = null;

	private byte state = 0; // 0空闲状态 1行走状态 2战斗状态 3发送战斗

	static private Vector<LogicPos> randomPos = new Vector<LogicPos>();

	static private Vector<LogicPos> randomPos2 = new Vector<LogicPos>();

	static private Vector<LogicPos> randomPos3 = new Vector<LogicPos>();

	static private Vector<Integer> randomMaps = new Vector<>();

	boolean isNet = false;

	public Multiple(LoginRole role) {
		super(role);
		init();
	}

	public Multiple(LoginRole role, boolean isNet) {
		super(role);
		this.isNet = isNet;
		initNet();
	}

	/**
	 * 外网用
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月4日 下午6:59:43
	 * @version 1.0
	 */
	private void initNet() {
		// 调整等级
		if (role.getLevel() <= 40) {
			role.sendCommand("//addlevel " + (40 - role.getLevel()) + " equip");
			// 设置伙伴用于战斗
			role.sendCommand("//addjclienthb");
		}
		// 选择一些挂机的地图和坐标来测试机器人
		// 银堡一层（1629）
		randomPos.add(new LogicPos(92 * 16, 51 * 16));
		randomPos.add(new LogicPos(60 * 16, 36 * 16));
		randomPos.add(new LogicPos(70 * 16, 36 * 16));
		randomPos.add(new LogicPos(80 * 16, 34 * 16));
		randomPos.add(new LogicPos(62 * 16, 52 * 16));
		randomPos.add(new LogicPos(70 * 16, 40 * 16));
		randomPos.add(new LogicPos(25 * 16, 80 * 16));
		randomPos.add(new LogicPos(26 * 16, 60 * 16));
		// 废矿三层（1626~1628）
		randomPos2.add(new LogicPos(22 * 16, 22 * 16));
		randomPos2.add(new LogicPos(27 * 16, 19 * 16));
		randomPos2.add(new LogicPos(47 * 16, 13 * 16));
		randomPos2.add(new LogicPos(63 * 16, 26 * 16));
		randomPos2.add(new LogicPos(75 * 16, 29 * 16));
		randomPos2.add(new LogicPos(88 * 16, 38 * 16));
		randomPos2.add(new LogicPos(96 * 16, 56 * 16));
		randomPos2.add(new LogicPos(87 * 16, 84 * 16));
		randomPos2.add(new LogicPos(67 * 16, 87 * 16));
		randomPos2.add(new LogicPos(45 * 16, 76 * 16));
		randomPos2.add(new LogicPos(33 * 16, 62 * 16));
		// ==========
		randomPos3.add(new LogicPos(37 * 16, 71 * 16));
		randomPos3.add(new LogicPos(56 * 16, 78 * 16));
		randomPos3.add(new LogicPos(71 * 16, 70 * 16));
		randomPos3.add(new LogicPos(72 * 16, 65 * 16));
		randomPos3.add(new LogicPos(75 * 16, 29 * 16));
		randomPos3.add(new LogicPos(77 * 16, 45 * 16));
		randomPos3.add(new LogicPos(59 * 16, 42 * 16));
		randomPos3.add(new LogicPos(34 * 16, 33 * 16));
		randomPos3.add(new LogicPos(65 * 16, 15 * 16));
		randomPos3.add(new LogicPos(35 * 16, 14 * 16));
		// 溶洞三层（1624，,1625）
		randomMaps.add(1624);
		randomMaps.add(1625);
		randomMaps.add(1626);
		randomMaps.add(1627);
		randomMaps.add(1628);
		randomMaps.add(1629);
		// 随机一个宠物
		int petid = PetUtils.getNetRandomPetId();
		role.sendCommand("//addpet " + petid + " 45");
	}

	private void init() {
		// 调整等级
		if (role.getLevel() <= 40) {
			role.sendCommand("//addlevel " + (40 - role.getLevel()) + " equip");
			// 设置伙伴用于战斗
			role.sendCommand("//addjclienthb");
		}
		// 选择一些挂机的地图和坐标来测试机器人
		// 银堡一层（1629）银堡二层（1630）银堡三层（1631）
		// 埋骨圣地一层（1603） 埋骨圣地二层（1637） 埋骨圣地三层（1638）
		randomPos.add(new LogicPos(34 * 16, 14 * 16));
		randomPos.add(new LogicPos(62 * 16, 15 * 16));
		randomPos.add(new LogicPos(58 * 16, 42 * 16));
		randomPos.add(new LogicPos(47 * 16, 50 * 16));
		randomPos.add(new LogicPos(33 * 16, 32 * 16));
		randomPos.add(new LogicPos(39 * 16, 76 * 16));
		randomPos.add(new LogicPos(60 * 16, 80 * 16));
		randomPos.add(new LogicPos(71 * 16, 72 * 16));
		// 教堂一层（1634） 教堂二层（1635） 教堂三层（1636）
		// 遗迹一层（1639）遗迹二层（1640） 遗迹三层（1641）
		randomPos2.add(new LogicPos(51 * 16, 11 * 16));
		randomPos2.add(new LogicPos(65 * 16, 27 * 16));
		randomPos2.add(new LogicPos(93 * 16, 50 * 16));
		randomPos2.add(new LogicPos(76 * 16, 57 * 16));
		randomPos2.add(new LogicPos(66 * 16, 86 * 16));
		randomPos2.add(new LogicPos(38 * 16, 101 * 16));
		randomPos2.add(new LogicPos(7 * 16, 103 * 16));
		randomPos2.add(new LogicPos(9 * 16, 68 * 16));
		randomPos2.add(new LogicPos(23 * 16, 48 * 16));
		randomPos2.add(new LogicPos(54 * 16, 49 * 16));
		// 银堡一层（1629）银堡二层（1630）银堡三层（1631）
		// 埋骨圣地一层（1603） 埋骨圣地二层（1637） 埋骨圣地三层（1638）
		// 教堂一层（1634） 教堂二层（1635） 教堂三层（1636）
		// 遗迹一层（1639）遗迹二层（1640） 遗迹三层（1641）
		randomMaps.add(1629);
		randomMaps.add(1630);
		randomMaps.add(1631);
		randomMaps.add(1603);
		randomMaps.add(1637);
		randomMaps.add(1638);
		randomMaps.add(1634);
		randomMaps.add(1635);
		randomMaps.add(1636);
		randomMaps.add(1639);
		randomMaps.add(1640);
		randomMaps.add(1641);
		// 随机一个宠物
		int petid = PetUtils.getRandomPetId();
		role.sendCommand("//addpet " + petid + " 99");
	}

	private LogicPos getRandomPos(long mapid) {
		if (isNet) {
			return getRandomPosNet(mapid);
		}
		if (mapid == 1629 || mapid == 1630 || mapid == 1631 || mapid == 1603 || mapid == 1637 || mapid == 1638) {
			// 银堡一层（1629）银堡二层（1630）银堡三层（1631）
			// 埋骨圣地一层（1603） 埋骨圣地二层（1637） 埋骨圣地三层（1638）
			int index = Misc.getRandomBetween(0, randomPos.size() - 1);
			return randomPos.get(index);
		} else {
			int index = Misc.getRandomBetween(0, randomPos.size() - 1);
			return randomPos2.get(index);
		}
	}

	/**
	 * 外网随机指定地图随机坐标
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月4日 下午7:49:46
	 * @version 1.0
	 * @param mapid
	 * @return
	 */
	static private LogicPos getRandomPosNet(long mapid) {
		// 溶洞三层（1624，,1625）
		if (mapid == 1624 || mapid == 1625) {
			int index = Misc.getRandomBetween(0, randomPos3.size() - 1);
			return randomPos3.get(index);
		} else if (mapid == 1626 || mapid == 1627 || mapid == 1628) {
			int index = Misc.getRandomBetween(0, randomPos2.size() - 1);
			return randomPos2.get(index);
		} else {
			int index = Misc.getRandomBetween(0, randomPos.size() - 1);
			return randomPos.get(index);
		}
	}

	static private int getRandomMap() {
		int index = Misc.getRandomBetween(0, randomMaps.size() - 1);
		return randomMaps.get(index);
	}

	@Override
	public void start() {
		int randomMap = getRandomMap();
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
		rolestop.pos = new LogicPos(role.getPosX(), role.getPosY()).toProtocolPos();
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
				if (!isNet)
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
				}, 10, TimeUnit.SECONDS);
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
				role.setPos(((SRoleEnterScene) p).destpos.x, ((SRoleEnterScene) p).destpos.y);
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
			targetx = nowpos.getX() + ((deltax > 0) ? Constant.MOVE_SPEED : -Constant.MOVE_SPEED);
		if (Math.abs(deltay) > Constant.MOVE_SPEED)
			targety = nowpos.getY() + ((deltay > 0) ? Constant.MOVE_SPEED : -Constant.MOVE_SPEED);
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
