package fire.pb.clan.fight;

import mkdb.Procedure;

/***
 * 公会战场时间结束 by changhao
 *
 */
public class PClanFightTimeOutEnd extends Procedure {
	private final long clanfightid;

	public PClanFightTimeOutEnd(long clanfightid)
	{
		this.clanfightid = clanfightid;
	}

	@Override
	/***
	 * lock  clanfightid -> roleids by changhao
	 */
	protected boolean process() throws Exception
	{
		//检查那边赢 by changhao
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, true);
		if (bf == null)
		{
			return false;
		}
		
		int whichwin = -1;
		if (bf.getClanfightBean().getClanscore1() > bf.getClanfightBean().getClanscore2())
		{
			whichwin = 0;
		}
		else
		{
			whichwin = 1;
			if (bf.getClanfightBean().getClanscore1() == bf.getClanfightBean().getClanscore2())
			{
				if (bf.getClanfightBean().getClan1roleids().size() > bf.getClanfightBean().getClan2roleids().size())
				{
					whichwin = 0;
				}
				else
				{
					whichwin = 1;
				}
			}
		}
		
		long clanid = 0;
		if (whichwin == 0)
		{
			clanid = bf.getClanfightBean().getClanid1();
		}
		else
		{
			clanid = bf.getClanfightBean().getClanid2();
		}
		
		//执行战场结束 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new PClanFightOver(clanfightid, whichwin, bf.getClanfightBean().getDatetime(), clanid));

		return true;
	}

}
