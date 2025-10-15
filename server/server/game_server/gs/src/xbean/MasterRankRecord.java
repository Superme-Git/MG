
package xbean;

public interface MasterRankRecord extends mkdb.Bean {
	public MasterRankRecord copy(); // deep clone
	public MasterRankRecord toData(); // a Data instance
	public MasterRankRecord toBean(); // a Bean instance
	public MasterRankRecord toDataIf(); // a Data instance If need. else return this
	public MasterRankRecord toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 人物ID
	public String getRolename(); // 人物名称
	public com.locojoy.base.Octets getRolenameOctets(); // 人物名称
	public long getShidezhi(); // 师德值

	public void setRoleid(long _v_); // 人物ID
	public void setRolename(String _v_); // 人物名称
	public void setRolenameOctets(com.locojoy.base.Octets _v_); // 人物名称
	public void setShidezhi(long _v_); // 师德值
}
