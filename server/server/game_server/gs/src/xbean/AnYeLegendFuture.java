
package xbean;

public interface AnYeLegendFuture extends mkdb.Bean {
	public AnYeLegendFuture copy(); // deep clone
	public AnYeLegendFuture toData(); // a Data instance
	public AnYeLegendFuture toBean(); // a Bean instance
	public AnYeLegendFuture toDataIf(); // a Data instance If need. else return this
	public AnYeLegendFuture toBeanIf(); // a Bean instance If need. else return this

	public java.util.concurrent.ScheduledFuture<?> getLegendfuture(); // 

	public void setLegendfuture(java.util.concurrent.ScheduledFuture<?> _v_); // 
}
