package fire.pb.clan.srv;
/**
 * 公会人数变化更新 公会等级榜  by changhao
 */
public class PClanUpdateMemberNum extends mkdb.Procedure
{
	long clanid;
	int num;
	
	public PClanUpdateMemberNum(long clanid, int num)
	{
		this.clanid = clanid;
		this.num = num;
	}
	
	@Override
	protected boolean process() throws Exception
	{			
		xbean.ClanRankList list = xtable.Clanranklist.get(1);
		
		if (list != null)
		{
			for (xbean.ClanRankRecord e : list.getRecords())
			{
				if (e.getClankey() == clanid)
				{
					e.setMembernum(num);
					break;
				}
			}			
		}
			
		return true;
	}
}
