
package xbean;

public interface MarshalPetScoreRecord extends mkdb.Bean {
	public MarshalPetScoreRecord copy(); // deep clone
	public MarshalPetScoreRecord toData(); // a Data instance
	public MarshalPetScoreRecord toBean(); // a Bean instance
	public MarshalPetScoreRecord toDataIf(); // a Data instance If need. else return this
	public MarshalPetScoreRecord toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // roleid
	public long getUniquepetid(); // 宠物的唯一id
	public String getNickname(); // 名字
	public com.locojoy.base.Octets getNicknameOctets(); // 名字
	public String getPetname(); // 宠物名字
	public com.locojoy.base.Octets getPetnameOctets(); // 宠物名字
	public int getPetgrade(); // 宠物评分
	public int getRank(); // 排名
	public int getColour(); // 宠物颜色

	public void setRoleid(long _v_); // roleid
	public void setUniquepetid(long _v_); // 宠物的唯一id
	public void setNickname(String _v_); // 名字
	public void setNicknameOctets(com.locojoy.base.Octets _v_); // 名字
	public void setPetname(String _v_); // 宠物名字
	public void setPetnameOctets(com.locojoy.base.Octets _v_); // 宠物名字
	public void setPetgrade(int _v_); // 宠物评分
	public void setRank(int _v_); // 排名
	public void setColour(int _v_); // 宠物颜色
}
