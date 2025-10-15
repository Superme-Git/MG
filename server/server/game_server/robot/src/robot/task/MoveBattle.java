package robot.task;

import java.util.LinkedList;
import java.util.List;

import mkio.Protocol;
import robot.Constant;
import robot.LoginRole;
import robot.Main;
import robot.Misc;
import robot.RandValue;
import robot.pos.LogicPos;
import fire.pb.move.CCheckMove;
import fire.pb.move.CRoleMove;
import fire.pb.move.CRoleStop;
import fire.pb.move.SRoleEnterScene;
import fire.pb.move.SRoleJumpDrawback;
import fire.pb.move.SRoleMove;
import fire.pb.talk.CTransChatMessage2Serv;
import fire.pb.talk.ChannelType;
import fire.pb.team.COneKeyTeamMatch;
import fire.pb.team.CRequestTeamMatch;
import fire.pb.team.SRequestTeamMatch;
import fire.pb.team.STeamError;

/**
 * 随机移动任务，还包含了战斗，随机说话功能
 * 
 * @author yebin
 */
public class MoveBattle extends Task_RoleBase {

	// private int index = 0;
	private boolean isChangeMap = true;

	private boolean isSay = true;

	private int num = 0;

	int changeMapDelay = 0;

	boolean isDelay = false;

	boolean isInitGoMap = true;

	public MoveBattle(LoginRole r) {
		super(r);
		init();
	}

	public MoveBattle(LoginRole r, boolean isChangeMap) {
		super(r);
		this.isChangeMap = isChangeMap;
		init();
	}

	public MoveBattle(LoginRole r, boolean isChangeMap, boolean isSay) {
		super(r);
		this.isChangeMap = isChangeMap;
		this.isSay = isSay;
		init();
	}

	public MoveBattle(LoginRole r, boolean isChangeMap, boolean isSay, boolean isInitGoMap) {
		super(r);
		this.isChangeMap = isChangeMap;
		this.isSay = isSay;
		this.isInitGoMap = isInitGoMap;
		init();
	}

	private void init() {
		if (role.canMove() && role.getRoleBag().emptyNumbers() > 0 && !role.canJump() && !role.isInitump()) {
			// 调整等级
			if (role.getLevel() <= 80) {
				role.sendCommand("//addlevel " + (36 - role.getLevel()));
				role.sendCommand("//addhyd 5000");
			}
			// // 调一本技能书
			// if (role.getRoleBag().hasItem(50190)) {
			// role.getRoleBag().useItem(50190);
			// } else {
			// role.sendCommand("//additem 50190");
			// role.setInitump(true);
			// }
		}
		role.sendCommand("//addmoney 1000000000");
		if (isInitGoMap) {
			int newmap = randomInitMap();
			role.sendCommand("//gomap " + newmap);
		}
	}

	private LogicPos targetpos = null;

	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	// -1 空闲状态 0行走状态 1轻功状态
	private byte state = 0;

	@Override
	public void run() {
		// 先处理protocols
		dealProtocols();
		if (isSay)
			randomSay();
		if (!role.canMove()) {// 不能移动
			end();
			return;
		}
		// if (index == Integer.MAX_VALUE) {
		// index = 0;
		// }
		// // 每两秒走动一次
		// if (index % 2 != 0) {
		// index++;
		// return;
		// }
		// index++;
		// roleMove();
		// 空闲状态
		if (state == -1) {
			end();
			if (goMapChance()) {
				// 切换地图
				changeMap();
			} else {
				// if (jumpChance()) {
				// state = 1;
				// index = 0;
				// roleJump();
				// } else {
				state = 0;
				roleMove();
				// }
			}
			// 行走状态
		} else if (state == 0) {
			roleMove();
		} else {
			end();
		}
	}

	/**
	 * 切换地图
	 * 
	 * @author yangtao
	 * @dateTime 2016年5月22日 下午4:07:53
	 * @version 1.0
	 */
	private void changeMap() {
		if (role.getMapId() != 1608) {
			role.sendCommand("//gomap 1608");
			// mkdb.Trace.info("role gomap 1608. userid=" +
			// role.userId);
		} else {
			int newmap = randomInitMap();
			role.sendCommand("//gomap " + newmap);
			// mkdb.Trace.info("role gomap " + newmap + ". userid=" +
			// role.userId);
		}
	}

	private boolean goMapChance() {
		return Misc.getRandomBetween(1, 11) >= 10;
	}

	private int randomInitMap() {
		int index = Misc.getRandomBetween(11, 16);
		if (index == 11) {
			return 1618;
		} else if (index == 12) {
			return 1621;
		} else if (index == 13) {
			return 1620;
		} else if (index == 14) {
			return 1622;
		} else if (index == 15) {
			return 1619;
		} else if (index == 16) {
			return 1617;
		}
		return 1608;
	}

	private void roleMove() {
		if (targetpos == null) {
			// // 加了延迟，为了处理切换地图的时候时间长没有及时返回，造成地图id变了，坐标是错误的，
			// if (isDelay) {
			// changeMapDelay++;
			// if (changeMapDelay % 5 != 0) {
			// return;
			// }
			// isDelay = false;
			// }
			// mkdb.Trace.info("==========roleMove()1===");
			// 开始一次新的随机移动
			LogicPos startpos = new LogicPos(role.getPosX(), role.getPosY());
			targetpos = randomTargetPos(startpos);
			moveToNextPoint(startpos);
			num = 0;
		} else {
			// mkdb.Trace.info("==========roleMove()2===");
			LogicPos nowpos = new LogicPos(role.getPosX(), role.getPosY());
			LogicPos nextcheckpos = getNextCheckPos(nowpos, targetpos);
			role.setPos(nextcheckpos.getX(), nextcheckpos.getY());
			// System.out.println("---------nowpos:" + nowpos + "nextcheckpos:" + nextcheckpos + "id:" + role.roleId);
			CCheckMove checkmove = new CCheckMove();
			checkmove.sceneid = role.getSceneID();
			// checkmove.poslist.add(nowpos.toProtocolPos());
			checkmove.curpos = nextcheckpos.toProtocolPos();
			role.sendProtocol(checkmove);
			num++;
			// //每9秒切换一次地图
			// if (index % 9 == 0) {
			// roleStop();
			// // 切换地图
			// changeMap();
			// end();
			// return;
			// }
			if (nextcheckpos.equals(targetpos)) {
				roleStop();
				end();
				return;
			}
			// 为了防止走到不可行走的区域
			if (num > 60 && isChangeMap) {
				roleStop();
				// 切换地图
				changeMap();
				end();
			}
		}
	}

	private void dealProtocols() {
		synchronized (protocols) {
			for (mkio.Protocol p : protocols) {
				dealProtocol(p);
			}
			protocols.clear();
		}
	}

	private void end() {
		targetpos = null;
		if (!isChangeMap) {
			state = 0;
		} else {
			state = -1;
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

	// 要在4步达到目标点
	@SuppressWarnings("unused")
	private LogicPos getNextJumpCheckPos(LogicPos starPos, LogicPos nowpos, LogicPos targetpos) {
		int newposx = 0;
		int newposy = 0;
		int distancex = targetpos.getX() - starPos.getX();
		int distancey = targetpos.getY() - starPos.getY();
		// 每次差值
		int detax = distancex / 3;
		int detay = distancey / 3;
		// 目标点
		int targetx = targetpos.getX();
		int targety = targetpos.getY();
		int currentx = nowpos.getX();
		int currenty = nowpos.getY();
		if (Math.abs(currentx + detax - targetx) < 4) {
			newposx = targetx;
		} else {
			newposx = currentx + detax;
		}
		if (Math.abs(currenty + detay - targety) < 4) {
			newposy = targety;
		} else {
			newposy = currenty + detay;
		}
		return new LogicPos(newposx, newposy);
	}

	private void roleStop() {
		CRoleStop rolestop = new CRoleStop();
		rolestop.pos = new LogicPos(role.getPosX(), role.getPosY()).toProtocolPos();
		// mkdb.Trace.info("state=" + state+ "场景id\t"+rolestop.sceneid +
		// "\tsend CRoleStop:pos.x=" + rolestop.pos.x + ",pos.y=" +
		// rolestop.pos.y);
		role.sendProtocol(rolestop);
	}

	@Override
	public final void stop() {
		CRoleStop stop = new CRoleStop();
		stop.pos = new LogicPos(role.getPosX(), role.getPosY()).toProtocolPos();
		role.sendProtocol(stop);
		end();
	}

	private final void moveToNextPoint(LogicPos startpos) {
		final CRoleMove rolemove = new CRoleMove();
		rolemove.sceneid = role.getSceneID();
		rolemove.srcpos = startpos.toProtocolPos();
		rolemove.destpos = targetpos.toProtocolPos();
		role.getLoginui().getLoginInstance().send(rolemove);
	}

	@Override
	public void start() {
	}

	@Override
	public void processProtocol(Protocol p) {
		synchronized (protocols) {
			if (p instanceof SRoleMove) {
				SRoleMove sRoleMove = (SRoleMove) p;
				if (sRoleMove.roleid == role.roleId) {
					protocols.add(p);
				}
			} else {
				protocols.add(p);
			}
		}
	}

	void randomSay() {
		// if(params == null || params.isEmpty())
		// return;
		if (role == null || role.getRoleBase() == null)
			return;
		double rand = Math.random();
		if (rand < 0.3) {
			CTransChatMessage2Serv p = new CTransChatMessage2Serv();
			if (Main.GetTaskType() == 13 || (Main.GetTaskType() == 14 && Math.random() > 0.5)) {
				p.message = "voice link=" + "\"ffbb8a6d0e314075a8194aae8c29f855\" time=10";
				// p.msg = "voice link=" +
				// "\"ffffa0702e3f4df2849e82a31d68801c\" time=12";
			} else {
				p.message = "<T t=\"" + role.getRoleBase().rolename + "\" ></T>";
			}
			if (rand < 0.1) {
				p.messagetype = ChannelType.CHANNEL_WORLD;
			} else {
				if (rand < 0.15 && role.getTeam() != null) {
					p.messagetype = ChannelType.CHANNEL_TEAM;
				} else {
					p.messagetype = ChannelType.CHANNEL_CURRENT;
				}
			}
			role.sendProtocol(p);
		} else if (rand > 0.9 && role.getTeam() != null && role.getTeam().isLeader(role.roleId)) {
			COneKeyTeamMatch coktm = new COneKeyTeamMatch();
			role.sendProtocol(coktm);
		}
	}

	public static final int randomdelta = 3 * 16 * 2;

	private LogicPos randomTargetPos(LogicPos startpos) {
		int deltax = RandValue.randRanger(-randomdelta, randomdelta);
		int deltay = RandValue.randRanger(-randomdelta, randomdelta);
		LogicPos target = new LogicPos(Math.min(Short.MAX_VALUE, Math.max(0, startpos.getX() + deltax)), Math.min(
				Short.MAX_VALUE, Math.max(0, startpos.getY() + deltay)));
		return target;
	}

	protected void dealProtocol(mkio.Protocol p) {
		if (p instanceof SRoleMove) {
			SRoleMove sRoleMove = (SRoleMove) p;
			if (sRoleMove.roleid == role.roleId) {
				role.setPos(sRoleMove.destpos.x, sRoleMove.destpos.y);
				end();
			}
		} else if (p instanceof SRoleEnterScene) {
			role.setScene(((SRoleEnterScene) p).sceneid);
			role.setPos(((SRoleEnterScene) p).destpos.x, ((SRoleEnterScene) p).destpos.y);
			end();
		} else if (p instanceof SRoleJumpDrawback) {
			role.setPos(((SRoleJumpDrawback) p).srcpos.x, ((SRoleJumpDrawback) p).srcpos.y);
			end();
		} else if (p instanceof STeamError) {
			int errCode = ((STeamError) p).teamerror;
			if (errCode == 34) {
				role.sendProtocol(new CRequestTeamMatch(1, 27, 1, (int) (80 * Math.random() + 2)));
			}
		} else if (p instanceof SRequestTeamMatch) {
			role.sendProtocol(new COneKeyTeamMatch());
		}
	}
}
