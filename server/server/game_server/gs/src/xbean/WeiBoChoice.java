
package xbean;

public interface WeiBoChoice extends mkdb.Bean {
	public WeiBoChoice copy(); // deep clone
	public WeiBoChoice toData(); // a Data instance
	public WeiBoChoice toBean(); // a Bean instance
	public WeiBoChoice toDataIf(); // a Data instance If need. else return this
	public WeiBoChoice toBeanIf(); // a Bean instance If need. else return this

	public int getTimes(); // 次数
	public int getSetting(); // 设定

	public void setTimes(int _v_); // 次数
	public void setSetting(int _v_); // 设定
}
