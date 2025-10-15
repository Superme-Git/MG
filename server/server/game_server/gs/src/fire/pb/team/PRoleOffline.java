package fire.pb.team;

/***
 * 角色下线
 * @author changhao
 *
 */
public class PRoleOffline extends mkdb.Procedure
{
	
	private long roleId;

	public PRoleOffline(long roleId)
	{
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process()
	{
		Long teamId = xtable.Roleid2teamid.select(roleId);
		if(teamId == null)
			return true;
		
		try
		{
			Team team = new Team(teamId, false);
			//先把所有队员锁上，不然队员队长同时下线容易死锁
			lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, team.getAllMemberIds()));
			if(team.isInTeam(roleId))
			{
				team.roleOffline(roleId);
				TeamManager.logger.debug("队员  roleid=" + roleId + " 下线。");
			}
		}
		catch(Exception e)
		{
			TeamManager.logger.error(e);
		}
		return true;
	}
}
