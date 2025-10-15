
package xbean;

public interface LogBean extends mkdb.Bean {
	public LogBean copy(); // deep clone
	public LogBean toData(); // a Data instance
	public LogBean toBean(); // a Bean instance
	public LogBean toDataIf(); // a Data instance If need. else return this
	public LogBean toBeanIf(); // a Bean instance If need. else return this

	public int getItemid(); // 
	public int getLevel(); // 
	public int getNumber(); // 
	public int getMoney(); // 

	public void setItemid(int _v_); // 
	public void setLevel(int _v_); // 
	public void setNumber(int _v_); // 
	public void setMoney(int _v_); // 
}
