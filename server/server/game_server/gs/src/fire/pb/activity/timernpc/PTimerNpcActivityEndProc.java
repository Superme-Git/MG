package fire.pb.activity.timernpc;

import mkdb.Procedure;

public class PTimerNpcActivityEndProc extends Procedure {
	private final int actId;
	
	public PTimerNpcActivityEndProc(final int actId) {
		this.actId = actId;
	}
	
	@Override
	protected boolean process() throws Exception {
		xbean.timerNpcInfoCol npcInfo = xtable.Timernpcinfotable.get(actId);
		if(npcInfo == null){
			return true;
		}
		TimerNpcService.getInstance().clearMonster(npcInfo);
		return true;
	}

}
