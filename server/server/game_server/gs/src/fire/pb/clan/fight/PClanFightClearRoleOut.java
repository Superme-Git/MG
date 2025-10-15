package fire.pb.clan.fight;

import fire.pb.activity.clanfight.ActivityClanFightManager;
/**
 * 清理战场某一边的人出去  by changhao
 */
public class PClanFightClearRoleOut extends mkdb.Procedure
{
	long clanfightid;
	int which;
	
	public PClanFightClearRoleOut(long clanfightid, int which)
	{
		this.clanfightid = clanfightid;
		this.which = which;
	}
	
	@Override
	/***
	 * locs clanfightid by changhao
	 */
	protected boolean process() throws Exception
	{	
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf != null)
		{
			java.util.Set<Long> roleids = new java.util.HashSet<Long>();
			
			if (which == 0)
			{
				roleids.addAll(bf.getClanfightBean().getClan1roleids().keySet());
			}
			else
			{
				roleids.addAll(bf.getClanfightBean().getClan2roleids().keySet());
			}
			
			for (Long r : roleids)
			{
				mkdb.Procedure.pexecuteWhileCommit(new PClanFightLeaveClanFieldBattleField(clanfightid, r, true, true));				
			}
			
			ActivityClanFightManager.logger.info("PClanFightClearRoleOut: 战场清人:[" + bf.getClanfightBean().getClanfightid() + "]which:[" + which +"]");
		}
		
		return true;
	}
}
