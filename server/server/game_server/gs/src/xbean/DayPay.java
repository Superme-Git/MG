
package xbean;

public interface DayPay extends mkdb.Bean {
	public DayPay copy(); // deep clone
	public DayPay toData(); // a Data instance
	public DayPay toBean(); // a Bean instance
	public DayPay toDataIf(); // a Data instance If need. else return this
	public DayPay toBeanIf(); // a Bean instance If need. else return this

	public int getUserid(); // 
	public long getRoleid(); // 
	public long getExpiretime(); // 结束时间 by changhao
	public int getFirstprompt(); // 还没提示过就是0 by changhao

	public void setUserid(int _v_); // 
	public void setRoleid(long _v_); // 
	public void setExpiretime(long _v_); // 结束时间 by changhao
	public void setFirstprompt(int _v_); // 还没提示过就是0 by changhao
}
