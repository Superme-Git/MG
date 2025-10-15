package fire.pb.circletask.catchit;

import java.util.ArrayList;
import java.util.List;

import fire.pb.circletask.CircTaskClass;
import fire.pb.circletask.CircTaskConf;
import fire.pb.circletask.CircleTaskManager;

public class CatchItTaskTimer implements Runnable {
	private final long roleid;
	private final int questid;
	
	public CatchItTaskTimer(final long roleid, final int questid) {
		this.roleid = roleid;
		this.questid = questid;
	}

	@Override
	public void run() {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.CircleTaskMap quest_map = xtable.Rolecircletask.select(roleid);
				if (quest_map == null)
					return false;
				xbean.CircleTaskInfo sqinfo = quest_map.getTaskmap().get(questid);
				if (null == sqinfo || sqinfo.getQuestclass() != CircTaskClass.CircTask_CatchIt) {
					return false;
				}
				
				PGiveUpCatchItTask giveup = new PGiveUpCatchItTask(roleid, questid);
				pexecuteWhileCommit(giveup);
				
				xbean.SpecailquestFuture sf = xtable.Rolespecialquesttimer.get(roleid);
				if (sf != null) {
					sf.getQuestfuture().remove(questid);
					if (sf.getQuestfuture().isEmpty()) 
						xtable.Rolespecialquesttimer.remove(roleid);
				}
				
				CircTaskConf ctc = CircleTaskManager.getCircTaskConf(sqinfo.getQuesttype());
				if (ctc != null) {
					List<String> para = new ArrayList<String>(1);
					para.add(ctc.getStrtaskname());
					fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 180040, para);
				}
				
				return true;
			}
		}.submit();

	}

}
