
package xbean;

public interface PvP3RaceRole extends mkdb.Bean {
	public PvP3RaceRole copy(); // deep clone
	public PvP3RaceRole toData(); // a Data instance
	public PvP3RaceRole toBean(); // a Bean instance
	public PvP3RaceRole toDataIf(); // a Data instance If need. else return this
	public PvP3RaceRole toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 
	public String getName(); // 
	public com.locojoy.base.Octets getNameOctets(); // 
	public int getScore(); // 积分

	public void setRoleid(long _v_); // 
	public void setName(String _v_); // 
	public void setNameOctets(com.locojoy.base.Octets _v_); // 
	public void setScore(int _v_); // 积分
}
