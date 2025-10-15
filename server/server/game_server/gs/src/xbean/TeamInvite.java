
package xbean;

public interface TeamInvite extends mkdb.Bean {
	public TeamInvite copy(); // deep clone
	public TeamInvite toData(); // a Data instance
	public TeamInvite toBean(); // a Bean instance
	public TeamInvite toDataIf(); // a Data instance If need. else return this
	public TeamInvite toBeanIf(); // a Bean instance If need. else return this

	public long getTeamid(); // 
	public long getRoleid(); // 
	public long getInvitetime(); // 

	public void setTeamid(long _v_); // 
	public void setRoleid(long _v_); // 
	public void setInvitetime(long _v_); // 
}
