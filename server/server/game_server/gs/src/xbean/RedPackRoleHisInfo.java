
package xbean;

public interface RedPackRoleHisInfo extends mkdb.Bean {
	public RedPackRoleHisInfo copy(); // deep clone
	public RedPackRoleHisInfo toData(); // a Data instance
	public RedPackRoleHisInfo toBean(); // a Bean instance
	public RedPackRoleHisInfo toDataIf(); // a Data instance If need. else return this
	public RedPackRoleHisInfo toBeanIf(); // a Bean instance If need. else return this

	public long getRoleid(); // 角色Id
	public int getRedpackmoney(); // 红包金额
	public long getReceivetime(); // 领取时间

	public void setRoleid(long _v_); // 角色Id
	public void setRedpackmoney(int _v_); // 红包金额
	public void setReceivetime(long _v_); // 领取时间
}
