package fire.pb.clan.fight;

import java.util.concurrent.TimeUnit;

import fire.pb.PAddExpProc;
import fire.pb.StateCommon;
import mkdb.Procedure;

/***
 * 公会战场 定时器一分钟一次 by changhao
 *
 */
public class PClanFightTimer extends Procedure {
	private final long clanfightid;

	public PClanFightTimer(long clanfightid)
	{
		this.clanfightid = clanfightid;
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
		
		//战场结束了 by changhao
		if (bf.getClanfightBean().getState() == xbean.ClanFightBattleField.STATE_CLAN_FIGHT_OVER)
		{
			return false;
		}
		
		//在战斗时间内 才给经验 by changhao
		if (cur > bf.getClanfightBean().getActivityendtime() || cur < bf.getClanfightBean().getActivitypreparetime())
		{
			return false;
		}
		
		java.util.Set<Long> roleids = new java.util.HashSet<Long>();
		roleids.addAll(bf.getClanfightBean().getClan1roleids().keySet());
		roleids.addAll(bf.getClanfightBean().getClan2roleids().keySet());
		
		this.lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids)); //锁所有角色 by changhao
		
		for (Long r : bf.getClanfightBean().getClan1roleids().keySet())
		{
			if (r != null)
			{
				if (!StateCommon.isOnlineBuffer(r))
				{
					continue;
				}
				
				xbean.EClanFightStatistics stat = xtable.Roleid2clanfightstatistics.get(r);
				if (stat != null)
				{
					if (stat.getAct() <= 0)
					{
						mkdb.Executor.getInstance().schedule(new PClanFightLeaveClanFieldBattleFieldTask(clanfightid, r, true, true), 10000, TimeUnit.MILLISECONDS);
						ClanFightBattleField.logger.error("roleid:[" + r +"] 行动力为0");
					}
				}
				
				Integer curlevel = xtable.Properties.selectLevel(r);
				if (curlevel == null)
				{
					continue;
				}
				
				Double expAward = 400 * Math.min(curlevel, 111 * 10 - 1051)	* 0.0392 * (0.4 + 0.1 * 111);
				pexecuteWhileCommit(new PAddExpProc(r, expAward.longValue(), PAddExpProc.INST_MAP, "公会战场加经验"));				
			}
		}
		
		for (Long r : bf.getClanfightBean().getClan2roleids().keySet())
		{
			if (r != null)
			{
				if (!StateCommon.isOnlineBuffer(r))
				{
					continue;
				}
				
				xbean.EClanFightStatistics stat = xtable.Roleid2clanfightstatistics.get(r);
				if (stat != null)
				{
					if (stat.getAct() <= 0)
					{
						mkdb.Executor.getInstance().schedule(new PClanFightLeaveClanFieldBattleFieldTask(clanfightid, r, true, true), 10000, TimeUnit.MILLISECONDS);
						ClanFightBattleField.logger.error("roleid:[" + r +"] 行动力为0");
					}
				}
				
				Integer curlevel = xtable.Properties.selectLevel(r);
				if (curlevel == null)
				{
					continue;
				}
				
				Double expAward = 400 * Math.min(curlevel, 111 * 10 - 1051)	* 0.0392 * (0.4 + 0.1 * 111);
				pexecuteWhileCommit(new PAddExpProc(r, expAward.longValue(), PAddExpProc.INST_MAP, "公会战场加经验"));				
			}
		}

		return true;
	}

}
