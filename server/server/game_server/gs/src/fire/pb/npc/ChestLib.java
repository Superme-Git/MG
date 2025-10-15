package fire.pb.npc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fire.pb.util.Misc;

public class ChestLib
{
	public final int libId;
	
	public final Map<Integer,SBaoxiang> chests = new HashMap<Integer, SBaoxiang>();
	
	public ChestLib(int libId)
	{
		this.libId = libId;
	}
	
	
	public List<SBaoxiang> getRandomChests(int num, boolean canrepeat)
	{
		List<SBaoxiang> randomchests = new LinkedList<SBaoxiang>();
		if(canrepeat)
		{
			for(int i = 0 ; i < num; i++)
				randomchests.add(Misc.getRandom(chests.values()));
		}
		else
		{
			List<SBaoxiang> list = new LinkedList<SBaoxiang>();
			list.addAll(chests.values());
			Misc.randomlizeList(list);
			int max = Math.min(num, list.size());
			for(int i = 0 ; i < max; i++)
				randomchests.add(list.get(i));
		}
		return randomchests;
	}
	
}
