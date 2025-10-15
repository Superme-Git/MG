package fire.pb.circletask;

import fire.pb.activity.winner.WinnerManager;
import fire.pb.circletask.catchit.PGiveUpCatchItTask;
import fire.pb.mission.instance.InstanceManager;

public class PAbandonCircleTask extends mkdb.Procedure{
	private long roleid;
	private int questid;
	
	public PAbandonCircleTask(long roleid, int questid){
		this.roleid = roleid;
		this.questid = questid;
	}
	
	public boolean process(){
		if(questid == WinnerManager.WINNER_TASK_ID||questid == WinnerManager.WINNER_TASK_ID_ACCEPT)
			return WinnerManager.getInstance().abandonWinnerTask(roleid);
		
		if(InstanceManager.getInstance().isInstanceTask(questid)){
			InstanceManager.getInstance().abandonInstanceTask(roleid, questid);
			return true;
		}
		
		if (questid == 1030000) {
			return new PGiveUpCatchItTask(roleid, questid).call();
		}
			
		final CircleTask sq = new CircleTask(roleid, false);
		sq.abandonSpecialQuest(questid);
		
		pexecuteWhileCommit(new PGiveUpCircleTask(roleid, questid));
		return true;	
	}

}
