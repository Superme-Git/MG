package fire.pb.clan.fight;
/**
 * 公会战清理玩家 统计信息 by changhao
 */
public class PClanFightClearRolesStatistics extends mkdb.Procedure
{
	long clanfightid;
	
	public PClanFightClearRolesStatistics(long clanfightid)
	{
		this.clanfightid = clanfightid;
	}
	
	@Override
	/***
	 * clanfightid -> roleids by changhao
	 */
	protected boolean process() throws Exception
	{	
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf == null)
		{
			return false;
		}
		
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, bf.getClanfightBean().getEnterroleids().keySet())); //锁定roleids by changhao		
				
		bf.clearStatistics();
			
		return true;
	}
}
