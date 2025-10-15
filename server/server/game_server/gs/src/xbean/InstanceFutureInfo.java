
package xbean;

public interface InstanceFutureInfo extends mkdb.Bean {
	public InstanceFutureInfo copy(); // deep clone
	public InstanceFutureInfo toData(); // a Data instance
	public InstanceFutureInfo toBean(); // a Bean instance
	public InstanceFutureInfo toDataIf(); // a Data instance If need. else return this
	public InstanceFutureInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.concurrent.ScheduledFuture<?> getTimeoutfuture(); // 

	public void setTimeoutfuture(java.util.concurrent.ScheduledFuture<?> _v_); // 
}
