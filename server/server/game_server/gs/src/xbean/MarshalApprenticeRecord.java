
package xbean;

public interface MarshalApprenticeRecord extends mkdb.Bean {
	public MarshalApprenticeRecord copy(); // deep clone
	public MarshalApprenticeRecord toData(); // a Data instance
	public MarshalApprenticeRecord toBean(); // a Bean instance
	public MarshalApprenticeRecord toDataIf(); // a Data instance If need. else return this
	public MarshalApprenticeRecord toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // roleid
	public String getNickname(); // 名字
	public com.locojoy.base.Octets getNicknameOctets(); // 名字
	public int getLevel(); // 等级
	public int getSchoolid(); // 职业id
	public int getApprenticenum(); // 出徒数量
	public int getRank(); // 排名

	public void setRoleid(long _v_); // roleid
	public void setNickname(String _v_); // 名字
	public void setNicknameOctets(com.locojoy.base.Octets _v_); // 名字
	public void setLevel(int _v_); // 等级
	public void setSchoolid(int _v_); // 职业id
	public void setApprenticenum(int _v_); // 出徒数量
	public void setRank(int _v_); // 排名
}
