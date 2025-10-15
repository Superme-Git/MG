package fire.pb.mission.instance.line;

import java.util.ArrayList;
import java.util.List;

import fire.pb.battle.BattleType;
import fire.pb.battle.PNewBattle;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.mission.SLineTask;
import fire.pb.mission.SLineTaskFight;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Lockeys;
import mkdb.Procedure;

public class PGiveLineProc extends Procedure{
	
	private final long roleid;
	private final long npckey;
	private final int serviceId;
	
	public PGiveLineProc(long roleid, long npckey, int serviceId){
		this.roleid = roleid;
		this.npckey = npckey;
		this.serviceId = serviceId;
	}
	
	@Override
	protected boolean process() throws Exception {
		Team team = TeamManager.getTeamByRoleId(roleid);
		if (team == null) {
			return false;
		}
		
		List<Long> allRoleIDs = new ArrayList<Long>();
		allRoleIDs.addAll(team.getNormalMemberIds());
		Lockeys.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, allRoleIDs));
		
		final int instanceId = LineInstManager.getInstance().getInstanceIdByServiceId(serviceId);
		if (instanceId == -1)
			return false;
		
		xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.select(roleid);
		if (taskInfo == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 144838, null);
			return false;
		}
		
		xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(instanceId);
		if (info == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,144838, null);
			return false;
		}
		
		final long taskId = info.getInstanceonlyid();
		xbean.InstanceInfo instInfo = xtable.Instancetask.get(taskId);
		if (instInfo == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,144838, null);
			return false;
		}
		
		instInfo.setServiceid(serviceId);
		
		Role mapRole = RoleManager.getInstance().getRoleByID(roleid);
		if(null == mapRole){
			return false;
		}
		
		long currentScenceId = mapRole.getScene();
		if (!check(instInfo, taskId, currentScenceId, instanceId)) {
			return false;
		}
		
		SLineTaskFight fightCnf = LineInstManager.getInstance().getFightConfig(serviceId);
		if (fightCnf == null)
			return false;
			
		final int battleid = LineInstManager.getInstance().getFightId(serviceId);
		LineBattleEndHandler handler = new LineBattleEndHandler(serviceId, npckey, 
				info.getInstanceonlyid(), instanceId, fightCnf.awardtimes, fightCnf.yuanzhuawardtimes, 
				fightCnf.award, fightCnf.yuanzhuawardid, fightCnf.itemawardid);
		PNewBattle battle = new PNewBattle(roleid, battleid, handler);
		battle.setBattleType(BattleType.BATTLE_LINE);
		return battle.call();
	}

	
	private boolean check(final xbean.InstanceInfo instInfo, final long instOnlyId, final long scenceId, final int instanceId) {
		SLineTask taskCofig = LineInstManager.getInstance().getInstanceTask(instanceId);
		if (taskCofig == null)
			return false;
		
		int tasktypeid = instInfo.getInstanceid();
		int mapId = LineInstManager.getInstance().getMapIdByTaskType(tasktypeid, instInfo.getCurrentstep());
		if (mapId == -1) {
			return false;
		}
		
		//不是自己的副本
		if (!LineInstManager.getInstance().isSameDynamicScene(scenceId, instOnlyId, mapId)){
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,144839, null);
			return false;
		}

		Team team = TeamManager.selectTeamByRoleId(roleid);
		if (team == null) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,144840, null);
			return false;
		} 
		
		if (team.getTeamLeaderId() != roleid) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid,144841, null);
			return false;
		}
		
		List<Long> allRoleIDs = new ArrayList<Long>();
		allRoleIDs.addAll(team.getNormalMemberIds());
		int count = 0;
		for (Long roleId : allRoleIDs) {
			xbean.InstanceInfoCol taskInfo = xtable.Roleinstancetask.select(roleId);
			if (taskInfo == null) {
				continue;
			}
			
			xbean.InstanceTaskInfo info = taskInfo.getInstinfo().get(instanceId);
			if (info.getInstanceonlyid() == instOnlyId) {
				count ++;
			}
		}
		
		//和队长任务相同的人数小于3人
		if (count < taskCofig.getTeamnum()) {
			fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 144840, null);
			return false;
		}
		
		return true;
	}
}

