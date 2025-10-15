package fire.pb.buff.single;

import fire.pb.skill.SkillConstant;

/**使用复活药剂*/
public class ItemRelive extends Relive
{
	private boolean checked = false;
	private float improve = 1f;
	
	public ItemRelive(SingleBuffConfig buffConfig)
	{
		super(buffConfig);
	}
	
	protected int amendChangeWound(int changewound)
	{
		if (!checked)
		{
/*			if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.SPECIAL_SKILL_SHEN_NONG))
				improve = 1.1f;
*/			checked = true;
		}
		return (int)(changewound * improve);
	}

	protected int amendAddHp(int addhp)
	{
		if (!checked)
		{
/*			if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.SPECIAL_SKILL_SHEN_NONG))
				improve = 1.1f;
*/			checked = true;
		}
		return (int)(addhp * improve);
	}
	
	protected int amendAddMp(int addmp)
	{
		if (!checked)
		{
/*			if (opfighter.getSkillAgent().hasEffectSkill(SkillConstant.SPECIAL_SKILL_SHEN_NONG))
				improve = 1.1f;
*/			checked = true;
		}
		return (int)(addmp * improve);
	}
}
