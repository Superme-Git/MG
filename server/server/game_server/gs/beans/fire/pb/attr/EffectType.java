
package fire.pb.attr;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class EffectType implements Marshal , Comparable<EffectType>{
	public final static int CONS_ABL = 11;
	public final static int CONS_PCT = 12;
	public final static int IQ_ABL = 21;
	public final static int IQ_PCT = 22;
	public final static int STR_ABL = 31;
	public final static int STR_PCT = 32;
	public final static int ENDU_ABL = 41;
	public final static int ENDU_PCT = 42;
	public final static int AGI_ABL = 51;
	public final static int AGI_PCT = 52;
	public final static int MAX_HP_ABL = 61;
	public final static int MAX_HP_PCT = 62;
	public final static int UP_LIMITED_HP_ABL = 71;
	public final static int UP_LIMITED_HP_PCT = 72;
	public final static int HP_ABL = 81;
	public final static int HP_PCT = 82;
	public final static int MAX_MP_ABL = 91;
	public final static int MAX_MP_PCT = 92;
	public final static int MP_ABL = 101;
	public final static int MP_PCT = 102;
	public final static int MAX_SP_ABL = 111;
	public final static int MAX_SP_PCT = 112;
	public final static int SP_ABL = 121; // 怒气
	public final static int SP_PCT = 122; // 怒气
	public final static int DAMAGE_ABL = 131;
	public final static int DAMAGE_PCT = 132;
	public final static int DEFEND_ABL = 141;
	public final static int DEFEND_PCT = 142;
	public final static int MAGIC_ATTACK_ABL = 151; // 法术攻击力
	public final static int MAGIC_ATTACK_PCT = 152; // 法术攻击力
	public final static int MAGIC_DEF_ABL = 161; // 法术防御力
	public final static int MAGIC_DEF_PCT = 162; // 法术防御力
	public final static int MEDICAL_ABL = 171; // 治疗强度
	public final static int SEAL_ABL = 181; // 控制命中
	public final static int UNSEAL_ABL = 191; // 封系抵抗
	public final static int UNSEAL_PCT = 192; // 封系抵抗
	public final static int SPEED_ABL = 201;
	public final static int SPEED_PCT = 202;
	public final static int HIT_RATE_ABL = 211;
	public final static int HIT_RATE_PCT = 212;
	public final static int DODGE_RATE_ABL = 221;
	public final static int DODGE_RATE_PCT = 222;
	public final static int PHY_CRITC_LEVEL_ABL = 231; // 物理暴击等级
	public final static int PHY_CRITC_LEVEL_PCT = 232;
	public final static int ANTI_PHY_CRITC_LEVEL_ABL = 241; // 抗物理暴击等级
	public final static int PHYSIC_CRIT_PCT_ABL = 251; // 物理暴击加成（初始为200%,即2倍普通伤害）
	public final static int PHYSIC_CRIT_PCT_PCT = 252; // 物理暴击加成（初始为200%,即2倍普通伤害）
	public final static int MAGIC_CRITC_LEVEL_ABL = 261; // 魔法暴击等级
	public final static int MAGIC_CRITC_LEVEL_PCT = 262;
	public final static int ANTI_MAGIC_CRITC_LEVEL_ABL = 271; // 抗魔法暴击等级
	public final static int MAGIC_CRIT_PCT_ABL = 281; // 魔法暴击程度
	public final static int MAGIC_CRIT_PCT_PCT = 282;
	public final static int STEAL_HP_PCT_ABL = 361; // 吸血比例（吸血占造成伤害的百分比，默认0%)
	public final static int STEAL_HP_PCT_PCT = 362; // 吸血比例（吸血占造成伤害的百分比，默认0%)
	public final static int STEAL_MP_PCT_ABL = 381; // 吸蓝比例（吸蓝占造成mp伤害的百分比，默认0%)
	public final static int POISON_RATE_ABL = 391; // 使对方中毒几率（默认0%)
	public final static int POISON_RATE_PCT = 392; // 使对方中毒几率（默认0%)
	public final static int COMBO_ATTACK_RATE_ABL = 451; // 连击几率
	public final static int COMBO_ATTACK_RATE_PCT = 452; // 连击几率
	public final static int COMBO_ATTACK_PCT_ABL = 461; // 连击伤害系数
	public final static int COMBO_ATTACK_PCT_PCT = 462;
	public final static int COMBO_ATTACK_COUNT_ABL = 471; // 连击次数
	public final static int COMBO_ATTACK_COUNT_PCT = 472; // 连击次数
	public final static int MAGIC_COMBO_ATTACK_RATE_ABL = 481; // 法术连击几率
	public final static int MAGIC_COMBO_ATTACK_RATE_PCT = 482;
	public final static int MAGIC_COMBO_ATTACK_PCT_ABL = 491; // 法术连击伤害系数
	public final static int MAGIC_COMBO_ATTACK_PCT_PCT = 492;
	public final static int MAGIC_COMBO_ATTACK_COUNT_ABL = 501; // 法术连击次数
	public final static int MAGIC_COMBO_ATTACK_COUNT_PCT = 502; // 法术连击次数
	public final static int EXTRA_ATTACK_RATE_ABL = 511; // 追击几率
	public final static int EXTRA_ATTACK_RATE_PCT = 512; // 追击几率
	public final static int EXTRA_ATTACK_DEGREE_ABL = 521; // 追击伤害占正常伤害的比率
	public final static int EXTRA_ATTACK_DEGREE_PCT = 522; // 追击伤害占正常伤害的比率
	public final static int EXTRA_ATTACK_COUNT_ABL = 531; // 追击次数
	public final static int EXTRA_ATTACK_COUNT_PCT = 532; // 追击次数
	public final static int PARRY_RATE_ABL = 571; // 招架几率（默认0%)
	public final static int PARRY_RATE_PCT = 572; // 招架几率（默认0%)
	public final static int RETURN_HURT_RATE_ABL = 591; // 反震几率
	public final static int RETURN_HURT_RATE_PCT = 592; // 反震几率
	public final static int RETURN_HURT_PCT_ABL = 601; // 反震程度（反震伤害占造成伤害的百分比，默认30%)
	public final static int RETURN_HURT_PCT_PCT = 602; // 反震程度（反震伤害占造成伤害的百分比，默认30%)
	public final static int RETURN_ATTACK_RATE_ABL = 611; // 反击几率
	public final static int RETURN_ATTACK_RATE_PCT = 612; // 反击几率
	public final static int RETURN_ATTACK_PCT_ABL = 621; // 反击程度（正常反击伤害的百分比，默认100%）
	public final static int RETURN_ATTACK_PCT_PCT = 622; // 反击程度（正常反击伤害的百分比，默认100%）
	public final static int ABSORB_FIRE_ODDS_ABL = 651; // 火系法术吸收率
	public final static int ABSORB_WATER_ODDS_ABL = 661; // 水系法术吸收率
	public final static int ABSORB_EARTH_ODDS_ABL = 671; // 土系法术吸收率
	public final static int ABSORB_THUNDER_ODDS_ABL = 681; // 雷系法术吸收率
	public final static int ABSORB_OTHER_ODDS_ABL = 691; // 预留系法术吸收率
	public final static int SEAL_LEVEL_ABL = 811; // 封印命中等级
	public final static int ANTI_SEAL_LEVEL_ABL = 821; // 封印抗性等级
	public final static int PHYSIC_CRIT_RATE_ABL = 831; // 物理暴击几率
	public final static int PHYSIC_CRIT_RATE_PCT = 832; // 物理暴击几率
	public final static int MAGIC_CRIT_RATE_ABL = 851; // 法术暴击几率（初始为0%）
	public final static int MAGIC_CRIT_RATE_PCT = 852; // 法术暴击几率（初始为0%）
	public final static int ATTACK_BACK_COUNT_ABL = 931; // 反击次数
	public final static int EFFECT_POINT_ABL = 1011; // 技能效果点
	public final static int TEMP_SP_ABL = 1021; // 临时怒气
	public final static int AMEND_HIDDEN_WEAPON_ACTIVE_ABL = 1220; // 飞镖主动伤害修正
	public final static int PET_LIFE_ABL = 1361; // 宠物寿命
	public final static int PET_ATTACK_APT_ABL = 1441; // 宠物攻击资质
	public final static int PET_DEFEND_APT_ABL = 1451; // 宠物防御资质
	public final static int PET_PHYFORCE_APT_ABL = 1461; // 宠物体力资质
	public final static int PET_MAGIC_APT_ABL = 1471; // 宠物法力资质
	public final static int PET_SPEED_APT_ABL = 1481; // 宠物速度资质
	public final static int PET_DODGE_APT_ABL = 1491; // 宠物躲闪资质
	public final static int ENLIMIT_ABL = 1521; // 活力上限
	public final static int PFLIMIT_ABL = 1531; // 体力上限
	public final static int OPERATOR = 1581;
	public final static int AIM = 1591;
	public final static int OPERATE_TYPE = 1171;
	public final static int OPERATE_ID = 1181;
	public final static int ATTACK_BACK_LEVEL_ABL = 1751; // 反击等级
	public final static int RETURN_HURT_LEVEL_ABL = 1761; // 物理反震等级
	public final static int ANTI_ATTACK_BACK_LEVEL_ABL = 1771; // 抗反击等级
	public final static int ANTI_RETURN_HURT_LEVEL_ABL = 1781; // 抗物理反震等级
	public final static int QILIZHI_ABL = 1201; // 气力值
	public final static int KONGZHI_JIACHENG = 2131; // 控制加成
	public final static int KONGZHI_MIANYI = 2141; // 控制免疫
	public final static int SHAPE_ID = 3001; // 模型id


	public EffectType() {
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
		if (_o1_ instanceof EffectType) {
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

	public int compareTo(EffectType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

}

