package fire.pb.battle.battleflag;

import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PReqBattleFlag extends Procedure {

	private long roleId;

	public PReqBattleFlag(long roleid) {
		this.roleId = roleid;
	}

	@Override
	public boolean process() {
		
		xbean.BattleFlags battleFlags = xtable.Battleflag.get(roleId);
		if(battleFlags == null)
		{
			battleFlags = xbean.Pod.newBattleFlags();
			xtable.Battleflag.insert(roleId, battleFlags);
		}	
		
		if(battleFlags.getFriendflag().size() > PModifyBattleFlag.MAX_FRIENDBATTLEFLAG_SIZE)
		{ 
			for(int i = PModifyBattleFlag.MAX_FRIENDBATTLEFLAG_SIZE; i < battleFlags.getFriendflag().size();i++)
				battleFlags.getFriendflag().remove(i);
		}
		if(battleFlags.getEnemyflag().size() > PModifyBattleFlag.MAX_ENEMYBATTLEFLAG_SIZE)
		{ 
			for(int i = PModifyBattleFlag.MAX_ENEMYBATTLEFLAG_SIZE; i < battleFlags.getEnemyflag().size();i++)
				battleFlags.getEnemyflag().remove(i);
		}
		SSendBattleFlag send = new SSendBattleFlag();
		send.friendflags.addAll(battleFlags.getFriendflag());
		send.enemyflags.addAll(battleFlags.getEnemyflag());
		psend(roleId, send);
		
		Team team = TeamManager.getTeamByRoleId(roleId);
		if(team != null&&team.isTeamLeader(roleId))
		{
			SSetCommander send2 = new SSetCommander(team.GetCommanderRoleId());
			mkdb.Procedure.psend(roleId, send2);
		}
		return true;
	}
}
