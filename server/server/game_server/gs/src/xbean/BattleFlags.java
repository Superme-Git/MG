
package xbean;

public interface BattleFlags extends mkdb.Bean {
	public BattleFlags copy(); // deep clone
	public BattleFlags toData(); // a Data instance
	public BattleFlags toBean(); // a Bean instance
	public BattleFlags toDataIf(); // a Data instance If need. else return this
	public BattleFlags toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<String> getFriendflag(); // 
	public java.util.List<String> getFriendflagAsData(); // 
	public java.util.List<String> getEnemyflag(); // 
	public java.util.List<String> getEnemyflagAsData(); // 

}
