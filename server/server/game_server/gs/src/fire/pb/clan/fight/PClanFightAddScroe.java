package fire.pb.clan.fight;

import java.util.concurrent.TimeUnit;

import fire.pb.PAddExpProc;
import fire.pb.StateCommon;
import mkdb.Procedure;

/***
 * 公会战场 定时器4分钟一次 by changhao
 *
 */
public class PClanFightAddScroe extends Procedure {
	private final long clanfightid;

	public PClanFightAddScroe(long clanfightid)
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
		
		boolean change = false;
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
					int scroe = stat.getScore();
					stat.setScore(scroe + fire.pb.activity.clanfight.ActivityClanFightManager.CLAN_FIGHT_ADD_SCROE_4_MINUTES);
					bf.setRoleIdScore(r, stat.getScore());
					
					bf.addClanScore(0, fire.pb.activity.clanfight.ActivityClanFightManager.CLAN_FIGHT_ADD_SCROE_4_MINUTES);
					
					change = true;
					
					if (stat.getAct() <= 0)
					{
						mkdb.Executor.getInstance().schedule(new PClanFightLeaveClanFieldBattleFieldTask(clanfightid, r, true, true), 10000, TimeUnit.MILLISECONDS);
						ClanFightBattleField.logger.error("roleid:[" + r +"] 行动力为0");
					}
				}
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
					int scroe = stat.getScore();
					stat.setScore(scroe + fire.pb.activity.clanfight.ActivityClanFightManager.CLAN_FIGHT_ADD_SCROE_4_MINUTES);
					bf.setRoleIdScore(r, stat.getScore());
					
					bf.addClanScore(1, fire.pb.activity.clanfight.ActivityClanFightManager.CLAN_FIGHT_ADD_SCROE_4_MINUTES);
					
					change = true;
					
					if (stat.getAct() <= 0)
					{
						mkdb.Executor.getInstance().schedule(new PClanFightLeaveClanFieldBattleFieldTask(clanfightid, r, true, true), 10000, TimeUnit.MILLISECONDS);
						ClanFightBattleField.logger.error("roleid:[" + r +"] 行动力为0");
					}	
				}
			}
		}
		
		if (change == true) //积分有改变，考虑排一次序 by changhao
		{
			bf.scroeSort(0, false);
			bf.scroeSort(1, false);
		}
		
		if (cur > bf.getClanfightBean().getActivitynoentertime() && cur < bf.getClanfightBean().getActivityendtime())
		{
			//检测战场是否结束 by changhao
			bf.checkClanFightOver();
		}

		return true;
	}

}
