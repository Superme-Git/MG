
package xbean;

public interface ProfessionLeaderCand extends mkdb.Bean {
	public ProfessionLeaderCand copy(); // deep clone
	public ProfessionLeaderCand toData(); // a Data instance
	public ProfessionLeaderCand toBean(); // a Bean instance
	public ProfessionLeaderCand toDataIf(); // a Data instance If need. else return this
	public ProfessionLeaderCand toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Long> getCandidatelist(); // 职业领袖候选人list,该表每周清除一次
	public java.util.List<Long> getCandidatelistAsData(); // 职业领袖候选人list,该表每周清除一次

}
