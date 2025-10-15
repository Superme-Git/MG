
package xbean;

public interface GongHuiBaoXiangInfo extends mkdb.Bean {
	public GongHuiBaoXiangInfo copy(); // deep clone
	public GongHuiBaoXiangInfo toData(); // a Data instance
	public GongHuiBaoXiangInfo toBean(); // a Bean instance
	public GongHuiBaoXiangInfo toDataIf(); // a Data instance If need. else return this
	public GongHuiBaoXiangInfo toBeanIf(); // a Bean instance If need. else return this

	public int getOpentimes(); // 拾取次数
	public long getLastopentime(); // 上次拾取时间

	public void setOpentimes(int _v_); // 拾取次数
	public void setLastopentime(long _v_); // 上次拾取时间
}
