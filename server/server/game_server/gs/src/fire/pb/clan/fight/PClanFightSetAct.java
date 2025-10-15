package fire.pb.clan.fight;

/**
 *  设置行动力 by changhao
 */
public class PClanFightSetAct extends mkdb.Procedure
{
	public long roleid;
	public int act;
	
	public PClanFightSetAct(long roleid, int act)
	{
		this.roleid = roleid;
		this.act = act;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		xbean.EClanFightStatistics stat = xtable.Roleid2clanfightstatistics.get(roleid);
		if (stat != null)
		{
			stat.setAct(act);
			
			SBattleFieldAct msg = new SBattleFieldAct();
			msg.roleact = act;
			mkdb.Procedure.psendWhileCommit(roleid, msg);
		}
		
		return true;
	}
}
