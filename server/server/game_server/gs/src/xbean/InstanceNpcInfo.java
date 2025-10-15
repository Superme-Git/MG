
package xbean;

public interface InstanceNpcInfo extends mkdb.Bean {
	public InstanceNpcInfo copy(); // deep clone
	public InstanceNpcInfo toData(); // a Data instance
	public InstanceNpcInfo toBean(); // a Bean instance
	public InstanceNpcInfo toDataIf(); // a Data instance If need. else return this
	public InstanceNpcInfo toBeanIf(); // a Bean instance If need. else return this

	public long getNpckey(); // 目的npc的key
	public int getNpcid(); // 目的npc的id
	public int getMapid(); // 目的npc的mapid
	public int getPosx(); // 目的坐标
	public int getPosy(); // 目的坐标

	public void setNpckey(long _v_); // 目的npc的key
	public void setNpcid(int _v_); // 目的npc的id
	public void setMapid(int _v_); // 目的npc的mapid
	public void setPosx(int _v_); // 目的坐标
	public void setPosy(int _v_); // 目的坐标
}
