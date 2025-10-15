package fire.pb.team;

import java.util.Set;

import mkdb.Procedure;

/**
 * 最大Hp和Mp变化时通知队友
 * 
 * @author nobody
 *
 */
public class PUpdateMemberMaxHpMp extends Procedure {

	private final long roleId;
	private final int maxhp;
	private final int maxmp;
	
	public PUpdateMemberMaxHpMp(long roleId,int maxhp,int maxmp)
	{
		this.roleId = roleId;
		this.maxhp = maxhp;
		this.maxmp = maxmp;
	}
	
	@Override
	protected boolean process()
	{
		Long battleId = xtable.Roleid2battleid.select(roleId);
		if(battleId != null)
			return true;//战斗中不广播给队友
		Team team = TeamManager.selectTeamByRoleId(roleId);
		if(team == null)
			return true;//无队伍
		Set<Long> roleIds = team.getAllMemberIdSet();
		
		SUpdateMemberMaxHPMP update = new SUpdateMemberMaxHPMP(roleId,maxhp,maxmp);
		psend(roleIds, update);
		return true;
	}
	
	
}
