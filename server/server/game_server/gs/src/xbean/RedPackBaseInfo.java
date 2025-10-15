
package xbean;

public interface RedPackBaseInfo extends mkdb.Bean {
	public RedPackBaseInfo copy(); // deep clone
	public RedPackBaseInfo toData(); // a Data instance
	public RedPackBaseInfo toBean(); // a Bean instance
	public RedPackBaseInfo toDataIf(); // a Data instance If need. else return this
	public RedPackBaseInfo toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 角色Id
	public String getRedpackid(); // 红包Id
	public com.locojoy.base.Octets getRedpackidOctets(); // 红包Id
	public long getSendtime(); // 发送时间

	public void setRoleid(long _v_); // 角色Id
	public void setRedpackid(String _v_); // 红包Id
	public void setRedpackidOctets(com.locojoy.base.Octets _v_); // 红包Id
	public void setSendtime(long _v_); // 发送时间
}
