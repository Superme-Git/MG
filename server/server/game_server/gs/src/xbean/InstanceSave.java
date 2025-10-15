
package xbean;

public interface InstanceSave extends mkdb.Bean {
	public InstanceSave copy(); // deep clone
	public InstanceSave toData(); // a Data instance
	public InstanceSave toBean(); // a Bean instance
	public InstanceSave toDataIf(); // a Data instance If need. else return this
	public InstanceSave toBeanIf(); // a Bean instance If need. else return this

	public int getSaveid(); // 
	public int getState(); // 
	public java.util.Map<Integer, Integer> getSubsaves(); // 子进度状态，value是子进度计数
	public java.util.Map<Integer, Integer> getSubsavesAsData(); // 子进度状态，value是子进度计数
	public java.util.Map<Long, xbean.InstanceNpcSave> getNpcsaves(); // npc的状态存储, key 是npckey
	public java.util.Map<Long, xbean.InstanceNpcSave> getNpcsavesAsData(); // npc的状态存储, key 是npckey

	public void setSaveid(int _v_); // 
	public void setState(int _v_); // 
}
