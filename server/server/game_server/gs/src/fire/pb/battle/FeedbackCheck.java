package fire.pb.battle;

/**超时检查*/
public class FeedbackCheck implements Runnable {
	
	FeedbackCheck(final long battleID, final int round){
		this.battleID = battleID;
		this.round = round;
	}
	
	@Override
	public void run() {
		//if (BattleField.logger.isDebugEnabled()) {
			BattleField.logger.error("战斗  "+battleID+"  Feedback超时，可以开始下一回合。");
		//	}
		new PRoundEnd(battleID,round).submit();
	}
	
	private final long battleID;
	private final int round;
}
