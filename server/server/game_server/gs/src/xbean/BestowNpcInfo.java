
package xbean;

public interface BestowNpcInfo extends mkdb.Bean {
	public BestowNpcInfo copy(); // deep clone
	public BestowNpcInfo toData(); // a Data instance
	public BestowNpcInfo toBean(); // a Bean instance
	public BestowNpcInfo toDataIf(); // a Data instance If need. else return this
	public BestowNpcInfo toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 宝箱刷新的角色

	public void setRoleid(long _v_); // 宝箱刷新的角色
}
