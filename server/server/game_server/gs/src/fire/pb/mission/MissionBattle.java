package fire.pb.mission;


import fire.pb.battle.PNewBattle;

public class MissionBattle extends PNewBattle {
	@SuppressWarnings("unused")
	private final int animationID;
	
	public MissionBattle(long roleID, int battleConfId, MissionBattleEndHandler handler,
			final int animationID ) {
		super(roleID, battleConfId, handler);
		this.animationID = animationID;
	}

	@Override
	public boolean process() {
		return super.process();
	}
	
}
