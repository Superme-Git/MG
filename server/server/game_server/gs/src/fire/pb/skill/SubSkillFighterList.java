package fire.pb.skill;

import java.util.ArrayList;
import java.util.List;

public class SubSkillFighterList
{
	private List<Integer> nFighterIDs;
	
	public SubSkillFighterList()
	{
		nFighterIDs = new ArrayList<Integer>();
	}	
	
	public List<Integer> getFighterIDs()
	{
		return nFighterIDs;
	}
	public void addFighterID(int fighterid)
	{
		if(!nFighterIDs.contains(fighterid))
		{
			nFighterIDs.add(fighterid);
		}	
	}
	public void clearlist()
	{
		nFighterIDs.clear();
	}
	
}
