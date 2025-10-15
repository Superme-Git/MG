package fire.pb.circletask.catchit;

import java.util.concurrent.TimeUnit;

import fire.pb.circletask.CircTask;
import fire.pb.circletask.CircTaskClass;
import fire.pb.circletask.CircleTaskManager;

public class CatchItQuest {
	
	public static void addCatchItQuestTimer(final long roleid, final int questid, final int circTaskId) {
		
		final CircTask ct = CircleTaskManager.getInstance().getCircTask(circTaskId);
		if(ct == null) {
			return;
		}
		
		if (ct.timeover == 0) {
			return;
		}
		
		//CircTask.timeover是以秒为单位的,如果要改,需要跟策划商定
		java.util.concurrent.ScheduledFuture<?> fur = mkdb.Mkdb.getInstance().getExecutor().schedule(
				 new CatchItTaskTimer(roleid, questid), ct.timeover/60, TimeUnit.MINUTES);
		xbean.SpecailquestFuture sf = xtable.Rolespecialquesttimer.get(roleid);
		if (sf == null) {
			sf = xbean.Pod.newSpecailquestFuture();
			xtable.Rolespecialquesttimer.add(roleid, sf);
		}
		if (sf.getQuestfuture().get(questid) != null)
			 sf.getQuestfuture().get(questid).cancel(true);
		 
		sf.getQuestfuture().remove(questid);
		sf.getQuestfuture().put(questid, fur);
	}
	
	public static void cancelCatchItQuestTimer(final long roleid, final int questid) {
		xbean.SpecailquestFuture sf = xtable.Rolespecialquesttimer.get(roleid);
		if (sf == null) {
			return;
		}
		
		if (sf.getQuestfuture().get(questid) != null)
			 sf.getQuestfuture().get(questid).cancel(true);
		 
		sf.getQuestfuture().remove(questid);
	}
	
	public static void clearCathcItQuestNpc(final long roleid) {
		mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleid);
				
				final xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(roleid);
				if (quest_map == null)
					return false;
				
				// 对于循环师门任务的处理
				for (Integer curType : CircleTaskManager.getInstance().getCircTaskTypes()) {
					final xbean.CircleTaskInfo ctsqinfo = quest_map.getTaskmap().get(curType);
					if (ctsqinfo == null)
						continue;
					
					if (ctsqinfo.getQuestclass() != CircTaskClass.CircTask_CatchIt)
						continue;
					
					if (ctsqinfo.getDstnpckey() == 0)
						continue;
					
					CircTask circTask = CircleTaskManager.getInstance().getCircTask(ctsqinfo.getId());
					if (circTask == null)
						continue;
					
					if (circTask.teamnum > 0) {
						if (team == null)
							continue;
						
						if (!team.isTeamLeader(roleid))
							continue;
					}
					
					fire.pb.map.SceneNpcManager.premoveNpcWhileCommit(ctsqinfo.getDstnpckey());
				}
				
				return true;
			}
		});
	}
}
