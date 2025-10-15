
package xbean;

public interface NpcFollowInfo extends mkdb.Bean {
	public NpcFollowInfo copy(); // deep clone
	public NpcFollowInfo toData(); // a Data instance
	public NpcFollowInfo toBean(); // a Bean instance
	public NpcFollowInfo toDataIf(); // a Data instance If need. else return this
	public NpcFollowInfo toBeanIf(); // a Bean instance If need. else return this

	public int getNpcid(); // npc跟随的id
	public int getQuestid(); // 
	public long getValiddate(); // npc跟随的时限

	public void setNpcid(int _v_); // npc跟随的id
	public void setQuestid(int _v_); // 
	public void setValiddate(long _v_); // npc跟随的时限
}
