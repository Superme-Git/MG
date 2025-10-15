
package xbean;

public interface RuneInfo extends mkdb.Bean {
	public RuneInfo copy(); // deep clone
	public RuneInfo toData(); // a Data instance
	public RuneInfo toBean(); // a Bean instance
	public RuneInfo toDataIf(); // a Data instance If need. else return this
	public RuneInfo toBeanIf(); // a Bean instance If need. else return this

	public int getDayrequestnum(); // 请求次数
	public int getAllgivenum(); // 捐符数
	public int getAllacceptnum(); // 收符数
	public int getItemlevel(); // 物品等级

	public void setDayrequestnum(int _v_); // 请求次数
	public void setAllgivenum(int _v_); // 捐符数
	public void setAllacceptnum(int _v_); // 收符数
	public void setItemlevel(int _v_); // 物品等级
}
