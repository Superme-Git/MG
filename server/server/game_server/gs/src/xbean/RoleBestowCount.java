
package xbean;

public interface RoleBestowCount extends mkdb.Bean {
	public RoleBestowCount copy(); // deep clone
	public RoleBestowCount toData(); // a Data instance
	public RoleBestowCount toBean(); // a Bean instance
	public RoleBestowCount toDataIf(); // a Data instance If need. else return this
	public RoleBestowCount toBeanIf(); // a Bean instance If need. else return this

	public int getOpentimes(); // 拾取次数
	public long getLastopentime(); // 上次拾取时间

	public void setOpentimes(int _v_); // 拾取次数
	public void setLastopentime(long _v_); // 上次拾取时间
}
