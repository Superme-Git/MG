
package xbean;

public interface eventNpcInfoCol extends mkdb.Bean {
	public eventNpcInfoCol copy(); // deep clone
	public eventNpcInfoCol toData(); // a Data instance
	public eventNpcInfoCol toBean(); // a Bean instance
	public eventNpcInfoCol toDataIf(); // a Data instance If need. else return this
	public eventNpcInfoCol toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.eventNpcInfo> getNpcinfo(); // //npckey 对应的Npc状态信息
	public java.util.Map<Long, xbean.eventNpcInfo> getNpcinfoAsData(); // //npckey 对应的Npc状态信息

}
