package fire.pb.team;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送和修改更新申请列表 by changhao
 */
public class PSendAndModifyTeamApplierids extends mkdb.Procedure{
	
	private final long roleId;
	
	public PSendAndModifyTeamApplierids(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception
	{
		Long teamid = xtable.Roleid2teamid.select(roleId);
		if (teamid != null)
		{
			xbean.TeamInfo teamInfo = xtable.Team.get(teamid);
			if (teamInfo == null)
			{
				return false;
			}
			
			Team team = new Team(teamid, false);
			
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, team.getTeamInfo().getApplierids().keySet().toArray()));//锁所有申请者
			
			long now = System.currentTimeMillis();
			SAddTeamApply sAddTeamApply = new SAddTeamApply();
			List<Long> timeoutList = new ArrayList<Long>();
			for (Long applierRoleId : team.getTeamInfo().getApplierids().keySet())
			{
				if ((now - team.getTeamInfo().getApplierids().get(applierRoleId)) <= TeamManager.MAX_ARRLY_TIMEOUT)
				{
					fire.pb.team.TeamApplyBasic teamApplyBasic = new fire.pb.team.TeamApplyBasic();
					xbean.Properties applierProperty = xtable.Properties.select(applierRoleId);
					//teamApplyBasic.face = applierProperty.getFaction();
					teamApplyBasic.level = applierProperty.getLevel();
					teamApplyBasic.roleid = applierRoleId;
					teamApplyBasic.rolename = applierProperty.getRolename();
					teamApplyBasic.school = applierProperty.getSchool();
					teamApplyBasic.shape = applierProperty.getShape();
					fire.pb.map.Role.getPlayerComponents(applierRoleId, teamApplyBasic.components);
					// teamApplyBasic.title = applierProperty.getTitle();
					sAddTeamApply.applylist.add(teamApplyBasic);
				}
				else
				{
					timeoutList.add(applierRoleId);
				}
			}

			for (Long key : timeoutList)
			{
				team.getTeamInfo().getApplierids().remove(key);
			}

			mkdb.Procedure.psendWhileCommit(roleId, sAddTeamApply);				
		}
		
		return true;	
	}
}
