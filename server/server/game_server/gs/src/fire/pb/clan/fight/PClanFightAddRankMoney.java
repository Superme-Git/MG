package fire.pb.clan.fight;

import fire.pb.activity.clanfight.ActivityClanFightManager;

/**
 * 给排名公会加工资 by changhao
 * lock Clanfightracelist
 */
public class PClanFightAddRankMoney extends mkdb.Procedure
{	
	@Override
	protected boolean process() throws Exception
	{	
		long cur = System.currentTimeMillis();

		xbean.ClanFightRaceRankList list = xtable.Clanfightracelist.select(ActivityClanFightManager.GetMonday0000ByTime(cur));
		if (list != null)
		{
			java.util.List<xbean.ClanFightRaceRankRecord> records = list.getRecords();
			if (records != null)
			{
				int size = records.size();
				if (size > 50)
					size = 50;
				
				for (int i = 0; i < size; i ++)
				{
					xbean.ClanFightRaceRankRecord r = records.get(i);
					if (r != null)
					{
						int money = 1000;
						if (i == 0)
						{
							money = 6000;
						}
						else if (i > 0 && i < 3)
						{
							money = 4000;
						}
						else if (i >= 3 && i < 10)
						{
							money = 2000;
						}
						
						fire.pb.clan.PAddClanMoneyByClanid addmoney = new fire.pb.clan.PAddClanMoneyByClanid(r.getMarshaldata().getClanid(), money); 
						mkdb.Procedure.pexecuteWhileCommit(addmoney);
					}
				}
			}
		}
			
		return true;
	}
}
