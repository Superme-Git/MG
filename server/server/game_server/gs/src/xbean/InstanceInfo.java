
package xbean;

public interface InstanceInfo extends mkdb.Bean {
	public InstanceInfo copy(); // deep clone
	public InstanceInfo toData(); // a Data instance
	public InstanceInfo toBean(); // a Bean instance
	public InstanceInfo toDataIf(); // a Data instance If need. else return this
	public InstanceInfo toBeanIf(); // a Bean instance If need. else return this

	public long getOwnerid(); // 副本的组织者,队长id
	public java.util.Set<Long> getRoleids(); // 一起接任务的人的id,包括自己,如果有人放弃了任务,则删除他的id
	public java.util.Set<Long> getRoleidsAsData(); // 一起接任务的人的id,包括自己,如果有人放弃了任务,则删除他的id
	public java.util.List<xbean.InstanceNpcInfo> getNpcs(); // 召出来的npc列表,如果服务器重启,npc都要被重新召出来
	public java.util.List<xbean.InstanceNpcInfo> getNpcsAsData(); // 召出来的npc列表,如果服务器重启,npc都要被重新召出来
	public java.util.List<xbean.InstanceNpcInfo> getAsistnpcs(); // 召出来的辅助npc列表
	public java.util.List<xbean.InstanceNpcInfo> getAsistnpcsAsData(); // 召出来的辅助npc列表
	public int getCopyflag(); // 副本类型  1为单人副本   2为组队副本
	public int getInstanceid(); // 副本id 跟 任务配置.xlsx 表里的 副本ID 对应
	public int getCurrentstep(); // 当前做到哪一阶段
	public int getCurrentstate(); // 当前阶段的状态
	public int getCurrenttaskid(); // 当前做到哪一步了
	public int getServiceid(); // 当前服务id
	public int getCurrentmapid(); // 当前在哪个地图
	public long getAccepttime(); // 接任务时间
	public int getFinalcounts(); // 已经完成的次数
	public java.util.Map<Integer, Integer> getSubsaves(); // 子进度状态，value是子进度计数
	public java.util.Map<Integer, Integer> getSubsavesAsData(); // 子进度状态，value是子进度计数

	public void setOwnerid(long _v_); // 副本的组织者,队长id
	public void setCopyflag(int _v_); // 副本类型  1为单人副本   2为组队副本
	public void setInstanceid(int _v_); // 副本id 跟 任务配置.xlsx 表里的 副本ID 对应
	public void setCurrentstep(int _v_); // 当前做到哪一阶段
	public void setCurrentstate(int _v_); // 当前阶段的状态
	public void setCurrenttaskid(int _v_); // 当前做到哪一步了
	public void setServiceid(int _v_); // 当前服务id
	public void setCurrentmapid(int _v_); // 当前在哪个地图
	public void setAccepttime(long _v_); // 接任务时间
	public void setFinalcounts(int _v_); // 已经完成的次数
}
