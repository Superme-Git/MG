
package xbean;

public interface subscription extends mkdb.Bean {
	public subscription copy(); // deep clone
	public subscription toData(); // a Data instance
	public subscription toBean(); // a Bean instance
	public subscription toDataIf(); // a Data instance If need. else return this
	public subscription toBeanIf(); // a Bean instance If need. else return this

	public long getExpiretime(); // 订阅过期时间
	public long getSubscribetime(); // 上次订阅开始时间

	public void setExpiretime(long _v_); // 订阅过期时间
	public void setSubscribetime(long _v_); // 上次订阅开始时间
}
