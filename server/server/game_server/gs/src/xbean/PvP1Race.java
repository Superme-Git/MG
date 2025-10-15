
package xbean;

public interface PvP1Race extends mkdb.Bean {
	public PvP1Race copy(); // deep clone
	public PvP1Race toData(); // a Data instance
	public PvP1Race toBean(); // a Bean instance
	public PvP1Race toDataIf(); // a Data instance If need. else return this
	public PvP1Race toBeanIf(); // a Bean instance If need. else return this

	public java.util.List<xbean.PvP1RaceRole> getAllroles(); // 赛场中的角色的积分排行
	public java.util.List<xbean.PvP1RaceRole> getAllrolesAsData(); // 赛场中的角色的积分排行
	public java.util.List<Long> getAllrolesid(); // 赛场中的角色ID
	public java.util.List<Long> getAllrolesidAsData(); // 赛场中的角色ID
	public java.util.List<xbean.PvP1QueueRole> getWaitingqueue(); // 等待序列
	public java.util.List<xbean.PvP1QueueRole> getWaitingqueueAsData(); // 等待序列

}
