
package xbean;

public interface CommitedScenarioMission extends mkdb.Bean {
	public CommitedScenarioMission copy(); // deep clone
	public CommitedScenarioMission toData(); // a Data instance
	public CommitedScenarioMission toBean(); // a Bean instance
	public CommitedScenarioMission toDataIf(); // a Data instance If need. else return this
	public CommitedScenarioMission toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Integer> getCommitted(); // 
	public java.util.List<Integer> getCommittedAsData(); // 

}
