
package xbean;

public interface FriendGroups extends mkdb.Bean {
	public FriendGroups copy(); // deep clone
	public FriendGroups toData(); // a Data instance
	public FriendGroups toBean(); // a Bean instance
	public FriendGroups toDataIf(); // a Data instance If need. else return this
	public FriendGroups toBeanIf(); // a Bean instance If need. else return this

	public int getRefusestrangermsg(); // 0=收听陌生人消息  1= 拒绝陌生人消息
	public java.util.Map<Long, xbean.FriendBean> getFriendmap(); // 好友列表 key=roleid
	public java.util.Map<Long, xbean.FriendBean> getFriendmapAsData(); // 好友列表 key=roleid
	public xbean.FriendlyDegreesLimit getFriendlydegreeslimit(); // 好友度每日限制
	public String getSign(); // 签名
	public com.locojoy.base.Octets getSignOctets(); // 签名
	public java.util.List<xbean.OfflineMsg> getOfflinemsglist(); // 角色收到的离线消息上线发送以后立刻清除
	public java.util.List<xbean.OfflineMsg> getOfflinemsglistAsData(); // 角色收到的离线消息上线发送以后立刻清除

	public void setRefusestrangermsg(int _v_); // 0=收听陌生人消息  1= 拒绝陌生人消息
	public void setSign(String _v_); // 签名
	public void setSignOctets(com.locojoy.base.Octets _v_); // 签名
}
