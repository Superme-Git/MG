
package xbean;

public interface WeekIndex extends mkdb.Bean {
	public WeekIndex copy(); // deep clone
	public WeekIndex toData(); // a Data instance
	public WeekIndex toBean(); // a Bean instance
	public WeekIndex toDataIf(); // a Data instance If need. else return this
	public WeekIndex toBeanIf(); // a Bean instance If need. else return this

	public int getIndex(); // 伙伴免费轮换索引
	public int getInstanceindex(); // 副本轮换索引

	public void setIndex(int _v_); // 伙伴免费轮换索引
	public void setInstanceindex(int _v_); // 副本轮换索引
}
