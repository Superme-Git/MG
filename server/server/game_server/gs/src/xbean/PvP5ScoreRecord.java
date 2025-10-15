
package xbean;

public interface PvP5ScoreRecord extends mkdb.Bean {
	public PvP5ScoreRecord copy(); // deep clone
	public PvP5ScoreRecord toData(); // a Data instance
	public PvP5ScoreRecord toBean(); // a Bean instance
	public PvP5ScoreRecord toDataIf(); // a Data instance If need. else return this
	public PvP5ScoreRecord toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // roleid
	public String getRolename(); // 角色名字
	public com.locojoy.base.Octets getRolenameOctets(); // 角色名字
	public int getScore(); // 积分
	public int getSchool(); // 职业

	public void setRoleid(long _v_); // roleid
	public void setRolename(String _v_); // 角色名字
	public void setRolenameOctets(com.locojoy.base.Octets _v_); // 角色名字
	public void setScore(int _v_); // 积分
	public void setSchool(int _v_); // 职业
}
