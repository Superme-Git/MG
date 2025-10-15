
package xbean;

public interface OfflineMsgList extends mkdb.Bean {
	public OfflineMsgList copy(); // deep clone
	public OfflineMsgList toData(); // a Data instance
	public OfflineMsgList toBean(); // a Bean instance
	public OfflineMsgList toDataIf(); // a Data instance If need. else return this
	public OfflineMsgList toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.OfflineMsgProtocol> getProtocollist(); // 
	public java.util.List<xbean.OfflineMsgProtocol> getProtocollistAsData(); // 

}
