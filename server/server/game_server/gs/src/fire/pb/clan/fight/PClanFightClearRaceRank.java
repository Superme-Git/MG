package fire.pb.clan.fight;
import fire.pb.activity.clanfight.ActivityClanFightManager;
import mkdb.Procedure;

/***
 * 清理公会战竞赛排行数据 by changhao
 *
 */
public class PClanFightClearRaceRank extends Procedure {

	@Override
	/***
	 * lock  clanfightid -> roleids by changhao
	 */
	protected boolean process() throws Exception
	{
		long cur = System.currentTimeMillis();
		long mon00 = ActivityClanFightManager.GetMonday0000ByTime(cur);
		
		xbean.ClanFightRaceRankList list2 = xtable.Clanfightracelist.get(mon00);
		if (list2 != null)
		{
			list2.getRecords().clear();
		}

		return true;
	}

}
