package fire.pb.team;

/**
 * 发送申请列表 by changhao
 */
public class PSendTeamApplierids extends mkdb.Procedure{
	
	private final long roleId;
	
	public PSendTeamApplierids(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception
	{
		Long teamid = xtable.Roleid2teamid.select(roleId);
		if (teamid != null)
		{
			Team team = new Team(teamid, true);
			
			//lock(xdb.Lockeys.get(xtable.Locks.ROLELOCK, teamInfo.getApplierids().keySet().toArray()));//锁所有申请者
			for (java.util.Map.Entry<Long, Long> e : team.getTeamInfo().getApplierids().entrySet())
			{
				long roleid = e.getKey();

				//向队长更新申请列表 by changhao
				SAddTeamApply sAddTeamApply = new SAddTeamApply();
				fire.pb.team.TeamApplyBasic  teamApplyBasic = new fire.pb.team.TeamApplyBasic();
				xbean.Properties applierProperty = xtable.Properties.select(roleid);
				teamApplyBasic.level = applierProperty.getLevel();
				teamApplyBasic.roleid = roleid;
				teamApplyBasic.rolename = applierProperty.getRolename();
				teamApplyBasic.school = applierProperty.getSchool();
				teamApplyBasic.shape = applierProperty.getShape();
				fire.pb.map.Role.getPlayerComponents(roleid, teamApplyBasic.components);
				sAddTeamApply.applylist.add(teamApplyBasic);
				mkdb.Procedure.psendWhileCommit(roleId, sAddTeamApply);
			}			
		}
	
		return true;		
	}
}
