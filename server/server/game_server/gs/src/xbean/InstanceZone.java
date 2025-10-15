
package xbean;

public interface InstanceZone extends mkdb.Bean {
	public InstanceZone copy(); // deep clone
	public InstanceZone toData(); // a Data instance
	public InstanceZone toBean(); // a Bean instance
	public InstanceZone toDataIf(); // a Data instance If need. else return this
	public InstanceZone toBeanIf(); // a Bean instance If need. else return this

	public final static int STATE_INITING = 0; // 副本区域正在初始化
	public final static int STATE_ACTIVE = 1; // 副本区域激活，可以正常进入
	public final static int STATE_CLOSING = 2; // 副本区域正在进行关闭处理，不能进入
	public final static int STATE_CLOSED = 3; // 副本区域已关闭

	public int getInstzoneid(); // 
	public long getOwnerid(); // 
	public int getZonestate(); // 副本区域当前的状态
	public long getExtid(); // 副本如果有额外存储的数据,-1为没有
	public java.util.List<Long> getRoleids(); // 当前在副本内的角色
	public java.util.List<Long> getRoleidsAsData(); // 当前在副本内的角色
	public java.util.Map<Integer, xbean.InstanceSave> getSaves(); // 进度状态
	public java.util.Map<Integer, xbean.InstanceSave> getSavesAsData(); // 进度状态
	public java.util.Map<Integer, xbean.InstanceNpc> getNpcs(); // 副本NPC状态，注意这里保存的是npcid相关的npc状态，不具体到实例
	public java.util.Map<Integer, xbean.InstanceNpc> getNpcsAsData(); // 副本NPC状态，注意这里保存的是npcid相关的npc状态，不具体到实例
	public java.util.Map<Integer, xbean.InstanceNpcServ> getServices(); // 副本NPC服务状态，注意这里保存的是serviceid相关的，不具体到哪个npc
	public java.util.Map<Integer, xbean.InstanceNpcServ> getServicesAsData(); // 副本NPC服务状态，注意这里保存的是serviceid相关的，不具体到哪个npc
	public java.util.Map<Integer, xbean.InstanceTimer> getTimers(); // 副本定时器，key=定时器id
	public java.util.Map<Integer, xbean.InstanceTimer> getTimersAsData(); // 副本定时器，key=定时器id
	public long getStarttime(); // 副本的开始时间(表里配置的时间,不是创建的时间)

	public void setInstzoneid(int _v_); // 
	public void setOwnerid(long _v_); // 
	public void setZonestate(int _v_); // 副本区域当前的状态
	public void setExtid(long _v_); // 副本如果有额外存储的数据,-1为没有
	public void setStarttime(long _v_); // 副本的开始时间(表里配置的时间,不是创建的时间)
}
