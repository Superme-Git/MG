package fire.pb.clan.fight;

import fire.pb.PAddExpProc;
import fire.pb.StateCommon;
import mkdb.Procedure;

/***
 * 公会战场 给赢的人结算总经验 by changhao
 *
 */
public class PClanFightBalanceExp extends Procedure {
	private final long clanfightid;
	private final int whichwin;

	public PClanFightBalanceExp(long clanfightid, int whichwin)
	{
		this.clanfightid = clanfightid;
		this.whichwin = whichwin;
	}

	@Override
	/***
	 * lock  clanfightid -> roleids by changhao
	 */
	protected boolean process() throws Exception
	{
		long cur = System.currentTimeMillis();
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf == null)
		{
			return false;
		}
		
		java.util.Map<Long, Integer> whichroleids = bf.getClanroleidsByWhich(whichwin);
		if (whichroleids == null)
		{
			return false;
		}
		
		long timecount = (bf.getClanfightBean().getActivityendtime() - cur) / (1 * 60 * 1000);
		
		if (timecount > 0)
		{
			this.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, whichroleids.keySet())); //锁所有角色 by changhao
			
			for (Long r : whichroleids.keySet())
			{
				if (r != null)
				{
					if (!StateCommon.isOnlineBuffer(r))
					{
						continue;
					}
					
					Integer curlevel = xtable.Properties.selectLevel(r);
					if (curlevel == null)
					{
						continue;
					}
					
					Double expAward = 400 * Math.min(curlevel, 111 * 10 - 1051)	* 0.0392 * (0.4 + 0.1 * 111);
					pexecuteWhileCommit(new PAddExpProc(r, expAward.longValue() * timecount, PAddExpProc.INST_MAP, "公会战场加经验"));				
				}
			}		
		}

		return true;
	}

}
