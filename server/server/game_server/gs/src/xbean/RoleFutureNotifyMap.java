
package xbean;

public interface RoleFutureNotifyMap extends mkdb.Bean {
	public RoleFutureNotifyMap copy(); // deep clone
	public RoleFutureNotifyMap toData(); // a Data instance
	public RoleFutureNotifyMap toBean(); // a Bean instance
	public RoleFutureNotifyMap toDataIf(); // a Data instance If need. else return this
	public RoleFutureNotifyMap toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, java.util.concurrent.ScheduledFuture<?>> getNotifymap(); // 

}
