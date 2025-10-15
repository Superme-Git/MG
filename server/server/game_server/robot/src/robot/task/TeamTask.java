package robot.task;

import java.util.LinkedList;
import java.util.List;

import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.team.Team;
import robot.team.TeamMgr;
import robot.team.TeamRoleState;
import mkio.Protocol;
import fire.pb.team.CAbsentReturnTeam;
import fire.pb.team.CAcceptToTeam;
import fire.pb.team.CCreateTeam;
import fire.pb.team.CRequestJoinTeam;
import fire.pb.team.SAddTeamApply;

/**
 * 随机组队任务，只能跟本机器人控制的角色组队 随机一部分人建立队伍，一部分人去申请加队
 */
public class TeamTask extends Task_RoleBase {

	private boolean isrequesting = false;
	private long requesttime = 0;
	public static long REQUEST_PERIOD = 30000;
	private List<mkio.Protocol> protocols = new LinkedList<mkio.Protocol>();

	public TeamTask(LoginRole role) {
		super(role);
	}

	@Override
	public void run() {
		// 发送心跳
		Global.DoGlobalTask(this);

		if (!protocols.isEmpty()) {
			Protocol p = protocols.remove(0);// 每次只处理一个请求
			if (p instanceof SAddTeamApply) {
				if (role.getTeam() == null) {
					protocols.clear();
					return;
				}
				if (!role.getTeam().isLeader(role.roleId)) {
					protocols.clear();
					return;
				}
				if (role.getTeam().isTeamFull()) {
					protocols.clear();
					return;
				}
				role.getTeam().pushApply((SAddTeamApply) p);
			}
		}
		randomTeamOper();// 没有队伍的随机组队操作

		processApply();// 队长处理申请

		if (Math.random() < 0.7)
			absent2return();// 随机，暂离队员归队

		// if(Math.random() < 0.1)
		// return2absent();//随机，归队队员暂离
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

	private void randomTeamOper() {
		if (role.getTeam() != null)
			return;// 有队伍
		if (isrequesting) {
			long now = System.currentTimeMillis();
			if ((now - requesttime) > REQUEST_PERIOD) {
				isrequesting = false;
				requesttime = 0;
			} else
				return;// 正在申请不要申请
		}
		if (!role.canMove())
			return;// 不能行走的时候先不去组队
		if (Math.random() < 0.3)
			return;// 30%概率开始组队处理

		int rolenum = LoginRoleMgr.getInstance().getRoles().size();
		int teamnum = TeamMgr.getInstance().getTeams().size();
		if (teamnum > (rolenum / 4)) {
			// 不再建立队伍，只去求组队
			requestJoinTeam();
		} else {
			if (Math.random() < 0.2) {
				// 建立队伍
				role.sendProtocol(new CCreateTeam());
			} else {
				// 求组队
				requestJoinTeam();
			}
		}
	}

	private void requestJoinTeam() {
		int teamnum = TeamMgr.getInstance().getTeams().size();
		int random = (int) (Math.random() * teamnum);
		int i = 0;
		for (Team team : TeamMgr.getInstance().getTeams().values()) {
			i++;
			if (i <= random)
				continue;
			if (team.leaderRoleId <= 0)
				continue;
			if (team.isTeamFull())
				continue;
			role.sendProtocol(new CRequestJoinTeam(team.leaderRoleId));
			isrequesting = true;
			requesttime = System.currentTimeMillis();
			break;
		}
	}

	@SuppressWarnings("unused")
	private void return2absent() {
		if (role.getTeam() == null)
			return;
		if (role.getTeamState() != TeamRoleState.TEAM_NORMAL)
			return;
		if (!role.canMove())
			return;
		role.sendProtocol(new CAbsentReturnTeam((byte) 1));
	}

	private void processApply() {
		if (role.getTeam() == null)
			return;
		if (!role.getTeam().isLeader(role.roleId)) {
			// System.out.println(role.roleId);
			return;
		}
		SAddTeamApply p = role.getTeam().pollApply();
		if (p == null)
			return;
		if (!role.canMove())
			return;
		if (role.getTeam().isTeamFull()) {
			role.getTeam().clearApplies();
			return;
		}
		if (p.applylist == null||p.applylist.size()==0) {
			return;
		}
		fire.pb.team.TeamApplyBasic apply = p.applylist.get(0);
		LoginRole applier = LoginRoleMgr.getInstance().getLoginRole(
				apply.roleid);
		if (applier == null)
			return;
		role.gmgoto(applier.getMapId(), applier.getGridX(), applier.getGridX());
		role.sendProtocol(new CAcceptToTeam(apply.roleid, 1));

	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public void processProtocol(Protocol p) {
		if (p instanceof SAddTeamApply) {
			synchronized (protocols) {
				protocols.add(p);
			}
		}
	}

}
