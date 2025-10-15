
package xbean;

public interface UniquePet extends mkdb.Bean {
	public UniquePet copy(); // deep clone
	public UniquePet toData(); // a Data instance
	public UniquePet toBean(); // a Bean instance
	public UniquePet toDataIf(); // a Data instance If need. else return this
	public UniquePet toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 所属角色

	public void setRoleid(long _v_); // 所属角色
}
