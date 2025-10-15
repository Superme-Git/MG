
package xbean;

public interface InstanceNpcSave extends mkdb.Bean {
	public InstanceNpcSave copy(); // deep clone
	public InstanceNpcSave toData(); // a Data instance
	public InstanceNpcSave toBean(); // a Bean instance
	public InstanceNpcSave toDataIf(); // a Data instance If need. else return this
	public InstanceNpcSave toBeanIf(); // a Bean instance If need. else return this

	public long getNpckey(); // npckey
	public long getRoleid(); // 有值表示被该角色占用,没有值表示空闲
	public int getFinishcount(); // 已经完成的数量
	public int getCount(); // 需要完成的数量

	public void setNpckey(long _v_); // npckey
	public void setRoleid(long _v_); // 有值表示被该角色占用,没有值表示空闲
	public void setFinishcount(int _v_); // 已经完成的数量
	public void setCount(int _v_); // 需要完成的数量
}
