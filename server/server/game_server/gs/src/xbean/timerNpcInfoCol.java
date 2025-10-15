
package xbean;

public interface timerNpcInfoCol extends mkdb.Bean {
	public timerNpcInfoCol copy(); // deep clone
	public timerNpcInfoCol toData(); // a Data instance
	public timerNpcInfoCol toBean(); // a Bean instance
	public timerNpcInfoCol toDataIf(); // a Data instance If need. else return this
	public timerNpcInfoCol toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, xbean.timerNpcInfo> getNpcinfo(); // //npckey 对应的Npc状态信息
	public java.util.Map<Long, xbean.timerNpcInfo> getNpcinfoAsData(); // //npckey 对应的Npc状态信息

}
