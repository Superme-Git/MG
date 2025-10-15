
package xbean;

public interface FriendBean extends mkdb.Bean {
	public FriendBean copy(); // deep clone
	public FriendBean toData(); // a Data instance
	public FriendBean toBean(); // a Bean instance
	public FriendBean toDataIf(); // a Data instance If need. else return this
	public FriendBean toBeanIf(); // a Bean instance If need. else return this

	public int getFriendlydegrees(); // 好友度

	public void setFriendlydegrees(int _v_); // 好友度
}
