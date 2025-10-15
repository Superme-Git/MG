package robot.task;

import robot.LoginRole;
import mkio.Protocol;

/**
 * 随机移动并且组队的任务 实际是RandomMoveTask和RandomTeamTask的组合
 */
public class TeamMove extends MoveBattle {
	private final TeamTask randomTeamTask;

	public TeamMove(LoginRole r) {
		super(r);
		randomTeamTask = new TeamTask(r);
	}

	@Override
	public void run() {
		// 发送心跳包
		Global.DoGlobalTask(this);
		super.run();
		randomTeamTask.run();
	}

	@Override
	public void processProtocol(Protocol p) {
		super.processProtocol(p);
		randomTeamTask.processProtocol(p);
	}
}
