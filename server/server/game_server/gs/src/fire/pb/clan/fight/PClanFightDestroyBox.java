package fire.pb.clan.fight;

/**
 * 销毁宝箱 by changhao
 */
public class PClanFightDestroyBox extends mkdb.Procedure
{
	long clanfightid;
	
	public PClanFightDestroyBox(long clanfightid)
	{
		this.clanfightid = clanfightid;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf != null)
		{
			bf.destroyVictoryBox();
			bf.destroyCelebrateBox();
		}
		
		return true;
	}
}
