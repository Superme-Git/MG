package fire.pb.clan.fight;

/**
 *  清理公会战状态 by changhao
 */
public class PClanFightClearClanFightState extends mkdb.Procedure
{
	public long roleid;
	
	public PClanFightClearClanFightState(long roleid)
	{
		this.roleid = roleid;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		Long clanfightid = xtable.Roleid2clanfightid.get(roleid);
		if (clanfightid != null)
		{
			xtable.Roleid2clanfightid.remove(roleid);
		}
		
		return true;
	}
}
