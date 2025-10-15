
package xbean;

public interface BingFengRole extends mkdb.Bean {
	public BingFengRole copy(); // deep clone
	public BingFengRole toData(); // a Data instance
	public BingFengRole toBean(); // a Bean instance
	public BingFengRole toDataIf(); // a Data instance If need. else return this
	public BingFengRole toBeanIf(); // a Bean instance If need. else return this

	public int getInstzoneid(); // 
	public int getTimes(); // 
	public int getChargetimes(); // 
	public java.util.Map<Integer, xbean.RoleBFInfo> getInfos(); // 每一个冰封王座的状态
	public java.util.Map<Integer, xbean.RoleBFInfo> getInfosAsData(); // 每一个冰封王座的状态

	public void setInstzoneid(int _v_); // 
	public void setTimes(int _v_); // 
	public void setChargetimes(int _v_); // 
}
