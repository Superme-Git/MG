
package xbean;

public interface SpecailquestFuture extends mkdb.Bean {
	public SpecailquestFuture copy(); // deep clone
	public SpecailquestFuture toData(); // a Data instance
	public SpecailquestFuture toBean(); // a Bean instance
	public SpecailquestFuture toDataIf(); // a Data instance If need. else return this
	public SpecailquestFuture toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, java.util.concurrent.ScheduledFuture<?>> getQuestfuture(); // 

}
