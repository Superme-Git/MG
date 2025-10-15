
package xbean;

public interface crossMessageBean extends mkdb.Bean {
	public crossMessageBean copy(); // deep clone
	public crossMessageBean toData(); // a Data instance
	public crossMessageBean toBean(); // a Bean instance
	public crossMessageBean toDataIf(); // a Data instance If need. else return this
	public crossMessageBean toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<String> getParms(); // 消息参数
	public java.util.List<String> getParmsAsData(); // 消息参数
	public int getMessageid(); // 消息id

	public void setMessageid(int _v_); // 消息id
}
