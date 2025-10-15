
package xbean;

public interface NewPlayActiveDay extends mkdb.Bean {
	public NewPlayActiveDay copy(); // deep clone
	public NewPlayActiveDay toData(); // a Data instance
	public NewPlayActiveDay toBean(); // a Bean instance
	public NewPlayActiveDay toDataIf(); // a Data instance If need. else return this
	public NewPlayActiveDay toBeanIf(); // a Bean instance If need. else return this

	public long getTime(); // 活动记录时间
	public float getActiveness(); // 今天总的活跃度值
	public java.util.Map<Integer, Integer> getChests(); // key为宝箱id,value为是否打开0未开,1已打开
	public java.util.Map<Integer, Integer> getChestsAsData(); // key为宝箱id,value为是否打开0未开,1已打开
	public java.util.Map<Integer, xbean.PlayActiveData> getActives(); // key为活动id,一天的计次数据
	public java.util.Map<Integer, xbean.PlayActiveData> getActivesAsData(); // key为活动id,一天的计次数据
	public java.util.Map<Integer, Integer> getActivescount(); // key为活动id, 一天的次数
	public java.util.Map<Integer, Integer> getActivescountAsData(); // key为活动id, 一天的次数
	public long getYingfutime(); // 盈福经验记录时间
	public long getYingfuexp(); // 累积的盈福经验

	public void setTime(long _v_); // 活动记录时间
	public void setActiveness(float _v_); // 今天总的活跃度值
	public void setYingfutime(long _v_); // 盈福经验记录时间
	public void setYingfuexp(long _v_); // 累积的盈福经验
}
