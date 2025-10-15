package fire.pb.battle.battleflag;

import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PSetCommander extends Procedure {

	private long roleId;
	private byte opttype; // 0 set, 1 reset
	private long commanderroleId;

	public PSetCommander(long roleid, byte opttype, long commanderroleId) {
		this.roleId = roleid;
		this.opttype = opttype;
		this.commanderroleId = commanderroleId;
	}

	@Override
	public boolean process() {
		xbean.BattleFlags battleFlags = xtable.Battleflag.get(roleId);
		if(battleFlags == null)
		{
			battleFlags = xbean.Pod.newBattleFlags();
			xtable.Battleflag.insert(roleId, battleFlags);
			//这里应该有发到客户端的错误提示
			SSendBattleFlag send = new SSendBattleFlag();
			send.friendflags.addAll(battleFlags.getFriendflag());
			send.enemyflags.addAll(battleFlags.getEnemyflag());
			psend(roleId, send);
			return true;	
		}	
		
		Team team = TeamManager.getTeamByRoleId(roleId);
		if(team == null||!team.isTeamLeader(roleId))
			return false;		
		if(!team.isInTeam(commanderroleId))
			return false;		
		if(opttype==1)
			commanderroleId = roleId;
		team.SetCommanderRoleId(commanderroleId);
		return true;
	}
}
