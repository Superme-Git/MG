
package xbean;

public interface PvP3Race extends mkdb.Bean {
	public PvP3Race copy(); // deep clone
	public PvP3Race toData(); // a Data instance
	public PvP3Race toBean(); // a Bean instance
	public PvP3Race toDataIf(); // a Data instance If need. else return this
	public PvP3Race toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.PvP3RaceRole> getAllroles(); // 赛场中的角色的积分排行
	public java.util.List<xbean.PvP3RaceRole> getAllrolesAsData(); // 赛场中的角色的积分排行
	public java.util.List<Long> getAllrolesid(); // 赛场中的角色ID
	public java.util.List<Long> getAllrolesidAsData(); // 赛场中的角色ID
	public java.util.List<xbean.PvP3QueueRole> getWaitingqueue(); // 等待序列
	public java.util.List<xbean.PvP3QueueRole> getWaitingqueueAsData(); // 等待序列

}
