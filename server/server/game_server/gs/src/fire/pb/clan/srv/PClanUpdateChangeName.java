package fire.pb.clan.srv;
/**
 * 公会名字变化更新 公会等级  by changhao
 */
public class PClanUpdateChangeName extends mkdb.Procedure
{
	long clanid;
	String clanname;
	
	public PClanUpdateChangeName(long clanid, String clanname)
	{
		this.clanid = clanid;
		this.clanname = clanname;
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
					e.setClanname(clanname);
					break;
				}
			}			
		}
			
		return true;
	}
}
