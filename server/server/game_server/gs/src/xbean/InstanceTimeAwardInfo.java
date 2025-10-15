
package xbean;

public interface InstanceTimeAwardInfo extends mkdb.Bean {
	public InstanceTimeAwardInfo copy(); // deep clone
	public InstanceTimeAwardInfo toData(); // a Data instance
	public InstanceTimeAwardInfo toBean(); // a Bean instance
	public InstanceTimeAwardInfo toDataIf(); // a Data instance If need. else return this
	public InstanceTimeAwardInfo toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getStepawardtimes(); // key stepId, value 该step给的奖励次数
	public java.util.Map<Integer, Integer> getStepawardtimesAsData(); // key stepId, value 该step给的奖励次数

}
