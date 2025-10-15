
package xbean;

public interface FestivalGift extends mkdb.Bean {
	public FestivalGift copy(); // deep clone
	public FestivalGift toData(); // a Data instance
	public FestivalGift toBean(); // a Bean instance
	public FestivalGift toDataIf(); // a Data instance If need. else return this
	public FestivalGift toBeanIf(); // a Bean instance If need. else return this

	public long getTime(); // 上次领取节日礼物的时间
	public long getOnlinetotal(); // 距上次领取礼物累计时间

	public void setTime(long _v_); // 上次领取节日礼物的时间
	public void setOnlinetotal(long _v_); // 距上次领取礼物累计时间
}
