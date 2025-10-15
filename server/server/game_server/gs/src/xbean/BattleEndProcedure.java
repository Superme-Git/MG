
package xbean;

public interface BattleEndProcedure extends mkdb.Bean {
	public BattleEndProcedure copy(); // deep clone
	public BattleEndProcedure toData(); // a Data instance
	public BattleEndProcedure toBean(); // a Bean instance
	public BattleEndProcedure toDataIf(); // a Data instance If need. else return this
	public BattleEndProcedure toBeanIf(); // a Bean instance If need. else return this

	public int getFighterid(); // 战斗者ID，（现在应该是只有角色，将来还可能会有宠物）
	public mkdb.Procedure getEndprocedure(); // 战斗者出战斗时执行的Procedure

	public void setFighterid(int _v_); // 战斗者ID，（现在应该是只有角色，将来还可能会有宠物）
	public void setEndprocedure(mkdb.Procedure _v_); // 战斗者出战斗时执行的Procedure
}
