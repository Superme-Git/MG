package robot.task;

import robot.LoginRole;
import mkio.Protocol;

/**
 * 机器人组队打怪
 * 
 * @author Bill_Ye 2016年1月14日 下午9:11:40
 */

public class TeamFightTask extends Multiple {
	private final TeamTask randomTeamTask;

	public TeamFightTask(LoginRole role) {
		super(role);
		randomTeamTask = new TeamTask(role);
		if (role.getLevel() < 80) {
			role.sendCommand("//strong 5000");
			// 调整等级
			//role.sendCommand("//addlevel " + (53 - role.getLevel()) + " equip");
		}
	}
	
	public TeamFightTask(LoginRole role,boolean isNet) {
		super(role,isNet);
		randomTeamTask = new TeamTask(role);
		if (role.getLevel() < 80) {
			role.sendCommand("//strong 5000");
			// 调整等级
			//role.sendCommand("//addlevel " + (53 - role.getLevel()) + " equip");
		}
	}

	@Override
	public void run() {
		// 父类已经有发送心跳等
		super.run();
		randomTeamTask.run();
	}

	@Override
	public void processProtocol(Protocol p) {
		super.processProtocol(p);
		randomTeamTask.processProtocol(p);
	}
}
