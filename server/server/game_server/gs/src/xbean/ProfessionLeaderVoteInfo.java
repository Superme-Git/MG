
package xbean;

public interface ProfessionLeaderVoteInfo extends mkdb.Bean {
	public ProfessionLeaderVoteInfo copy(); // deep clone
	public ProfessionLeaderVoteInfo toData(); // a Data instance
	public ProfessionLeaderVoteInfo toBean(); // a Bean instance
	public ProfessionLeaderVoteInfo toDataIf(); // a Data instance If need. else return this
	public ProfessionLeaderVoteInfo toBeanIf(); // a Bean instance If need. else return this

	public long getVotetime(); // role最近一次投票的时间
	public long getChallengetime(); // role最近一次挑战成功的时间

	public void setVotetime(long _v_); // role最近一次投票的时间
	public void setChallengetime(long _v_); // role最近一次挑战成功的时间
}
