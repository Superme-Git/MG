package fire.pb.team;

import java.util.List;

/**
 * 人物升级时通知队友
 * @author nobody
 *
 */
public class PRoleLevelUp extends mkdb.Procedure
{
	
	private final long roleId;
	private final int level;
	public PRoleLevelUp(long roleId,int level)
	{
		this.roleId = roleId;
		this.level = level;
	}
	
	@Override
	protected boolean process()
	{
		Team team = TeamManager.selectTeamByRoleId(roleId);
		if(team == null)
			return true;
		List<Long> roleIds = team.getAllMemberIds();
		
		//roleIds.remove((Long)roleId);
		
		SUpdateMemberLevel sUpdateMemberLevel = new SUpdateMemberLevel(roleId,level);
		
		psendWhileCommit(roleIds, sUpdateMemberLevel);
		
		return true;
	}
	
}
