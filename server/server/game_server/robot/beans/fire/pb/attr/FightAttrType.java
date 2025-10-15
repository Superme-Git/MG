
package fire.pb.attr;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class FightAttrType implements Marshal , Comparable<FightAttrType>{
	public final static int CONS = 10; // 体质
	public final static int IQ = 20; // 智力
	public final static int STR = 30; // 力量
	public final static int ENDU = 40; // 耐力
	public final static int AGI = 50; // 敏捷
	public final static int MAX_HP = 60; // 最大生命
	public final static int MAX_MP = 90; // 最大法力
	public final static int MAX_SP = 110; // 最大怒气
	public final static int ATTACK = 130; // 攻击
	public final static int DEFEND = 140; // 防御
	public final static int MAGIC_ATTACK = 150; // 法术攻击力
	public final static int MAGIC_DEF = 160; // 法术防御力
	public final static int MEDICAL = 170; // 治疗强度
	public final static int SEAL = 180; // 控制命中
	public final static int UNSEAL = 190; // 封系抵抗率
	public final static int SPEED = 200; // 速度
	public final static int HIT_RATE = 210; // 命中值
	public final static int DODGE_RATE = 220; // 躲避值
	public final static int PHY_CRITC_LEVEL = 230; // 物理暴击等级
	public final static int ANTI_PHY_CRITC_LEVEL = 240; // 抗物理暴击等级
	public final static int PHYSIC_CRIT_PCT = 250; // 物理暴击程度（初始为200%,即2倍普通伤害）
	public final static int MAGIC_CRITC_LEVEL = 260; // 魔法暴击等级
	public final static int ANTI_MAGIC_CRITC_LEVEL = 270; // 抗魔法暴击等级
	public final static int MAGIC_CRIT_PCT = 280; // 魔法暴击程度（初始为200%）
	public final static int HEAL_RATE = 290; // 治疗暴击率
	public final static int HEAL_DEGREE = 300; // 治疗暴击程度
	public final static int IGNORE_PHYSIC_DEFEND_RATE = 310; // 忽视物理防御几率
	public final static int IGNORE_PHYSIC_DEFEND_PCT = 320; // 忽视物理防御系数
	public final static int IGNORE_MAGIC_DEFEND_RATE = 330; // 忽视法术防御几率
	public final static int IGNORE_MAGIC_DEFEND_PCT = 340; // 忽视法术防御系数
	public final static int STEAL_HP_RATE = 350; // 吸血几率
	public final static int STEAL_HP_PCT = 360; // 吸血系数（吸血占造成伤害的百分比，默认0%)
	public final static int FIRE_MP_RATE = 370; // 打蓝几率
	public final static int FIRE_MP_PCT = 380; // 打蓝系数
	public final static int POISON_RATE = 390; // 普通攻击使对方中毒几率（默认0%)
	public final static int PIOSON_DAMGE_HP = 400; // 毒掉血系数
	public final static int PIOSON_DAMGE_MP = 410; // 毒掉蓝系数
	public final static int PIOSON_DAMGE_ROUND = 420; // 毒回合数
	public final static int DIE_FORBID_RATE = 430; // 死亡禁锢几率
	public final static int DIE_FORBID_ROUND = 440; // 死亡禁锢回合数
	public final static int PHYSIC_COMBO_ATTACK_RATE = 450; // 物理连击几率
	public final static int PHYSIC_COMBO_ATTACK_PCT = 460; // 物理连击伤害系数
	public final static int PHYSIC_COMBO_ATTACK_COUNT = 470; // 物理连击次数上限（原连击次数改成连击次数上限）
	public final static int MAGIC_COMBO_ATTACK_RATE = 480; // 法术连击几率
	public final static int MAGIC_COMBO_ATTACK_PCT = 490; // 法术连击伤害系数
	public final static int MAGIC_COMBO_ATTACK_COUNT = 500; // 法术连击刺伤上限
	public final static int EXTRA_ATTACK_RATE = 510; // 追击率
	public final static int EXTRA_ATTACK_PCT = 520; // 追击伤害系数
	public final static int EXTRA_ATTACK_COUNT = 530; // 追击次数
	public final static int DIFFUSE_ATTACK_RATE = 540; // 溅射几率
	public final static int DIFFUSE_ATTACK_PCT = 550; // 溅射伤害系数
	public final static int DIFFUSE_ATTACK_COUNT = 560; // 溅射数量
	public final static int PARRY_RATE = 570; // 招架几率（默认0%)
	public final static int PARRY_PCT = 580; // 格挡减伤系数
	public final static int RETURN_HURT_RATE = 590; // 反震率
	public final static int RETURN_HURT_PCT = 600; // 反震程度（反震伤害占造成伤害的百分比，默认30%)
	public final static int ATTACK_BACK_RATE = 610; // 反击率
	public final static int ATTACK_BACK_PCT = 620; // 反击程度（正常反击伤害的百分比，默认100%）
	public final static int GOD_BLESS_RATE = 630; // 神佑几率（原复活几率无用改为神佑几率）
	public final static int GOD_BLESS_PCT = 640; // 神佑复活血量系数
	public final static int ABSORB_FIRE_ODDS = 650; // 火系法术吸收率
	public final static int ABSORB_WATER_ODDS = 660; // 水系法术吸收率
	public final static int ABSORB_EARTH_ODDS = 670; // 土系法术吸收率
	public final static int ABSORB_THUNDER_ODDS = 680; // 雷系法术吸收率
	public final static int ABSORB_OTHER_ODDS = 690; // 预留系法术吸收率
	public final static int ABSORB_FIRE_PCT = 700; // 火系法术吸收系数
	public final static int ABSORB_WATER_PCT = 710; // 水系法术吸收系数
	public final static int ABSORB_EARTH_PCT = 720; // 土系法术吸收系数
	public final static int ABSORB_THUNDER_PCT = 730; // 雷系法术吸收系数
	public final static int ABSORB_OTHER_PCT = 740; // 预留系法术吸收系数
	public final static int PHYSIC_DAMGE_RATE = 750; // 物理伤害效果修正
	public final static int ANTI_PHYSIC_DAMGE_RATE = 760; // 受物理伤害效果修正
	public final static int MAGIC_DAMGE_RATE = 770; // 法术伤害效果修正
	public final static int ANTI_MAGIC_DAMGE_RATE = 780; // 受法术伤害效果修正
	public final static int HEAL_EFFECT_RATE = 790; // 治疗效果修正
	public final static int ANTI_HEAL_EFFECT_RATE = 800; // 被治疗效果修正
	public final static int SEAL_LEVEL = 810; // 封印命中等级
	public final static int ANTI_SEAL_LEVEL = 820; // 封印抗性等级
	public final static int PHYSIC_CRIT_RATE = 830; // 物理暴击几率修正
	public final static int ANTI_PHYSIC_CRIT_RATE = 840; // 被物理暴击几率修正
	public final static int MAGIC_CRIT_RATE = 850; // 法术暴击几率修正
	public final static int ANTI_MAGIC_CRIT_RATE = 860; // 被法术暴击几率修正
	public final static int HEAL_CRIT_LEVEL = 870; // 治疗暴击几率修正
	public final static int ANTI_HEAL_CRIT_LEVEL = 880; // 被治疗暴击几率修正
	public final static int ANTI_POISON_RATE = 890; // 攻击和被攻击时中毒几率修正
	public final static int ANTI_DIE_FORBID_RATE = 900; // 被死亡禁锢几率修正
	public final static int ANTI_PARRY_RATE = 910; // 攻击时被格挡几率修正
	public final static int ANTI_RETURN_HURT_RATE = 920; // 攻击时被反震几率修正
	public final static int ANTI_ATTACK_BACK_RATE = 930; // 攻击时被反击几率修正（原反击次数）
	public final static int ANTI_GOD_BLESS_RATE = 940; // 攻击时被神佑几率修正
	public final static int PHYSIC_DAMGE_PIERCE_RATE = 950; // 物理穿透
	public final static int ANTI_PHYSIC_DAMGE_PIERCE_RATE = 960; // 物理抵抗
	public final static int MAGIC_DAMGE_PIERCE_RATE = 970; // 法术穿透
	public final static int ANTI_MAGIC_DAMGE_PIERCE_RATE = 980; // 法术抵抗
	public final static int HEAL_DEEP_RATE = 990; // 治疗加深
	public final static int ANTI_HEAL_DEEP_RATE = 1000; // 被治疗加深
	public final static int EFFECT_POINT = 1010; // 技能效果点
	public final static int TEMP_SP = 1020; // 临时怒气
	public final static int ENLIMIT = 1520; // 活力上限
	public final static int PFLIMIT = 1530; // 体力上限
	public final static int AMEND_HIDDEN_WEAPON_PASSIVE = 1540; // 飞镖被动伤害修正
	public final static int ATTACK_BACK_LEVEL = 1750; // 反击等级
	public final static int RETURN_HURT_LEVEL = 1760; // 物理反震等级
	public final static int ANTI_ATTACK_BACK_LEVEL = 1770; // 抗反击等级
	public final static int ANTI_RETURN_HURT_LEVEL = 1780; // 抗物理反震等级
	public final static int MAGIC_HIT = 1840; // 法术命中值
	public final static int HEALEDREVISE = 800; // 特技重数
	public final static int ANTI_CRITC_LEVEL = 2090; // 抗暴等级
	public final static int KONGZHI_JIACHENG = 2130; // 控制加成
	public final static int KONGZHI_MIANYI = 2140; // 控制免疫
	public final static int PHYSIC_FLOAT_VALUE = 2150; // 物理波动
	public final static int MAGIC_FLOAT_VALUE = 2160; // 法术波动
	public final static int HEAL_FLOAT_VALUE = 2170; // 治疗波动
	public final static int DEEP_HEAL_CRITC_LEVEL = 2180; // 被治疗暴击
	public final static int SHAPE_ID = 3000; // 模型id


	public FightAttrType() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof FightAttrType) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(FightAttrType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

