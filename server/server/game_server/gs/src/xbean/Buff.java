
package xbean;

public interface Buff extends mkdb.Bean {
	public Buff copy(); // deep clone
	public Buff toData(); // a Data instance
	public Buff toBean(); // a Bean instance
	public Buff toDataIf(); // a Data instance If need. else return this
	public Buff toBeanIf(); // a Bean instance If need. else return this

	public final static int BATTLE_END_IMPACT = 1; // 战斗结束detach
	public final static int BATTLE_END_PROCESS = 2; // 战斗结束process

	public int getIndex(); // buff类型Id，一种类型的buff只能有一个
	public long getImpacttime(); // buff attach时的时间，用于计算剩余时间和检测到期
	public long getRemaintime(); // 计时buff总持续时间（period时的period）
	public int getRound(); // 计数buff剩余回合（period时的count）
	public long getValue(); // buff的剩余量（period时的initDelay）
	public java.util.Map<Integer, Float> getEffects(); // key = effect type id
	public java.util.Map<Integer, Float> getEffectsAsData(); // key = effect type id
	public int getFighterkey(); // 当在战斗中添加的buff时，记录战斗者key

	public void setIndex(int _v_); // buff类型Id，一种类型的buff只能有一个
	public void setImpacttime(long _v_); // buff attach时的时间，用于计算剩余时间和检测到期
	public void setRemaintime(long _v_); // 计时buff总持续时间（period时的period）
	public void setRound(int _v_); // 计数buff剩余回合（period时的count）
	public void setValue(long _v_); // buff的剩余量（period时的initDelay）
	public void setFighterkey(int _v_); // 当在战斗中添加的buff时，记录战斗者key
}
