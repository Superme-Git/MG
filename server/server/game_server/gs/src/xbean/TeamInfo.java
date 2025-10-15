
package xbean;

public interface TeamInfo extends mkdb.Bean {
	public TeamInfo copy(); // deep clone
	public TeamInfo toData(); // a Data instance
	public TeamInfo toBean(); // a Bean instance
	public TeamInfo toDataIf(); // a Data instance If need. else return this
	public TeamInfo toBeanIf(); // a Bean instance If need. else return this

	public long getTeamleaderid(); // 队长ID
	public long getCommanderroleid(); // 指挥ID
	public int getMinlevel(); // 最小级别
	public int getMaxlevel(); // 最大级别
	public int getFormation(); // 阵型id
	public int getFormationlevel(); // 光环等级
	public long getSwitchleaderid(); // 改变队长时，保存新队长id，用于返回时验证，不处于改变队长状态时为-1
	public long getSwitchleadertime(); // 记录改变队长操作的时间，判断超时用（30秒）
	public long getSuccessswitchtime(); // 记录上一次改变队长成功的时间，成功更换后2分钟内不能更换队长
	public java.util.List<xbean.TeamMember> getMembers(); // 
	public java.util.List<xbean.TeamMember> getMembersAsData(); // 
	public java.util.Map<Long, Long> getApplierids(); // 
	public java.util.Map<Long, Long> getApplieridsAsData(); // 
	public java.util.Map<Long, Long> getInvitingids(); // 被邀请者的IDs 和时间 map by changhao
	public java.util.Map<Long, Long> getInvitingidsAsData(); // 被邀请者的IDs 和时间 map by changhao
	public int getState(); // 队伍状态，参考协议中的fire.pb.team.TeamState
	public int getSmapid(); // 地图玩法ID，主要用于在3状态时保存玩法ID；当处于普通队伍状态时，smapId为0
	public java.util.Map<Long, Long> getHugs(); // 拥抱的队员们
	public java.util.Map<Long, Long> getHugsAsData(); // 拥抱的队员们
	public int getTargetid(); // 
	public long getOnekeytimestamp(); // 一键喊话时间戳
	public long getCreatetime(); // 创建队伍时的时间戳

	public void setTeamleaderid(long _v_); // 队长ID
	public void setCommanderroleid(long _v_); // 指挥ID
	public void setMinlevel(int _v_); // 最小级别
	public void setMaxlevel(int _v_); // 最大级别
	public void setFormation(int _v_); // 阵型id
	public void setFormationlevel(int _v_); // 光环等级
	public void setSwitchleaderid(long _v_); // 改变队长时，保存新队长id，用于返回时验证，不处于改变队长状态时为-1
	public void setSwitchleadertime(long _v_); // 记录改变队长操作的时间，判断超时用（30秒）
	public void setSuccessswitchtime(long _v_); // 记录上一次改变队长成功的时间，成功更换后2分钟内不能更换队长
	public void setState(int _v_); // 队伍状态，参考协议中的fire.pb.team.TeamState
	public void setSmapid(int _v_); // 地图玩法ID，主要用于在3状态时保存玩法ID；当处于普通队伍状态时，smapId为0
	public void setTargetid(int _v_); // 
	public void setOnekeytimestamp(long _v_); // 一键喊话时间戳
	public void setCreatetime(long _v_); // 创建队伍时的时间戳
}
