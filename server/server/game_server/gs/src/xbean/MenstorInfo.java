
package xbean;

public interface MenstorInfo extends mkdb.Bean {
	public MenstorInfo copy(); // deep clone
	public MenstorInfo toData(); // a Data instance
	public MenstorInfo toBean(); // a Bean instance
	public MenstorInfo toDataIf(); // a Data instance If need. else return this
	public MenstorInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.CurrApprent> getCurrapprentices(); // 当前的所有的徒弟注意要有顺序性
	public java.util.List<xbean.CurrApprent> getCurrapprenticesAsData(); // 当前的所有的徒弟注意要有顺序性
	public java.util.List<xbean.HasApprent> getApprentices(); // 当前已经出徒的徒弟
	public java.util.List<xbean.HasApprent> getApprenticesAsData(); // 当前已经出徒的徒弟
	public long getUpdatetime(); // 每日上线的时候 只更新一次

	public void setUpdatetime(long _v_); // 每日上线的时候 只更新一次
}
