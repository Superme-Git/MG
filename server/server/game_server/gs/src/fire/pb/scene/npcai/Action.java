package fire.pb.scene.npcai;

import fire.pb.scene.movable.NPC;

public abstract class Action 
{
	Action(final NPC n)
	{
		npc = n;
	}
	
	final NPC getNPC()
	{
		return npc;
	}
	
	abstract boolean checkEnd();
	abstract void start();
	abstract void exec(long curTick);
	
	private final NPC npc;
}

