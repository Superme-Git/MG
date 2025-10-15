
package xbean;

public interface MarshalBingFengRecord extends mkdb.Bean {
	public MarshalBingFengRecord copy(); // deep clone
	public MarshalBingFengRecord toData(); // a Data instance
	public MarshalBingFengRecord toBean(); // a Bean instance
	public MarshalBingFengRecord toDataIf(); // a Data instance If need. else return this
	public MarshalBingFengRecord toBeanIf(); // a Bean instance If need. else return this

	public int getShool(); // 职业
	public int getRank(); // 排名
	public long getRoleid(); // roleid
	public String getRolename(); // 玩家的名字
	public com.locojoy.base.Octets getRolenameOctets(); // 玩家的名字
	public int getStage(); // 关数
	public int getRounds(); // 总回合数
	public int getTimes(); // 耗时

	public void setShool(int _v_); // 职业
	public void setRank(int _v_); // 排名
	public void setRoleid(long _v_); // roleid
	public void setRolename(String _v_); // 玩家的名字
	public void setRolenameOctets(com.locojoy.base.Octets _v_); // 玩家的名字
	public void setStage(int _v_); // 关数
	public void setRounds(int _v_); // 总回合数
	public void setTimes(int _v_); // 耗时
}
