
package xbean;

public interface FestivalData extends mkdb.Bean {
	public FestivalData copy(); // deep clone
	public FestivalData toData(); // a Data instance
	public FestivalData toBean(); // a Bean instance
	public FestivalData toDataIf(); // a Data instance If need. else return this
	public FestivalData toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Long> getRewardmap(); // 已经领取的节日奖励(key-奖励ID, value-领取时间)
	public java.util.Map<Integer, Long> getRewardmapAsData(); // 已经领取的节日奖励(key-奖励ID, value-领取时间)

}
