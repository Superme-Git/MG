package fire.pb.clan.fight;

import fire.pb.PAddExpProc;
import fire.pb.StateCommon;
import mkdb.Procedure;

/***
 * 公会战场 给某个人增加积分 by changhao
 *
 */
public class PClanFightAddScroeToRole extends Procedure {
	public long roleid;
	public int scroe;
	public boolean forcesort;
	

	public PClanFightAddScroeToRole(long roleid, int scroe, boolean forcesort)
	{
		this.roleid = roleid;
		this.scroe = scroe;
		this.forcesort = forcesort;
		
	}

	@Override
	/***
	 * lock  clanfightid -> roleids by changhao
	 */
	protected boolean process() throws Exception
	{
		Long clanfightid = xtable.Roleid2clanfightid.select(roleid);
		if (clanfightid == null)
		{
			return true;
		}
		
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf == null)
		{
			return false;
		}
		
		xbean.EClanFightStatistics stat = xtable.Roleid2clanfightstatistics.get(roleid);
		if (stat == null)
		{
			return true;
		}
		
		stat.setScore(stat.getScore() + scroe);
		
		int which = bf.getSideByRoleId(roleid);
		bf.addClanScore(which, scroe);
		bf.scroeSort(which, forcesort);
		
		return true;
	}

}
