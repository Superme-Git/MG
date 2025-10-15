
package xbean;

public interface NpcSaleBusiness extends mkdb.Bean {
	public NpcSaleBusiness copy(); // deep clone
	public NpcSaleBusiness toData(); // a Data instance
	public NpcSaleBusiness toBean(); // a Bean instance
	public NpcSaleBusiness toDataIf(); // a Data instance If need. else return this
	public NpcSaleBusiness toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.GoodsBusiness> getGoodsmap(); // key=goodsid
	public java.util.Map<Integer, xbean.GoodsBusiness> getGoodsmapAsData(); // key=goodsid
	public short getIsactive(); // 是否统计,默认为0不启动,1是启动
	public long getTime(); // 最后统计时间

	public void setIsactive(short _v_); // 是否统计,默认为0不启动,1是启动
	public void setTime(long _v_); // 最后统计时间
}
