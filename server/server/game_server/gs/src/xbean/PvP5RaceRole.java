
package xbean;

public interface PvP5RaceRole extends mkdb.Bean {
	public PvP5RaceRole copy(); // deep clone
	public PvP5RaceRole toData(); // a Data instance
	public PvP5RaceRole toBean(); // a Bean instance
	public PvP5RaceRole toDataIf(); // a Data instance If need. else return this
	public PvP5RaceRole toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 
	public String getName(); // 
	public com.locojoy.base.Octets getNameOctets(); // 
	public int getScore(); // 积分
	public int getBattlenum(); // 本场比赛的次数
	public int getWinnum(); // 本场比赛赢的次数

	public void setRoleid(long _v_); // 
	public void setName(String _v_); // 
	public void setNameOctets(com.locojoy.base.Octets _v_); // 
	public void setScore(int _v_); // 积分
	public void setBattlenum(int _v_); // 本场比赛的次数
	public void setWinnum(int _v_); // 本场比赛赢的次数
}
