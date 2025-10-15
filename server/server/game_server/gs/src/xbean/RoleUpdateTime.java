
package xbean;

public interface RoleUpdateTime extends mkdb.Bean {
	public RoleUpdateTime copy(); // deep clone
	public RoleUpdateTime toData(); // a Data instance
	public RoleUpdateTime toBean(); // a Bean instance
	public RoleUpdateTime toDataIf(); // a Data instance If need. else return this
	public RoleUpdateTime toBeanIf(); // a Bean instance If need. else return this

	public long getDateupdatetime(); // 每日更新时间
	public long getWeekupdatetime(); // 每周更新时间

	public void setDateupdatetime(long _v_); // 每日更新时间
	public void setWeekupdatetime(long _v_); // 每周更新时间
}
