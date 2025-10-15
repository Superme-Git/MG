package fire.pb.battle;

/**战斗结束以后什么也不做的处理类*/
public class EmptyBattleEndHandler extends BasicEndHandler{

	@Override
	public boolean handleDeath() {
		
		return true;
	}

	@Override
	protected boolean handleAward() {
		return true;
	}
}
