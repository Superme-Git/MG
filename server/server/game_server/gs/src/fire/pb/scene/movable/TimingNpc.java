package fire.pb.scene.movable;

import fire.pb.move.NpcBasic;
import fire.pb.scene.Scene;
import fire.pb.scene.sPos.Position;

/**
 * 有定时的npc
 * @author nobody
 *
 */
public class TimingNpc extends NPC
{
	
	TimingNpc(long npckey, int npcid, String name, Scene scene, Position pos, int dir)
	{
		super(npckey, npcid, name, scene, pos, dir);
	}

	private long expiredtime;//到期时间

	public long getExpiredtime()
	{
		return expiredtime;
	}

	public void setExpiredtime(long expiredtime)
	{
		this.expiredtime = expiredtime;
	}
	
	@Override
	public NpcBasic getNpcBase()
	{
		NpcBasic npcbasic = super.getNpcBase();
		npcbasic.speed = (int)Math.max((this.expiredtime - System.currentTimeMillis()) , 0);
		return npcbasic;
	}
}
