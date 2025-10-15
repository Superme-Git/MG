
package xbean;

public interface EMonthCard extends mkdb.Bean {
	public EMonthCard copy(); // deep clone
	public EMonthCard toData(); // a Data instance
	public EMonthCard toBean(); // a Bean instance
	public EMonthCard toDataIf(); // a Data instance If need. else return this
	public EMonthCard toBeanIf(); // a Bean instance If need. else return this

	public long getEndtime(); // 结束时间戳 by changhao
	public long getGrabtime(); // 领取时间戳 by changhao
	public long getFirstprompttime(); // 少于3天提示 by changhao

	public void setEndtime(long _v_); // 结束时间戳 by changhao
	public void setGrabtime(long _v_); // 领取时间戳 by changhao
	public void setFirstprompttime(long _v_); // 少于3天提示 by changhao
}
