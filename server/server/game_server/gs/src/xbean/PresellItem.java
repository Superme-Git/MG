
package xbean;

public interface PresellItem extends mkdb.Bean {
	public PresellItem copy(); // deep clone
	public PresellItem toData(); // a Data instance
	public PresellItem toBean(); // a Bean instance
	public PresellItem toDataIf(); // a Data instance If need. else return this
	public PresellItem toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Long> getRoleid(); // 参与预售玩家
	public java.util.List<Long> getRoleidAsData(); // 参与预售玩家
	public long getExpiretime(); // 到期时间

	public void setExpiretime(long _v_); // 到期时间
}
