
package xbean;

public interface FriendGiveNum extends mkdb.Bean {
	public FriendGiveNum copy(); // deep clone
	public FriendGiveNum toData(); // a Data instance
	public FriendGiveNum toBean(); // a Bean instance
	public FriendGiveNum toDataIf(); // a Data instance If need. else return this
	public FriendGiveNum toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Long, Integer> getGivenummap(); // key为角色id value为数量 *byte够了
	public java.util.Map<Long, Integer> getGivenummapAsData(); // key为角色id value为数量 *byte够了
	public long getResettime(); // 重置时间

	public void setResettime(long _v_); // 重置时间
}
