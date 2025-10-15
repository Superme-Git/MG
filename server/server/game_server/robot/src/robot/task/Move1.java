package robot.task;

import fire.pb.move.CCheckMove;
import fire.pb.move.CRoleMove;
import fire.pb.move.CRoleStop;
import fire.pb.move.SRoleEnterScene;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import robot.Constant;
import robot.LoginRole;
import robot.Misc;
import robot.pos.LogicPos;
import mkio.Protocol;

/**
 * 
 * @author dc@20150730
 *
 */
public class Move1 extends Task_RoleBase {
	private BlockingQueue<mkio.Protocol> protocols = new LinkedBlockingQueue<mkio.Protocol>();
	private LogicPos targetpos = null;
	private byte state = 0; // 0空闲状态 1行走状态

	static private Vector<LogicPos> randomPos = new Vector<LogicPos>();
	static private Vector<LogicPos> randomPos1617 = new Vector<LogicPos>();
	static private Vector<LogicPos> randomPos1609 = new Vector<LogicPos>();
	static private Vector<LogicPos> randomPos1616 = new Vector<LogicPos>();

	static private Vector<Integer> randomMaps = new Vector<>();

	private int index = 0;

	public Move1(LoginRole role, int index) {
		super(role);
		this.index = index;
		init();
	}

	private void init() {
		// 调整等级
		// if (role.getLevel() <= 60) {
		// role.sendCommand("//addlevel " + (60 - role.getLevel()) + " equip");
		// }

		// 1617
		randomPos1617.add(new LogicPos(29 * 16, 37 * 16));
		randomPos1617.add(new LogicPos(51 * 16, 21 * 16));
		randomPos1617.add(new LogicPos(56 * 16, 45 * 16));
		randomPos1617.add(new LogicPos(66 * 16, 34 * 16));
		randomPos1617.add(new LogicPos(40 * 16, 29 * 16));
		randomPos1617.add(new LogicPos(50 * 16, 32 * 16));
		randomPos1617.add(new LogicPos(47 * 16, 42 * 16));

		// 1618 1619 1620 1621 1622
		randomPos.add(new LogicPos(25 * 16, 43 * 16));
		randomPos.add(new LogicPos(47 * 16, 18 * 16));
		randomPos.add(new LogicPos(56 * 16, 45 * 16));
		randomPos.add(new LogicPos(55 * 16, 44 * 16));
		randomPos.add(new LogicPos(35 * 16, 24 * 16));
		randomPos.add(new LogicPos(56 * 16, 27 * 16));
		randomPos.add(new LogicPos(63 * 16, 37 * 16));
		randomPos.add(new LogicPos(42 * 16, 49 * 16));

		// 1609 1605 1606 1608 1611
		randomPos1609.add(new LogicPos(68 * 16, 86 * 16));
		randomPos1609.add(new LogicPos(53 * 16, 64 * 16));
		randomPos1609.add(new LogicPos(37 * 16, 38 * 16));
		randomPos1609.add(new LogicPos(14 * 16, 18 * 16));
		randomPos1609.add(new LogicPos(11 * 16, 74 * 16));
		randomPos1609.add(new LogicPos(36 * 16, 92 * 16));
		randomPos1609.add(new LogicPos(59 * 16, 86 * 16));
		randomPos1609.add(new LogicPos(98 * 16, 53 * 16));

		// 1616 1615 1614
		randomPos1616.add(new LogicPos(43 * 16, 143 * 16));
		randomPos1616.add(new LogicPos(28 * 16, 145 * 16));
		randomPos1616.add(new LogicPos(62 * 16, 18 * 16));
		randomPos1616.add(new LogicPos(84 * 16, 88 * 16));
		randomPos1616.add(new LogicPos(98 * 16, 92 * 16));
		randomPos1616.add(new LogicPos(96 * 16, 106 * 16));
		randomPos1616.add(new LogicPos(99 * 16, 77 * 16));
		randomPos1616.add(new LogicPos(126 * 16, 36 * 16));
		randomPos1616.add(new LogicPos(139 * 16, 43 * 16));
		randomPos1616.add(new LogicPos(118 * 16, 151 * 16));
		randomPos1616.add(new LogicPos(140 * 16, 168 * 16));

		//

		// randomMaps.add(1617);//

	}

	static private LogicPos getRandomPos(long mapid) {
		if (mapid == 1617) {
			int index = Misc.getRandomBetween(0, randomPos1617.size() - 1);
			return randomPos1617.get(index);
		} else if (mapid == 1609 || mapid == 1605 || mapid == 1606
				|| mapid == 1608 || mapid == 1611) {
			int index = Misc.getRandomBetween(0, randomPos1609.size() - 1);
			return randomPos1609.get(index);
		} else if (mapid == 1618 || mapid == 1619 || mapid == 1620
				|| mapid == 1621 || mapid == 1622) {
			// 1618 1619 1620 1621 1622
			int index = Misc.getRandomBetween(0, randomPos.size() - 1);
			return randomPos.get(index);
		} else {
			int index = Misc.getRandomBetween(0, randomPos1616.size() - 1);
			return randomPos1616.get(index);
		}
	}

	@SuppressWarnings("unused")
	static private int getRandomMap() {
		int index = Misc.getRandomBetween(0, randomMaps.size() - 1);
		return randomMaps.get(index);
	}

	public static final int MAPINDEX[] = new int[] { 1617, 1618, 1619, 1620,
			1621, 1622, 1606, 1609, 1608, 1615, 1605, 1611, 1614, 1616, 1615 };

	@Override
	public void start() {
		// int curmap = getRandomMap();
		int mindex = index / 20;
		if (mindex > MAPINDEX.length - 1) {
			mindex = MAPINDEX.length - 1;
		}
		int curmap = MAPINDEX[mindex];
		// mkdb.Trace.info("当前地图======"+curmap);
		role.sendCommand("//gomap " + curmap);
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
			roleMove();
		} else if (state == 1) {
			checkMove();
		} else {
			end();
		}
	}

	private void roleMove() {
		if (targetpos == null) {
			LogicPos startpos = new LogicPos(role.getPosX(), role.getPosY());
			targetpos = getRandomPos(role.getSceneID());

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
			// mkdb.Trace.info("state=" + state +
			// "场景id\t"+checkmove.sceneid+"\tsend CCheckMove:curpos.x=" +
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
		// mkdb.Trace.info("state=" + state+ "场景id\t"+rolestop.sceneid +
		// "\tsend CRoleStop:pos.x=" + rolestop.pos.x + ",pos.y=" +
		// rolestop.pos.y);
		role.sendProtocol(rolestop);
	}

	private void dealProtocols() {
		synchronized (protocols) {
			Protocol p = protocols.poll();
			if (p == null) {
				return;
			}
			if (p instanceof SRoleEnterScene) {
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
}
