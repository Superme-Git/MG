
package xbean;

public interface TimerFuture extends mkdb.Bean {
	public TimerFuture copy(); // deep clone
	public TimerFuture toData(); // a Data instance
	public TimerFuture toBean(); // a Bean instance
	public TimerFuture toDataIf(); // a Data instance If need. else return this
	public TimerFuture toBeanIf(); // a Bean instance If need. else return this

	public java.util.concurrent.ScheduledFuture<?> getFuture(); // 

	public void setFuture(java.util.concurrent.ScheduledFuture<?> _v_); // 
}
