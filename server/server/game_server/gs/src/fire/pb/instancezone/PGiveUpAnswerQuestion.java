package fire.pb.instancezone;

import fire.pb.npc.SAskQuestion;
import mkdb.Procedure;

public class PGiveUpAnswerQuestion extends Procedure {

	private long roleid;
	private long npckey;

	public PGiveUpAnswerQuestion(final long roleid, final long npckey) {
		this.roleid = roleid;
		this.npckey = npckey;
	}

	@Override
	protected boolean process() throws Exception {
		InstanceZone instzone = InstanceZoneFactory.getRoleCurInstanceZone(roleid, false);
		if (instzone == null)
			return false;
		
		SAskQuestion sAskQuestion = new SAskQuestion();
		sAskQuestion.lastresult = -1;
	    sAskQuestion.questionid = -1;
	    sAskQuestion.questiontype = SAskQuestion.GUILD_ANSWER;
	    sAskQuestion.npckey = npckey;
//	    Onlines.getInstance().send(landkey, sAskQuestion);
	    instzone.reSetSaveNpcState(npckey);
		Module.getInstance().setDynamicNpcState(instzone.getLandkey(), npckey, 0);
		
		Module.getInstance().cancleNpcTimerSchedule(instzone.getLandkey(), npckey);
		return true;
	}
	
}
