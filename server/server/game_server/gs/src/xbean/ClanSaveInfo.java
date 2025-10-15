
package xbean;

public interface ClanSaveInfo extends mkdb.Bean {
	public ClanSaveInfo copy(); // deep clone
	public ClanSaveInfo toData(); // a Data instance
	public ClanSaveInfo toBean(); // a Bean instance
	public ClanSaveInfo toDataIf(); // a Data instance If need. else return this
	public ClanSaveInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Long> getSavestate(); // key为进度id; value为进度使用的时间
	public java.util.Map<Integer, Long> getSavestateAsData(); // key为进度id; value为进度使用的时间
	public long getRefreshtime(); // 刷新时间

	public void setRefreshtime(long _v_); // 刷新时间
}
