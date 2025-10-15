
package xbean;

public interface LiveDieWeekNum extends mkdb.Bean {
	public LiveDieWeekNum copy(); // deep clone
	public LiveDieWeekNum toData(); // a Data instance
	public LiveDieWeekNum toBean(); // a Bean instance
	public LiveDieWeekNum toDataIf(); // a Data instance If need. else return this
	public LiveDieWeekNum toBeanIf(); // a Bean instance If need. else return this

	public long getWeeknum(); // 生死战清除录像记录

	public void setWeeknum(long _v_); // 生死战清除录像记录
}
