package fire.pb.clan.fight;

import fire.pb.activity.clanfight.ActivityClanFightManager;
/**
 * 结束战场里的所有战斗  by changhao
 */
public class PClanFightEndAllBattle extends mkdb.Procedure
{
	long clanfightid;
	
	public PClanFightEndAllBattle(long clanfightid)
	{
		this.clanfightid = clanfightid;
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
			roleids.addAll(bf.getClanfightBean().getClan1roleids().keySet());
			roleids.addAll(bf.getClanfightBean().getClan2roleids().keySet());
			
			for (Long r : roleids)
			{
				Long battleid = xtable.Roleid2battleid.select(r);
				if (battleid != null)
				{
					mkdb.Procedure.pexecuteWhileCommit(new fire.pb.battle.PBattleEnd(battleid, fire.pb.battle.BattleField.BATTLE_DRAW));					
				}
			}
			
			ActivityClanFightManager.logger.info("PClanFightEndAllBattle: 结束战场里的所有战斗:[" + bf.getClanfightBean().getClanfightid() + "]");
		}
		
		return true;
	}
}
