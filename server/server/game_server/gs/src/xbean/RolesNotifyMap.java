
package xbean;

public interface RolesNotifyMap extends mkdb.Bean {
	public RolesNotifyMap copy(); // deep clone
	public RolesNotifyMap toData(); // a Data instance
	public RolesNotifyMap toBean(); // a Bean instance
	public RolesNotifyMap toDataIf(); // a Data instance If need. else return this
	public RolesNotifyMap toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.RoleFutureNotifyMap> getRolesfuturemap(); // 

}
