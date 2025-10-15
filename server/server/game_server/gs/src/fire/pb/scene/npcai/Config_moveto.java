

package fire.pb.scene.npcai;

import fire.pb.scene.movable.NPC;
import fire.pb.scene.sPos.GridPos;
import fire.pb.scene.sPos.Position;


public final class Config_moveto extends Config
{
	Config_moveto()
	{
	}
	
	Config_moveto(int x, int y, int speed)
	{
		this.x = x;
		this.y = y;
		this.speed = speed;
	}
	
	Position getDest()
	{
		return new GridPos(x, y).toPosition();
	}
	
	public String toString()
	{
		return "moveto " + getDest() + " by speed " + speed;
	}
	
	int getSpeed()
	{
		return speed;
	}
	
	@Override
	public Action generateAction(NPC npc)
	{
		return new MoToAction(npc, this);
	}
	
	private int x;
	private int y;
	private int speed;
}