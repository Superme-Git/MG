package fire.pb.clan.fight;

import fire.pb.activity.clanfight.ActivityClanFightManager;

/**
 * 公会战战场初始化一些东西 by changhao
 */
public class PClanFightInit extends mkdb.Procedure
{
	@Override
	protected boolean process() throws Exception
	{
		long cur = System.currentTimeMillis();
		long mon = ActivityClanFightManager.GetMonday1500ByTime(cur);
		long wed = ActivityClanFightManager.GetWednesday1500ByTime(cur);
		
		java.util.List<Long> timelist = new java.util.ArrayList<Long>();
		timelist.add(mon);
		timelist.add(wed);
		
		for (Long t : timelist)
		{
			xbean.ClanFights clanfights = xtable.Clanfights.select(t);
			if (clanfights != null)
			{
				for (Long e : clanfights.getClan1vschan2().keySet())
				{
					if (e != null)
					{
						fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(e, true);
						if (bf != null)
						{
							bf.createTimer();
						}
					}
				}
			}			
		}
	
		return true;
	}
}
