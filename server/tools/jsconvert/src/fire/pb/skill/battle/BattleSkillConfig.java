package fire.pb.skill.battle;

import java.util.ArrayList;
import java.util.List;

import fire.pb.skill.SubSkillConfig;
import fire.script.JavaScript;


public class BattleSkillConfig
{
	private int skillId;
	private JavaScript hpConsumeJs;//消耗Hp
	private JavaScript mpConsumeJs;//消耗Mp
	private JavaScript spConsumeJs;//消耗Sp
	private JavaScript epConsumeJs;//消耗Ep

	private SubSkillConfig[] subSkills;
	private String scriptName;
	private boolean caneffectbyhate;//是否被嘲讽影响
	
	private List<Integer> usedattrtypea;
	private List<Integer> usedattrtypeb;

	public BattleSkillConfig(){
		this.usedattrtypea = new ArrayList<Integer>();
		this.usedattrtypeb = new ArrayList<Integer>();
	}
	
	public BattleSkillConfig(BattleSkillConfig o)
	{
		this.skillId 		 = o.skillId 		;
		this.hpConsumeJs	 = o.hpConsumeJs	;//消耗Hp
		this.mpConsumeJs	 = o.mpConsumeJs	;//消耗Mp
		this.spConsumeJs	 = o.spConsumeJs	;//消耗Sp	  
		this.epConsumeJs	 = o.epConsumeJs	;//消耗Ep	  
		
		this.scriptName		 = o.scriptName		;	
		this.caneffectbyhate = o.caneffectbyhate;	
		
		if(this.usedattrtypea == null)
			this.usedattrtypea = new ArrayList<Integer>();
		else
			this.usedattrtypea.clear();
		this.usedattrtypea.addAll(o.usedattrtypea);

		if(this.usedattrtypeb == null)
			this.usedattrtypeb = new ArrayList<Integer>();
		else
			this.usedattrtypeb.clear();
		this.usedattrtypeb.addAll(o.usedattrtypeb);

		if(o.getSubSkills() != null)
		{
			this.subSkills	=  new SubSkillConfig[o.getSubSkills().length];
			for(int i = 0; i < o.getSubSkills().length; i++)
			{
				this.subSkills[i] = o.getSubSkills()[i].copy();
			}
		}
		
		//private SubSkillConfig[] subSkills;		
	}
	
	public BattleSkillConfig copy() {
		return new BattleSkillConfig(this);
	}	
	
	public int getSkillId()
	{
		return skillId;
	}
	public void setSkillId(int skillId)
	{
		this.skillId = skillId;
	}


	
	
	public JavaScript getHpConsumeJs()
	{
		return hpConsumeJs;
	}
	public void setHpConsumeJs(JavaScript hpConsumeJs)
	{
		this.hpConsumeJs = hpConsumeJs;
	}
	public JavaScript getMpConsumeJs()
	{
		return mpConsumeJs;
	}
	public void setMpConsumeJs(JavaScript mpConsumeJs)
	{
		this.mpConsumeJs = mpConsumeJs;
	}
	public JavaScript getSpConsumeJs()
	{
		return spConsumeJs;
	}
	public void setSpConsumeJs(JavaScript spConsumeJs)
	{
		this.spConsumeJs = spConsumeJs;
	}
	public JavaScript getEpConsumeJs()
	{
		return epConsumeJs;
	}
	public void setEpConsumeJs(JavaScript epConsumeJs)
	{
		this.epConsumeJs = epConsumeJs;
	}
	
	
	public SubSkillConfig[] getSubSkills()
	{
		return subSkills;
	}
	public void setSubSkills(SubSkillConfig[] subSkills)
	{
		this.subSkills = subSkills;
	}

	public boolean getCaneffectbyhate()
	{
		return caneffectbyhate;
	}
	public void setCaneffectbyhate(boolean caneffectbyhate)
	{
		this.caneffectbyhate = caneffectbyhate;
	}	
	
	public String getScriptName(){
		return this.scriptName;
	}
	
	public void setScriptName(String scriptName){
		this.scriptName=scriptName;
	}		
	
	public List<Integer> getUsedAttrTypea()
	{
		return this.usedattrtypea;
	}

	public List<Integer> getUsedAttrTypeb()
	{
		return this.usedattrtypeb;
	}	
	
	public void  parseUsedAttrTypes()
	{
		if(hpConsumeJs!= null) hpConsumeJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(mpConsumeJs!= null) mpConsumeJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(spConsumeJs!= null) spConsumeJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(epConsumeJs!= null) epConsumeJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(this.subSkills!=null && this.subSkills.length > 0)
		{
			for(int i = 0; i<this.subSkills.length; i++)
			{
				if(subSkills[i] != null)
					subSkills[i].parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
			}
		}
	}	
}
