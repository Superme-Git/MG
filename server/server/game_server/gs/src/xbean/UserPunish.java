
package xbean;

public interface UserPunish extends mkdb.Bean {
	public UserPunish copy(); // deep clone
	public UserPunish toData(); // a Data instance
	public UserPunish toBean(); // a Bean instance
	public UserPunish toDataIf(); // a Data instance If need. else return this
	public UserPunish toBeanIf(); // a Bean instance If need. else return this

	public int getUserid(); // 用户id
	public java.util.List<xbean.PunishRecord> getRecords(); // 封禁记录
	public java.util.List<xbean.PunishRecord> getRecordsAsData(); // 封禁记录
	public long getReleasetime(); // 解封的时间,gacd用
	public int getWaiguatimes(); // 使用外挂的次数,gacd用
	public long getSendmsgtime(); // 解封后发消息给玩家的时间,如果没有发过消息,为0

	public void setUserid(int _v_); // 用户id
	public void setReleasetime(long _v_); // 解封的时间,gacd用
	public void setWaiguatimes(int _v_); // 使用外挂的次数,gacd用
	public void setSendmsgtime(long _v_); // 解封后发消息给玩家的时间,如果没有发过消息,为0
}
