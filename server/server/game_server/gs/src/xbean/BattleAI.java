
package xbean;

public interface BattleAI extends mkdb.Bean {
	public BattleAI copy(); // deep clone
	public BattleAI toData(); // a Data instance
	public BattleAI toBean(); // a Bean instance
	public BattleAI toDataIf(); // a Data instance If need. else return this
	public BattleAI toBeanIf(); // a Bean instance If need. else return this

	public int getId(); // 
	public int getCount(); // 成功执行的次数
	public int getEnableround(); // 启用时的回合数

	public void setId(int _v_); // 
	public void setCount(int _v_); // 成功执行的次数
	public void setEnableround(int _v_); // 启用时的回合数
}
