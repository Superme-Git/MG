
#pragma once


namespace fire { 
namespace pb { 
namespace attr { 

class AttrType : public FireNet::Marshal
{
public:
	enum {
		CONS = 10, // 体质
		IQ = 20, // 智力
		STR = 30, // 力量
		ENDU = 40, // 耐力
		AGI = 50, // 敏捷
		MAX_HP = 60, // 最大生命上限
		UP_LIMITED_HP = 70, // 当前生命上限（小于等于最大生命上限）
		HP = 80, // 当前生命
		SPIRIT = 90, // 灵力
		MAX_MP = 90, // 最大法力
		MP = 100, // 当前法力
		MAX_SP = 110, // 最大怒气
		SP = 120, // 怒气
		ATTACK = 130, // 攻击
		DEFEND = 140, // 防御
		MAGIC_ATTACK = 150, // 法术攻击力
		MAGIC_DEF = 160, // 法术防御力
		MEDICAL = 170, // 治疗强度
		SEAL = 180, // 控制命中
		UNSEAL = 190, // 控制抗性
		SPEED = 200, // 速度
		HIT_RATE = 210, // 命中值
		DODGE_RATE = 220, // 躲避值
		PHY_CRIT_PCT = 250, // 物理暴击程度（初始为200%,即2倍普通伤害）
		MAGIC_CRIT_PCT = 280, // 魔法暴击程度（初始为200%）
		PHY_CRITC_LEVEL = 230, // 物理暴击等级
		ANTI_PHY_CRITC_LEVEL = 240, // 物理抗性等级
		MAGIC_CRITC_LEVEL = 260, // 魔法暴击等级
		ANTI_MAGIC_CRITC_LEVEL = 270, // 魔法抗性等级
		HEAL_CRIT_LEVEL = 290, // 治疗暴击等级
		HEAL_CRIT_PCT = 300, // 治疗暴击程度
		PHFORCE = 450, // 当前体力
		EXP = 470, // 经验
		NEXP = 480, // 升级经验
		RENQI = 610, // 人气值
		SCHOOLFUND = 850, // 职业贡献度
		WULI_CHUANTOU = 950, // 物理穿透
		WULI_DIKANG = 960, // 物理抵抗
		FASHU_CHUANTOU = 970, // 法术穿透
		FASHU_DIKANG = 980, // 法术抵抗
		ZHILIAO_JIASHEN = 990, // 治疗加深
		EFFECT_POINT = 1010, // 技能效果点
		TEMP_SP = 1020, // 临时怒气
		MASTER_REPUTATION = 1080, // 师徒声望
		PET_XUEMAI_MAX = 1150, // 宠物资质上限
		PET_LOW_SKILL = 1170, // 宠物低级技能数
		PET_HIGH_SKILL = 1180, // 宠物高级技能数
		PET_SUPER_SKILL = 1190, // 宠物超级技能数
		LEVEL = 1230, // 等级
		PET_LIFE = 1360, // 宠物寿命
		ACTIVESTAR = 1380, // 活跃度幸运星
		POINT = 1400, // 潜能
		QILIZHI = 1410, // 气力值
		QILIZHI_LIMIT = 1420, // 气力值上限
		PET_XUE_MAI_LEVEL = 1430, // 资质星级
		PET_FIGHT_LEVEL = 1430, // 宠物出战等级
		PET_ATTACK_APT = 1440, // 宠物攻击资质
		PET_DEFEND_APT = 1450, // 宠物防御资质
		PET_PHYFORCE_APT = 1460, // 宠物体力资质
		PET_MAGIC_APT = 1470, // 宠物法力资质
		PET_SPEED_APT = 1480, // 宠物速度资质
		PET_DODGE_APT = 1490, // 宠物躲闪资质
		PET_GROW_RATE = 1500, // 宠物成长率
		ENLIMIT = 1520, // 活力上限
		PFLIMIT = 1530, // 体力上限
		PET_SCALE = 1810, // 宠物大小 1-4
		ACTIVENESS = 1820, // 活跃度值
		ANTI_CRIT_LEVEL = 2090, // 暴击抗性等级
		KONGZHI_JIACHENG = 2130, // 控制加成
		KONGZHI_MIANYI = 2140, // 控制免疫
		ENERGY = 3010, // 当前活力
	};

	AttrType() {
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

	bool operator<(const AttrType &_o_) const {
		return compareTo(_o_) < 0;
	}

	int compareTo(const AttrType &_o_) const {
		if (&_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

};

} 
} 
} 
