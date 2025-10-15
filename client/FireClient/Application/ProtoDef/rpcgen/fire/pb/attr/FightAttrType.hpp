
#pragma once


namespace fire { 
namespace pb { 
namespace attr { 

class FightAttrType : public FireNet::Marshal
{
public:
	enum {
		CONS = 10, // 体质
		IQ = 20, // 智力
		STR = 30, // 力量
		ENDU = 40, // 耐力
		AGI = 50, // 敏捷
		MAX_HP = 60, // 最大生命
		MAX_MP = 90, // 最大法力
		MAX_SP = 110, // 最大怒气
		ATTACK = 130, // 攻击
		DEFEND = 140, // 防御
		MAGIC_ATTACK = 150, // 法术攻击力
		MAGIC_DEF = 160, // 法术防御力
		MEDICAL = 170, // 治疗强度
		SEAL = 180, // 控制命中
		UNSEAL = 190, // 封系抵抗率
		SPEED = 200, // 速度
		HIT_RATE = 210, // 命中值
		DODGE_RATE = 220, // 躲避值
		PHY_CRITC_LEVEL = 230, // 物理暴击等级
		ANTI_PHY_CRITC_LEVEL = 240, // 抗物理暴击等级
		PHYSIC_CRIT_PCT = 250, // 物理暴击程度（初始为200%,即2倍普通伤害）
		MAGIC_CRITC_LEVEL = 260, // 魔法暴击等级
		ANTI_MAGIC_CRITC_LEVEL = 270, // 抗魔法暴击等级
		MAGIC_CRIT_PCT = 280, // 魔法暴击程度（初始为200%）
		HEAL_RATE = 290, // 治疗暴击率
		HEAL_DEGREE = 300, // 治疗暴击程度
		IGNORE_PHYSIC_DEFEND_RATE = 310, // 忽视物理防御几率
		IGNORE_PHYSIC_DEFEND_PCT = 320, // 忽视物理防御系数
		IGNORE_MAGIC_DEFEND_RATE = 330, // 忽视法术防御几率
		IGNORE_MAGIC_DEFEND_PCT = 340, // 忽视法术防御系数
		STEAL_HP_RATE = 350, // 吸血几率
		STEAL_HP_PCT = 360, // 吸血系数（吸血占造成伤害的百分比，默认0%)
		FIRE_MP_RATE = 370, // 打蓝几率
		FIRE_MP_PCT = 380, // 打蓝系数
		POISON_RATE = 390, // 普通攻击使对方中毒几率（默认0%)
		PIOSON_DAMGE_HP = 400, // 毒掉血系数
		PIOSON_DAMGE_MP = 410, // 毒掉蓝系数
		PIOSON_DAMGE_ROUND = 420, // 毒回合数
		DIE_FORBID_RATE = 430, // 死亡禁锢几率
		DIE_FORBID_ROUND = 440, // 死亡禁锢回合数
		PHYSIC_COMBO_ATTACK_RATE = 450, // 物理连击几率
		PHYSIC_COMBO_ATTACK_PCT = 460, // 物理连击伤害系数
		PHYSIC_COMBO_ATTACK_COUNT = 470, // 物理连击次数上限（原连击次数改成连击次数上限）
		MAGIC_COMBO_ATTACK_RATE = 480, // 法术连击几率
		MAGIC_COMBO_ATTACK_PCT = 490, // 法术连击伤害系数
		MAGIC_COMBO_ATTACK_COUNT = 500, // 法术连击刺伤上限
		EXTRA_ATTACK_RATE = 510, // 追击率
		EXTRA_ATTACK_PCT = 520, // 追击伤害系数
		EXTRA_ATTACK_COUNT = 530, // 追击次数
		DIFFUSE_ATTACK_RATE = 540, // 溅射几率
		DIFFUSE_ATTACK_PCT = 550, // 溅射伤害系数
		DIFFUSE_ATTACK_COUNT = 560, // 溅射数量
		PARRY_RATE = 570, // 招架几率（默认0%)
		PARRY_PCT = 580, // 格挡减伤系数
		RETURN_HURT_RATE = 590, // 反震率
		RETURN_HURT_PCT = 600, // 反震程度（反震伤害占造成伤害的百分比，默认30%)
		ATTACK_BACK_RATE = 610, // 反击率
		ATTACK_BACK_PCT = 620, // 反击程度（正常反击伤害的百分比，默认100%）
		GOD_BLESS_RATE = 630, // 神佑几率（原复活几率无用改为神佑几率）
		GOD_BLESS_PCT = 640, // 神佑复活血量系数
		ABSORB_FIRE_ODDS = 650, // 火系法术吸收率
		ABSORB_WATER_ODDS = 660, // 水系法术吸收率
		ABSORB_EARTH_ODDS = 670, // 土系法术吸收率
		ABSORB_THUNDER_ODDS = 680, // 雷系法术吸收率
		ABSORB_OTHER_ODDS = 690, // 预留系法术吸收率
		ABSORB_FIRE_PCT = 700, // 火系法术吸收系数
		ABSORB_WATER_PCT = 710, // 水系法术吸收系数
		ABSORB_EARTH_PCT = 720, // 土系法术吸收系数
		ABSORB_THUNDER_PCT = 730, // 雷系法术吸收系数
		ABSORB_OTHER_PCT = 740, // 预留系法术吸收系数
		PHYSIC_DAMGE_RATE = 750, // 物理伤害效果修正
		ANTI_PHYSIC_DAMGE_RATE = 760, // 受物理伤害效果修正
		MAGIC_DAMGE_RATE = 770, // 法术伤害效果修正
		ANTI_MAGIC_DAMGE_RATE = 780, // 受法术伤害效果修正
		HEAL_EFFECT_RATE = 790, // 治疗效果修正
		ANTI_HEAL_EFFECT_RATE = 800, // 被治疗效果修正
		SEAL_LEVEL = 810, // 封印命中等级
		ANTI_SEAL_LEVEL = 820, // 封印抗性等级
		PHYSIC_CRIT_RATE = 830, // 物理暴击几率修正
		ANTI_PHYSIC_CRIT_RATE = 840, // 被物理暴击几率修正
		MAGIC_CRIT_RATE = 850, // 法术暴击几率修正
		ANTI_MAGIC_CRIT_RATE = 860, // 被法术暴击几率修正
		HEAL_CRIT_LEVEL = 870, // 治疗暴击几率修正
		ANTI_HEAL_CRIT_LEVEL = 880, // 被治疗暴击几率修正
		ANTI_POISON_RATE = 890, // 攻击和被攻击时中毒几率修正
		ANTI_DIE_FORBID_RATE = 900, // 被死亡禁锢几率修正
		ANTI_PARRY_RATE = 910, // 攻击时被格挡几率修正
		ANTI_RETURN_HURT_RATE = 920, // 攻击时被反震几率修正
		ANTI_ATTACK_BACK_RATE = 930, // 攻击时被反击几率修正（原反击次数）
		ANTI_GOD_BLESS_RATE = 940, // 攻击时被神佑几率修正
		PHYSIC_DAMGE_PIERCE_RATE = 950, // 物理穿透
		ANTI_PHYSIC_DAMGE_PIERCE_RATE = 960, // 物理抵抗
		MAGIC_DAMGE_PIERCE_RATE = 970, // 法术穿透
		ANTI_MAGIC_DAMGE_PIERCE_RATE = 980, // 法术抵抗
		HEAL_DEEP_RATE = 990, // 治疗加深
		ANTI_HEAL_DEEP_RATE = 1000, // 被治疗加深
		EFFECT_POINT = 1010, // 技能效果点
		TEMP_SP = 1020, // 临时怒气
		ENLIMIT = 1520, // 活力上限
		PFLIMIT = 1530, // 体力上限
		AMEND_HIDDEN_WEAPON_PASSIVE = 1540, // 飞镖被动伤害修正
		ATTACK_BACK_LEVEL = 1750, // 反击等级
		RETURN_HURT_LEVEL = 1760, // 物理反震等级
		ANTI_ATTACK_BACK_LEVEL = 1770, // 抗反击等级
		ANTI_RETURN_HURT_LEVEL = 1780, // 抗物理反震等级
		MAGIC_HIT = 1840, // 法术命中值
		HEALEDREVISE = 800, // 特技重数
		ANTI_CRITC_LEVEL = 2090, // 抗暴等级
		KONGZHI_JIACHENG = 2130, // 控制加成
		KONGZHI_MIANYI = 2140, // 控制免疫
		PHYSIC_FLOAT_VALUE = 2150, // 物理波动
		MAGIC_FLOAT_VALUE = 2160, // 法术波动
		HEAL_FLOAT_VALUE = 2170, // 治疗波动
		DEEP_HEAL_CRITC_LEVEL = 2180, // 被治疗暴击
		SHAPE_ID = 3000, // 模型id
	};

	FightAttrType() {
	}

	bool _validator_() const {
		return true;
	}

	FireNet::Marshal::OctetsStream & marshal(FireNet::Marshal::OctetsStream & _os_) const {
		return _os_;
	}

	const FireNet::Marshal::OctetsStream & unmarshal(const FireNet::Marshal::OctetsStream & _os_) {
		return _os_;
	}

	bool operator<(const FightAttrType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const FightAttrType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
