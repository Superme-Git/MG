package fire.pb.clan.fight;

import fire.pb.activity.clanfight.ActivityClanFightManager;
import fire.pb.title.Title;
import mkdb.Procedure;

/***
 * 公会战场第一积分加title by changhao
 *
 */
public class PClanFightAddTitle extends Procedure {

	@Override
	/***
	 * lock  clanid -> roleids by changhao
	 */
	protected boolean process() throws Exception
	{		
		long cur = System.currentTimeMillis();
		long datetime = ActivityClanFightManager.GetMonday0000ByTime(cur);	
		xbean.ClanFightRaceRankList ranlListBean = xtable.Clanfightracelist.select(datetime); //得到这周的轮排名 by changhao		
		if (ranlListBean == null)
		{
			return false;
		}
		
		if (ranlListBean.getRecords().size() == 0)
		{
			return false;
		}
		
		xbean.ClanFightRaceRankRecord record = ranlListBean.getRecords().get(0);
		long clanid = record.getMarshaldata().getClanid();
		
		xbean.ClanInfo claninfo = xtable.Clans.get(clanid);

		java.util.Set<Long> roleids = new java.util.HashSet<Long>();
		if (claninfo != null)
			roleids.addAll(claninfo.getMembers().keySet());
		
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, roleids)); //锁定公会成员id by changhao
		
		if (claninfo != null)
		{
			for (Long r : claninfo.getMembers().keySet())
			{
				long day1 = 24 * 60 * 60 * 1000;
				Title.removeTitle(r, fire.pb.activity.clanfight.ActivityClanFightManager.NUMBER_ONE_CLAN_FIGHT_TITLE);
				Title.addTitle(r, fire.pb.activity.clanfight.ActivityClanFightManager.NUMBER_ONE_CLAN_FIGHT_TITLE, "", day1 * 7 - 5000);
			}
		}
		
		return true;
	}

}
