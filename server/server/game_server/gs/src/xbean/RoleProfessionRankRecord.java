
package xbean;

public interface RoleProfessionRankRecord extends mkdb.Bean {
	public RoleProfessionRankRecord copy(); // deep clone
	public RoleProfessionRankRecord toData(); // a Data instance
	public RoleProfessionRankRecord toBean(); // a Bean instance
	public RoleProfessionRankRecord toDataIf(); // a Data instance If need. else return this
	public RoleProfessionRankRecord toBeanIf(); // a Bean instance If need. else return this

	public int getRank(); // 排名
	public long getRoleid(); // 人物ID
	public String getRolename(); // 人物名称
	public com.locojoy.base.Octets getRolenameOctets(); // 人物名称
	public int getSchool(); // 职业
	public int getLevel(); // 等级
	public int getScore(); // 总评分
	public String getClanname(); // 帮会 by changhao
	public com.locojoy.base.Octets getClannameOctets(); // 帮会 by changhao
	public long getTriggertime(); // 触发时间 by changhao

	public void setRank(int _v_); // 排名
	public void setRoleid(long _v_); // 人物ID
	public void setRolename(String _v_); // 人物名称
	public void setRolenameOctets(com.locojoy.base.Octets _v_); // 人物名称
	public void setSchool(int _v_); // 职业
	public void setLevel(int _v_); // 等级
	public void setScore(int _v_); // 总评分
	public void setClanname(String _v_); // 帮会 by changhao
	public void setClannameOctets(com.locojoy.base.Octets _v_); // 帮会 by changhao
	public void setTriggertime(long _v_); // 触发时间 by changhao
}
