package fire.pb.clan.fight;
import mkdb.Procedure;

/***
 * 清理公会战历史排行数据 by changhao
 *
 */
public class PClanFightClearHistroyRank extends Procedure {

	@Override
	/***
	 * lock  clanfightid -> roleids by changhao
	 */
	protected boolean process() throws Exception
	{
		xbean.ClanFightHistroyRankList ranlListBean = xtable.Clanfighthistroylist.get(0);
		if (ranlListBean != null)
		{
			ranlListBean.getRecords().clear();
		}

		return true;
	}

}
