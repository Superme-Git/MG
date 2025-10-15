package fire.pb.skill;

import java.util.ArrayList;
import java.util.List;

import fire.pb.skill.BuffUnit;
import fire.script.JavaScript;

public class SubSkillConfig
{
	private int subSkillId;
	private String subName;
	private JavaScript confilictStateJavascript;
	private int confilictStatesnotifyid;
	private int[] requiredBuffIndexs;
	private int[] confilictBuffIndexs;

	private int relevanceSubSkillId;
	private int[] actionConditions;
	private int goalCampType;
	private int[] goalRaceTypes;
	private int[] goalRelationTypes;
	private int[] jobIds;
	private int[] goalMonsterids;
	
	private int targetType;
	private JavaScript targetCountJavascript;
	private String sortParam;
	private JavaScript targetRateJavascript;	
	private JavaScript minMagicDamageJavascript;

	private BuffUnit[] buffUnits;
	
	private int subSkillBeginTime;
	private int mainBuffId;
	
	public SubSkillConfig(int subSkillId)
	{
		this.subSkillId = subSkillId;
	}	
	
	public SubSkillConfig(SubSkillConfig o)
	{
		subSkillId = o.subSkillId;
		subName = o.subName;
		confilictStateJavascript = o.confilictStateJavascript;
		confilictStatesnotifyid = o.confilictStatesnotifyid;
		relevanceSubSkillId = o.relevanceSubSkillId;
		goalCampType = o.goalCampType;
		targetType = o.targetType;
		targetRateJavascript = o.targetRateJavascript;
		targetCountJavascript = o.targetCountJavascript;
		minMagicDamageJavascript = o.minMagicDamageJavascript;
		sortParam = o.sortParam;
		subSkillBeginTime = o.subSkillBeginTime;
		mainBuffId = o.mainBuffId;
		
		if(o.requiredBuffIndexs != null)
		{
			requiredBuffIndexs = new int[o.requiredBuffIndexs.length];
			for(int i = 0; i < o.requiredBuffIndexs.length; i++)
				this.requiredBuffIndexs[i] = o.requiredBuffIndexs[i];
		}

		if(o.confilictBuffIndexs != null)
		{
			confilictBuffIndexs = new int[o.confilictBuffIndexs.length];
			for(int i = 0; i < o.confilictBuffIndexs.length; i++)
				this.confilictBuffIndexs[i] = o.confilictBuffIndexs[i];
		}

		if(o.actionConditions != null)
		{
			actionConditions = new int[o.actionConditions.length];
			for(int i = 0; i < o.actionConditions.length; i++)
				this.actionConditions[i] = o.actionConditions[i];
		}
		if(o.goalRaceTypes != null)
		{
			goalRaceTypes = new int[o.goalRaceTypes.length];
			for(int i = 0; i < o.goalRaceTypes.length; i++)
				this.goalRaceTypes[i] = o.goalRaceTypes[i];
		}

		if(o.goalRelationTypes != null)
		{
			goalRelationTypes = new int[o.goalRelationTypes.length];
			for(int i = 0; i < o.goalRelationTypes.length; i++)
				this.goalRelationTypes[i] = o.goalRelationTypes[i];
		}
		if(o.goalMonsterids != null)
		{
			goalMonsterids = new int[o.goalMonsterids.length];
			for(int i = 0; i < o.goalMonsterids.length; i++)
				this.goalMonsterids[i] = o.goalMonsterids[i];
		}		
		if(o.jobIds != null)
		{
			jobIds = new int[o.jobIds.length];
			for(int i = 0; i < o.jobIds.length; i++)
				this.jobIds[i] = o.jobIds[i];
		}		
		
		if(o.buffUnits != null)
		{
			buffUnits = new BuffUnit[o.buffUnits.length];
			for(int i = 0; i < o.buffUnits.length; i++)
			{
				if(o.buffUnits[i] != null)
				{
					this.buffUnits[i] = o.buffUnits[i].copy();
				}
			}
		}
	}
	
	public SubSkillConfig copy() {
		return new SubSkillConfig(this);
	}	
	
	public int getSubSkillId()
	{
		return subSkillId;
	}
	
	public void setSubSkillId(int subSkillId)
	{
		this.subSkillId = subSkillId;
	}
	
	
	public JavaScript getTargetRate()
	{
		return targetRateJavascript;
	}
	public void setTargetRate(JavaScript targetRate)
	{
		this.targetRateJavascript = targetRate;
	}

	
	public int getTargettype()
	{
		return targetType;
	}
	public void setTargettype(int targetType)
	{
		this.targetType = targetType;
	}

	public void ReplacetargetCountJs(String jsstr)
	{
		if (jsstr != null)
			targetCountJavascript = new JavaScript(jsstr);
		else
			targetCountJavascript = new JavaScript("1");
	}	
	public JavaScript getTargetCountJs()
	{
		return targetCountJavascript;
	}
	public void setTargetCountJs(JavaScript targetCountJavascript)
	{
		this.targetCountJavascript = targetCountJavascript;
	}
	
	public void ReplaceMinmagicDamageJs(String jsstr)
	{
		if (jsstr != null)
			minMagicDamageJavascript = new JavaScript(jsstr);
		else
			minMagicDamageJavascript = new JavaScript("1");
	}	
	public JavaScript getMinmagicdamageJs()
	{
		return minMagicDamageJavascript;
	}
	public void setMinmagicdamageJs(JavaScript minMagicDamageJavascript)
	{
		this.minMagicDamageJavascript = minMagicDamageJavascript;
	}	
	

	public int getRelevanceSubSkillId()
	{
		return relevanceSubSkillId;
	}
	
	public void setRelevanceSubSkillId(int relevanceSubSkillId)
	{
		this.relevanceSubSkillId = relevanceSubSkillId;
	}	

	public int[] getActionConditions()
	{
		return actionConditions;
	}
	
	public void setActionConditions(int[] actionConditions)
	{
		this.actionConditions = actionConditions;
	}	
	
	public int[] getGoalRaceTypes()
	{
		return goalRaceTypes;
	}
	
	public void setGoalRaceTypes(int[] goalRaceTypes)
	{
		this.goalRaceTypes = goalRaceTypes;
	}	
	
	public int[] getGoalRelationTypes()
	{
		return goalRelationTypes;
	}
	
	public void setGoalRelationTypes(int[] goalRelationTypes)
	{
		this.goalRelationTypes = goalRelationTypes;
	}		
	
	public int[] getJobIds()
	{
		return jobIds;
	}
	
	public void setJobIds(int[] jobIds)
	{
		this.jobIds = jobIds;
	}		
	
	public int[] getGoalMonsterids()
	{
		return goalMonsterids;
	}
	
	public void setGoalMonsterids(int[] goalMonsterids)
	{
		this.goalMonsterids = goalMonsterids;
	}		
	
	public int getGoalCampType()
	{
		return goalCampType;
	}
	
	public void setGoalCampType(int goalCampType)
	{
		this.goalCampType = goalCampType;
	}	
	
	public String getSortParam()
	{
		return sortParam;
	}
	public void setSortParam(String sortParam)
	{
		this.sortParam = sortParam;
	}
	
	/**
	 * 获取需求状态数组，可能为空
	 * @return
	 */
	public JavaScript getConfilictStateJs()
	{
		return confilictStateJavascript;
	}
	public void setConfilictStateJs(JavaScript confilictStateJavascript)
	{
		this.confilictStateJavascript = confilictStateJavascript;
	}
	
	
	public int getConfilictStatesnotifyId(){
		return this.confilictStatesnotifyid;
	}
	
	public void setConfilictStatesnotifyId(int v){
		this.confilictStatesnotifyid=v;
	}		
	/**
	 * 获取需求状态数组，可能为空
	 * @return
	 */
	public int[] getRequiredBuffIndexs()
	{
		return requiredBuffIndexs;
	}
	public void setRequiredBuffIndexs(int[] requiredBuffIndexs)
	{
		this.requiredBuffIndexs = requiredBuffIndexs;
	}
	/**
	 * 获取冲突状态数组，可能为空
	 * @return
	 */
	public int[] getConfilictBuffIndexs()
	{
		return confilictBuffIndexs;
	}
	public void setConfilictBuffIndexs(int[] confilictBuffIndexs)
	{
		this.confilictBuffIndexs = confilictBuffIndexs;
	}
	
	/**
	 * 返回拥有的buff参数，返回的是数组，可能会有空，使用前一定要判断
	 * @return
	 */
	public BuffUnit[] getBuffUnits()
	{
		return buffUnits;
	}
	public void setBuffUnits(BuffUnit[] buffUnits)
	{
		this.buffUnits = buffUnits;
	}

	public List<Integer> getBuffIDs()
	{
		List<Integer> buffIDs = new ArrayList<Integer>();
		for(int i = 0; i<this.buffUnits.length; i++)
		{
			if(buffUnits[i] != null&& buffUnits[i].buffIndex > 0)
				buffIDs.add(buffUnits[i].buffIndex);
		}
		return buffIDs;
	}
	
	public int getFirstBuffID()
	{
		if(buffUnits.length > 0 && buffUnits[0] != null)
		{
			return buffUnits[0].buffIndex;
		}
		return 0;
	}
	public String getSubName()
	{
		return subName;
	}
	public void setSubName(String subName)
	{
		this.subName = subName;
	}

	public int getSkillBeginTime()
	{
		return subSkillBeginTime;
	}
	
	public void setSkillBeginTime(int subSkillBeginTime)
	{
		this.subSkillBeginTime = subSkillBeginTime;
	}
	
	public int getMainbuffId(){
		return this.mainBuffId;
	}
	
	public void setMainbuffId(int v){
		this.mainBuffId=v;
	}	
	
	public void  parseUsedAttrTypes(List<Integer> usedattrtypea, List<Integer> usedattrtypeb)
	{
		if(minMagicDamageJavascript != null) minMagicDamageJavascript .parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(confilictStateJavascript != null) confilictStateJavascript .parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(targetCountJavascript != null) targetCountJavascript .parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(targetRateJavascript != null) targetRateJavascript .parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		
		if(this.buffUnits!=null && this.buffUnits.length > 0)
		{
			for(int i = 0; i<this.buffUnits.length; i++)
			{
				if(buffUnits[i] != null)
					buffUnits[i].parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
			}
		}
	}
/*	public int getScriptTime()
	{
		return scripttime;
	}
	public void setScriptTime(int scripttime)
	{
		this.scripttime = scripttime;
	}*/
}
