
package xbean;

public interface RoleActiveTimerNpcInfo extends mkdb.Bean {
	public RoleActiveTimerNpcInfo copy(); // deep clone
	public RoleActiveTimerNpcInfo toData(); // a Data instance
	public RoleActiveTimerNpcInfo toBean(); // a Bean instance
	public RoleActiveTimerNpcInfo toDataIf(); // a Data instance If need. else return this
	public RoleActiveTimerNpcInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.RoleTimerNpcInfo> getActinfo(); // //key是活动id
	public java.util.Map<Integer, xbean.RoleTimerNpcInfo> getActinfoAsData(); // //key是活动id

}
