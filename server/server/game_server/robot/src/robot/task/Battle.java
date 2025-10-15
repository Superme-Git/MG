package robot.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import robot.ConfigMgr;
import robot.LoginRole;
import mkio.Protocol;
import fire.pb.battle.CSendAction;
import fire.pb.battle.CSendRoundPlayEnd;
import fire.pb.battle.OperationType;
import fire.pb.battle.SSendBattleEnd;
import fire.pb.battle.SSendBattleStart;
import fire.pb.battle.SSendRoundPlayEnd;
import fire.pb.battle.SSendRoundScript;
import fire.pb.battle.SSendRoundStart;

/**
 * 随机多重任务
 * 
 * @author dc@20150730
 */
public class Battle extends Task_RoleBase {
	private BlockingQueue<mkio.Protocol> protocols = new LinkedBlockingQueue<mkio.Protocol>();
	private static final int OPERATION_PERIOD = 10000;
	private long lastOperateTime = 0;
	private int battleCount = 0;
	private boolean isInBattle = false;

	public Battle(LoginRole role) {
		super(role);
		init();
	}

	private void init() {
		// lastOperateTime = System.currentTimeMillis();
		if (role.canMove() && role.getRoleBag().emptyNumbers() > 0
				&& !role.canJump() && !role.isInitump()) {
			// 调整等级
			if (role.getLevel() <= 36) {
				role.sendCommand("//addlevel " + (36 - role.getLevel()));
			}
			// // 调一本技能书
			// if (role.getRoleBag().hasItem(50190)) {
			// role.getRoleBag().useItem(50190);
			// } else {
			// role.sendCommand("//additem 50190");
			// role.setInitump(true);
			// }
			// role.sendCommand("//battle start 8500");
		}
	}

	@Override
	public void run() {
		dealProtocols();
		if (System.currentTimeMillis() - lastOperateTime < OPERATION_PERIOD) {
			return;
		}
		lastOperateTime = System.currentTimeMillis();
		if (isInBattle) {
			return;
		}
		mkdb.Trace.info("role[" + role.roleId + "]请求战斗！");
		role.sendCommand("//battle start 8500");
	}

	private void dealProtocols() {
		synchronized (protocols) {
			Protocol p = protocols.poll();
			if (p == null) {
				return;
			}
			if (p instanceof SSendBattleStart) {
				isInBattle = true;
				mkdb.Trace.info("role[" + role.roleId
						+ "]recv:SSendBattleStart====战斗开始！");
			} else if (p instanceof SSendRoundStart) {
				mkdb.Trace.info("role[" + role.roleId
						+ "]recv:SSendRoundStart====回合开始！");
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
						mkdb.Trace.info("role[" + role.roleId
								+ "]send:role,CSendAction optype:"
								+ snd.action.operationtype + " opid:"
								+ snd.action.operationid);
						CSendAction sndpet = new CSendAction();
						sndpet.isrole = 0;
						sndpet.action.aim = 0;
						sndpet.action.operationtype = OperationType.ACTION_ATTACK;
						role.sendProtocol(sndpet);
						mkdb.Trace.info("role[" + role.roleId
								+ "]send:pet,CSendAction optype:"
								+ sndpet.action.operationtype + " opid:"
								+ sndpet.action.operationid);
					}
				}, 3, TimeUnit.SECONDS);
			} else if (p instanceof SSendRoundScript) {
				mkdb.Trace.info("role[" + role.roleId
						+ "]recv:SSendRoundScript====开始播放本回合战斗动画！");
				// 简单模拟客户端战斗动画播放,5秒后反馈给服务端
				mkdb.Executor.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						role.sendProtocol(new CSendRoundPlayEnd());
						mkdb.Trace.info("role[" + role.roleId
								+ "]send：CSendRoundPlayEnd====本回合战斗动画结束！");
					}
				}, 5, TimeUnit.SECONDS);
			} else if (p instanceof SSendRoundPlayEnd) {
				mkdb.Trace.info("role[" + role.roleId
						+ "]recv：SSendRoundPlayEnd====确认本回合战斗动画结束！");
			} else if (p instanceof SSendBattleEnd) {
				++battleCount;
				isInBattle = false;
				mkdb.Trace.info("role[" + role.roleId
						+ "]recv：SSendBattleEnd====战斗结束！==battleCount:"
						+ battleCount);
				// role.sendCommand("//battle start 8500");
			}
		}
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	public final void stop() {
		// TODO Auto-generated method stub
	}

	@Override
	public void processProtocol(Protocol p) {
		synchronized (protocols) {
			protocols.offer(p);
		}
	}

}
