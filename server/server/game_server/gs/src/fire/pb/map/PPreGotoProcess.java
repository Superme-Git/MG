package fire.pb.map;

import fire.pb.battle.watch.PEndWatchBattle;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.move.SRoleEnterScene;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.team.TeamMemberState;
import mkdb.Procedure;

/**
 * 跳转前的处理，特别是对强跳前的处理
 * @author nobody
 *
 */
public class PPreGotoProcess extends Procedure{
	
	final private long roleId;
	final private int gototype;

	PPreGotoProcess(long roleId,int gototype){
		this.roleId = roleId;
		this.gototype = gototype;
	}
	
	@Override
	protected boolean process() throws Exception{
		if (gototype == SRoleEnterScene.FORCE_GOTO){
			Team team = TeamManager.getTeamByRoleId(roleId);
			if (team != null){
				if (team.isTeamLeader(roleId)) {// 是队长，正常队员都暂离
					for (long memberrid : team.getNormalMemberIds())
						team.setTeamMemberStateWithSP(memberrid,TeamMemberState.eTeamAbsent);
				} else if (team.isNormalMember(roleId)) {// 是正常队员，自己暂离
					team.setTeamMemberStateWithSP(roleId,TeamMemberState.eTeamAbsent);
				}
			}
			// 强制跳转的特殊处理
			BuffAgent agent = new BuffRoleImpl(roleId);
			if (agent.existState(BuffConstant.StateType.STATE_BATTLE_WATCHER)){
				// 观战中
				new PEndWatchBattle(roleId).call();
			}
			if (agent.existBuff(BuffConstant.StateType.STATE_REPLAY)){
				// 回放
				agent.removeCBuff(BuffConstant.StateType.STATE_REPLAY);
			}
			
			if (agent.existBuff(BuffConstant.CONTINUAL_PLAY_CG)){
				// 播动画
				agent.removeCBuff(BuffConstant.CONTINUAL_PLAY_CG);
			}
		}
		return true;
	}
}
