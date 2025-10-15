package fire.pb.clan.fight;

/***
 * 工会战角色上线 by changhao
 * @author changhao
 *
 */
public class PRoleOnline extends mkdb.Procedure
{
	private long roleId;

	public PRoleOnline(long roleId)
	{
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process(){
		
		try
		{
			Long clanfightid = xtable.Roleid2clanfightid.select(roleId);
			if (clanfightid != null) //如果在公会战场中 by changhao
			{				
				xbean.EClanFightStatistics statistics = xtable.Roleid2clanfightstatistics.select(roleId);
				if (statistics != null)
				{
					SBattleFieldAct msg = new SBattleFieldAct();
					msg.roleact = statistics.getAct();
					mkdb.Procedure.psendWhileCommit(roleId, msg); //发送行动力数据 by changhao
				}
				
				fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
				if (bf != null) //发送战场基本信息 by changhao
				{
					SBattleFieldInfo infomsg = new SBattleFieldInfo();
					infomsg.clanid1 = bf.getClanfightBean().getClanid1();
					infomsg.clanid2 = bf.getClanfightBean().getClanid2();
					infomsg.clanname1 = bf.getClanfightBean().getClanname1();
					infomsg.clanname2 = bf.getClanfightBean().getClanname2();
					mkdb.Procedure.psendWhileCommit(roleId, infomsg);						
				}
			}
		}
		catch(Exception e)
		{
			fire.pb.activity.clanfight.ActivityClanFightManager.logger.error(e);
		}
		
		return true;
	}
}
