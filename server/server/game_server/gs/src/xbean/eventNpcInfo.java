
package xbean;

public interface eventNpcInfo extends mkdb.Bean {
	public eventNpcInfo copy(); // deep clone
	public eventNpcInfo toData(); // a Data instance
	public eventNpcInfo toBean(); // a Bean instance
	public eventNpcInfo toDataIf(); // a Data instance If need. else return this
	public eventNpcInfo toBeanIf(); // a Bean instance If need. else return this

	public int getNpcid(); // //npcid 补充怪物时需要用到
	public int getNpcstatus(); // //npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
	public int getBattletime(); // //战斗中的人数
	public int getBattleendtime(); // //战斗结束并胜利的人数
	public long getRoleid(); // //正在战斗的roleid
	public long getCreatetime(); // //npc创建时间

	public void setNpcid(int _v_); // //npcid 补充怪物时需要用到
	public void setNpcstatus(int _v_); // //npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
	public void setBattletime(int _v_); // //战斗中的人数
	public void setBattleendtime(int _v_); // //战斗结束并胜利的人数
	public void setRoleid(long _v_); // //正在战斗的roleid
	public void setCreatetime(long _v_); // //npc创建时间
}
