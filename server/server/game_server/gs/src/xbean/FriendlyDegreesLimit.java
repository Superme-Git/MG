
package xbean;

public interface FriendlyDegreesLimit extends mkdb.Bean {
	public FriendlyDegreesLimit copy(); // deep clone
	public FriendlyDegreesLimit toData(); // a Data instance
	public FriendlyDegreesLimit toBean(); // a Bean instance
	public FriendlyDegreesLimit toDataIf(); // a Data instance If need. else return this
	public FriendlyDegreesLimit toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, Integer> getTodayfriendlydegreesmap(); // key=roleid value=今日增加的好友度
	public java.util.Map<Long, Integer> getTodayfriendlydegreesmapAsData(); // key=roleid value=今日增加的好友度
	public long getResettime(); // 每日增加的好友度重置时间

	public void setResettime(long _v_); // 每日增加的好友度重置时间
}
