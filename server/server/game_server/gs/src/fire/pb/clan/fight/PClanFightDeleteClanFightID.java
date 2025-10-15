package fire.pb.clan.fight;

import mkdb.Procedure;

/***
 * 删除公会关联 战场ID by changhao
 *
 */
public class PClanFightDeleteClanFightID extends Procedure {

	public long clanid1;
	public long clanid2;
	public long clanfightid;
	
	public PClanFightDeleteClanFightID(long clanid1, long clanid2, long clanfightid)
	{
		this.clanid1 = clanid1;
		this.clanid2 = clanid2;
		this.clanfightid = clanfightid;
	}
	
	@Override
	protected boolean process() throws Exception
	{		
		xbean.EClanfightids ids1 = xtable.Clanid2clanfightids.get(clanid1);
		if (ids1 != null)
		{
			ids1.getIds().remove(clanfightid);
		}
		
		xbean.EClanfightids ids2 = xtable.Clanid2clanfightids.get(clanid2);
		if (ids2 != null)
		{
			ids2.getIds().remove(clanfightid);
		}
		
		return true;
	}

}
