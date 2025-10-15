
package xbean;

public interface ShowingMission extends mkdb.Bean {
	public ShowingMission copy(); // deep clone
	public ShowingMission toData(); // a Data instance
	public ShowingMission toBean(); // a Bean instance
	public ShowingMission toDataIf(); // a Data instance If need. else return this
	public ShowingMission toBeanIf(); // a Bean instance If need. else return this

	public int getMissionid(); // 
	public boolean getIsleader(); // 

	public void setMissionid(int _v_); // 
	public void setIsleader(boolean _v_); // 
}
