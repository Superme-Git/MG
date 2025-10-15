
package xbean;

public interface GoldOrder extends mkdb.Bean {
	public GoldOrder copy(); // deep clone
	public GoldOrder toData(); // a Data instance
	public GoldOrder toBean(); // a Bean instance
	public GoldOrder toDataIf(); // a Data instance If need. else return this
	public GoldOrder toBeanIf(); // a Bean instance If need. else return this

	public long getPid(); // 订单编号
	public long getNumber(); // 金币数量, 整数
	public long getPrice(); // 出售价格, 整数, 单位人民币分
	public int getPublicity(); // 公示时间, 整数, 单位小时.如果不需要公示传0
	public int getLocktime(); // 锁定订单时间,单位秒.锁定时不能下架
	public int getState(); // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
	public long getTime(); // 订单创建时间,单位毫秒

	public void setPid(long _v_); // 订单编号
	public void setNumber(long _v_); // 金币数量, 整数
	public void setPrice(long _v_); // 出售价格, 整数, 单位人民币分
	public void setPublicity(int _v_); // 公示时间, 整数, 单位小时.如果不需要公示传0
	public void setLocktime(int _v_); // 锁定订单时间,单位秒.锁定时不能下架
	public void setState(int _v_); // 订单状态, 1在售, 2锁定, 3已售, 4待领取, 5领取完成
	public void setTime(long _v_); // 订单创建时间,单位毫秒
}
