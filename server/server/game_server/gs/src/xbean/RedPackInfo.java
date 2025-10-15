
package xbean;

public interface RedPackInfo extends mkdb.Bean {
	public RedPackInfo copy(); // deep clone
	public RedPackInfo toData(); // a Data instance
	public RedPackInfo toBean(); // a Bean instance
	public RedPackInfo toDataIf(); // a Data instance If need. else return this
	public RedPackInfo toBeanIf(); // a Bean instance If need. else return this

	public String getRedpackid(); // 红包Id
	public com.locojoy.base.Octets getRedpackidOctets(); // 红包Id
	public int getModeltype(); // 红包类型
	public long getValuekey(); // 工会或者队伍id
	public long getSendroleid(); // 角色Id
	public String getRedpackdes(); // 红包寄语
	public com.locojoy.base.Octets getRedpackdesOctets(); // 红包寄语
	public int getRedpackstate(); // 红包状态
	public int getRedpackallnum(); // 红包总个数
	public int getRedpackreceivednum(); // 红包已经领取个数
	public int getRedpackallmoney(); // 红包总金额
	public int getRedpackreceiveallmoney(); // 已经领取红包总金额  便于退回红包
	public long getSendtime(); // 发送时间
	public int getBackflag(); // 0没退   1已经退钱了
	public java.util.List<xbean.RedPackRoleHisInfo> getRedpackrolehisinfolist(); // 领取红包记录
	public java.util.List<xbean.RedPackRoleHisInfo> getRedpackrolehisinfolistAsData(); // 领取红包记录

	public void setRedpackid(String _v_); // 红包Id
	public void setRedpackidOctets(com.locojoy.base.Octets _v_); // 红包Id
	public void setModeltype(int _v_); // 红包类型
	public void setValuekey(long _v_); // 工会或者队伍id
	public void setSendroleid(long _v_); // 角色Id
	public void setRedpackdes(String _v_); // 红包寄语
	public void setRedpackdesOctets(com.locojoy.base.Octets _v_); // 红包寄语
	public void setRedpackstate(int _v_); // 红包状态
	public void setRedpackallnum(int _v_); // 红包总个数
	public void setRedpackreceivednum(int _v_); // 红包已经领取个数
	public void setRedpackallmoney(int _v_); // 红包总金额
	public void setRedpackreceiveallmoney(int _v_); // 已经领取红包总金额  便于退回红包
	public void setSendtime(long _v_); // 发送时间
	public void setBackflag(int _v_); // 0没退   1已经退钱了
}
