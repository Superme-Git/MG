
package xbean;

public interface RoleRankNotifyTimeInfo extends mkdb.Bean {
	public RoleRankNotifyTimeInfo copy(); // deep clone
	public RoleRankNotifyTimeInfo toData(); // a Data instance
	public RoleRankNotifyTimeInfo toBean(); // a Bean instance
	public RoleRankNotifyTimeInfo toDataIf(); // a Data instance If need. else return this
	public RoleRankNotifyTimeInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Long> getLasttime(); // 上次各个排行榜变化通知时间
	public java.util.Map<Integer, Long> getLasttimeAsData(); // 上次各个排行榜变化通知时间

}
