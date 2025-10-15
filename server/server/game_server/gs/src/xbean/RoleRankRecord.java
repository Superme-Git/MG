
package xbean;

public interface RoleRankRecord extends mkdb.Bean {
	public RoleRankRecord copy(); // deep clone
	public RoleRankRecord toData(); // a Data instance
	public RoleRankRecord toBean(); // a Bean instance
	public RoleRankRecord toDataIf(); // a Data instance If need. else return this
	public RoleRankRecord toBeanIf(); // a Bean instance If need. else return this

	public int getRank(); // 排名 by changhao
	public long getRoleid(); // 人物ID by changhao
	public String getRolename(); // 人物名称 by changhao
	public com.locojoy.base.Octets getRolenameOctets(); // 人物名称 by changhao
	public int getSchool(); // 职业 by changhao
	public int getLevel(); // 等级 by changhao
	public int getScore(); // 人物评分 by changhao
	public long getTriggertime(); // 触发时间 by changhao

	public void setRank(int _v_); // 排名 by changhao
	public void setRoleid(long _v_); // 人物ID by changhao
	public void setRolename(String _v_); // 人物名称 by changhao
	public void setRolenameOctets(com.locojoy.base.Octets _v_); // 人物名称 by changhao
	public void setSchool(int _v_); // 职业 by changhao
	public void setLevel(int _v_); // 等级 by changhao
	public void setScore(int _v_); // 人物评分 by changhao
	public void setTriggertime(long _v_); // 触发时间 by changhao
}
