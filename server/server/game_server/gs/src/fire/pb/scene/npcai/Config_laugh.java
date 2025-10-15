package fire.pb.scene.npcai;

import fire.pb.scene.movable.NPC;

/**
 * 这就是一个例子。没有实际作用
 */
public final class Config_laugh extends Config
{
	public String toString()
	{
		return "laugh " + words;
	}
	
	@Override
	public Action generateAction(NPC npc)
	{
		return null;
	}
	
	private String words;
}

