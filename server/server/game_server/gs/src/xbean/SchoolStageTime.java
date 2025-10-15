
package xbean;

public interface SchoolStageTime extends mkdb.Bean {
	public SchoolStageTime copy(); // deep clone
	public SchoolStageTime toData(); // a Data instance
	public SchoolStageTime toBean(); // a Bean instance
	public SchoolStageTime toDataIf(); // a Data instance If need. else return this
	public SchoolStageTime toBeanIf(); // a Bean instance If need. else return this

	public java.util.Map<Integer, Integer> getStagetime(); // 按职业区分的每关时间
	public java.util.Map<Integer, Integer> getStagetimeAsData(); // 按职业区分的每关时间
	public java.util.Map<Integer, Integer> getStageround(); // 记录每个关卡最快通关的回合数
	public java.util.Map<Integer, Integer> getStageroundAsData(); // 记录每个关卡最快通关的回合数
	public java.util.Map<Integer, Long> getStagebest(); // 记录每个关卡最快通关的角色的id
	public java.util.Map<Integer, Long> getStagebestAsData(); // 记录每个关卡最快通关的角色的id

}
