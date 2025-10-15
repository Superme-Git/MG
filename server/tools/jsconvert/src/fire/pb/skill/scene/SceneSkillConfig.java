package fire.pb.skill.scene;

import java.util.ArrayList;
import java.util.List;

import fire.pb.skill.SubSkillConfig;
import fire.script.JavaScript;


public class SceneSkillConfig
{

	private int skillId;
	private JavaScript hpConsumeJs;
	private JavaScript mpConsumeJs;
	private JavaScript spConsumeJs;
	private SubSkillConfig[] subSkills;
	
	private List<Integer> usedattrtypea;
	private List<Integer> usedattrtypeb;
	
	public SceneSkillConfig(int skillId)
	{
		this.usedattrtypea = new ArrayList<Integer>();
		this.usedattrtypeb = new ArrayList<Integer>();
		this.skillId = skillId;
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
	
	public SubSkillConfig[] getSubSkills()
	{
		return subSkills;
	}
	public void setSubSkills(SubSkillConfig[] subSkills)
	{
		this.subSkills = subSkills;
	}
	
	public void  parseUsedAttrTypes()
	{
		hpConsumeJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		mpConsumeJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		spConsumeJs.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
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
