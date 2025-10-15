#ifndef __ROLESKILLCOMMON_H
#define __ROLESKILLCOMMON_H

#define FOOD_LEVEL1_TYPE	306

#define MENGHUOYOU_BASEID		36029
#define MENGHUOYOUBIND_BASEID	36030
#define CAOYAOJINGHUA_BASEID	36028

#define FOODSTUFF_LEVEL1		36191

#define MakeDrugsSkill_ID		310101
#define MakeCooksSkill_ID		310201

#define MakeBaoZiSkill_ID		310202
#define PurifyDrugSkill_ID		310102

#define QuenchSkill_ID			300402
#define GemInLaySkill_ID		300401
#define GemComposeSkill_ID		300403

#define SUI_SHEN_SHANG_DIAN         600018

#define color_yellow    CEGUI::colour(0xFFFFFF33)
#define color_white   CEGUI::colour(0xFFFFFFFF)
#define MACRO_RED		CEGUI::colour(0xFFFF0000) 
#define MACRO_GREEN	CEGUI::colour(0xFF00FF00) 

#define BLACKSMITH	10021
#define TAILOR		10022
#define CRAFTSMAN	10023

const int MATERIA_WRONGFUL	= -1;
const int ENERGY_NOT_ENOUGH = -2;
const int HANTIE_NOT_ENOUGH = -3;
const int TUZHI_NOT_ENOUGH	= -4;
const int MONEY_NOT_ENOUGH	= -5;
const int PHYFORE_NOT_ENOUGH= -6;
const int LEVEL_NOT_ENOUGH	= -7;
const int LING_QUAN_LU_NOT_ENOUGH = -8;
const int CAO_YAO_JING_HUA_NOT_ENOUGH = -9;
const int BAO_LUO_LAN_KONGWEI_NOT_ENOUGH = -10;

struct stLifeSkillInfo
{
	int	Level;			//技能等级
	int skilled;		//技能熟练度
	stLifeSkillInfo(int l,int s):Level(l),skilled(s){}
	stLifeSkillInfo()
	{
		Level = 0;
		skilled = 0;
	}
};

#define OTHER_SKILL_MIN					100000
#define NORMAL_SKILL_MIN				110000
#define PET_SKILL_MIN					200000
#define LIFE_SKILL_MIN					300000
#define EQUIP_SKILL_MIN					400000
#define MONSTER_NORMAL_SKILL_MIN		500000
#define ZAXUE_SKILL_MIN					600000
#define MARRIAGE_SKILL_MIN				700000
#define NUQI_SKILL_MIN					800000

//技能种类
enum eSkillType
{
	eSkillType_NULL,
	eSkillType_Other,
	eSkillType_Normal,
	eSkillType_Pet,
	eSkillType_Life,
	eSkillType_Equip,
	eSkillType_ZaXue,
	eSkillType_Marry,
	eSkillType_NuQi,
};

//根据技能ID判断技能种类
inline eSkillType gGetSkillTypeByID(int skillid)
{
	if (skillid >= 900000)
	{
		return eSkillType_NULL;
	}
	else if (skillid >= NUQI_SKILL_MIN)
	{
		return eSkillType_NuQi;
	}
	else if (skillid >= MARRIAGE_SKILL_MIN)
	{
		return eSkillType_Marry;
	}
	else if (skillid >= ZAXUE_SKILL_MIN)
	{
		return eSkillType_ZaXue;
	}
	else if (skillid >= MONSTER_NORMAL_SKILL_MIN)
	{
		return eSkillType_Normal;
	}
	else if (skillid >= EQUIP_SKILL_MIN)
	{
		return eSkillType_Equip;
	}
	else if (skillid >= LIFE_SKILL_MIN)
	{
		return eSkillType_Life;
	}
	else if (skillid >= PET_SKILL_MIN)
	{
		return eSkillType_Pet;
	}
	else if (skillid >= NORMAL_SKILL_MIN)
	{
		return eSkillType_Normal;
	}
	else if (skillid >= OTHER_SKILL_MIN)
	{
		return eSkillType_Other;
	}
	else
	{
		return eSkillType_NULL;
	}
}
#endif
