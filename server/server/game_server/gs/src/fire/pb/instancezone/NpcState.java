package fire.pb.instancezone;

public class NpcState {

	/**
	 * npc的状态
	 */
	public int npcstate;
	
	/**
	 * 是否能被多场战斗
	 */
	public int multibattle;
	
	/**
	 * 正在进行的战斗id 
	 */
	public long battleid;
	
	public long getBattleid() {
		return battleid;
	}

	public void setBattleid(long battleid) {
		this.battleid = battleid;
	}

	public int getNpcstate() {
		return npcstate;
	}

	public void setNpcstate(int npcstate) {
		this.npcstate = npcstate;
	}

	public int getMultibattle() {
		return multibattle;
	}

	public void setMultibattle(int multibattle) {
		this.multibattle = multibattle;
	}
	
	public NpcState(final int npcstate, final int multibattle) {
		this.npcstate = npcstate;
		this.multibattle = multibattle;
	}
	
	public NpcState(final int npcstate, final int multibattle, final long battleid) {
		this.npcstate = npcstate;
		this.multibattle = multibattle;
		this.battleid = battleid;
	}
	
}
