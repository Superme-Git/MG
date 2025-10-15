
package xbean;

public interface AppstoretidStatus extends mkdb.Bean {
	public AppstoretidStatus copy(); // deep clone
	public AppstoretidStatus toData(); // a Data instance
	public AppstoretidStatus toBean(); // a Bean instance
	public AppstoretidStatus toDataIf(); // a Data instance If need. else return this
	public AppstoretidStatus toBeanIf(); // a Bean instance If need. else return this

	public long getChargesn(); // 
	public int getStatus(); // 0处理中,1充值成功,2充值失败

	public void setChargesn(long _v_); // 
	public void setStatus(int _v_); // 0处理中,1充值成功,2充值失败
}
