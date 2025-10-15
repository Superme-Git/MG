package fire.pb.instancezone.faction;

import java.util.TimerTask;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;
import fire.pb.instancezone.Module;
import fire.pb.npc.SAskQuestion;
import gnet.link.Onlines;

public class FactionInstAskQuestionTimer extends TimerTask {
	private final long npckey;
	private final long landkey;
	
	public FactionInstAskQuestionTimer(final long landkey, final long npckey) {
		this.landkey = landkey;
		this.npckey = npckey;
	}

	@Override
	public void run() {
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				InstanceZone instzone = InstanceZoneFactory.getInstanceZone(landkey, false);
				if (instzone == null)
					return false;
				
				SAskQuestion sAskQuestion = new SAskQuestion();
				sAskQuestion.lastresult = -1;
			    sAskQuestion.questionid = -1;
			    sAskQuestion.questiontype = SAskQuestion.GUILD_ANSWER;
			    sAskQuestion.npckey = npckey;
			    Onlines.getInstance().send(landkey, sAskQuestion);
			    instzone.reSetSaveNpcState(npckey);
				Module.getInstance().setDynamicNpcState(instzone.getLandkey(), npckey, 0);
			    
				return true;
			}
			
		}.submit();
	}

}
