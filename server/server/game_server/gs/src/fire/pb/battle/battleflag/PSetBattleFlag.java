package fire.pb.battle.battleflag;

import fire.pb.battle.BattleField;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import mkdb.Procedure;

public class PSetBattleFlag extends Procedure {

	public static final int MAX_BATTLEFLAG_SIZE = 10;//创建队伍,判断队长

	private long roleId;
	private byte opttype; // 0 add, 1 delete, 2 modify
	private byte index; // 0-9 友方, 10-19敌方
	private String flag;

	public PSetBattleFlag(long roleid, byte opttype,byte index,String flag) {
		this.roleId = roleid;
		this.opttype = opttype;
		this.index = index;
		this.flag = flag;
	}

	@Override
	public boolean process() {
		xbean.BattleFlags battleFlags = xtable.Battleflag.get(roleId);
		if(battleFlags == null)
		{//按理走不到这里
			battleFlags = xbean.Pod.newBattleFlags();
			xtable.Battleflag.insert(roleId, battleFlags);
			//这里应该有发到客户端的错误提示
			SSendBattleFlag send = new SSendBattleFlag();
			send.friendflags.addAll(battleFlags.getFriendflag());
			send.enemyflags.addAll(battleFlags.getEnemyflag());
			psend(roleId, send);
			return true;	
		}
		final Long battleID = xtable.Roleid2battleid.select(roleId);
		if(null == battleID) 
			return false;
		
		Team team = TeamManager.getTeamByRoleId(roleId);
		if(team == null||team.GetCommanderRoleId()!=roleId)
			return false;
		
		//锁battlelock
		if(!xtable.Battle.selectRoleids(battleID).containsKey(roleId))
			return false;
		
		int fighterid = xtable.Battle.selectRoleids(battleID).get(roleId);
		
		if(fighterid <BattleField.HOST_FIRST_ID || fighterid >= BattleField.GUEST_FIRST_ID+BattleField.A_SIDE_FIGHTER_NUM)
			return false;
		
		if(index <BattleField.HOST_FIRST_ID || index >= BattleField.GUEST_FIRST_ID+BattleField.A_SIDE_FIGHTER_NUM)
			return false;
		
		SSetBattleFlag send = new SSetBattleFlag(opttype, index, flag);
		mkdb.Procedure.psend(team.getTeamLeaderId(), send);
		for (xbean.TeamMember member : team.getTeamInfo().getMembers()) {
			if(xtable.Battle.selectRoleids(battleID).containsKey(member.getRoleid()))
				mkdb.Procedure.psend(member.getRoleid(), send);
		}
		return true;
	}
}
