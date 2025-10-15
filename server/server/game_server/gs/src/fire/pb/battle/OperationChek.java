package fire.pb.battle;

/**用户选择操作的超时检测*/
public class OperationChek implements Runnable {
	
	private final long battleID;
	private final int round;
	OperationChek(final long battleID, final int round){
		this.battleID = battleID;
		this.round = round;
	}
	
	@Override
	public void run() {
		if (BattleField.logger.isDebugEnabled()) {BattleField.logger.debug("操作超时检测");}
		try {
			new PDefaultAction(battleID,round).submit();
			//new PPlayScript(battleID).submit();
		} catch (Exception e) {
			BattleField.logger.error("操作超时检测失败",e);
		} 
		
	}
	
}
