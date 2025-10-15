
package xbean;

public interface ServiceSet extends mkdb.Bean {
	public ServiceSet copy(); // deep clone
	public ServiceSet toData(); // a Data instance
	public ServiceSet toBean(); // a Bean instance
	public ServiceSet toDataIf(); // a Data instance If need. else return this
	public ServiceSet toBeanIf(); // a Bean instance If need. else return this

	public int getBindtelagain(); // 重新绑定手机的次数
	public long getBindtelagaintime(); // 重新绑定手机的时间

	public void setBindtelagain(int _v_); // 重新绑定手机的次数
	public void setBindtelagaintime(long _v_); // 重新绑定手机的时间
}
