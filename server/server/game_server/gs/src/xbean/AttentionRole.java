
package xbean;

public interface AttentionRole extends mkdb.Bean {
	public AttentionRole copy(); // deep clone
	public AttentionRole toData(); // a Data instance
	public AttentionRole toBean(); // a Bean instance
	public AttentionRole toDataIf(); // a Data instance If need. else return this
	public AttentionRole toBeanIf(); // a Bean instance If need. else return this

	public java.util.Set<Long> getRoleids(); // 角色id
	public java.util.Set<Long> getRoleidsAsData(); // 角色id

}
