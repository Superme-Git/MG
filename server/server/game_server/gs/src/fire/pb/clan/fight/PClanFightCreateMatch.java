package fire.pb.clan.fight;

import fire.pb.activity.clanfight.ActivityClanFightManager;

/**
 * 匹配公会战战场 by changhao
 */
public class PClanFightCreateMatch extends mkdb.Procedure
{
	public boolean force;
	
	public PClanFightCreateMatch(boolean force)
	{
		this.force = force;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		ActivityClanFightManager.getInstance().CreateClanFightMatch(force);
	
		return true;
	}
}
