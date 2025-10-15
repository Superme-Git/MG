package fire.pb.skill;

import java.util.LinkedList;
import java.util.List;

public class InbornConfig {
	private int id;
	private List<Integer> dependInborns = new LinkedList<Integer>(); 
	private List<Integer> nbSkillDependLevels = new LinkedList<Integer>(); 

	public InbornConfig(int id)
	{
		this.id = id;
	}

	public InbornConfig(fire.pb.skill.SInbornInfo inbornInfo)
	{
		this.id = inbornInfo.id;
		if (inbornInfo.dependInborn != null)
		{
			String[] strs = inbornInfo.dependInborn.split(",");
			for (String str : strs)
			{
				dependInborns.add((int)Float.parseFloat(str));
			}
		}
		if (inbornInfo.nbskilldependLevel != null)
		{
			String[] strs = inbornInfo.nbskilldependLevel.split(",");
			for (String str : strs)
			{
				nbSkillDependLevels.add((int)Float.parseFloat(str));
			}
		}
	}
	
	public InbornConfig(InbornConfig o)
	{
		this.id 		 = o.id ;
		this.dependInborns.addAll(o.getDependInborns());
		this.nbSkillDependLevels.addAll(o.getNbskilldependLevels());
	}
	
	public int getInbornId()
	{
		return id;
	}
	
	
	public List<Integer> getDependInborns()
	{
		return dependInborns;
	}
	
	public List<Integer> getNbskilldependLevels()
	{
		return nbSkillDependLevels;
	}
}
