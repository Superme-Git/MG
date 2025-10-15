package fire.pb.clan.fight;

/**
 * 匹配公会战战场 by changhao
 */
public class PClanFightRefreshBox extends mkdb.Procedure
{
	long clanfightid;
	int which;
	
	public PClanFightRefreshBox(long clanfightid, int which)
	{
		this.clanfightid = clanfightid;
		this.which = which;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf != null)
		{	
			
			int num = 10;
			java.util.Map<Long, Integer> map = bf.getClanroleidsByWhich(which);
			if (map != null)
			{
				float n = (float)map.size() * 0.7f;
				num = (int)n;
			}
			
			bf.createVictoryBox(num);
			
			bf.createCelebrateBox(which);
		}
		
		return true;
	}
}
