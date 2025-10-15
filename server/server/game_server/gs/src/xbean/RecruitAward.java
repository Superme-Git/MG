
package xbean;

public interface RecruitAward extends mkdb.Bean {
	public RecruitAward copy(); // deep clone
	public RecruitAward toData(); // a Data instance
	public RecruitAward toBean(); // a Bean instance
	public RecruitAward toDataIf(); // a Data instance If need. else return this
	public RecruitAward toBeanIf(); // a Bean instance If need. else return this

	public int getGetnum(); // 领取次数
	public long getGettime(); // 上次领取的时间

	public void setGetnum(int _v_); // 领取次数
	public void setGettime(long _v_); // 上次领取的时间
}
