
package xbean;

public interface BroadcastMsg extends mkdb.Bean {
	public BroadcastMsg copy(); // deep clone
	public BroadcastMsg toData(); // a Data instance
	public BroadcastMsg toBean(); // a Bean instance
	public BroadcastMsg toDataIf(); // a Data instance If need. else return this
	public BroadcastMsg toBeanIf(); // a Bean instance If need. else return this

	public xbean.OfflineMsgProtocol getMsgprotocol(); // 
	public long getBroadtime(); // 
	public long getBroadendtime(); // 如果默认是0，则没有广播的结束时间

	public void setBroadtime(long _v_); // 
	public void setBroadendtime(long _v_); // 如果默认是0，则没有广播的结束时间
}
