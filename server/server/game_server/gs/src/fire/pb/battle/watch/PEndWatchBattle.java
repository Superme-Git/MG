
package fire.pb.battle.watch;

import gnet.link.Onlines;
import mkdb.Procedure;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fire.msp.role.GSetRoleBattleInfo;
import fire.pb.GsClient;
import fire.pb.PropConf;
import fire.pb.battle.BattleEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.battle.SRemoveWatcher;
import fire.pb.battle.SSendBattleEnd;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.team.TeamMemberState;

public class PEndWatchBattle extends Procedure {

	private final long watcherid;
	private boolean isTeam;

	public PEndWatchBattle(long watcherid) {
		this.watcherid = watcherid;
		isTeam = false;
	}

	@Override
	protected boolean process() throws Exception {
		
		Long battleid = xtable.Watcherid2battleid.select(watcherid);
		if (battleid==null) 
			return false;
		xbean.BattleInfo battle = xtable.Battle.get(battleid);
		if (battle == null) {
			if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("Can't find battleid by watcherid:" + watcherid);}
			return false;
		}
		
		Team watcherTeam = TeamManager.getTeamByRoleId(watcherid);		
		if(watcherTeam != null){
			//是正常的队员不能观战
			if(watcherTeam.isNormalMember(watcherid)){
				return true;
			}			
			if(watcherTeam.isTeamLeader(watcherid))
				isTeam = true;
		}		
		
		if(isTeam&&watcherTeam != null)
		{//如果是队长
			for(long normalTeamMemberid: watcherTeam.getNormalMemberIds())
				removewatcher(battle,normalTeamMemberid);
		}
		else
		{
			boolean ok = removewatcher(battle,watcherid);
			if (ok)
			{
				if (battle.getQuitteamroleids().contains(watcherid))
				{
					pexecuteWhileCommit(new fire.pb.team.PQuitTeamProc(watcherid));	
				}
				
				if (watcherTeam != null && watcherTeam.isReturnMember(watcherid))
				{
					watcherTeam.setTeamMemberStateWithSP(watcherid, TeamMemberState.eTeamAbsent);
				}
			}

			return ok;
		}		
		return true;
	}
	private boolean removewatcher(xbean.BattleInfo battle, long watcherid)
	{
		lock(mkdb.Lockeys.get(xtable.Locks.ROLELOCK, new Object[]{watcherid}));
		Map<Long, Integer> audiences = battle.getAudience();
		int battleindex = 0;
		if (audiences.containsKey(watcherid)) {
			battleindex = audiences.get(watcherid);
			audiences.remove(watcherid);
			battle.getWatchers().remove(battleindex);
			xtable.Watcherid2battleid.remove(watcherid);
		} else
			return false;

		final SSendBattleEnd snd = new SSendBattleEnd();
		Onlines.getInstance().send(watcherid, snd); //这个地方不能用psend,否则在强制pk观战者的时候,观战者进入不了战斗场景
		// 通知其他人观战者退出,前10名观战者才发这个消息.有个问题,退出后后面的观战者要不要顶上,仍然保持10个观战者在战斗场景内?
		if (battleindex < BattleField.FIRST_WATCHER_BATTLE_INDEX+PropConf.Battle.WATCHERS_IN_BATTLE) {
			final SRemoveWatcher srw = new SRemoveWatcher();
			srw.fighterindex = battleindex;
			Set<Long> allFightersID = battle.getRoleids().keySet();
			Set<Long> receiverSet = new HashSet<Long>();
			receiverSet.addAll(allFightersID);
			if (audiences.size() >= 1) {
				receiverSet.addAll(audiences.keySet());
			}
			Onlines.getInstance().send(receiverSet, srw);
		}

		// 出战斗发送9屏协议
		GsClient.pSendWhileCommit(new GSetRoleBattleInfo(watcherid, 0));

		for (BattleEndHandler handler : battle.getBattleendhandlers())
		{
			handler.handleWatcherOutBattle(watcherid);
		}
		return true;		
	}
	
}
