
package xbean;

public interface RoleApplyClan extends mkdb.Bean {
	public RoleApplyClan copy(); // deep clone
	public RoleApplyClan toData(); // a Data instance
	public RoleApplyClan toBean(); // a Bean instance
	public RoleApplyClan toDataIf(); // a Data instance If need. else return this
	public RoleApplyClan toBeanIf(); // a Bean instance If need. else return this

	public long getClankey(); // 公会key
	public int getState(); // 申请状态
	public long getApplytiem(); // 申请时间

	public void setClankey(long _v_); // 公会key
	public void setState(int _v_); // 申请状态
	public void setApplytiem(long _v_); // 申请时间
}
