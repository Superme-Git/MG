
package xbean;

public interface TrackedMission extends mkdb.Bean {
	public TrackedMission copy(); // deep clone
	public TrackedMission toData(); // a Data instance
	public TrackedMission toBean(); // a Bean instance
	public TrackedMission toDataIf(); // a Data instance If need. else return this
	public TrackedMission toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.Track> getQuestids(); // 
	public java.util.Map<Integer, xbean.Track> getQuestidsAsData(); // 

}
