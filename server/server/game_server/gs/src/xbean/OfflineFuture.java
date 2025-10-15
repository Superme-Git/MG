
package xbean;

public interface OfflineFuture extends mkdb.Bean {
	public OfflineFuture copy(); // deep clone
	public OfflineFuture toData(); // a Data instance
	public OfflineFuture toBean(); // a Bean instance
	public OfflineFuture toDataIf(); // a Data instance If need. else return this
	public OfflineFuture toBeanIf(); // a Bean instance If need. else return this

	public java.util.concurrent.ScheduledFuture<?> getTimefuture(); // 人物下线后的计时任务

	public void setTimefuture(java.util.concurrent.ScheduledFuture<?> _v_); // 人物下线后的计时任务
}
