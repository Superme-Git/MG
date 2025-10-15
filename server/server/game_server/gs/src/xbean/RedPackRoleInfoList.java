
package xbean;

public interface RedPackRoleInfoList extends mkdb.Bean {
	public RedPackRoleInfoList copy(); // deep clone
	public RedPackRoleInfoList toData(); // a Data instance
	public RedPackRoleInfoList toBean(); // a Bean instance
	public RedPackRoleInfoList toDataIf(); // a Data instance If need. else return this
	public RedPackRoleInfoList toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<String, xbean.RedPackInfo> getRedpackinfolist(); // 红包记录 key=redpackid
	public java.util.Map<String, xbean.RedPackInfo> getRedpackinfolistAsData(); // 红包记录 key=redpackid

}
