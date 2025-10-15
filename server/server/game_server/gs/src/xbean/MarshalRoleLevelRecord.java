
package xbean;

public interface MarshalRoleLevelRecord extends mkdb.Bean {
	public MarshalRoleLevelRecord copy(); // deep clone
	public MarshalRoleLevelRecord toData(); // a Data instance
	public MarshalRoleLevelRecord toBean(); // a Bean instance
	public MarshalRoleLevelRecord toDataIf(); // a Data instance If need. else return this
	public MarshalRoleLevelRecord toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // roleid
	public String getRolename(); // 名字
	public com.locojoy.base.Octets getRolenameOctets(); // 名字
	public int getLevel(); // 等级
	public int getSchool(); // 职业id
	public int getRank(); // 排名

	public void setRoleid(long _v_); // roleid
	public void setRolename(String _v_); // 名字
	public void setRolenameOctets(com.locojoy.base.Octets _v_); // 名字
	public void setLevel(int _v_); // 等级
	public void setSchool(int _v_); // 职业id
	public void setRank(int _v_); // 排名
}
