package fire.pb.clan.fight;

import fire.pb.team.Team;
import fire.pb.team.TeamManager;

/**
 * 离开战场 by changhao
 * lock teamid -> clanfightid -> roleid by changaho
 */
public class PClanFightLeaveClanFieldBattleField extends mkdb.Procedure
{
	public long clanfightid; //战场id by changhao
	public long roleid;
	public boolean tran;
	public boolean dismiss;
	
	PClanFightLeaveClanFieldBattleField(long clanfightid, long roleid, boolean tran, boolean dismiss)
	{
		this.clanfightid = clanfightid;
		this.roleid = roleid;
		this.tran = tran;
		this.dismiss = dismiss;
	}
	
	@Override
	protected boolean process() throws Exception
	{
		Team team = TeamManager.getTeamByRoleId(roleid); //锁定teamid by changhao
		
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf == null)
		{
			return false;
		}
		
		bf.leaveBattleField(roleid, tran, dismiss);
			
		return true;
	}
}
