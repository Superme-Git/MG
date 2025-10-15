
package xbean;

public interface TeamMember extends mkdb.Bean {
	public TeamMember copy(); // deep clone
	public TeamMember toData(); // a Data instance
	public TeamMember toBean(); // a Bean instance
	public TeamMember toDataIf(); // a Data instance If need. else return this
	public TeamMember toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 
	public int getState(); // 

	public void setRoleid(long _v_); // 
	public void setState(int _v_); // 
}
