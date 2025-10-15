
package xbean;

public interface YbNum extends mkdb.Bean {
	public YbNum copy(); // deep clone
	public YbNum toData(); // a Data instance
	public YbNum toBean(); // a Bean instance
	public YbNum toDataIf(); // a Data instance If need. else return this
	public YbNum toBeanIf(); // a Bean instance If need. else return this

	public int getNum(); // 现金充值符石数
	public int getSysnum(); // 系统赠送符石数
	public int getNopresentnum(); // 没有包含赠送的实际充值总符石
	public java.util.List<Long> getSnlist(); // 玩家充值的序列号
	public java.util.List<Long> getSnlistAsData(); // 玩家充值的序列号
	public int getBindorsysnumtoday(); // 该玩家今天获得的符石数量(不包括充值的)
	public long getBindorsysnumtodaytime(); // 这个time跟下面time不一样,这个time只有绑定或系统符石增加的时候才会变
	public long getTime(); // 最近一次符石数量发生变化的时间
	public long getFushiall(); // 累计符石总数，包括所有产出途径的符石数

	public void setNum(int _v_); // 现金充值符石数
	public void setSysnum(int _v_); // 系统赠送符石数
	public void setNopresentnum(int _v_); // 没有包含赠送的实际充值总符石
	public void setBindorsysnumtoday(int _v_); // 该玩家今天获得的符石数量(不包括充值的)
	public void setBindorsysnumtodaytime(long _v_); // 这个time跟下面time不一样,这个time只有绑定或系统符石增加的时候才会变
	public void setTime(long _v_); // 最近一次符石数量发生变化的时间
	public void setFushiall(long _v_); // 累计符石总数，包括所有产出途径的符石数
}
