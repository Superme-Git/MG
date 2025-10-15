
package xbean;

public interface RoleApplyClanList extends mkdb.Bean {
	public RoleApplyClanList copy(); // deep clone
	public RoleApplyClanList toData(); // a Data instance
	public RoleApplyClanList toBean(); // a Bean instance
	public RoleApplyClanList toDataIf(); // a Data instance If need. else return this
	public RoleApplyClanList toBeanIf(); // a Bean instance If need. else return this

	public long getOnekeylasttime(); // 上次一键申请时间
	public java.util.Map<Long, xbean.RoleApplyClan> getOnekeyapplymap(); // 
	public java.util.Map<Long, xbean.RoleApplyClan> getOnekeyapplymapAsData(); // 
	public java.util.Map<Long, xbean.RoleApplyClan> getApplymap(); // 
	public java.util.Map<Long, xbean.RoleApplyClan> getApplymapAsData(); // 

	public void setOnekeylasttime(long _v_); // 上次一键申请时间
}
