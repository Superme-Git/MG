package fire.pb.scene.movable;

import fire.pb.scene.sPos.GridPos;

public final class NpcAiConfig 
{
	NpcAiConfig(int id, int spd, java.util.List<GridPos> g)
	{
		aid = id;
		speed = spd;
		pathGrids = g;
	}
	
	public int getAiID()
	{
		return aid;
	}
	
	public int getAiSpeed()
	{
		return speed;
	}
	
	public java.util.List<GridPos> getPathdGrids()
	{
		return pathGrids;
	}
	
	@Override
	public String toString()
	{
		String val = "ai\t" + aid + "\t" + speed;
		for(GridPos gp : pathGrids)
		{
			val += gp + " ";
		}
		return val;
	}
	
	private final int aid;
	private final int speed;
	private final java.util.List<GridPos> pathGrids; 
}

