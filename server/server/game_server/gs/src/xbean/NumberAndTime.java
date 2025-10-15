
package xbean;

public interface NumberAndTime extends mkdb.Bean {
	public NumberAndTime copy(); // deep clone
	public NumberAndTime toData(); // a Data instance
	public NumberAndTime toBean(); // a Bean instance
	public NumberAndTime toDataIf(); // a Data instance If need. else return this
	public NumberAndTime toBeanIf(); // a Bean instance If need. else return this

	public int getNumber(); // 购买或售卖次数
	public long getTime(); // 最后购买或售卖时间

	public void setNumber(int _v_); // 购买或售卖次数
	public void setTime(long _v_); // 最后购买或售卖时间
}
