package fire.pb.circletask;

import fire.pb.circletask.SRefreshSpecialQuestState;
import fire.pb.circletask.SpecialQuestState;
import fire.pb.mission.Module;
import fire.pb.mission.SReqMissionCanAccept;
import mkdb.Procedure;

public class PGiveUpCircleTask extends Procedure {

	private final long roleId;
	private final int questId;
	
	public PGiveUpCircleTask(final long roleId, final int questId) {
		this.roleId = roleId;
		this.questId = questId;
	}
	
	public boolean process() {
		fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(roleId, questId);
		if (ct == null) {
			return false;
		}
		
		if (ct.teamnum > 0) {
			if (null == team)
				return false;
			
			if (!team.isTeamLeader(roleId))
				return false;
			
			// 全队正常状态成员 加锁
			final java.util.List<Long> members = team.getNormalMemberIds();
			this.lock(xtable.Locks.ROLELOCK, members);
			
			for (Long rid : members) {
				xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(rid);
				if (quest_map == null)
					return false;
				xbean.CircleTaskInfo questinfo = quest_map.getTaskmap().get(questId);
				if (questinfo == null) {
					return false;
				}
				
				//只有队长才能放弃队伍的npc
				if (questinfo.getQuestclass() == CircTaskClass.CircTask_CatchIt && rid == roleId) {
					fire.pb.map.SceneNpcManager.removeNpc(questinfo.getDstnpckey());
				}
				
				questinfo.setDstitemid(0);
				questinfo.setDstitemnum(0);
				questinfo.setDstmapid(0);
				questinfo.setDstnpcid(0);
				questinfo.setDstnpckey(0);
				questinfo.setDstx(0);
				questinfo.setDsty(0);
				questinfo.setQueststate(SpecialQuestState.ABANDONED);
				questinfo.setQuesttype(0);
				questinfo.setBattlenum(0);
				questinfo.setNpcname("");
				questinfo.setBigaward(0);
				questinfo.setRound(0);
				
				final SRefreshSpecialQuestState sendstate = new SRefreshSpecialQuestState();
				sendstate.questid = questId;
				sendstate.state = SpecialQuestState.ABANDONED;
				Procedure.psendWhileCommit(rid, sendstate);
				
				new mkdb.Procedure() {
					@Override
					protected boolean process() {
						SReqMissionCanAccept send = new SReqMissionCanAccept();
						Module.getInstance().getCanAcceptMission(rid, send.missions);
						mkdb.Procedure.psend(rid, send);
						return true;
					}
				}.call();
			}
			return true;
		}
		else {
			xbean.CircleTaskMap quest_map = xtable.Rolecircletask.get(roleId);
			if (quest_map == null)
				return false;
			xbean.CircleTaskInfo sqinfo = quest_map.getTaskmap().get(questId);
			if (sqinfo == null) {
				return false;
			}
			
			if (sqinfo.getQuestclass() == CircTaskClass.CircTask_CatchIt) {
				fire.pb.map.SceneNpcManager.removeNpc(sqinfo.getDstnpckey());
			}
			
			sqinfo.setDstitemid(0);
			sqinfo.setDstitemnum(0);
			sqinfo.setDstmapid(0);
			sqinfo.setDstnpcid(0);
			sqinfo.setDstnpckey(0);
			sqinfo.setDstx(0);
			sqinfo.setDsty(0);
			sqinfo.setQueststate(SpecialQuestState.ABANDONED);
			sqinfo.setQuesttype(0);
			sqinfo.setBattlenum(0);
			sqinfo.setNpcname("");
			sqinfo.setBigaward(0);
			sqinfo.setRound(0);
			
			final SRefreshSpecialQuestState sendstate = new SRefreshSpecialQuestState();
			sendstate.questid = questId;
			sendstate.state = SpecialQuestState.ABANDONED;
			Procedure.psendWhileCommit(roleId, sendstate);
			
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					SReqMissionCanAccept send = new SReqMissionCanAccept();
					Module.getInstance().getCanAcceptMission(roleId, send.missions);
					mkdb.Procedure.psend(roleId, send);
					return true;
				}
			}.call();
			return true;
		}
	}
}
