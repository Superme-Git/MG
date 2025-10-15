
package xbean;

public interface RefreshMonsterNpcInfo extends mkdb.Bean {
	public RefreshMonsterNpcInfo copy(); // deep clone
	public RefreshMonsterNpcInfo toData(); // a Data instance
	public RefreshMonsterNpcInfo toBean(); // a Bean instance
	public RefreshMonsterNpcInfo toDataIf(); // a Data instance If need. else return this
	public RefreshMonsterNpcInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, Integer> getNpcinfos(); // //npckey 对应的战斗状态 0=空闲  1=战斗中
	public java.util.Map<Long, Integer> getNpcinfosAsData(); // //npckey 对应的战斗状态 0=空闲  1=战斗中

}
