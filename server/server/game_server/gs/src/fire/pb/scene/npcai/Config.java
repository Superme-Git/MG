package fire.pb.scene.npcai;

import fire.pb.scene.movable.NPC;


public abstract class Config 
{
	abstract public Action generateAction(NPC npc);
}

