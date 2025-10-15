
package xbean;

public interface LDVideoRoleRoseInfo extends mkdb.Bean {
	public LDVideoRoleRoseInfo copy(); // deep clone
	public LDVideoRoleRoseInfo toData(); // a Data instance
	public LDVideoRoleRoseInfo toBean(); // a Bean instance
	public LDVideoRoleRoseInfo toDataIf(); // a Data instance If need. else return this
	public LDVideoRoleRoseInfo toBeanIf(); // a Bean instance If need. else return this

	public String getVideoid(); // 录像id
	public com.locojoy.base.Octets getVideoidOctets(); // 录像id
	public int getRosenum(); // 次数

	public void setVideoid(String _v_); // 录像id
	public void setVideoidOctets(com.locojoy.base.Octets _v_); // 录像id
	public void setRosenum(int _v_); // 次数
}
