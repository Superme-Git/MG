
package xbean;

public interface OnlinerefreshFuture extends mkdb.Bean {
	public OnlinerefreshFuture copy(); // deep clone
	public OnlinerefreshFuture toData(); // a Data instance
	public OnlinerefreshFuture toBean(); // a Bean instance
	public OnlinerefreshFuture toDataIf(); // a Data instance If need. else return this
	public OnlinerefreshFuture toBeanIf(); // a Bean instance If need. else return this

	public java.util.concurrent.ScheduledFuture<?> getFurvec(); // 

	public void setFurvec(java.util.concurrent.ScheduledFuture<?> _v_); // 
}
