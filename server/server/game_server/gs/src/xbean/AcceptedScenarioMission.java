
package xbean;

public interface AcceptedScenarioMission extends mkdb.Bean {
	public AcceptedScenarioMission copy(); // deep clone
	public AcceptedScenarioMission toData(); // a Data instance
	public AcceptedScenarioMission toBean(); // a Bean instance
	public AcceptedScenarioMission toDataIf(); // a Data instance If need. else return this
	public AcceptedScenarioMission toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, xbean.Mission> getAccepted(); // 支线任务
	public java.util.Map<Integer, xbean.Mission> getAcceptedAsData(); // 支线任务

}
