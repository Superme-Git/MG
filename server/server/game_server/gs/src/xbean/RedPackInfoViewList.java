
package xbean;

public interface RedPackInfoViewList extends mkdb.Bean {
	public RedPackInfoViewList copy(); // deep clone
	public RedPackInfoViewList toData(); // a Data instance
	public RedPackInfoViewList toBean(); // a Bean instance
	public RedPackInfoViewList toDataIf(); // a Data instance If need. else return this
	public RedPackInfoViewList toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<String, xbean.RedPackBaseInfo> getRedpackinfoviewlist(); // 红包记录 key=redpackid
	public java.util.Map<String, xbean.RedPackBaseInfo> getRedpackinfoviewlistAsData(); // 红包记录 key=redpackid

}
