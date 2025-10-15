
package xbean;

public interface WheelInfo extends mkdb.Bean {
	public WheelInfo copy(); // deep clone
	public WheelInfo toData(); // a Data instance
	public WheelInfo toBean(); // a Bean instance
	public WheelInfo toDataIf(); // a Data instance If need. else return this
	public WheelInfo toBeanIf(); // a Bean instance If need. else return this

	public long getTime(); // 答题的时间
	public int getNpcbaseid(); // 大转盘可能有多个npc有服务,所以要有npc的id
	public int getServiceid(); // 一个npc可以有多个大转盘,所以要有服务的id
	public boolean getFinish(); // 是否完成了这个大转盘
	public int getBasemoney(); // 金钱的基数(在翻倍的时候用得上)
	public int getBasesmoney(); // 
	public int getBaseexp(); // 
	public int getMsgid(); // 策划要求每个大转盘给的闲话id都不一样
	public java.util.List<String> getMsgparas(); // 
	public java.util.List<String> getMsgparasAsData(); // 
	public int getIndex(); // 真正中奖的那个item的下标
	public java.util.List<xbean.WheelItem> getWheelitems(); // 一个大转盘所包含的格子
	public java.util.List<xbean.WheelItem> getWheelitemsAsData(); // 一个大转盘所包含的格子

	public void setTime(long _v_); // 答题的时间
	public void setNpcbaseid(int _v_); // 大转盘可能有多个npc有服务,所以要有npc的id
	public void setServiceid(int _v_); // 一个npc可以有多个大转盘,所以要有服务的id
	public void setFinish(boolean _v_); // 是否完成了这个大转盘
	public void setBasemoney(int _v_); // 金钱的基数(在翻倍的时候用得上)
	public void setBasesmoney(int _v_); // 
	public void setBaseexp(int _v_); // 
	public void setMsgid(int _v_); // 策划要求每个大转盘给的闲话id都不一样
	public void setIndex(int _v_); // 真正中奖的那个item的下标
}
