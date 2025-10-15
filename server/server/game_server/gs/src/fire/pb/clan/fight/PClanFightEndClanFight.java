package fire.pb.clan.fight;

/**
 * 公会战结算  by changhao
 */
public class PClanFightEndClanFight extends mkdb.Procedure
{
	public long clanfightid;
	
	public PClanFightEndClanFight(long clanfightid)
	{
		this.clanfightid = clanfightid;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		if (clanfightid != -1)
		{
			pexecuteWhileCommit(new PClanFightTimeOutEnd(clanfightid));
		}
		else
		{
			long cur = System.currentTimeMillis();
			long createtime1 = fire.pb.activity.clanfight.ActivityClanFightManager.GetMonday1500ByTime(cur);
			long createtime2 = fire.pb.activity.clanfight.ActivityClanFightManager.GetWednesday1500ByTime(cur);
			xbean.ClanFights clanfights1 = xtable.Clanfights.select(createtime1);
			if (clanfights1 != null)
			{
				for (Long c : clanfights1.getClan1vschan2().keySet())
				{
					pexecuteWhileCommit(new PClanFightTimeOutEnd(c));				
				}				
			}
			
			xbean.ClanFights clanfights2 = xtable.Clanfights.select(createtime2);
			if (clanfights2 != null)
			{
				for (Long c : clanfights2.getClan1vschan2().keySet())
				{
					pexecuteWhileCommit(new PClanFightTimeOutEnd(c));				
				}				
			}
		}
		
		return true;
	}
}
