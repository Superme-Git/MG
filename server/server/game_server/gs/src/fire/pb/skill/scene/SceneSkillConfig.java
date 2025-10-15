package fire.pb.skill.scene;

import java.util.ArrayList;
import java.util.List;

import fire.pb.skill.SubSkillConfig;
import fire.script.JavaScript;

/**
 * 在场景中使用的技能配置
 * @author nobody
 *
 */
public class SceneSkillConfig
{

	private int skillId;
	private String skillName;
	private int type;
	private int fighterType;
	private int targettype;
	private JavaScript hpConsumeJavaScript;
	private JavaScript mpConsumeJavaScript;
	private JavaScript spConsumeJavaScript;
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
	public int getTargettype()
	{
		return targettype;
	}
	public void setTargettype(int targettype)
	{
		this.targettype = targettype;
	}

	public JavaScript getHpConsumeJavaScript()
	{
		return hpConsumeJavaScript;
	}
	public void setHpConsumeJavaScript(JavaScript hpConsumeJavaScript)
	{
		this.hpConsumeJavaScript = hpConsumeJavaScript;
	}
	public JavaScript getMpConsumeJavaScript()
	{
		return mpConsumeJavaScript;
	}
	public void setMpConsumeJavaScript(JavaScript mpConsumeJavaScript)
	{
		this.mpConsumeJavaScript = mpConsumeJavaScript;
	}
	public JavaScript getSpConsumeJavaScript()
	{
		return spConsumeJavaScript;
	}
	public void setSpConsumeJavaScript(JavaScript spConsumeJavaScript)
	{
		this.spConsumeJavaScript = spConsumeJavaScript;
	}
	public SubSkillConfig[] getSubSkills()
	{
		return subSkills;
	}
	public void setSubSkills(SubSkillConfig[] subSkills)
	{
		this.subSkills = subSkills;
	}
	public String getSkillName()
	{
		return skillName;
	}
	public void setSkillName(String skillName)
	{
		this.skillName = skillName;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public int getFighterType()
	{
		return fighterType;
	}
	public void setFighterType(int fighterType)
	{
		this.fighterType = fighterType;
	}
	
	public void  parseUsedAttrTypes()
	{
		hpConsumeJavaScript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		mpConsumeJavaScript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		spConsumeJavaScript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
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
