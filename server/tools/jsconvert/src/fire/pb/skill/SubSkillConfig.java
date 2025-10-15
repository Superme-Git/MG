package fire.pb.skill;

import java.util.ArrayList;
import java.util.List;

import fire.pb.skill.BuffArg;
import fire.script.JavaScript;

public class SubSkillConfig
{
	private int skillId;
	private String name;
	private JavaScript confilictStateJs;
	private int confilictStatesnotifyid;
	private int[] requiredBuffIds;
	private int[] confilictBuffIds;

	private int relevanceSkillID;
	private int[] actionConditions;
	private int aimCampType;
	private int[] aimRaceTypes;
	private int[] aimRelationTypes;
	private int[] jobids;
	private int[] aimMonsterids;
	
	private int targettype;
	private JavaScript targetCountJs;
	private String sortArg;
	private JavaScript targetRateJs;	
	private JavaScript minmagicdamageJs;

	private BuffArg[] buffArgs;
	
	private int subskillstarttime;
	private int mainbuffId;
	
	public SubSkillConfig(int skillId)
	{
		this.skillId = skillId;
	}	
	
	public SubSkillConfig(SubSkillConfig o)
	{
		skillId = o.skillId;
		name = o.name;
		confilictStateJs = o.confilictStateJs;
		confilictStatesnotifyid = o.confilictStatesnotifyid;
		relevanceSkillID = o.relevanceSkillID;
		aimCampType = o.aimCampType;
		targettype = o.targettype;
		targetRateJs = o.targetRateJs;
		targetCountJs = o.targetCountJs;
		minmagicdamageJs = o.minmagicdamageJs;
		sortArg = o.sortArg;
		subskillstarttime = o.subskillstarttime;
		mainbuffId = o.mainbuffId;
		
		if(o.requiredBuffIds != null)
		{
			requiredBuffIds = new int[o.requiredBuffIds.length];
			for(int i = 0; i < o.requiredBuffIds.length; i++)
				this.requiredBuffIds[i] = o.requiredBuffIds[i];
		}

		if(o.confilictBuffIds != null)
		{
			confilictBuffIds = new int[o.confilictBuffIds.length];
			for(int i = 0; i < o.confilictBuffIds.length; i++)
				this.confilictBuffIds[i] = o.confilictBuffIds[i];
		}

		if(o.actionConditions != null)
		{
			actionConditions = new int[o.actionConditions.length];
			for(int i = 0; i < o.actionConditions.length; i++)
				this.actionConditions[i] = o.actionConditions[i];
		}
		if(o.aimRaceTypes != null)
		{
			aimRaceTypes = new int[o.aimRaceTypes.length];
			for(int i = 0; i < o.aimRaceTypes.length; i++)
				this.aimRaceTypes[i] = o.aimRaceTypes[i];
		}

		if(o.aimRelationTypes != null)
		{
			aimRelationTypes = new int[o.aimRelationTypes.length];
			for(int i = 0; i < o.aimRelationTypes.length; i++)
				this.aimRelationTypes[i] = o.aimRelationTypes[i];
		}
		if(o.aimMonsterids != null)
		{
			aimMonsterids = new int[o.aimMonsterids.length];
			for(int i = 0; i < o.aimMonsterids.length; i++)
				this.aimMonsterids[i] = o.aimMonsterids[i];
		}		
		if(o.jobids != null)
		{
			jobids = new int[o.jobids.length];
			for(int i = 0; i < o.jobids.length; i++)
				this.jobids[i] = o.jobids[i];
		}		
		
		if(o.buffArgs != null)
		{
			buffArgs = new BuffArg[o.buffArgs.length];
			for(int i = 0; i < o.buffArgs.length; i++)
			{
				if(o.buffArgs[i] != null)
				{
					this.buffArgs[i] = o.buffArgs[i].copy();
				}
			}
		}
	}
	
	public SubSkillConfig copy() {
		return new SubSkillConfig(this);
	}	
	
	public int getSkillId()
	{
		return skillId;
	}
	
	public void setSkillId(int skillId)
	{
		this.skillId = skillId;
	}
	
	
	public JavaScript getTargetRate()
	{
		return targetRateJs;
	}
	public void setTargetRate(JavaScript targetRate)
	{
		this.targetRateJs = targetRate;
	}

	
	public int getTargettype()
	{
		return targettype;
	}
	public void setTargettype(int targettype)
	{
		this.targettype = targettype;
	}

	public void ReplacetargetCountJs(String jsstr)
	{
		if (jsstr != null)
			targetCountJs = new JavaScript(jsstr);
		else
			targetCountJs = new JavaScript("1");
	}	
	public JavaScript getTargetCountJs()
	{
		return targetCountJs;
	}
	public void setTargetCountJs(JavaScript targetCountJs)
	{
		this.targetCountJs = targetCountJs;
	}
	
	public void ReplaceminmagicdamageJs(String jsstr)
	{
		if (jsstr != null)
			minmagicdamageJs = new JavaScript(jsstr);
		else
			minmagicdamageJs = new JavaScript("1");
	}	
	public JavaScript getMinmagicdamageJs()
	{
		return minmagicdamageJs;
	}
	public void setMinmagicdamageJs(JavaScript minmagicdamageJs)
	{
		this.minmagicdamageJs = minmagicdamageJs;
	}	
	

	public int getRelevanceSkillID()
	{
		return relevanceSkillID;
	}
	
	public void setRelevanceSkillID(int relevanceSkillID)
	{
		this.relevanceSkillID = relevanceSkillID;
	}	

	public int[] getActionConditions()
	{
		return actionConditions;
	}
	
	public void setActionConditions(int[] actionConditions)
	{
		this.actionConditions = actionConditions;
	}	
	
	public int[] getAimRaceTypes()
	{
		return aimRaceTypes;
	}
	
	public void setAimRaceTypes(int[] aimRaceTypes)
	{
		this.aimRaceTypes = aimRaceTypes;
	}	
	
	public int[] getAimRelationTypes()
	{
		return aimRelationTypes;
	}
	
	public void setAimRelationTypes(int[] aimRelationTypes)
	{
		this.aimRelationTypes = aimRelationTypes;
	}		
	
	public int[] getjobids()
	{
		return jobids;
	}
	
	public void setjobids(int[] jobids)
	{
		this.jobids = jobids;
	}		
	
	public int[] getaimMonsterids()
	{
		return aimMonsterids;
	}
	
	public void setaimMonsterids(int[] aimMonsterids)
	{
		this.aimMonsterids = aimMonsterids;
	}		
	
	public int getAimCampType()
	{
		return aimCampType;
	}
	
	public void setAimCampType(int aimCampType)
	{
		this.aimCampType = aimCampType;
	}	
	
	public String getSortArg()
	{
		return sortArg;
	}
	public void setSortArg(String sortArg)
	{
		this.sortArg = sortArg;
	}
	
	
	public JavaScript getConfilictStateJs()
	{
		return confilictStateJs;
	}
	public void setConfilictStateJs(JavaScript confilictStateJs)
	{
		this.confilictStateJs = confilictStateJs;
	}
	
	
	public int getConfilictStatesnotifyId(){
		return this.confilictStatesnotifyid;
	}
	
	public void setConfilictStatesnotifyId(int v){
		this.confilictStatesnotifyid=v;
	}		
	
	public int[] getRequiredBuffIds()
	{
		return requiredBuffIds;
	}
	public void setRequiredBuffIds(int[] requiredBuffIds)
	{
		this.requiredBuffIds = requiredBuffIds;
	}
	
	public int[] getConfilictBuffIds()
	{
		return confilictBuffIds;
	}
	public void setConfilictBuffIds(int[] confilictBuffIds)
	{
		this.confilictBuffIds = confilictBuffIds;
	}
	
	
	public BuffArg[] getBuffArgs()
	{
		return buffArgs;
	}
	public void setBuffArgs(BuffArg[] buffArgs)
	{
		this.buffArgs = buffArgs;
	}

	public List<Integer> getBuffIDs()
	{
		List<Integer> buffIDs = new ArrayList<Integer>();
		for(int i = 0; i<this.buffArgs.length; i++)
		{
			if(buffArgs[i] != null&& buffArgs[i].buffId > 0)
				buffIDs.add(buffArgs[i].buffId);
		}
		return buffIDs;
	}
	
	public int getFirstBuffID()
	{
		if(buffArgs.length > 0 && buffArgs[0] != null)
		{
			return buffArgs[0].buffId;
		}
		return 0;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}

	public int getSkillStartTime()
	{
		return subskillstarttime;
	}
	
	public void setSkillStartTime(int subskillstarttime)
	{
		this.subskillstarttime = subskillstarttime;
	}
	
	public int getMainbuffId(){
		return this.mainbuffId;
	}
	
	public void setMainbuffId(int v){
		this.mainbuffId=v;
	}	
	
	public void  parseUsedAttrTypes(List<Integer> usedattrtypea, List<Integer> usedattrtypeb)
	{
		if(minmagicdamageJs!= null) minmagicdamageJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(confilictStateJs!= null) confilictStateJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(targetCountJs!= null) targetCountJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(targetRateJs!= null) targetRateJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		
		if(this.buffArgs!=null && this.buffArgs.length > 0)
		{
			for(int i = 0; i<this.buffArgs.length; i++)
			{
				if(buffArgs[i] != null)
					buffArgs[i].parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
			}
		}
	}
	
	public void  parseUsedAttrTypes()
	{
		List<Integer> usedattrtypea = new ArrayList<>(), usedattrtypeb = new ArrayList<>();
		if(minmagicdamageJs!= null) minmagicdamageJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(confilictStateJs!= null) confilictStateJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(targetCountJs!= null) targetCountJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(targetRateJs!= null) targetRateJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		
		if(this.buffArgs!=null && this.buffArgs.length > 0)
		{
			for(int i = 0; i<this.buffArgs.length; i++)
			{
				if(buffArgs[i] != null)
					buffArgs[i].parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
			}
		}
	}
}
