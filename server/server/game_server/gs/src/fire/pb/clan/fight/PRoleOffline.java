package fire.pb.clan.fight;

/***
 * 公会战角色下线 by changhao
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
	/***
	 * lock clanfightid -> roleid by changhao
	 */
	protected boolean process()
	{		
		try
		{
			Long clanfightid = xtable.Roleid2clanfightid.select(roleId);
			if (clanfightid != null) //如果在公会战场中 by changhao
			{
				fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
				if (bf != null)
				{
					//bf.leaveBattleField(roleId);
					mkdb.Procedure.pexecuteWhileCommit(new PClanFightLeaveClanFieldBattleField(clanfightid, roleId, true, true));
				}
				
				xtable.Roleid2clanfightid.remove(roleId);
			}			
		}
		catch(Exception e)
		{
			fire.pb.activity.clanfight.ActivityClanFightManager.logger.error(e);
		}
		
		return true;
	}
}
