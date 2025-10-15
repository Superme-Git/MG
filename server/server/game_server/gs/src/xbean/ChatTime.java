
package xbean;

public interface ChatTime extends mkdb.Bean {
	public ChatTime copy(); // deep clone
	public ChatTime toData(); // a Data instance
	public ChatTime toBean(); // a Bean instance
	public ChatTime toDataIf(); // a Data instance If need. else return this
	public ChatTime toBeanIf(); // a Bean instance If need. else return this

	public long getLastworldchattime(); // 上次世界聊天时间
	public long getLastcurrentchattime(); // 上次当前聊天时间
	public long getLastprofessionchattime(); // 上次职业聊天时间
	public long getLastclanchattime(); // 上次公会聊天时间
	public long getLastteamapplychattime(); // 上次组队申请喊话时间

	public void setLastworldchattime(long _v_); // 上次世界聊天时间
	public void setLastcurrentchattime(long _v_); // 上次当前聊天时间
	public void setLastprofessionchattime(long _v_); // 上次职业聊天时间
	public void setLastclanchattime(long _v_); // 上次公会聊天时间
	public void setLastteamapplychattime(long _v_); // 上次组队申请喊话时间
}
