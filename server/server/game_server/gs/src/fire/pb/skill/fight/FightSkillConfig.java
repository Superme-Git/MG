package fire.pb.skill.fight;

import java.util.ArrayList;
import java.util.List;

import fire.pb.skill.SubSkillConfig;
import fire.script.JavaScript;

// 战斗技能的配置
public class FightSkillConfig
{
	private int skillIndex;
	private String skillName;
	private int type;//技能归类
	private boolean isActiveSkill;//是否主动
	private boolean isUseInBattle;//是否战斗内使用
	private boolean isPreCountAim;//是否预先统计目标数
	private int targettype; //选择目标类型
	private int speedBonus; //速度加成
	private JavaScript hpConsumeJavaScript;//消耗Hp
	private JavaScript mpConsumeJavaScript;//消耗Mp
	private JavaScript spConsumeJavaScript;//消耗Sp
	private JavaScript epConsumeJavaScript;//消耗Ep

	private SubSkillConfig[] subSkills;
	private boolean canAtackBack;//能否反击
	private boolean canSkillDouble;//是否能连击
	private int forbidDendefPrt;//是否禁止防御和保护
	private int ignoreHide;//是否忽略隐身
	private int addAutoSkill;//是否默认自动技能
	private int playTime;//技能的战斗播放时间
	private int skillSpecialAddTime; //技能特殊延长时间 by changhao
	private int skillBelongType;//0其他，1技能，2特技 3特效 4绝技--服务器分类
	private boolean canEffectByHate;//是否被嘲讽影响
	private boolean isOnlyPve;//是否只能pve使用
	
	private List<Integer> usedattrtypea;
	private List<Integer> usedattrtypeb;

	public FightSkillConfig(){
		this.usedattrtypea = new ArrayList<Integer>();
		this.usedattrtypeb = new ArrayList<Integer>();
	}
	
	public FightSkillConfig(FightSkillConfig o)
	{
		this.skillIndex 		 = o.skillIndex 		;
		this.skillName			 = o.skillName			;
		this.type			 = o.type			;//技能归类
		this.isActiveSkill		 = o.isActiveSkill		;//是否主动
		this.isUseInBattle		 = o.isUseInBattle		;//是否战斗内使用
		this.isPreCountAim		 = o.isPreCountAim		;//是否战斗内使用		
		this.targettype		 = o.targettype		; //选择目标类型
		this.speedBonus	 = o.speedBonus	; //先发速度
		this.hpConsumeJavaScript	 = o.hpConsumeJavaScript	;//消耗Hp
		this.mpConsumeJavaScript	 = o.mpConsumeJavaScript	;//消耗Mp
		this.spConsumeJavaScript	 = o.spConsumeJavaScript	;//消耗Sp	    
		this.epConsumeJavaScript	 = o.epConsumeJavaScript	;//消耗Ep	  
		
		this.canAtackBack	 = o.canAtackBack	;//能否反击
		this.canSkillDouble		 = o.canSkillDouble		;//是否能法术连击
		this.forbidDendefPrt = o.forbidDendefPrt;//是否禁止防御和保护	
		this.addAutoSkill = o.addAutoSkill;//是否默认自动技能		
		this.ignoreHide = o.ignoreHide;//是否忽略隐身
		this.playTime		 = o.playTime		;//技能的战斗播放时间
		this.skillSpecialAddTime = o.skillSpecialAddTime; //技能特殊延长时间 by changhao
		this.skillBelongType		 = o.skillBelongType		;//0其他，1技能，2特技 3特效 4绝技--服务器分类
		this.canEffectByHate = o.canEffectByHate;	
		this.isOnlyPve = o.isOnlyPve;	
		
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
	
	public FightSkillConfig copy() {
		return new FightSkillConfig(this);
	}	
	
	public int getSkillIndex()
	{
		return skillIndex;
	}
	public void setSkillIndex(int skillIndex)
	{
		this.skillIndex = skillIndex;
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
	public JavaScript getEpConsumeJavaScript()
	{
		return epConsumeJavaScript;
	}
	public void setEpConsumeJavaScript(JavaScript epConsumeJavaScript)
	{
		this.epConsumeJavaScript = epConsumeJavaScript;
	}
	

	public SubSkillConfig[] getSubSkills()
	{
		return subSkills;
	}
	public void setSubSkills(SubSkillConfig[] subSkills)
	{
		this.subSkills = subSkills;
	}
	public int getSpeedBonus()
	{
		return speedBonus;
	}
	public void setSpeedBonus(int speedBonus)
	{
		this.speedBonus = speedBonus;
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
	public boolean isActiveSkill()
	{
		return isActiveSkill;
	}
	public void setActiveSkill(boolean isActiveSkill)
	{
		this.isActiveSkill = isActiveSkill;
	}
	public boolean isUseInBattle()
	{
		return isUseInBattle;
	}
	public void setUseInBattle(boolean isUseInBattle)
	{
		this.isUseInBattle = isUseInBattle;
	}
	public boolean isPreCountAim()
	{
		return isPreCountAim;
	}
	public void setPreCountAim(boolean isprecountaim)
	{
		this.isPreCountAim = isprecountaim;
	}

	
	public int getAddautoSkill()
	{
		return addAutoSkill;
	}
	public void setAddautoSkill(int addAutoSkill)
	{
		this.addAutoSkill = addAutoSkill;
	}
	

	public int getForbiddenDefPrt()
	{
		return forbidDendefPrt;
	}
	public void setForbiddenDefPrt(int forbidDendefPrt)
	{
		this.forbidDendefPrt = forbidDendefPrt;
	}

	public int getIgnoreHide()
	{
		return ignoreHide;
	}
	public void setIgnoreHide(int ignorehide)
	{
		this.ignoreHide = ignorehide;
	}
	
	public boolean canAtackBack()
	{
		return canAtackBack;
	}
	public void setCanAtackBack(boolean canAtackBack)
	{
		this.canAtackBack = canAtackBack;
	}

	public boolean getCanEffectByHate()
	{
		return canEffectByHate;
	}
	public void setCanEffectByHate(boolean caneffectbyhate)
	{
		this.canEffectByHate = caneffectbyhate;
	}	
	
	public boolean getIsOnlyPve()
	{
		return isOnlyPve;
	}
	public void setIsOnlyPve(boolean isonlypve)
	{
		this.isOnlyPve = isonlypve;
	}		
	
	public void setSkillBelongType(int type)
	{
		this.skillBelongType = type;
	}
	public int getSkillBelongType()
	{
		return skillBelongType;
	}
		
	public boolean canSkillDouble()
	{
		return canSkillDouble;
	}
	public void setCanDouble(boolean canSkillDouble)
	{
		this.canSkillDouble = canSkillDouble;
	}
	public int getPlayTime()
	{
		return playTime;
	}
	public void setPlayTime(int playTime)
	{
		this.playTime = playTime;
	}
	
	public int getSkillSpecialAddTime()
	{
		return skillSpecialAddTime;
	}
	public void setSkillSpecialAddTime(int skillSpecialAddTime)
	{
		this.skillSpecialAddTime = skillSpecialAddTime;
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
		if(hpConsumeJavaScript!= null) hpConsumeJavaScript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(mpConsumeJavaScript!= null) mpConsumeJavaScript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(spConsumeJavaScript!= null) spConsumeJavaScript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
		if(epConsumeJavaScript!= null) epConsumeJavaScript.parseUsedAttrTypes(usedattrtypea, usedattrtypeb);
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
