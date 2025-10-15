
package xbean;

public interface BattleFieldScroeRank extends mkdb.Bean {
	public BattleFieldScroeRank copy(); // deep clone
	public BattleFieldScroeRank toData(); // a Data instance
	public BattleFieldScroeRank toBean(); // a Bean instance
	public BattleFieldScroeRank toDataIf(); // a Data instance If need. else return this
	public BattleFieldScroeRank toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // id by changhao
	public int getScroe(); // 积分 by changhao

	public void setRoleid(long _v_); // id by changhao
	public void setScroe(int _v_); // 积分 by changhao
}
