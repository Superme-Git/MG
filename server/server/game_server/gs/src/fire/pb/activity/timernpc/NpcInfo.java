package fire.pb.activity.timernpc;

public class NpcInfo {
	public int npcid;
	/**
	 * npckey 对应的战斗状态 0=空闲 1=战斗中 2=已经消失
	 */
	public int npcstate;
	/**
	 * 事件刷新表.xlsx id
	 */
	public int biaoid;
	
	public int battletimes;
	
	/**
	 * 胜利次数为0时清除npc
	 */
	public int battlewintime;
	
	/**
	 * 正在战斗的roleid
	 */
	public long roleid;
	
	public NpcInfo(int npcid, int npcstate, int group, int battletimes) {
		this.npcid = npcid;
		this.npcstate = npcstate;
		this.biaoid = group;
		this.battletimes = battletimes;
		this.battlewintime = battletimes;
	}

	public void setBattleRoleid(final long roleid) {
		this.roleid = roleid;
	}
}
