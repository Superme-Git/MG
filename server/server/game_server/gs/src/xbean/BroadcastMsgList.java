
package xbean;

public interface BroadcastMsgList extends mkdb.Bean {
	public BroadcastMsgList copy(); // deep clone
	public BroadcastMsgList toData(); // a Data instance
	public BroadcastMsgList toBean(); // a Bean instance
	public BroadcastMsgList toDataIf(); // a Data instance If need. else return this
	public BroadcastMsgList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.BroadcastMsg> getMsglist(); // 
	public java.util.List<xbean.BroadcastMsg> getMsglistAsData(); // 

}
