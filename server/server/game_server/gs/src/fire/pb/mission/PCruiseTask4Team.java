package fire.pb.mission;

import java.util.ArrayList;
import java.util.List;

import fire.pb.team.PAbsentReturnTeam;
import mkdb.Procedure;

public class PCruiseTask4Team extends Procedure {
	protected final long roleid;
	protected final int taskid;
	private RoleMission mission;
	
	public PCruiseTask4Team(final long roleid, final int taskid, RoleMission mission) {
		this.roleid = roleid;
		this.taskid = taskid;
		this.mission = mission;
	}
	
	@Override
	protected boolean process() throws Exception {
		if(mission.conf.exeIndo.cruiseID <= 0)
			return true;
		fire.pb.team.Team team = fire.pb.team.TeamManager.getTeamByRoleId(roleid);
		if (team != null && team.isTeamLeader(roleid)) {
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, team.getAllMemberIds()));
			//有队伍且自己是队长，暂离所有成员
			for (long memid : team.getNormalMemberIds()) {
				if(memid != roleid) {
					new PAbsentReturnTeam(memid, 1).call();
				}
			}
			Module.logger.debug(roleid + "有队伍且自己是队长,暂离所有成员!");
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160433, null);
			 
			fire.pb.team.TeamManager.getInstance().delTeamMatchAsyn(roleid);			
			
		} else if (team != null && !team.isTeamLeader(roleid)) {
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, team.getAllMemberIds()));
			//有队伍且自己是队员，暂离自己
			new PAbsentReturnTeam(roleid, 1).call();
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 160433, null);
			//(队员状态不应该接到这个任务)
		} else if (team == null) {
			List<Long> locks = new ArrayList<Long>();
			locks.add(roleid);  //给roleid上锁
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, locks));
			
			fire.pb.team.TeamManager.getInstance().delTeamMatchAsyn(roleid);	
		}
		
		SAcceptMission send = mission.toProtocol();
		mkdb.Transaction.tsendWhileCommit(roleid, send);
		
		UtilHelper.cruisetask(roleid, mission.conf.exeIndo.cruiseID, mission.conf.missionId);
		if(mission.conf.exeIndo.cruiseID > 0){
			mission.data.setTaskvalue(1);
			Module.logger.debug("角色[" + roleid + "]接受巡游任务" + mission.getId() + ",巡游id:" + mission.conf.exeIndo.cruiseID);
		}
		
		return true;
	}

}
