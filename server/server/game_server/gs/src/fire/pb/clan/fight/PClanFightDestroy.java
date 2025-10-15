package fire.pb.clan.fight;

import fire.pb.activity.clanfight.ActivityClanFightManager;

/**
 * 删除公会战场 by changhao
 *
 */
public class PClanFightDestroy extends mkdb.Procedure
{
	private final long clanfightid;

	public PClanFightDestroy(long clanfightid)
	{
		this.clanfightid = clanfightid;
	}

	@Override
	protected boolean process() throws Exception
	{
		fire.pb.clan.fight.ClanFightFactory.destroyClanFightBattleFieldById(clanfightid);
		
		ActivityClanFightManager.logger.info("PClanFightDestroy:id:[" + clanfightid +"]");
			
		return true;
	}
}
