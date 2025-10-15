
package xbean;

public interface CommitedMissions extends mkdb.Bean {
	public CommitedMissions copy(); // deep clone
	public CommitedMissions toData(); // a Data instance
	public CommitedMissions toBean(); // a Bean instance
	public CommitedMissions toDataIf(); // a Data instance If need. else return this
	public CommitedMissions toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<Integer> getCommitted(); // 
	public java.util.List<Integer> getCommittedAsData(); // 

}
