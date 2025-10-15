
package xbean;

public interface ClanWeekNum extends mkdb.Bean {
	public ClanWeekNum copy(); // deep clone
	public ClanWeekNum toData(); // a Data instance
	public ClanWeekNum toBean(); // a Bean instance
	public ClanWeekNum toDataIf(); // a Data instance If need. else return this
	public ClanWeekNum toBeanIf(); // a Bean instance If need. else return this

	public long getWeeknum(); // 公会周更新记录周数

	public void setWeeknum(long _v_); // 公会周更新记录周数
}
