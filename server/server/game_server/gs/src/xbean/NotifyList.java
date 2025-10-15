
package xbean;

public interface NotifyList extends mkdb.Bean {
	public NotifyList copy(); // deep clone
	public NotifyList toData(); // a Data instance
	public NotifyList toBean(); // a Bean instance
	public NotifyList toDataIf(); // a Data instance If need. else return this
	public NotifyList toBeanIf(); // a Bean instance If need. else return this

	public int getNotifytypeid(); // 
	public java.util.List<Integer> getBuffids(); // 
	public java.util.List<Integer> getBuffidsAsData(); // 

	public void setNotifytypeid(int _v_); // 
}
