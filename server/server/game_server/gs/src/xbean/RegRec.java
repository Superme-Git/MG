
package xbean;

public interface RegRec extends mkdb.Bean {
	public RegRec copy(); // deep clone
	public RegRec toData(); // a Data instance
	public RegRec toBean(); // a Bean instance
	public RegRec toDataIf(); // a Data instance If need. else return this
	public RegRec toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.RegMonth> getMonthmap(); // 月Map
	public java.util.Map<Integer, xbean.RegMonth> getMonthmapAsData(); // 月Map
	public long getLastregtime(); // 上次签到时间

	public void setLastregtime(long _v_); // 上次签到时间
}
