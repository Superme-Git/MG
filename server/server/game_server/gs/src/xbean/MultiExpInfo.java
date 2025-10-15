
package xbean;

public interface MultiExpInfo extends mkdb.Bean {
	public MultiExpInfo copy(); // deep clone
	public MultiExpInfo toData(); // a Data instance
	public MultiExpInfo toBean(); // a Bean instance
	public MultiExpInfo toDataIf(); // a Data instance If need. else return this
	public MultiExpInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getMultiexptime(); // key 为buff类型，即为buffid， value为剩余的可领取的时间，单位为秒
	public java.util.Map<Integer, Integer> getMultiexptimeAsData(); // key 为buff类型，即为buffid， value为剩余的可领取的时间，单位为秒
	public int getRemainnum(); // 本周可以使用的个数
	public int getDupdannum(); // 本周可以使用的双倍丹的个数
	public long getLastbacktime(); // 上次返还多倍时间的时间
	public long getRefreshtime(); // 刷新的时间

	public void setRemainnum(int _v_); // 本周可以使用的个数
	public void setDupdannum(int _v_); // 本周可以使用的双倍丹的个数
	public void setLastbacktime(long _v_); // 上次返还多倍时间的时间
	public void setRefreshtime(long _v_); // 刷新的时间
}
