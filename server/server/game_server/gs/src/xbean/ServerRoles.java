
package xbean;

public interface ServerRoles extends mkdb.Bean {
	public ServerRoles copy(); // deep clone
	public ServerRoles toData(); // a Data instance
	public ServerRoles toBean(); // a Bean instance
	public ServerRoles toDataIf(); // a Data instance If need. else return this
	public ServerRoles toBeanIf(); // a Bean instance If need. else return this

	public int getRolenum(); // 
	public long getCreatetime(); // 

	public void setRolenum(int _v_); // 
	public void setCreatetime(long _v_); // 
}
