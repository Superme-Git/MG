
package xbean;

public interface BingFengInfo extends mkdb.Bean {
	public BingFengInfo copy(); // deep clone
	public BingFengInfo toData(); // a Data instance
	public BingFengInfo toBean(); // a Bean instance
	public BingFengInfo toDataIf(); // a Data instance If need. else return this
	public BingFengInfo toBeanIf(); // a Bean instance If need. else return this

	public int getInstzoneid(); // 
	public java.util.Map<Integer, Integer> getStagetime(); // 记录每个关卡最快通关的时间
	public java.util.Map<Integer, Integer> getStagetimeAsData(); // 记录每个关卡最快通关的时间
	public java.util.Map<Integer, Integer> getStageround(); // 记录每个关卡最快通关的回合数
	public java.util.Map<Integer, Integer> getStageroundAsData(); // 记录每个关卡最快通关的回合数
	public java.util.Map<Integer, Long> getStagebest(); // 记录每个关卡最快通关的角色的id
	public java.util.Map<Integer, Long> getStagebestAsData(); // 记录每个关卡最快通关的角色的id
	public java.util.Map<Integer, xbean.SchoolStageTime> getSchoolstage(); // key 职业id
	public java.util.Map<Integer, xbean.SchoolStageTime> getSchoolstageAsData(); // key 职业id

	public void setInstzoneid(int _v_); // 
}
